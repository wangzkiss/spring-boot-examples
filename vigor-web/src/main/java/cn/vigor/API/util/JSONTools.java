package cn.vigor.API.util;


import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JSONTools {
	public static Gson gson = new Gson();
	
	public static  <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
	    return gson.fromJson(json, classOfT);
	  }
	
	 public static String toJson(Object src) {
		    return gson.toJson(src);
	 }
	 
	 
	public static String search(Map m,String[] keys,int current,int level){
		if(current<level-1){
			if(m.get(keys[current])==null){
				return null;
			}else{
				Map mp = (Map) m.get(keys[current]);
				return search(mp,keys,current+1,level);
			}
		}else{
			if(m.get(keys[current])!=null){
				return  m.get(keys[current]).toString();
			}else{
				return null;
			}
			
		}
	}
	
	public static void main(String[] args){
		Map m = new HashMap();
		
		m.put("a", "nihao");
		String[] keys = new String[1];
		keys[0]="a";
		System.out.println(search(m,keys,0,1));
	}
}
