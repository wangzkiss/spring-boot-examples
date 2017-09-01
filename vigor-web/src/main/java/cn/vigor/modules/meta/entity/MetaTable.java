package cn.vigor.modules.meta.entity;


/**
 * 表Entity
 * @author kiss
 * @version 2016-05-13
 */
public class MetaTable {


	/**
	 * 序号
	 */
	private int index=1;
	
	/**
	 * 状态  已抽取  未抽取
	 */
	private String tablestatus="未抽取";
	
	/**
     * 状态  已抽取 对应的数据id
     */
    private String existId;
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
     * 抽取人
     */
    private String operator;
	/**
	 * 表描述
	 */
	private String tableDesc;
	
	/**
	 * 类型 1 表 2 视图 3存储过程 4 函数
	 */
	private int type = 1;
	
	
	
	public String getOperator()
    {
        return operator;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    public String getExistId()
    {
        return existId;
    }
    public void setExistId(String existId)
    {
        this.existId = existId;
    }
    public String getTablestatus()
    {
        return tablestatus;
    }
    public void setTablestatus(String tablestatus)
    {
        this.tablestatus = tablestatus;
    }
    public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableDesc() {
		return tableDesc;
	}
	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
    public int getType()
    {
        return type;
    }
    public void setType(int type)
    {
        this.type = type;
    }
	
	
	
}
