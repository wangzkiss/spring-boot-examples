package cn.vigor.modules.meta.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpUtil
{
    public FTPClient ftp;
    
    /**  
     *   
     * @param path 上传到ftp服务器哪个路径下     
     * @param addr 地址  
     * @param port 端口号  
     * @param username 用户名  
     * @param password 密码  
     * @return  
     * @throws Exception  
     */
    public boolean connect(String path, String addr, int port, String username,
            String password) throws Exception
    {
        boolean result = false;
        ftp = new FTPClient();
        int reply;
        ftp.connect(addr, port);
        ftp.login(username, password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply))
        {
            ftp.disconnect();
            return result;
        }
        ftp.changeWorkingDirectory(path);
        result = true;
        return result;
    }
    
    /**  
     *   
     * @param file 上传的文件或文件夹  
     * @throws Exception  
     */
    public void upload(File file) throws Exception
    {
        if (file.isDirectory())
        {
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++)
            {
                File file1 = new File(file.getPath() + "\\" + files[i]);
                if (file1.isDirectory())
                {
                    upload(file1);
                    ftp.changeToParentDirectory();
                }
                else
                {
                    File file2 = new File(file.getPath() + "\\" + files[i]);
                    FileInputStream input = new FileInputStream(file2);
                    ftp.storeFile(file2.getName(), input);
                    input.close();
                }
            }
        }
        else
        {
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);
            ftp.storeFile(file2.getName(), input);
            input.close();
        }
    }
    
    /**
     *  
     * @param context
     * @param destfile
     * @throws Exception
     */
    public void upload(String context, String destfile) throws Exception
    {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(
                context.getBytes());
        ftp.storeFile(destfile, byteStream);
    }
    
    /**
     * 
     * @param destfile
     * @return
     * @throws Exception
     */
    public boolean mkdir(String destfile) throws Exception
    {
        boolean flag=false;
        String allPath="";
        for (String path : destfile.split("/"))
        {
            if(path!=null&&!"".equals(path))
            {
               allPath=allPath+"/"+path;
               ftp.makeDirectory(allPath);
               flag=true;
            }
        }
        return flag;
    }
    
    /**
     * 
     * @param destfile 文件全路径名
     * @return
     * @throws Exception
     */
    public boolean deleteFile(String destfile) throws Exception
    {
         return ftp.deleteFile(destfile);
    }
    /**
     * 
     * @param destDir 目录路劲   只能删除空目录
     * @return
     * @throws Exception
     */
    public boolean deleteDir(String destDir) throws Exception
    {
        return ftp.removeDirectory(destDir);
    }
    
    public void cloose() throws Exception
    {
        ftp.disconnect();
    }
    /**
     * ByteArrayInputStream byteStream = new ByteArrayInputStream(context.getBytes());
        upFile(byteStream,dstFile);
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        FtpUtil t = new FtpUtil();
        t.connect("/", "192.168.12.181", 21, "source_user", "source_pwd");
        t.mkdir("test3//test4");
        //t.upload("", "test3///test4/kissskiss.txt"); 
       /* boolean j=t.deleteDir("/test1/test2/");
        boolean l=t.deleteFile("/test1/kissskiss.txt");
        boolean k=t.deleteDir("/test1");
        System.out.println(j+"\t"+k+"\t"+l);*/
        
    }
}
