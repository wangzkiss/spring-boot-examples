package cn.vigor.modules.compute.bean;

import java.util.Date;
import java.util.List;

/**
 * 结果集 实体类
 * @author Hewei
 *
 */
public class ComputeMetaResult extends ModelObject{
	
	private int resultId;
	
	private int repoId;
	
	private String resultName;
	
	private int resultType;
	
	private String delimiter;
	
	private String resultDesc;
	
	private String resultFile;
	
	private Date createTime;
	
	private String createUser;
	
	private Date updateTime;
	
	private char delFlag;
	
	private List<MetaResultField> metaResultFields ;
	
	public List<MetaResultField> getMetaResultFields() {
		return metaResultFields;
	}

	public void setMetaResultFields(List<MetaResultField> metaResultFields) {
		this.metaResultFields = metaResultFields;
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public int getRepoId() {
		return repoId;
	}

	public void setRepoId(int repoId) {
		this.repoId = repoId;
	}

	public String getResultName() {
		return resultName;
	}

	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	public int getResultType() {
		return resultType;
	}

	public void setResultType(int resultType) {
		this.resultType = resultType;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

	public String getResultFile() {
		return resultFile;
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

}
