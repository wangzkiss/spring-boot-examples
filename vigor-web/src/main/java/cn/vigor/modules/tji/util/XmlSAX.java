package cn.vigor.modules.tji.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlSAX extends DefaultHandler
{
    private StringBuffer buf;  
    
    private String str;
    
    private String currentTag = null;
    
    private String nodeName = null;
    
    public static Map<String,String> map = new HashMap<String,String>();
    
    /**
     * MR/spark任务函数参数列表
     */
    public static List<HashMap<String,String>> mrlist = null;
    
    private HashMap<String,String> mp = null;
    
    public static Map<String,String> mrAttrs = null;
    
    /**
     * 数据质量任务函数参数列表
     */
    public static List<HashMap<String, String>> dqslist = null;
    public static HashMap<String,String> dqsAttrs = null;
    public static List<HashMap<String, String>> dqmlist = null;
    
    public XmlSAX(){  
        super();  
    }  
    
    public XmlSAX(String nodeName){
        this.nodeName = nodeName;
    }
    
    public List<HashMap<String, String>> getMrlist() {
        return mrlist;
    }
    
    public void startDocument() throws SAXException{  
         buf = new StringBuffer();  
         mrlist = new ArrayList<HashMap<String, String>>();
         dqslist = new ArrayList<HashMap<String, String>>();
         dqmlist = new ArrayList<HashMap<String, String>>();
         System.out.println("*******开始解析XML*******");  
    }  
      
    public void endDocument() throws SAXException{          
         System.out.println("*******XML解析结束*******");  
    }  
    
    @Override
    public void startElement(String url, String localName, String qName,
            Attributes attributes) throws SAXException
    {
//        System.out.println("startElement localName: " + localName + "qName:" + qName);
        currentTag = qName;
        if (nodeName.equals(qName)) { // nodeName 是 person ,由构造函数传入。
            mp = new HashMap<String, String>();
//            mp.put("id", attributes.getValue("id"));
        }
        if(qName.equals("mr")){
            mrAttrs = new HashMap<String, String>();
        }
        if(qName.equals("dqm")){
            dqsAttrs = new HashMap<String, String>();
        }
    }
       
    public void endElement(String namespaceURI,String localName,String fullName )throws SAXException{
//        System.out.println("endElement in " + localName);
        if ("arg".equals(fullName)) {
            mrlist.add(mp);
            dqslist.add(mp);
        }
        if("dqm".equals(fullName)){
            dqmlist.add(dqsAttrs);
        }
        map.put(fullName, buf.toString().replaceAll("\r|\n", "").trim());
//        System.out.println("节点="+fullName+",value="+buf+",长度="+buf.length());  
        buf.delete(0,buf.length());
        currentTag = null;
    }  
      
    public void characters( char[] chars, int start, int length )throws SAXException{  
        //将元素内容累加到StringBuffer中 
        buf.append(chars,start,length);  
        if(mrAttrs!=null){
            if("functionType".equals(currentTag)){
                mrAttrs.put("functionType", new String(chars, start, length));
            }
            if("functionId".equals(currentTag)){
                mrAttrs.put("functionId", new String(chars, start, length));
            }
            if("functionJarName".equals(currentTag)){
                mrAttrs.put("functionJarName", new String(chars, start, length));
            }
            if("functionJarPath".equals(currentTag)){
                mrAttrs.put("functionJarPath", new String(chars, start, length));
            }
            if("functionName".equals(currentTag)){
                mrAttrs.put("functionName", new String(chars, start, length));
            }
            if("ip".equals(currentTag)){
                mrAttrs.put("ip", new String(chars, start, length));
            }
            if("functionClass".equals(currentTag)){
                mrAttrs.put("functionClass", new String(chars, start, length));
            }
            if("port".equals(currentTag)){
                mrAttrs.put("port", new String(chars, start, length));
            }
            if("userName".equals(currentTag)){
                mrAttrs.put("userName", new String(chars, start, length));
            }
            if("passWord".equals(currentTag)){
                mrAttrs.put("passWord", new String(chars, start, length));
            }
            if("dbName".equals(currentTag)){
                mrAttrs.put("dbName", new String(chars, start, length));
            }
            if("etlModeName".equals(currentTag)){
                mrAttrs.put("etlModeName", new String(chars, start, length));
            }
            if("dbType".equals(currentTag)){
                mrAttrs.put("dbType", new String(chars, start, length));
            }
            if("shellPath".equals(currentTag)){
                mrAttrs.put("shellPath", new String(chars, start, length));
            }
            if("ruleExpression".equals(currentTag)){
                mrAttrs.put("ruleExpression", new String(chars, start, length));
            }
        }
        
        //数据质量
        if(dqsAttrs!=null){
            if("fieldName".equals(currentTag)){
                dqsAttrs.put("fieldName", new String(chars, start, length));
            }
            if("fieldIndex".equals(currentTag)){
                dqsAttrs.put("fieldIndex", new String(chars, start, length));
            }
            if("fieldFormat".equals(currentTag)){
                dqsAttrs.put("fieldFormat", new String(chars, start, length));
            }
            if("fieldType".equals(currentTag)){
                dqsAttrs.put("fieldType", new String(chars, start, length));
            }
            if("functionId".equals(currentTag)){
                dqsAttrs.put("functionId", new String(chars, start, length));
            }
            if("functionName".equals(currentTag)){
                dqsAttrs.put("functionName", new String(chars, start, length));
            }
            if("functionClass".equals(currentTag)){
                dqsAttrs.put("functionClass", new String(chars, start, length));
            }
            if("functionJarName".equals(currentTag)){
                dqsAttrs.put("functionJarName", new String(chars, start, length));
            }
            if("functionJarPath".equals(currentTag)){
                dqsAttrs.put("functionJarPath", new String(chars, start, length));
            }
            if("functionExpr".equals(currentTag)){
                dqsAttrs.put("functionExpr", new String(chars, start, length));
            }
            if("exprType".equals(currentTag)){
                dqsAttrs.put("exprType", new String(chars, start, length));
            }
            if("sourceId".equals(currentTag)){
                dqsAttrs.put("sourceId", new String(chars, start, length));
            }
            if("sourceName".equals(currentTag)){
                dqsAttrs.put("sourceName", new String(chars, start, length));
            }
        }
        
        if("index".equals(currentTag)){
            if(mp!=null){
                mp.put("index", new String(chars, start, length));
            }
        }
        if("name".equals(currentTag)){
            mp.put("name", new String(chars, start, length));
        }
        if("value".equals(currentTag)){
            mp.put("value", new String(chars, start, length));
        }
        if("paramType".equals(currentTag)){
            mp.put("paramType", new String(chars, start, length));
        }
        if("paramSelect".equals(currentTag)){
            mp.put("paramSelect", new String(chars, start, length));
        }
    }
    
    

    public String getStr()
    {
        return str;
    }

    public void setStr(String str)
    {
        this.str = str;
    }
}
