package com.kyj.google.drive;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class QuickstartTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(QuickstartTest.class);

	@Before
	public void setup() {
		String file_encoding = "UTF-8";
		String jnu_encoding = "UTF-8";
		System.setProperty("file.encoding", file_encoding);
		System.setProperty("sun.jnu.encoding", jnu_encoding);
	}

	@Test
	public void listFiles() throws IOException {

		// Build a new authorized API client service.
		Drive service = GoogleDriveStore.getInstance();

		MP3MusicDrive mp3MusicDrive = new MP3MusicDrive(service);
		mp3MusicDrive.listFile().forEach(System.out::println);

	}

	@Test
	public void downloadTest() throws Exception {
		// Build a new authorized API client service.
		Drive service = GoogleDriveStore.getInstance();

		MP3MusicDrive mp3MusicDrive = new MP3MusicDrive(service);
		List<File> listFile = mp3MusicDrive.listFile();
		File file = listFile.get(0);
		String id = file.getId();
		String name = file.getName();

		LOGGER.debug(" ID : {} , name : {} ", id, name);

		java.io.File parent = new java.io.File("output");
		if (!parent.exists())
			parent.mkdirs();
		java.io.File outputFile = new java.io.File(parent, name);
		mp3MusicDrive.download(new FileOutputStream(outputFile), id);

	}

}
