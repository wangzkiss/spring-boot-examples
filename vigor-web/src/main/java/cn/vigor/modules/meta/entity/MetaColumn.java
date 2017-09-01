package cn.vigor.modules.meta.entity;


/**
 * 表Entity
 * @author kiss
 * @version 2016-05-13
 */
public class MetaColumn {

	/**
	 * 字段名 
	 */
	private String columnName ;
	/**
	 * 字段类型
	 */
	private String columnType;
	/**
	 * 字段描述
	 */
	private String columnComment;
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getColumnComment() {
		return columnComment;
	}
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}


	
	
	
}
