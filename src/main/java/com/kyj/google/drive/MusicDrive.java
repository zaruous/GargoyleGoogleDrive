/********************************
 *	프로젝트 : gargoyle-google-drive
 *	패키지   : com.kyj.google.drive
 *	작성일   : 2018. 7. 2.
 *	작성자   : KYJ
 *******************************/
package com.kyj.google.drive;

import com.google.api.services.drive.Drive;

/**
 * @author KYJ
 *
 */
public abstract class MusicDrive {
	private Drive service;

	public MusicDrive(Drive service) {
		this.service = service;
	}

	/**
	 * @return the service
	 */
	public Drive getService() {
		return service;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(Drive service) {
		this.service = service;
	}

}
