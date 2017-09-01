package cn.vigor.modules.meta.entity;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 结果数据集Entity
 * @author kiss
 * @version 2016-05-17
 */
public class MetaResultPro extends DataEntity<MetaResultPro> {
	
	private static final long serialVersionUID = 1L;
	private MetaResult resultId;		// 外键result_id 父类
	private String proName;		// 名称
	private Integer proIndex;		// 序号
	private String proType;		// 数据类型
	private String dataFormat;		// 数据格式
	private String proDesc;		// 备注信息
	private int type;
	/**
	 * 属性长度
	 */
	private String columnSize;
	
	public int getType()
    {
        return type;
    }
	
    public MetaResultPro(String proName, Integer proIndex, String proType, String dataFormat, String proDesc, int type,
			String columnSize) {
		super();
		this.id="";
		this.proName = proName;
		this.proIndex = proIndex;
		this.proType = proType;
		this.dataFormat = dataFormat;
		this.proDesc = proDesc;
		this.type = type;
		this.columnSize = columnSize;
	}

	public void setType(int type)
    {
        this.type = type;
    }

    public MetaResultPro() {
		super();
	}

	public MetaResultPro(String id){
		super(id);
	}

	public MetaResultPro(MetaResult resultId){
		this.resultId = resultId;
	}

	@Length(min=1, max=64, message="外键result_id长度必须介于 1 和 64 之间")
	public MetaResult getResultId() {
		return resultId;
	}

	public void setResultId(MetaResult resultId) {
		this.resultId = resultId;
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
	@ExcelField(title="数据类型", align=2, sort=4)
	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}
	
	@Length(min=1, max=64, message="数据格式长度必须介于 1 和 64 之间")
	@ExcelField(title="数据格式", align=2, sort=5)
	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	
	@Length(min=0, max=255, message="备注信息长度必须介于 0 和 255 之间")
	@ExcelField(title="备注信息", align=2, sort=6)
	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
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