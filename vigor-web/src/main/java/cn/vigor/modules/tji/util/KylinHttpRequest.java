package cn.vigor.modules.tji.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

public class KylinHttpRequest
{
    public static String httpPost(String params,String baseUrl,String method) throws IOException{
        String result = null;
        URL url = new URL(baseUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        byte[] key = ("ADMIN:KYLIN").getBytes();  
        String encoding = Base64.encodeBase64String(key);
        connection.setRequestProperty("Authorization", "Basic " + encoding);  
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setConnectTimeout(3000);
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        OutputStream os = connection.getOutputStream();
        os.write(params.getBytes("utf-8"));
        os.flush();
        os.close();
        boolean success = (connection.getResponseCode() == 200);
        if (success){
            InputStream is = connection.getInputStream();
            StringBuffer sb = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tempLine = rd.readLine();
            while (tempLine != null)
            {
                sb.append(tempLine);
                tempLine = rd.readLine();
            }
            result = sb.toString();
        }else{
            result = connection.getResponseMessage();
        }
        connection.disconnect();
        connection = null;
        return result;
    }
    
    public static String httpGet(String baseUrl,String method) throws IOException{
        String result = null;
        URL url = new URL(baseUrl);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        byte[] key = ("ADMIN:KYLIN").getBytes();  
        String encoding = Base64.encodeBase64String(key);
        connection.setRequestProperty("Authorization", "Basic " + encoding);  
        connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        connection.setConnectTimeout(3000);
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        boolean success = (connection.getResponseCode() == 200);
        if (success){
            InputStream is = connection.getInputStream();
            StringBuffer sb = new StringBuffer();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String tempLine = rd.readLine();
            while (tempLine != null)
            {
                sb.append(tempLine);
                tempLine = rd.readLine();
            }
            result = sb.toString();
        }else{
            result = connection.getResponseMessage();
        }
        connection.disconnect();
        connection = null;
        return result;
    }
}
