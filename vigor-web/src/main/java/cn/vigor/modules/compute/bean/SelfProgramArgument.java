package cn.vigor.modules.compute.bean;

/**
 * 自定义JAR包计算的参数类
 * @author yangtao
 *
 */
public class SelfProgramArgument extends ModelObject{
	private String name = "请输入参数名";
	
	private String value = "请输入参数值";
	
	private String paramId="";

	private String functionId="";
	
	private String paramName="";
	
	private String paramType="";
	
	private String paramDesc="";
	
	private int paramSelect;
	
	public int getParamSelect() {
		return paramSelect;
	}

	public void setParamSelect(int paramSelect) {
		this.paramSelect = paramSelect;
	}

	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		firePropertyChange("index", this.index, this.index = index);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
	

	public String getParamId() {
		return paramId;
	}

	public void setParamId(String paramId) {
		this.paramId = paramId;
	}
	
	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
}
