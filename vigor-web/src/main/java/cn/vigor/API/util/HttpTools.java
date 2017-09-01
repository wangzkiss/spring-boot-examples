package cn.vigor.API.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



@SuppressWarnings("deprecation")
public class HttpTools {
	
	private static HttpClient httpclient;
	
	private static HttpTools tools;
	
	private static final String encoding="UTF-8";
	
	private static final String user = "admin";
	private static final String password  = "admin";
	
	
	
	private HttpTools(){};
	
    private static void init() throws UnsupportedEncodingException{
		tools = new HttpTools();
		httpclient = new DefaultHttpClient();
	}
	
	public static HttpTools getInstance() throws UnsupportedEncodingException{
		if( null == tools){
			init();
			return tools;
		}else{
			return tools;
		}
	}
	
	public String delete(String url) throws URISyntaxException, ClientProtocolException, IOException {
	    String auth = user + ":" + password;
	    String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding));
	    HttpDelete httpDel = new HttpDelete(url);
	    httpDel.setHeader("Authorization", "Basic " + authorization);
	    httpDel.setHeader("X-Requested-By", "X-Requested-By");
	    HttpResponse response = httpclient.execute(httpDel);
	    return EntityUtils.toString(response.getEntity());
	}
	
	
	public synchronized String getContent(String url) throws URISyntaxException, ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 String content="";
		 HttpGet httpGet =null;
        try
        {
             httpGet = new HttpGet(url);
             httpGet.setHeader("Authorization", "Basic " +authorization);
             HttpResponse response = httpclient.execute(httpGet);
             content = EntityUtils.toString(response.getEntity());
            
        }
        catch (Exception e)
        {
           throw e;
        }finally{
            if(httpGet !=null){
                httpGet.releaseConnection();
            }
        }
		 return content;
	}
	public  HttpEntity getContentObject(String url) throws URISyntaxException, ClientProtocolException, IOException{
        String auth = user+":"+password;
        String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
        HttpGet httpGet =null;
        HttpEntity httpEntity=null;
       try
       {
            httpGet = new HttpGet(url);
            httpGet.setHeader("Authorization", "Basic " +authorization);
            HttpResponse response = httpclient.execute(httpGet);
            httpEntity= response.getEntity();
       }
       catch (Exception e)
       {
          throw e;
       }finally{
           if(httpGet !=null){
               httpGet.releaseConnection();
           }
       }
        return httpEntity;
   }
   
	
	public HttpResponse get(String url) throws URISyntaxException, ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 HttpGet httpGet = new HttpGet(url);
	     HttpResponse response=null;
        try
        {
            httpGet.setHeader("Authorization", "Basic " +authorization);
             response = httpclient.execute(httpGet);
        }
        catch (Exception e)
        {
            throw e;
        }finally{
            if(httpGet !=null){
                httpGet.releaseConnection();
            }
        }
		 return response;
	}
	
	
	
	public String putContent(String url,String entityString) throws ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 HttpPut httpput = new HttpPut(url);
		 String content="";
        try
        {
            httpput.setHeader("Authorization", "Basic " +authorization);
             httpput.addHeader("X-Requested-By", "ambari");
             StringEntity  entity = new StringEntity(entityString);
             httpput.setEntity(entity);
             HttpResponse response = httpclient.execute(httpput);
             content = EntityUtils.toString(response.getEntity());
        }
        catch (ParseException e)
        {
            throw e;
        }finally{
            if(httpput !=null){
                httpput.releaseConnection();
            }
        }
		 return content;
	}
	
	public HttpResponse put(String url,String entityString) throws ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 HttpPut httpput = new HttpPut(url);
		 HttpResponse response=null;
        try
        {
            httpput.setHeader("Authorization", "Basic " +authorization);
             httpput.addHeader("X-Requested-By", "ambari");
             StringEntity  entity = new StringEntity(entityString);
             httpput.setEntity(entity);
             response = httpclient.execute(httpput);
        }
        catch (ParseException e)
        {
            throw e;
        }finally{
            if(httpput !=null){
                httpput.releaseConnection();
            }
        }
		 return response;
	}
	
	
	public String postContent(String url) throws ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 HttpPost httppost = new HttpPost(url);
		 String content;
        try
        {
            httppost.setHeader("Authorization", "Basic " +authorization);
             httppost.addHeader("X-Requested-By", "ambari");
             HttpResponse response = httpclient.execute(httppost);
             content = EntityUtils.toString(response.getEntity());
        }
        catch (ParseException e)
        {
            throw e;
        }finally{
            if(httppost !=null){
                httppost.releaseConnection();
            }
        }
		 return content;
	}
	
	public String postContent(String url,String entityString) throws ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 String content;
		 HttpPost httppost = new HttpPost(url);
        try
        {
             httppost.setHeader("Authorization", "Basic " +authorization);
             httppost.addHeader("X-Requested-By", "ambari");
             StringEntity  entity = new StringEntity(entityString);
             httppost.setEntity(entity);
             HttpResponse response = httpclient.execute(httppost);
             content = EntityUtils.toString(response.getEntity());
        }
        catch (ParseException e)
        {
            throw e;
        }finally{
            if(httppost !=null){
                httppost.releaseConnection();
            }
        }
		 return content;
	}
	
	public HttpResponse post(String url,String entityString) throws ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 HttpPost httppost = new HttpPost(url);
		 HttpResponse response;
        try
        {
            httppost.setHeader("Authorization", "Basic " +authorization);
             httppost.addHeader("X-Requested-By", "ambari");
             StringEntity  entity = new StringEntity(entityString);
             httppost.setEntity(entity);
             response = httpclient.execute(httppost);
        }
        catch (ParseException e)
        {
            throw e;
        }finally{
            if(httppost !=null){
                httppost.releaseConnection();
            }
        }
		 return response;
	}
	
	
	public String postContent(String url,String entityString,Map<String,String> headers) throws ClientProtocolException, IOException{
		 String auth = user+":"+password;
		 String authorization = DatatypeConverter.printBase64Binary(auth.getBytes(encoding)) ;
		 String content;
		 HttpPost httppost = new HttpPost(url);
        try
        {
             Set<String> keyset = headers.keySet();
             httppost.setHeader("Authorization", "Basic " +authorization);
             for(String key:keyset){
            	 httppost.addHeader(key, headers.get(key));
             }
             httppost.addHeader("X-Requested-By", "ambari");
             StringEntity  entity = new StringEntity(entityString);
             httppost.setEntity(entity);
             HttpResponse response = httpclient.execute(httppost);
             content = EntityUtils.toString(response.getEntity());
        }
        catch (ParseException e)
        {
            throw e;
        }finally{
            if(httppost !=null){
                httppost.releaseConnection();
            }
        }
		 return content;
	}
	
	public static String postCluster(String urlString, String param) throws Exception {
        try {
            String method = "POST";
            String result = null;
            URL url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection) url
                    .openConnection();
            String userpass = "cluster:cluster";
            String basicAuth = "Basic "
                    + javax.xml.bind.DatatypeConverter
                            .printBase64Binary(userpass.getBytes());
            urlConn.setRequestProperty("Authorization", basicAuth);
            urlConn.setRequestMethod(method);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("Content-Type", "application/json;utf-8");
            OutputStreamWriter wr = new OutputStreamWriter(
                    urlConn.getOutputStream());
            wr.write(param);
            wr.flush();
            wr.close();
            boolean success = (urlConn.getResponseCode() == 200);
            if (success) {
                InputStream is = urlConn.getInputStream();
                StringBuffer sb = new StringBuffer();
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        is, "utf-8"));
                String tempLine = rd.readLine();
                while (tempLine != null) {
                    sb.append(tempLine);
                    tempLine = rd.readLine();
                }
                result = sb.toString();
            } else {
                result = urlConn.getResponseMessage();
            }
            urlConn.disconnect();
            urlConn = null;
            return result;
        } catch (Exception e) {
            return "";
        }
    }

	
	
	public  static void main(String[] args) throws Exception{
	String content = HttpTools.postCluster("http://172.18.84.68:15100/","");
	System.out.println(content);
	///api/v1/clusters/xdata2/services/HDFS/components/DATANODE
//		System.out.println(content);
//		String content = HttpTools.getInstance().putService("http://172.18.84.67:8080/api/v1/clusters/xdata2/services/HDFS");
//		System.out.println(content);
//		String content = ComponentHandler.stopDatanode("172.18.84.67", "8080", "xdata2", "xdata69");
//		System.out.println(content);
	}
	
	
}
