package cn.vigor.modules.compute.bean;

public class LogicCond {
	private String andor = "and";//and or
	private String name;
	private String sourceName;
	private String value = "";
	private String op = "=";// = > >= < <= != like
	private String type;
	private String left = "";//左括号
	private String right = "";//右括号
	private String dataFormat="";
	
	private boolean isReEexc=false;
	
	public boolean isReEexc() {
		return isReEexc;
	}

	public void setReEexc(boolean isReEexc) {
		this.isReEexc = isReEexc;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	
	public String getAndor() {
		return andor;
	}
	public void setAndor(String andor) {
		this.andor = andor;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	
}
