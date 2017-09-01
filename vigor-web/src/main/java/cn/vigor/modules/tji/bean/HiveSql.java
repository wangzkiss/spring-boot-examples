package cn.vigor.modules.tji.bean;

import java.util.LinkedList;
import java.util.List;

public class HiveSql extends ModelObject {
	private int type;
	
	private String value = "";
	
	private String masterTable="";
	
	private String dependentTable="";
	
	private List<TableAssociation> associationList=new LinkedList<TableAssociation>();
	
	public List<TableAssociation> getAssociationList() {
		return associationList;
	}

	public void setAssociationList(List<TableAssociation> associationList) {
		this.associationList = associationList;
	}

	public String getDependentTable() {
		return dependentTable;
	}

	public void setDependentTable(String dependentTable) {
		firePropertyChange("dependentTable", this.dependentTable, this.dependentTable = dependentTable);
	}

	public String getMasterTable() {
		return masterTable;
	}

	public void setMasterTable(String masterTable) {
		firePropertyChange("masterTable", this.masterTable, this.masterTable = masterTable);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		firePropertyChange("type", this.type, this.type = type);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
	
	private String dataBaseName="";
	
	public String getDataBaseName() {
		return dataBaseName;
	}

	public void setDataBaseName(String dataBaseName) {
		this.dataBaseName = dataBaseName;
	}

}
