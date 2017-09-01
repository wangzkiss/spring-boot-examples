package cn.vigor.modules.compute.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 平台存储实体类
 * @author Hewei
 *
 */
public class ComputeMetaStore extends ModelObject{
	
	/**
     * 
     */
    private static final long serialVersionUID = -1507914030684049183L;

    private int storeId;
	
	private int repoId;
	
	private String storeName;
	
	private int storeType;
	
	private String delimiter;
	
	private String storeDesc;
	
	private String storeFile;
	
	private int stroeStatus;
	
	private String hdfsDir;
	
	private String hdfsInfo;

	private String storeExternal;
	
	private Date createTime;
	
	private Date updateTime;
	
	private String createUser;
	
	private char delFlag;
	
	private List<MetaStoreField> metaStoreFields ;
	
	private int loginUserId;
	
	public int getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(int loginUserId) {
		this.loginUserId = loginUserId;
	}

	/**
	 * 类型名称
	 */
	private String storeTypeName;
	
	/**
	 * 获取 类型名称
	 * @return storeTypeName 类型名称
	 */ 
	public String getStoreTypeName() {
		return storeTypeName;
	}

	/**
	 * 设置 类型名称
	 * @param storeTypeName 类型名称
	 */
	public void setStoreTypeName(String storeTypeName) {
		this.storeTypeName = storeTypeName;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	
	public List<MetaStoreField> getMetaStoreFields() {
		return metaStoreFields;
	}

	public void setMetaStoreFields(List<MetaStoreField> metaStoreFields) {
		this.metaStoreFields = metaStoreFields;
	}

	public int getRepoId() {
		return repoId;
	}

	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public int getStoreType() {
		return storeType;
	}

	public void setStoreType(int storeType) {
		this.storeType = storeType;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getStoreDesc() {
		return storeDesc;
	}

	public void setStoreDesc(String storeDesc) {
		this.storeDesc = storeDesc;
	}

	public String getStoreFile() {
		return storeFile;
	}

	public void setStoreFile(String storeFile) {
		this.storeFile = storeFile;
	}

	public int getStroeStatus() {
		return stroeStatus;
	}

	public void setStroeStatus(int stroeStatus) {
		this.stroeStatus = stroeStatus;
	}

	public String getHdfsDir() {
		return hdfsDir;
	}

	public void setHdfsDir(String hdfsDir) {
		this.hdfsDir = hdfsDir;
	}
	
	public String getHdfsInfo() {
		return hdfsInfo;
	}

	public void setHdfsInfo(String hdfsInfo) {
		this.hdfsInfo = hdfsInfo;
	}
	
	public String getStoreExternal() {
		return storeExternal;
	}

	public void setStoreExternal(String storeExternal) {
		this.storeExternal = storeExternal;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}
}
