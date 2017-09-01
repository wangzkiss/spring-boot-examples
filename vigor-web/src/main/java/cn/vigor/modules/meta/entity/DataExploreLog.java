package cn.vigor.modules.meta.entity;

import cn.vigor.common.persistence.DataEntity;

/**
 * 数据探索查询日志类
 * @author 38342 huzeyuan
 * @version v4.0
 * date:20170717
 *
 */
public class DataExploreLog extends DataEntity<DataExploreLog>{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 连接信息ID
	 */
	private String repoId;
	
	/**
	 * 查询时间
	 */
	private String searchTime;
	
	/**
	 * 查询状态
	 */
	private int searchStatus;
	
	/**
	 * 查询的sql语句
	 */
	private String searchSql;
	
	/**
	 * 当前查询用户
	 */
	private String createUser;
	
	/**
	 * 数据探索查询类型
	 */
	private String searchType;
	
	/**
	 * 查询失败信息
	 */
	private String errorMsg;
	
	/**
	 * sql中涉及的表
	 */
	private String relationTables;

	public String getSearchTime() {
		return searchTime;
	}

	public int getSearchStatus() {
		return searchStatus;
	}

	public String getSearchSql() {
		return searchSql;
	}

	public String getCreateUser() {
		return createUser;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}

	public void setSearchStatus(int searchStatus) {
		this.searchStatus = searchStatus;
	}

	public void setSearchSql(String searchSql) {
		this.searchSql = searchSql;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	
	public String getRepoId() {
		return repoId;
	}

	public void setRepoId(String repoId) {
		this.repoId = repoId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataExploreLog [searchTime=");
		builder.append(searchTime);
		builder.append(", searchStatus=");
		builder.append(searchStatus);
		builder.append(", searchSql=");
		builder.append(searchSql);
		builder.append(", createUser=");
		builder.append(createUser);
		builder.append(", searchType=");
		builder.append(searchType);
		builder.append(",repoId=");
		builder.append(repoId);
		builder.append(",errorMsg=");
		builder.append(errorMsg);
		builder.append(",relationTables=");
		builder.append(relationTables);
		builder.append("]");
		return builder.toString();
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getRelationTables() {
		return relationTables;
	}

	public void setRelationTables(String relationTables) {
		this.relationTables = relationTables;
	}
}
