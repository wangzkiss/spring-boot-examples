package cn.vigor.modules.meta.util;


import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

public class HDFSUtil {  
	
    private static Logger log = Logger.getLogger(HDFSUtil.class);    
    
    public synchronized static FileSystem getFileSystem() {  
        FileSystem fs = null;  
        Configuration config = new Configuration(); 
        try {  
            fs = FileSystem.get(config);  
            log.info("fs.defaultFS=" + fs.getConf().get("fs.defaultFS"));
        } catch (Exception e) {   
            log.error("getFileSystem failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
        return fs;  
    }  
    
    public synchronized static FileSystem getFileSystemByPath(Path path) {  
        FileSystem fs = null;  
        Configuration config = new Configuration(); 
        try {  
        	fs = path.getFileSystem(config);
            log.info("fs.defaultFS=" + fs.getConf().get("fs.defaultFS"));
        } catch (Exception e) {   
            log.error("getFileSystem failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
        return fs;  
    }  
    public synchronized static void listNodes(FileSystem fs) {  
        DistributedFileSystem dfs = (DistributedFileSystem) fs;  
        try {  
            DatanodeInfo[] infos = dfs.getDataNodeStats();  
            for (DatanodeInfo node : infos) {  
                System.out.println("HostName: " + node.getHostName() + "/n"  
                        + node.getDatanodeReport());  
                System.out.println("--------------------------------");  
            }  
        } catch (Exception e) {  
            log.error("list node list failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
    }  
    /** 
     * 打印系统配置 
     *  
     * @param fs 
     */  
    public synchronized static void listConfig(FileSystem fs) {  
        Iterator<Entry<String, String>> entrys = fs.getConf().iterator();  
        while (entrys.hasNext()) {  
            Entry<String, String> item = entrys.next();  
            log.info(item.getKey() + ": " + item.getValue());  
//            System.out.println(item.getKey() + ": " + item.getValue());
        }  
    }  
    /** 
     * 创建目录和父目录 
     *  
     * @param fs 
     * @param dirName 
     */  
    public synchronized static void mkdirs(FileSystem fs, String dirName) {  
        // Path home = fs.getHomeDirectory();  
       // Path workDir = fs.getWorkingDirectory();  
        String dir =dirName;  
        Path src = new Path(dir);  
        // FsPermission p = FsPermission.getDefault();  
        boolean succ;  
        try {  
            succ = fs.mkdirs(src);  
            if (succ) {  
                log.info("create directory " + dir + " successed. ");  
            } else {  
                log.info("create directory " + dir + " failed. ");  
            }  
        } catch (Exception e) {  
            log.error("create directory " + dir + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
    }  
    /** 
     * 删除目录和子目录 
     *  
     * @param fs  
     * @param dirName 
     */  
    public synchronized static void rmdirs(FileSystem fs, String dirName) {  
        // Path home = fs.getHomeDirectory();  
        Path workDir = fs.getWorkingDirectory();  
        String dir = workDir + "/" + dirName;  
        Path src = new Path(dir);  
        boolean succ;  
        try {  
            succ = fs.delete(src, true);  
            if (succ) {  
                log.info("remove directory " + dir + " successed. ");  
            } else {  
                log.info("remove directory " + dir + " failed. ");  
            }  
        } catch (Exception e) {  
            log.error("remove directory " + dir + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
    }  
    /** 
     * 上传目录或文件 
     *  
     * @param fs 
     * @param local 
     * @param remote 
     */  
    public synchronized static void upload(FileSystem fs, String local,  
            String remote) {  
        // Path home = fs.getHomeDirectory();  
        Path workDir = fs.getWorkingDirectory();
        
        Path dst = new Path(workDir + "/" + remote);  
//        Path dst = new Path("hdfs://hdp247.localhost:8020/user/hdfs/" + remote);
        
        Path src = new Path(local);  
        try {  
            fs.copyFromLocalFile(false, true, src, dst);  
            log.info("upload " + local + " to  " + dst + " successed. ");  
        } catch (Exception e) {  
            log.error("upload " + local + " to  " + dst + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
    }  
    /** 
     * 下载目录或文件 
     *  
     * @param fs 
     * @param local 
     * @param remote 
     */  
    public synchronized static void download(FileSystem fs, String local,  
            String remote) {  
        // Path home = fs.getHomeDirectory();  
        Path workDir = fs.getWorkingDirectory();  
        Path dst = new Path(workDir + "/" + remote);  
        Path src = new Path(local);  
        try {  
            fs.copyToLocalFile(false, dst, src);  
            log.info("download from " + dst + " to  " + local  
                    + " successed. ");  
        } catch (Exception e) {  
            log.error("download from " + remote + " to  " + local + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
    }  
    /** 
     *  字节数转换 
     *  
     * @param size 
     * @return 
     */  
    public synchronized static String convertSize(long size) {  
        String result = String.valueOf(size);  
        if (size < 1024 * 1024) {  
            result = String.valueOf(size / 1024) + " KB";  
        } else if (size >= 1024 * 1024 && size < 1024 * 1024 * 1024) {  
            result = String.valueOf(size / 1024 / 1024) + " MB";  
        } else if (size >= 1024 * 1024 * 1024) {  
            result = String.valueOf(size / 1024 / 1024 / 1024) + " GB";  
        } else {  
            result = result + " B";  
        }  
        return result;  
    }  
    /** 
     * ����HDFS�ϵ��ļ���Ŀ¼ 
     *  
     * @param fs 
     * @param path 
     */  
    public synchronized static void listFile(FileSystem fs, String path) {  
        Path workDir = fs.getWorkingDirectory();  
        Path dst;  
        if (null == path || "".equals(path)) {  
            dst = new Path(workDir + "/" + path);  
        } else {  
            dst = new Path(path);  
        }  
        try {  
            String relativePath = "";  
            FileStatus[] fList = fs.listStatus(dst);  
            for (FileStatus f : fList) {  
                if (null != f) {  
                    relativePath = new StringBuffer()  
                            .append(f.getPath().getParent()).append("/")  
                            .append(f.getPath().getName()).toString();  
                    if (f.isDirectory()) {  
                        listFile(fs, relativePath);  
                    } else {  
                        System.out.println(convertSize(f.getLen()) + "/t/t"  
                                + relativePath);  
                    }  
                }  
            }  
        } catch (Exception e) {  
            log.error("list files of " + path + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        } finally {  
        }  
    }  
    public synchronized static void write(FileSystem fs, String path,  
            String data) {  
        // Path home = fs.getHomeDirectory();  
        Path workDir = fs.getWorkingDirectory();  
        Path dst = new Path(workDir + "/" + path);  
        try {  
            FSDataOutputStream dos = fs.create(dst);  
            dos.writeUTF(data);  
            dos.close();  
            log.info("write content to " + path + " successed. ");  
        } catch (Exception e) {  
            log.error("write content to " + path + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
    }  
    public synchronized static void append(FileSystem fs, String path,  
            String data) {  
        // Path home = fs.getHomeDirectory();  
        Path workDir = fs.getWorkingDirectory();  
        Path dst = new Path(workDir + "/" + path);  
        try {  
            FSDataOutputStream dos = fs.append(dst);  
            dos.writeUTF(data);  
            dos.close();  
            log.info("append content to " + path + " successed. ");  
        } catch (Exception e) {  
            log.error("append content to " + path + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
    }  
    public synchronized static String read(FileSystem fs, String path) {  
        String content = null;  
        // Path home = fs.getHomeDirectory();  
        Path workDir = fs.getWorkingDirectory();  
        Path dst = new Path(workDir + "/" + path);  
        try {  
            // reading  
            FSDataInputStream dis = fs.open(dst);  
            content = dis.readUTF();  
            dis.close();  
            log.info("read content from " + path + " successed. " + " content is : " + content);  
        } catch (Exception e) {  
            log.error("read content from " + path + " failed :"  
                    + ExceptionUtils.getFullStackTrace(e));  
        }  
        return content;  
    }  
    
    public static void main(String[] args) throws Exception {
	
        FileSystem fs = HDFSUtil.getFileSystem();
		HDFSUtil.mkdirs(fs, "/kiss/test");
		// DistributedFileSystem dfs = (DistributedFileSystem) fs;  
		// dfs.open(new Path("/user/yarn/tmp/20150625174751/invaliddata/part-m-00000"));
//		System.out.println(fs.getConf().get("fs.defaultFS"));	
//		String pathString = fs.getConf().get("fs.defaultFS") + "/user/yarn/tmp/20150625174751/invaliddata/part-m-00000";
//		Path path = new Path(pathString);
//		FileSystem f1 = HDFSUtil.getFileSystemByPath(path);
//		f1.open(path);
//		Configuration conf = new Configuration();
//		FileSystem f2 = path.getFileSystem(conf);
//		f2.open(path);
//		 getFile(path,f1);

//		HDFSUtil.listConfig(fs);
//		String dirName = "demo.txt";
//		HDFSUtil.mkdirs(fs, dirName);
//		HDFSUtil.rmdirs(fs, dirName);		String destName = "test";
//		HDFSUtil.upload(fs, "E:/hadoop/hadoop-1.0.4/test-in/", destName);
//		HDFSUtil.download(fs, "E:/hadoop/hadoop-1.0.4/test-in-2/", destName);
//		HDFSUtil.write(fs, dirName, "test-����");
//		HDFSUtil.append(fs, dirName, "/ntest-����2");
//		String content = HDFSUtil.read(fs, dirName);
//		System.out.println(content);
//		HDFSUtil.listFile(fs, "");
//		HDFSUtil.listNode(fs);  
//		f1.close();
	}
 

    public static void getFile(Path path,FileSystem fs) throws IOException {

        FileStatus[] fileStatus = fs.listStatus(path);       
        for(int i=0;i<fileStatus.length;i++){
            if(fileStatus[i].isDirectory()){
                Path p = new Path(fileStatus[i].getPath().toString());
                getFile(p,fs);
            }else{
                System.out.println(fileStatus[i].getPath().toString());
                FSDataInputStream stream=fs.open(path);
                IOUtils.copyBytes(stream,System.out,4096,false);
            }
        }
    }


} 
