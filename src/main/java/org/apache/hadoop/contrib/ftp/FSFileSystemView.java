package org.apache.hadoop.contrib.ftp;

import org.apache.ftpserver.ftplet.FileObject;
import org.apache.ftpserver.ftplet.FileSystemView;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.hadoop.fs.Path;

/**
 * Implemented FileSystemView to use HdfsFileObject
 */
public class FSFileSystemView implements FileSystemView {

	// the root directory will always end with '/'.
	private Path rootPath = null;
	// the first and the last character will always be '/'
	// It is always with respect to the root directory.
	private Path currPath = null;

	private User user;

	// private boolean writePermission;

	private boolean caseInsensitive = false;

	/**
	 * Constructor - set the user object.
	 */
	protected FSFileSystemView(User user) throws FtpException {
		this(user, true);
	}

	/**
	 * Constructor - set the user object.
	 */
	protected FSFileSystemView(User user, boolean caseInsensitive)
			throws FtpException {
		if (user == null) {
			throw new IllegalArgumentException("user can not be null");
		}
		if (user.getHomeDirectory() == null) {
			throw new IllegalArgumentException(
					"User home directory can not be null");
		}

		this.caseInsensitive = caseInsensitive;

		this.rootPath = new Path(user.getHomeDirectory());
		this.currPath = this.rootPath;
		
		this.user = user;

	}

	/**
	 * Get the user home directory. It would be the file system root for the
	 * user.
	 */
	public FileObject getHomeDirectory() {
		return new FSFileObject(this.rootPath, user);
	}

	/**
	 * Get the current directory.
	 */
	public FileObject getCurrentDirectory() {
		return new FSFileObject(this.currPath , user);
	}

	/**
	 * Get file object.
	 */
	public FileObject getFileObject(String file) {
		Path path;
		if (file.startsWith("/")) {
			path = new Path(this.rootPath, getRelativePath(file));
		} else  {
			path = new Path(this.currPath, file);
		} 
		
		if(path.toString().startsWith(this.rootPath.toString()))
			return new FSFileObject(path, user);
		else
			return null;
	}

	/**
	 * Change directory.
	 */
	public boolean changeDirectory(String dir) {
		Path path;
		if (dir.startsWith("/")) {
			path = new Path(this.rootPath, getRelativePath(dir));
		} else  {
			path = new Path(this.currPath, dir);
		} 
		
		
		FSFileObject file = new FSFileObject(path, user);
		if (file.isDirectory() && file.hasReadPermission() && path.toString().startsWith(this.rootPath.toString())) {
			currPath = path;
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
	
	private String getRelativePath(String curStr){
		if(curStr.equals("/"))
			return ".";
		else
			return curStr.substring(1);
	}
}
