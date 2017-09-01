package cn.vigor.modules.compute.bean;

import java.util.Date;

/**
 * 平台存储字段实体类
 * @author Hewei
 *
 */
public class MetaStoreField extends ModelObject implements Comparable<MetaStoreField>{
	
	private int storeFieldId;
	
	private int storeId;
	
	private String proName;
	
	private String proIndex;
	
	private String dataFormat;
	
	private String proType;
	
	private String proDesc;
	
	private String createBy;
	
	private Date createDate;
	
	private Date updateDate;
	
	private String updateBy;
	
	private char delFlag;
	
	public int getStoreFieldId() {
		return storeFieldId;
	}

	public void setStoreFieldId(int storeFieldId) {
		this.storeFieldId = storeFieldId;
	}

	
	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public String getProIndex() {
		return proIndex;
	}

	public void setProIndex(String proIndex) {
		this.proIndex = proIndex;
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
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

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

	@Override
	public int compareTo(MetaStoreField msf) {
		int flag = Integer.valueOf(this.getProIndex()).compareTo(
				Integer.valueOf(msf.getProIndex()));
			return flag;
	}
	
	private String aliasName = ""; 
	
	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		firePropertyChange("aliasName", this.aliasName, this.aliasName = aliasName);
	}
}
