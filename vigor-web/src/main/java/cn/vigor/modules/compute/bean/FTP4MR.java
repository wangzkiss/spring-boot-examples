package cn.vigor.modules.compute.bean;

import java.util.ArrayList;
import java.util.List;

public class FTP4MR extends ModelObject{
	private String ip;
	
	private int port;
	
	private String user="";
	
	private String userName="";
	
	private String passWord="";
	
	private String path="";
	
	private String fileName="";
	
	private String mainClass="";
	
	private String field="";
	
	private String functionId="";

	private String functionName="";
	
	private String functionClass="";
	
	private String functionJarPath="";
	
	private String functionJarName="";
	
	private String functionType="";

	private String dbName="";
	
	private String etlModeName;
	
	private String ruleExpression;
	
	private String shellPath;
	
	public String getShellPath() {
		return shellPath;
	}

	public void setShellPath(String shellPath) {
		this.shellPath = shellPath;
	}

	public String getRuleExpression() {
		return ruleExpression;
	}

	public void setRuleExpression(String ruleExpression) {
		this.ruleExpression = ruleExpression;
	}

	public String getEtlModeName() {
		return etlModeName;
	}

	public void setEtlModeName(String etlModeName) {
		this.etlModeName = etlModeName;
	}

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
	public String getFunctionType() {
		return functionType;
	}

	public void setFunctionType(String functionType) {
		this.functionType = functionType;
	}
	private List<SelfProgramArgument> arguments = new ArrayList<SelfProgramArgument>();
	
	/*private List<ModelProgramArgument> modelArguments = new ArrayList<ModelProgramArgument>();

	public List<ModelProgramArgument> getModelArguments() {
		return modelArguments;
	}

	public void setModelArguments(List<ModelProgramArgument> modelArguments) {
		this.modelArguments = modelArguments;
	}*/

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		firePropertyChange("mainClass", this.mainClass, this.mainClass = mainClass);
	}

	public List<SelfProgramArgument> getArguments() {
		return arguments;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
	public String getFunctionClass() {
		return functionClass;
	}

	public void setFunctionClass(String functionClass) {
		this.functionClass = functionClass;
	}

	public String getFunctionJarPath() {
		return functionJarPath;
	}

	public void setFunctionJarPath(String functionJarPath) {
		this.functionJarPath = functionJarPath;
	}

	public String getFunctionJarName() {
		return functionJarName;
	}

	public void setFunctionJarName(String functionJarName) {
		this.functionJarName = functionJarName;
	}
	
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
}
