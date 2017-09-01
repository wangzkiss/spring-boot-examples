package cn.vigor.modules.meta.entity;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 存储数据信息Entity
 * @author kiss
 * @version 2016-05-17
 */
public class MetaStorePro extends DataEntity<MetaStorePro> {
	
	private static final long serialVersionUID = 1L;
	private MetaStore storeId;		// 外键 父类
	private String proName;		// 名称
	private Integer proIndex;		// 顺序
	private String dataFormat;		// 数据格式
	private String proType;		// 数据类型
	private String proDesc;     // 备注信息
	private String proExternal;
	/**
	 * 属性长度
	 */
	private String columnSize;
	public String getProDesc()
    {
        return proDesc;
    }

    public void setProDesc(String proDesc)
    {
        this.proDesc = proDesc;
    }
    
    public MetaStorePro(String proName, Integer proIndex, String dataFormat, String proType, String proDesc,
			String proExternal, String columnSize) {
		super();
		this.id="";
		this.proName = proName;
		this.proIndex = proIndex;
		this.dataFormat = dataFormat;
		this.proType = proType;
		this.proDesc = proDesc;
		this.proExternal = proExternal;
		this.columnSize = columnSize;
	}

	/**
	 * 区别是分区字段      还是列祖字段   1 分区字段  2  普通字段 3  主键
	 */
	private int type;
	
	public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public MetaStorePro() {
		super();
	}

	public MetaStorePro(String id){
		super(id);
	}

	public MetaStorePro(MetaStore storeId){
		this.storeId = storeId;
	}

	public MetaStore getStoreId() {
		return storeId;
	}

	public void setStoreId(MetaStore storeId) {
		this.storeId = storeId;
	}
	
	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	@ExcelField(title="名称", align=2, sort=2)
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}
	
	@NotNull(message="顺序不能为空")
	@ExcelField(title="顺序", align=2, sort=3)
	public Integer getProIndex() {
		return proIndex;
	}

	public void setProIndex(Integer proIndex) {
		this.proIndex = proIndex;
	}
	
	//@Length(min=1, max=64, message="数据格式长度必须介于 1 和 64 之间")
	@ExcelField(title="数据格式", align=2, sort=4)
	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	
	@Length(min=1, max=64, message="数据类型长度必须介于 1 和 64 之间")
	@ExcelField(title="数据类型", dictType="data_type", align=2, sort=5)
	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

    public String getProExternal()
    {
        return proExternal;
    }

    public void setProExternal(String proExternal)
    {
        this.proExternal = proExternal;
    }

    public String getColumnSize()
    {
        return columnSize;
    }

    public void setColumnSize(String columnSize)
    {
        this.columnSize = columnSize;
    }
	
}