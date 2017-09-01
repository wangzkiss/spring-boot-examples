package cn.vigor.modules.tji.bean;

import java.util.List;



public class ComFunction extends ModelObject{
	private String id;
	
	/**
	 * 函数名称
	 */
	private String name;
	
	/**
	 * 函数类型
	 */
	private String type;
	
	/**
	 * 函数描述
	 */
	private String desc;
	
	/**
	 * 函数（）中替换的字符串
	 */
	private String functionClassName;
	
	/**
	 * 函数返回类型
	 */
	private String returnType="";

	/**
	 * 函数的参数的个数
	 */
	private int paraNumber;
	
	/**
	 * 函数中各参数的类型
	 */
	private String paraType;
	
	/**
	 * 函数中的参数字段
	 */
	private String calculateField;
	
	/**
	 * 函数别名
	 */
	private String alias;
	
	/**
	 * 扩充字段
	 */
	private String expansionField;
	
	/**
	 * 获取扩充字段 
	 * @return String
	 */
	public String getExpansionField() {
		return expansionField;
	}

	/**
	 * 设置扩充字段 
	 * @param expansionField
	 */
	public void setExpansionField(String expansionField) {
		this.expansionField = expansionField;
	}

	private int jarId;
	
	private String ruleExpression="";
	
	private String ruleType="";
	
	private String userName="";

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	private String passWord="";
	
	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	private String ip="";
	
	private int port;
	
	private String dbName="";
	
	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	private int dbType;
	
	public int getDbType() {
		return dbType;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	private List<SelfProgramArgument> selfProgramArgument;
	
	public List<SelfProgramArgument> getSelfProgramArgument() {
		return selfProgramArgument;
	}

	public void setSelfProgramArgument(List<SelfProgramArgument> selfProgramArgument) {
		this.selfProgramArgument = selfProgramArgument;
	}

	public int getJarId() {
		return jarId;
	}

	public void setJarId(int jarId) {
		this.jarId = jarId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getCalculateField() {
		return calculateField;
	}

	public void setCalculateField(String calculateField) {
		this.calculateField = calculateField;
	}

	public String getParaType() {
		return paraType;
	}

	public void setParaType(String paraType) {
		this.paraType = paraType;
	}

	public int getParaNumber() {
		return paraNumber;
	}

	public void setParaNumber(int paraNumber) {
		this.paraNumber = paraNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		firePropertyChange("desc", this.desc, this.desc = desc);
	}

	public String getFunctionClassName() {
		return functionClassName;
	}

	public void setFunctionClassName(String functionClassName) {
		firePropertyChange("functionClassName", this.functionClassName, this.functionClassName = functionClassName);
	}

	public String getType() {//shark
		return type;
	}

	public void setType(String type) {
		firePropertyChange("type", this.type, this.type = type);
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public String getRuleExpression() {
		return ruleExpression;
	}

	public void setRuleExpression(String ruleExpression) {
		this.ruleExpression = ruleExpression;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
}
