package cn.vigor.modules.jars.entity;


import java.util.HashMap;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 函数管理Entity
 * @author kiss
 * @version 2016-06-13
 */
public class Function extends DataEntity<Function> {
	
	private static final long serialVersionUID = 1L;
	private String functionName;		// 函数名，存储过程名，脚本名称
	private Integer functionType;		// 函数类型，关联函数类型表
	private String functionDesc;		// 函数描述，用户名
	private String functionClass;		// 函数关联执行类，脚本全路径
	private String returnType;		// 返回类型，数据库名
	private Integer paramNums;		// 参数个数
	private String paramType;
	private int functionStatus;
	// 参数类型
	private MapreduceJar jarId;		// 关联jar包，端口
	private String expansionField;		// json格式，名称如下：rule_type（规则类型，1正则，2非正则，数据库类型，1.mysql，2 oracle）rule_expression 正则表达式user_name 用户名password 密码ip ipport 端口db_name 库名db_type 数据库类型
	private List<FunctionParam> functionParamList = Lists.newArrayList();		// 子表列表
	private List<HashMap<String, Object>> functionParamMaps;// 子表列表
	private Expension expansion;
	
	public Expension getExpansion()
    {
	   
        return expansion;
    }

    public void setExpansion(Expension expansion)
    {
        this.expansion = expansion;
    }

    public int getFunctionStatus()
    {
        return functionStatus;
    }

    public void setFunctionStatus(int functionStatus)
    {
        this.functionStatus = functionStatus;
    }

    public Function() {
		super();
	}

	public Function(String id){
		super(id);
	}

	@Length(min=1, max=50, message="函数名，存储过程名，脚本名称长度必须介于 1 和 50 之间")
	@ExcelField(title="函数名，存储过程名，脚本名称", align=2, sort=1)
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	@NotNull(message="函数类型，关联函数类型表不能为空")
	@ExcelField(title="函数类型，关联函数类型表", dictType="", align=2, sort=2)
	public Integer getFunctionType() {
		return functionType;
	}

	public void setFunctionType(Integer functionType) {
		this.functionType = functionType;
	}
	
	@Length(min=0, max=500, message="函数描述，用户名长度必须介于 0 和 500 之间")
	@ExcelField(title="函数描述，用户名", align=2, sort=3)
	public String getFunctionDesc() {
		return functionDesc;
	}

	public void setFunctionDesc(String functionDesc) {
		this.functionDesc = functionDesc;
	}
	
	@Length(min=0, max=250, message="函数关联执行类，脚本全路径长度必须介于 0 和 250 之间")
	@ExcelField(title="函数关联执行类，脚本全路径", align=2, sort=4)
	public String getFunctionClass() {
		return functionClass;
	}

	public void setFunctionClass(String functionClass) {
		this.functionClass = functionClass;
	}
	
	@Length(min=0, max=50, message="返回类型，数据库名长度必须介于 0 和 50 之间")
	@ExcelField(title="返回类型，数据库名", align=2, sort=5)
	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	@ExcelField(title="参数个数", align=2, sort=6)
	public Integer getParamNums() {
		return paramNums;
	}

	public void setParamNums(Integer paramNums) {
		this.paramNums = paramNums;
	}
	
	@Length(min=0, max=50, message="参数类型长度必须介于 0 和 50 之间")
	@ExcelField(title="参数类型", align=2, sort=7)
	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	
	@ExcelField(title="关联jar包，端口", align=2, sort=8)
	public MapreduceJar getJarId() {
		return jarId;
	}

	public void setJarId(MapreduceJar jarId) {
		this.jarId = jarId;
	}
	
	public String getExpansionField() {
		return expansionField;
	}

	public void setExpansionField(String expansionField) {
		this.expansionField = expansionField;
	}
	
	public List<FunctionParam> getFunctionParamList() {
		return functionParamList;
	}

	public void setFunctionParamList(List<FunctionParam> functionParamList) {
		this.functionParamList = functionParamList;
	}

    public List<HashMap<String, Object>> getFunctionParamMaps()
    {
        return functionParamMaps;
    }

    public void setFunctionParamMaps(List<HashMap<String, Object>> functionParamMaps)
    {
        this.functionParamMaps = functionParamMaps;
    }
}