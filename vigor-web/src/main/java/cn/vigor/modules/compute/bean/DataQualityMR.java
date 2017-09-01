package cn.vigor.modules.compute.bean;

import java.util.ArrayList;
import java.util.List;


public class DataQualityMR extends ModelObject{
	
	private String ip="";
	
	private int port;
	
	private String user="";
	
	private String password="";
	
	private String path="";
	
	private String fileName="";
	
	private String functionClass="";
	
	private String functionJarPath="";
	
	private String functionJarName="";
	
	private int sourceId;
	
	private String sourceName="";
	
	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}

	public String getFunctionClass() {
		return functionClass;
	}

	public void setFunctionClass(String functionClass) {
		this.functionClass = functionClass;
	}

	private String fieldName="";
	
	private int fieldIndex;
	
	private String fieldFormat="";
	
	private String fieldType="";
	
	private String functionId="";

	private String functionName="";
	
	private String functionExpr="";
	
	private String exprType="";
	
	private List<SelfProgramArgument> arguments = new ArrayList<SelfProgramArgument>();

	public void setArguments(List<SelfProgramArgument> arguments) {
		this.arguments = arguments;
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<SelfProgramArgument> getArguments() {
		return arguments;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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
	
	public String getFieldFormat() {
		return fieldFormat;
	}

	public void setFieldFormat(String fieldFormat) {
		this.fieldFormat = fieldFormat;
	}

	public int getFieldIndex() {
		return fieldIndex;
	}

	public void setFieldIndex(int fieldIndex) {
		this.fieldIndex = fieldIndex;
	}
	
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
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
	
	public String getFunctionExpr() {
		return functionExpr;
	}

	public void setFunctionExpr(String functionExpr) {
		this.functionExpr = functionExpr;
	}

	public String getExprType() {
		return exprType;
	}

	public void setExprType(String exprType) {
		this.exprType = exprType;
	}
	
	private String shellPath;
	
	public String getShellPath() {
		return shellPath;
	}

	public void setShellPath(String shellPath) {
		this.shellPath = shellPath;
	}
}
