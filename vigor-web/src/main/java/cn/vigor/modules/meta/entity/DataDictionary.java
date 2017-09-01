package cn.vigor.modules.meta.entity;
import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 数据字典Entity
 * @author kiss
 * @version 2016-06-13
 */
public class DataDictionary extends DataEntity<DataDictionary> {
	
	private static final long serialVersionUID = 1L;
	private String columnName;		// 字段名
	private String columnType;		// 字段类型
	private String columnKey;		// 主键
	private String columnComment;		// 字段说明
	private String tableName;		// 表名
	private String tableComment;		// 表说明
	private String dataBaseName="ebd_copy";

    public String getDataBaseName()
    {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName)
    {
        this.dataBaseName = dataBaseName;
    }

    public DataDictionary() {
		super();
	}

	public DataDictionary(String id){
		super(id);
	}

	@Length(min=1, max=64, message="字段名长度必须介于 1 和 64 之间")
	@ExcelField(title="字段名", align=2, sort=1)
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	@ExcelField(title="字段类型", align=2, sort=2)
	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	@Length(min=1, max=3, message="主键长度必须介于 1 和 3 之间")
	@ExcelField(title="主键", align=2, sort=3)
	public String getColumnKey() {
		return columnKey;
	}

	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
	
	@Length(min=1, max=1024, message="字段说明长度必须介于 1 和 1024 之间")
	@ExcelField(title="字段说明", align=2, sort=4)
	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
	
	@Length(min=1, max=64, message="表名长度必须介于 1 和 64 之间")
	@ExcelField(title="表名", align=2, sort=5)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Length(min=1, max=2048, message="表说明长度必须介于 1 和 2048 之间")
	@ExcelField(title="表说明", align=2, sort=6)
	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}
	
}