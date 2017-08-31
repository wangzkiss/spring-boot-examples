package cn.vigor.common.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * http请求工具类
 * @author zhangfeng
 *
 */
public class HttpUtil
{
    public static boolean sendSchedPost(String urlString,
            Map<String, String> data)
    {
        try
        {
            String method = "POST";
            URL url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            urlConn.setRequestMethod(method);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            StringBuffer param = new StringBuffer();
            for (String key : data.keySet())
            {
                param.append("&").append(key).append("=").append(
                        URLEncoder.encode(data.get(key), "utf-8"));
            }
            OutputStream os = urlConn.getOutputStream();
            System.out.println("post infp: " + param.toString().substring(1));
            os.write(param.toString().substring(1).getBytes("utf-8"));
            os.flush();
            os.close();
            boolean success = urlConn.getResponseCode() == 200;
            if (success)
            {
                InputStream is = urlConn.getInputStream();
                StringBuffer sb = new StringBuffer();
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(is, "utf-8"));
                String tempLine = rd.readLine();
                while (tempLine != null)
                {
                    sb.append(tempLine);
                    tempLine = rd.readLine();
                }
                
                if ("success".equalsIgnoreCase(sb.toString()))
                {
                    success = true;
                }
                else
                {
                    success = false;
                }
            }
            urlConn.disconnect();
            urlConn = null;
            return success;
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String post(String urlString, String param) throws Exception
    {
        try
        {
            String method = "POST";
            String result = null;
            URL url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            urlConn.setRequestMethod(method);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            OutputStream os = urlConn.getOutputStream();
            os.write(param.getBytes("utf-8"));
            os.flush();
            os.close();
            boolean success = (urlConn.getResponseCode() == 200);
            if (success)
            {
                result = "success";
            }
            else
            {
                result = urlConn.getResponseMessage();
            }
            urlConn.disconnect();
            urlConn = null;
            return result;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    public static String postCluster(String urlString, String param)
            throws Exception
    {
        System.out.println("url is " + urlString);
        System.out.println("param is " + param);
        try
        {
            String method = "POST";
            String result = null;
            URL url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            urlConn.setRequestMethod(method);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type",
                    "application/json;utf-8");
            OutputStreamWriter wr = new OutputStreamWriter(
                    urlConn.getOutputStream());
            wr.write(param);
            wr.flush();
            wr.close();
            boolean success = (urlConn.getResponseCode() == 200);
            if (success)
            {
                InputStream is = urlConn.getInputStream();
                StringBuffer sb = new StringBuffer();
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(is, "utf-8"));
                String tempLine = rd.readLine();
                while (tempLine != null)
                {
                    sb.append(tempLine);
                    tempLine = rd.readLine();
                }
                result = sb.toString();
            }
            else
            {
                result = urlConn.getResponseMessage();
            }
            urlConn.disconnect();
            urlConn = null;
            return result;
        }
        catch (Exception e)
        {
            throw e;
        }
    }
    
    /**
     * 判断url是否活的
     * @throws Exception 
     * @throws Exception 所有异常都视为非active 
     */
    public static boolean isActive(String urlStr)
    {
        boolean flag = false;
        
        URL url = null;
        HttpURLConnection urlConn = null;
        try
        {
            url = new URL(urlStr);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            
            int retCode = urlConn.getResponseCode();
            
            if (200 == retCode || 500 == retCode)
            {
                flag = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            //关闭连接
            if (null != urlConn)
            {
                urlConn.disconnect();
            }
        }
        
        return flag;
    }
}
