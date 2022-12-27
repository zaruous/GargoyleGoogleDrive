/********************************
 *	프로젝트 : gargoyle-google-drive
 *	패키지   : com.kyj.google.drive
 *	작성일   : 2018. 7. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.google.drive;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

/**
 * @author KYJ
 *
 */
public class GoogleDriveStore {

	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/gargoyle-googleDrive");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/drive-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(

			/* 파일에 대한 메타 데이터를 읽어옴. 주로 리스팅 작업에 사용. */
			DriveScopes.DRIVE_METADATA_READONLY,
			/* 파일을 다운로드 하기 위한 권한으로 사용 */
			DriveScopes.DRIVE_READONLY

	);

	static {
		try {
			DATA_STORE_DIR.mkdirs();
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	static Credential authorize() throws IOException {
		// Load client secrets.
		Credential credential = null;

		// File parent = new File(System.getProperty("user.home"),
		// ".credentials");
		// File f = new File(parent, "client_id.json");
		// if (!f.exists()) {
		//
		// }

		try (InputStream in = GoogleDriveStore.class.getResourceAsStream("client_id.json")) {
			if (in == null)
				throw new RuntimeException("client_id.json is empty.");
			GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

			// Build flow and trigger user authorization request.
			GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
					.setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
			credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
			System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		}
		return credential;
	}

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	private static Drive driveService;

	public static synchronized Drive getInstance() throws IOException {
		if (driveService == null)
			driveService = getDriveService();
		return driveService;
	}

}
