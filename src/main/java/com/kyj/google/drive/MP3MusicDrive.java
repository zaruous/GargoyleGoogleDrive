/********************************
 *	프로젝트 : gargoyle-google-drive
 *	패키지   : com.kyj.google.drive
 *	작성일   : 2018. 7. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.google.drive;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpDownloaderProgressListener;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

/**
 * @author KYJ
 *
 */
public class MP3MusicDrive extends MusicDrive {

	/**
	 * @최초생성일 2018. 7. 2.
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(MP3MusicDrive.class);

	private String query = "name contains 'mp3'";

	public MP3MusicDrive(Drive service) {
		super(service);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @return
	 * @throws IOException
	 */
	public <T> List<File> listFile() throws IOException {
		return listFile(1000, f -> f);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @return
	 * @throws IOException
	 */
	public <T> List<File> listFile(int pageSize) throws IOException {
		return listFile(pageSize, f -> f);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public <T> List<T> listFile(Function<File, T> mapper) throws IOException {
		return listFile(1000, mapper);
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @param pageSize
	 * @param mapper
	 * @return
	 * @throws IOException
	 */
	public <T> List<T> listFile(int pageSize, Function<File, T> mapper) throws IOException {
		// Print the names and IDs for up to 10 files.
		com.google.api.services.drive.Drive.Files.List list = getService().files().list();
		String nextPageToken = null;
		List<T> result = new ArrayList<>();
		boolean complte = false;
		int page = 0;
		do {
			FileList r = list.setQ(query).setPageSize(pageSize).setFields("nextPageToken, files(id, name, mimeType)")
					.setPageToken(nextPageToken).setOrderBy("name").execute();
			nextPageToken = r.getNextPageToken();

			List<File> files = r.getFiles();
			if (files != null && !files.isEmpty()) {
				result.addAll(files.stream().map(mapper).collect(Collectors.toList()));
			}

			LOGGER.debug("page : {} , next : : {} ", page, nextPageToken);
			complte = nextPageToken != null;
			++page;
		} while (complte);

		return result;
	}

	class CustomProgressListener implements MediaHttpDownloaderProgressListener {
		public void progressChanged(MediaHttpDownloader downloader) {
			switch (downloader.getDownloadState()) {
			case MEDIA_IN_PROGRESS:
				LOGGER.debug("{}", downloader.getProgress());

				break;
			case MEDIA_COMPLETE:
				LOGGER.debug("Download is complete!");
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @param file
	 * @throws Exception
	 */
	public void download(OutputStream out, File downloadFile) throws Exception {
		download(out, downloadFile.getId());
	}

	/**
	 * @작성자 : KYJ
	 * @작성일 : 2018. 7. 2.
	 * @param out
	 * @param id
	 * @throws Exception
	 */
	public void download(OutputStream out, String id) throws Exception {
		Get req = getService().files().get(id);

		GenericUrl buildHttpRequestUrl = req.buildHttpRequestUrl();

		MediaHttpDownloader mediaHttpDownloader = req.getMediaHttpDownloader();
		// mediaHttpDownloader.setProgressListener(new
		// CustomProgressListener());
		// mediaHttpDownloader.setDirectDownloadEnabled(true);
		// req.executeAndDownloadTo(out);

		mediaHttpDownloader.download(buildHttpRequestUrl, out);
	}
}
