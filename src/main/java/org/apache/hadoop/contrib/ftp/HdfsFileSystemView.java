package org.apache.hadoop.contrib.ftp;

import org.apache.ftpserver.ftplet.FileObject;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;

/**
 * Implemented FileSystemView to use HdfsFileObject
 */
public class HdfsFileSystemView implements FileSystemView {

	// the root directory will always end with '/'.
	private String rootDir = "/";

	// the first and the last character will always be '/'
	// It is always with respect to the root directory.
	private String currDir = "/";

	private User user;

	// private boolean writePermission;

	private boolean caseInsensitive = false;

	/**
	 * Constructor - set the user object.
	 */
	protected HdfsFileSystemView(User user) throws FtpException {
		this(user, true);
	}

	/**
	 * Constructor - set the user object.
	 */
	protected HdfsFileSystemView(User user, boolean caseInsensitive)
			throws FtpException {
		if (user == null) {
			throw new IllegalArgumentException("user can not be null");
		}
		if (user.getHomeDirectory() == null) {
			throw new IllegalArgumentException(
					"User home directory can not be null");
		}

		this.caseInsensitive = caseInsensitive;

		// add last '/' if necessary
		String rootDir = user.getHomeDirectory();
		//  rootDir = NativeFileObject.normalizeSeparateChar(rootDir);
		if (!rootDir.endsWith("/")) {
			rootDir += '/';
		}
		this.rootDir = rootDir;
		this.user = user;


	}

	/**
	 * Get the user home directory. It would be the file system root for the
	 * user.
	 */
	public FileObject getHomeDirectory() {
		return new HdfsFileObject(this.rootDir, user);
	}

	/**
	 * Get the current directory.
	 */
	public FileObject getCurrentDirectory() {
		return new HdfsFileObject(this.rootDir + currDir.substring(1), user);
	}

	/**
	 * Get file object.
	 */
	public FileObject getFileObject(String file) {
		String path;
		if (file.startsWith("/")) {
			path = this.rootDir + file.substring(1);
		} else  {
			path = this.rootDir +  currDir.substring(1) + "/" + file;
		} 
		return new HdfsFileObject(path, user);
	}

	/**
	 * Change directory.
	 */
	public boolean changeDirectory(String dir) {
		String path;
		if (dir.startsWith("/")) {
			path = this.rootDir + dir.substring(1);
		} else  {
			path = this.rootDir +  currDir.substring(1) + "/" + dir;
		} 
		HdfsFileObject file = new HdfsFileObject(path, user);
		if (file.isDirectory() && file.hasReadPermission()) {
			currDir = path.replaceFirst(this.rootDir, "/");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Is the file content random accessible?
	 */
	public boolean isRandomAccessible() {
		return true;
	}

	/**
	 * Dispose file system view - does nothing.
	 */
	public void dispose() {
	}
}
