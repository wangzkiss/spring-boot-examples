package cn.vigor.modules.meta.util;

import java.util.HashMap;
import java.util.Map;

public class PrivilegeException extends Exception {

	private String msg="";
	private String code="101";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		HashMap<String, String> msgMap=new HashMap<String, String>();
		msgMap.put("101","101：缺少系统权限:"+msg);
		msg=msg==""?"该操作":msg;
		msgMap.put("102","102：缺少数据权限，用户对此数据无"+msg+"权限");
		msgMap.put("103","103：缺少数据权限，用户对此数据无删除权限");
		msgMap.put("104","104：缺少数据权限，用户对此数据无修改权限");
		msgMap.put("105","105：缺少数据权限，用户对此无停止权限");
		msgMap.put("106","106：缺少数据权限，用户对此无启动权限");
		msgMap.put("201","201：不能删除当前登录用户！");
		String desc= msgMap.get(code)==null?msg: msgMap.get(code);
		return desc;
	}
	public  PrivilegeException(String code,String msg){
		super(code+msg);
		this.code=code;
		this.msg=msg;	
 	}
	public  PrivilegeException(String code){
		super(code);
		this.code=code;
 	}
	public  PrivilegeException(String code,String msg,Exception e){
		super(code+msg,e);
	}
}
