/********************************
 *	프로젝트 : gargoyle-google-drive
 *	패키지   : com.kyj.google.drive
 *	작성일   : 2018. 3. 5.
 *	작성자   : KYJ
 *******************************/
package com.kyj.google.drive;

import java.io.IOException;

import com.google.api.services.drive.Drive;

public class Quickstart {

	public static void main(String[] args) throws IOException {
		// Build a new authorized API client service.
		Drive service = GoogleDriveStore.getInstance();

		MP3MusicDrive mp3MusicDrive = new MP3MusicDrive(service);
		mp3MusicDrive.listFile().forEach(System.out::println);

	}

}