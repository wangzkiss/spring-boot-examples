package cn.vigor.modules.compute.bean;

import java.util.Date;

/**
 * 结果集字段实体类
 * @author Hewei
 *
 */
public class MetaResultField extends ModelObject{
	
	private int resultFieldId;
	
	private int resultId;
	
	private String proName;
	
	private int proIndex;
	
	private String proType;
	
	private String dataFormat;
	
	private String proDesc;
	
	private String createBy;
	
	private Date createDate;
	
	private String updateBy;
	
	private Date updateDate;
	
	private char delFlag;
	
	public int getResultFieldId() {
		return resultFieldId;
	}

	public void setResultFieldId(int resultFieldId) {
		this.resultFieldId = resultFieldId;
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public int getProIndex() {
		return proIndex;
	}

	public void setProIndex(int proIndex) {
		this.proIndex = proIndex;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

}
