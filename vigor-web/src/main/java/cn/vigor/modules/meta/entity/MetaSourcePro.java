package cn.vigor.modules.meta.entity;


import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 源数据表信息Entity
 * @author kiss
 * @version 2016-05-17
 */
public class MetaSourcePro extends DataEntity<MetaSourcePro> {
	
	private static final long serialVersionUID = 1L;
	private MetaSource sourceId;		// 外键source_id 父类
	private String proName;		// 名称
	private Integer proIndex;		// 序号
	private String proType;		// 数据类型
	private String dataFormat;		// 数据格式
	private String proExternal;
	
	/**
	 * 属性长度
	 */
	private String columnSize;
	
	/**
	 * 字段类型
	 */
	private int type;
	
	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@JsonIgnore
	@Override
	public void preInsert() {
		
		User user = UserUtils.getUser();
		if (StringUtils.isNotBlank(user.getId())){
			this.updateBy = user;
			this.createBy = user;
		}
		this.updateDate = new Date();
		this.createDate = this.updateDate;
		
	}
	
	
	public MetaSourcePro(String proName, Integer proIndex, String proType, String dataFormat,
			String proExternal, String columnSize) {
		super();
		this.id="";
		this.proName = proName;
		this.proIndex = proIndex;
		this.proType = proType;
		this.dataFormat = dataFormat;
		this.proExternal = proExternal;
		this.columnSize = columnSize;
	}

	public MetaSourcePro() {
		super();
	}

	public MetaSourcePro(String id){
		super(id);
	}

	public MetaSourcePro(MetaSource sourceId){
		this.sourceId = sourceId;
	}
	
	@Length(min=0, max=64, message="外键source_id长度必须介于 0 和 64 之间")
	public MetaSource getSourceId() {
		return sourceId;
	}

	public void setSourceId(MetaSource sourceId) {
		this.sourceId = sourceId;
	}
	
	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
	@ExcelField(title="名称", align=2, sort=2)
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}
	
	@NotNull(message="序号不能为空")
	@ExcelField(title="序号", align=2, sort=3)
	public Integer getProIndex() {
		return proIndex;
	}

	public void setProIndex(Integer proIndex) {
		this.proIndex = proIndex;
	}
	
	@Length(min=1, max=64, message="数据类型长度必须介于 1 和 64 之间")
	@ExcelField(title="数据类型", dictType="pro_type", align=2, sort=4)
	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}
	
	//@Length(min=1, max=64, message="数据格式长度必须介于 1 和 64 之间")
	@ExcelField(title="数据格式", align=2, sort=5)
	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
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

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }
	
}