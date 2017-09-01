package cn.vigor.modules.jars.entity;


import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 执行包管理Entity
 * @author kiss
 * @version 2016-06-13
 */
public class MapreduceJar extends DataEntity<MapreduceJar> {
	
	private static final long serialVersionUID = 1L;
	private String jarName;		// jar包名称
	private String jarPath;		// jar包路径
	private Integer jarType;		// jar类型，1系统自带，2自定上传
	
	public MapreduceJar() {
		super();
	}

	public MapreduceJar(String id){
		super(id);
	}

	@Length(min=1, max=50, message="jar包名称长度必须介于 1 和 50 之间")
	@ExcelField(title="jar包名称", align=2, sort=1)
	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}
	
	@Length(min=1, max=50, message="jar包路径长度必须介于 1 和 50 之间")
	@ExcelField(title="jar包路径", align=2, sort=2)
	public String getJarPath() {
		return jarPath;
	}

	public void setJarPath(String jarPath) {
		this.jarPath = jarPath;
	}
	
	@ExcelField(title="jar类型，1系统自带，2自定上传", dictType="", align=2, sort=3)
	public Integer getJarType() {
		return jarType;
	}

	public void setJarType(Integer jarType) {
		this.jarType = jarType;
	}
	
}