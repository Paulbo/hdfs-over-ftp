package org.apache.hadoop.contrib.ftp;




import java.io.IOException;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.security.UserGroupInformation;





public class FSClient {
	
	private static DistributedFileSystem dfs = null;
	
	public static String HDFS_URI = "hdfs://168.7.1.67:8020";

	
	private static FileSystem fs = null;

	private static String superuser = "hdfs";
	
	public static void initHDFS(){
		Configuration conf = new Configuration();
		
		conf.addResource(new Path("./core-site.xml"));
		conf.addResource(new Path("./hdfs-site.xml"));
		
		UserGroupInformation.setConfiguration(conf);
		try {
//			UserGroupInformation.loginUserFromKeytab("impala/cib69@CIB_BIGDATA.COM", "impala.keytab");
			UserGroupInformation.loginUserFromKeytab("hdfs/cib69@CIB_BIGDATA.COM", FSClient.class.getResource("/hdfs.keytab").toString());
			
			;
			fs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dfs = new DistributedFileSystem();
		dfs = (DistributedFileSystem) fs;
		System.out.println(conf.get("hadoop.security.authentication"));
	}
	
	public static void setHDFS_URI(String HDFS_URI) {
		FSClient.HDFS_URI = HDFS_URI;
	}
	
	
	
	public static DistributedFileSystem getDfs() {
		if (dfs == null) {
			initHDFS();
		}
		return dfs;
	}
	
	
	public static void setSuperuser(String superuser) {
		FSClient.superuser = superuser;
	}
}
