package ftp.test;


import java.util.ArrayList;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.hadoop.contrib.ftp.*;
import org.apache.hadoop.fs.Path;

public class Test {
	public static void main(String[] args) throws Exception {
		HdfsUser ftpUser = new HdfsUser();
		ftpUser.setName("hive");
		ftpUser.setPassword("hive");
		
		ftpUser.setEnabled(true);
		ftpUser.setHomeDirectory("/tmp");
		ArrayList<String> groups = new ArrayList<String>();
		groups.add("users,hive");
		ftpUser.setGroups(groups );
		
		
		HdfsUserManager  ftpUm = new HdfsUserManager();
		ftpUm.save(ftpUser);
	}
}
