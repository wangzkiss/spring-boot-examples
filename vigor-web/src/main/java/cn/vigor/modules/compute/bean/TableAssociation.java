package cn.vigor.modules.compute.bean;

import java.util.ArrayList;
import java.util.List;


public class TableAssociation {

	/**
	 * 主表
	 */
	private String masterTable = "";

	/**
	 *  从表的名称.
	 */
	private String tableName = "";

	private String logic= "";
	
	private String alias="";
	
	private String fromTableType;
	
	private String masterTableType;
	
	public String getMasterTableType() {
		return masterTableType;
	}

	public void setMasterTableType(String masterTableType) {
		this.masterTableType = masterTableType;
	}

	public String getFromTableType() {
		return fromTableType;
	}

	public void setFromTableType(String fromTableType) {
		this.fromTableType = fromTableType;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	private List<AssociationCondition> associationConditionList = new ArrayList<AssociationCondition>();

	public String getMasterTable() {
		return masterTable;
	}

	public void setMasterTable(String masterTable) {
		this.masterTable = masterTable;
	}

	/**
	 * 获得从表的名称.
	 * @return
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 设置从表的名称
	 * @param fromTable
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<AssociationCondition> getAssociationConditionList() {
		return associationConditionList;
	}

	public void setAssociationConditionList(
			List<AssociationCondition> associationConditionList) {
		this.associationConditionList = associationConditionList;
	}
	
	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}
}
