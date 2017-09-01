package cn.vigor.modules.meta.util;

import java.io.IOException;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

/**
 * 实现对hadoop的相关操作
 * 
 * @author zhangfeng
 */
public class HadoopUtil {

	// 创建目录
	public static void mkdir(String path) throws IOException {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Path srcPath = new Path(path);
		boolean isok = fs.mkdirs(srcPath);
		if (isok) {
			System.out.println("create dir ok!");
		} else {
			System.out.println("create dir failure");
		}
		fs.close();
		
	}
	//创建新文件
    public static void createFile(String dst , byte[] contents) throws IOException{
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path dstPath = new Path(dst); //目标路径
        //打开一个输出流
        FSDataOutputStream outputStream = fs.create(dstPath);
        outputStream.write(contents);
        outputStream.close();
        fs.close();
        System.out.println("文件创建成功！");
    }
	public static void main1(String[] args) throws Exception {

		try {
			//HadoopUtil.mkdir("/kiss");
			//HadoopUtil.createFile("/kiss/test.tzt" ,new String("asdfasdf").getBytes());
			HadoopUtil.listFiles("/tmp");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void listFiles(String  path) throws Exception {
		 
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] statu = fs.listStatus(new Path(path));
		Path [] listPaths=FileUtil.stat2Paths(statu);
		for(Path p:listPaths){
			System.out.println(p);
		}
	}
	 public  static void WriteFile(String hdfs) throws IOException {
	        Configuration conf = new Configuration();
	        FileSystem fs = FileSystem.get(URI.create(hdfs),conf);
	        FSDataOutputStream hdfsOutStream = fs.create(new Path(hdfs));
	        hdfsOutStream.writeChars("hello");
	        hdfsOutStream.close();
	        fs.close();     
	    }
	     
	    public static void ReadFile(String hdfs) throws IOException {
	        Configuration conf = new Configuration();
	        FileSystem fs = FileSystem.get(URI.create(hdfs),conf);
	        FSDataInputStream hdfsInStream = fs.open(new Path(hdfs));
	         
	        byte[] ioBuffer = new byte[1024];
	        int readLen = hdfsInStream.read(ioBuffer);
	        while(readLen!=-1)
	        {
	            System.out.write(ioBuffer, 0, readLen);
	            readLen = hdfsInStream.read(ioBuffer);
	        }
	        hdfsInStream.close();
	        fs.close(); 
	    }
	         
	    public static void main(String[] args) throws IOException {     
	        String hdfs = "hdfs://192.168.12.143:9000/test/hello.txt";
	        HadoopUtil.WriteFile(hdfs);
	        HadoopUtil.ReadFile(hdfs);
	      }

}
