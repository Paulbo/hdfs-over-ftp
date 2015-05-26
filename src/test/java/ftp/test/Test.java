package ftp.test;


import java.util.ArrayList;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.hadoop.contrib.ftp.*;

public class Test {
	public static void main(String[] args) throws Exception {
		HdfsUser ftpUser = new HdfsUser();
		ftpUser.setName("test");
		ftpUser.setPassword("test");
		
		ftpUser.setEnabled(true);
		ftpUser.setHomeDirectory("/");
		ArrayList<String> groups = new ArrayList<String>();
		groups.add("users");
		ftpUser.setGroups(groups );
		
		
		HdfsUserManager  ftpUm = new HdfsUserManager();
		ftpUm.save(ftpUser);
	}
}
