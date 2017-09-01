package cn.vigor.modules.jars.entity;


import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 函数管理Entity
 * @author kiss
 * @version 2016-06-13
 */
public class FunctionParam extends DataEntity<FunctionParam> {
	
	private static final long serialVersionUID = 1L;
	private String paramId;		// 函数参数表id 主键
	private Function functionId;		// 函数id，关联函数表id 父类
	private String paramName;		// 函数参数名
	private Integer paramIndex;		// 函数参数索引，从1开始
	private Integer paramSelect;		// 参数输入方式
	private String paramType;		// 参数类型
	private String paramDesc;		// 参数描述
	
	public FunctionParam() {
		super();
	}

	public FunctionParam(String id){
		super(id);
	}

	public FunctionParam(Function functionId){
		this.functionId = functionId;
	}

	@Length(min=1, max=10, message="函数参数表id 主键长度必须介于 1 和 10 之间")
	@ExcelField(title="函数参数表id 主键", align=2, sort=0)
	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	
	@Length(min=1, max=10, message="函数id，关联函数表id长度必须介于 1 和 10 之间")
	public Function getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Function functionId) {
		this.functionId = functionId;
	}
	
	@Length(min=1, max=50, message="函数参数名长度必须介于 1 和 50 之间")
	@ExcelField(title="函数参数名", align=2, sort=2)
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	
	@ExcelField(title="函数参数索引，从1开始", align=2, sort=3)
	public Integer getParamIndex() {
		return paramIndex;
	}

	public void setParamIndex(Integer paramIndex) {
		this.paramIndex = paramIndex;
	}
	
	@ExcelField(title="参数输入方式", dictType="", align=2, sort=4)
	public Integer getParamSelect() {
		return paramSelect;
	}

	public void setParamSelect(Integer paramSelect) {
		this.paramSelect = paramSelect;
	}
	
	@Length(min=0, max=50, message="参数类型长度必须介于 0 和 50 之间")
	@ExcelField(title="参数类型", align=2, sort=5)
	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	
	@Length(min=0, max=500, message="参数描述长度必须介于 0 和 500 之间")
	@ExcelField(title="参数描述", align=2, sort=6)
	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
	
}