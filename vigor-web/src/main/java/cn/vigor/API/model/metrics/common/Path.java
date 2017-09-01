package cn.vigor.API.model.metrics.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Path {

	private String path;
	
	private String starttime;
	
	private String endtime;
	
	private String step;
	
	
	
	public String toPath(){
		return path;
	}
	
	public String toRangePath(){
		String param = path+"["+starttime+","+endtime+","+step+"]";
		return param;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}
	
	public static String search(Map m,List<String> keys,int current,int level){
		if(current<level-1){
			Map mp = (Map) m.get(keys.get(current));
			return search(mp,keys,current+1,level);
		}else{
			return (String) m.get(keys.get(current));
		}
	}
	
	
	
	public static String search(Map m,String[] keys,int current,int level){
		if(current<level-1){
			Map mp = (Map) m.get(keys[current]);
			return search(mp,keys,current+1,level);
		}else{
			return (String) m.get(keys[current]);
		}
	}
	
	public static void main(String[] args){
		Map<String,Map> m1 = new HashMap<String,Map>();
		Map<String,Map> m2 = new HashMap<String,Map>();
		Map<String,Map> m3 = new HashMap<String,Map>();
		Map<String,String> m4 = new HashMap<String,String>();
		m4.put("d", "nihao");
		m3.put("c", m4);
		m2.put("b", m3);
		m1.put("a", m2);
		
		List<String> ls = new ArrayList<String>();
		ls.add("a");
		ls.add("b");
		ls.add("c");
		ls.add("d");
		
		String[] arr = new String[4];
		arr[0]="a";
		arr[1]="b";
		arr[2]="c";
		arr[3]="d";
		
		String result = search(m1,arr,0,arr.length);
		System.out.println(result);
		
	}
	
	
}
