package cn.vigor.modules.compute.bean;

public class InParam {
	public static final int SOURCE_TYPE_HDFS = 3;
	public static final int SOURCE_TYPE_HBASE = 4;
	public static final int SOURCE_TYPE_KAFKA = 9;
	public static final int SOURCE_TYPE_HIVE = 5;
	public static final int SOURCE_TYPE_MYSQL = 10;
	
	public static final int RESULT_TYPE_MYSQL = 6;
	
	public static final int STORE_TYPE_MYSQL = 6;
	
	public static final int RESULT_TYPE_FTP = 2;
	
	public static final int RESULT_TYPE_HIVE = 5;
	
	public static final int STORE_TYPE_HIVE = 5;
	
	public static final int STORE_TYPE_HDFS = 3;
	
	public static final int FUNCTION_SQL = 8;
	
	public static final int RESULT_TYPE_TRAFODION = 11;
	
	public static final int STORE_TYPE_TRAFODION = 11;
	
	
	/**
	 *  0为外部数据源 ，1，为平台存储， 2为结果集
	 */
	public static final int SOURCE = 0; 
	
	/**
	 *  0为外部数据源 ，1，为平台存储， 2为结果集
	 */
	public static final int STORE = 1;
	
	/**
	 *  0为外部数据源 ，1，为平台存储， 2为结果集
	 */
	public static final int RESULT = 2;
	
	/**
	 * 函数类型
	 */
	private int functionType;
	
	public int getFunctionType() {
		return functionType;
	}

	public void setFunctionType(int functionType) {
		this.functionType = functionType;
	}

	/**
	 * 函数状态  1 所有人可见 2 定义的人可见
	 */
	public int functionStatus;
	
	/**
	 * 获取函数状态
	 * @return int
	 */
	public int getFunctionStatus() {
		return functionStatus;
	}

	/**
	 * 设置函数状态
	 * @param functionStatus
	 */
	public void setFunctionStatus(int functionStatus) {
		this.functionStatus = functionStatus;
	}

	private boolean isAdmin=false;

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	private String repoId;
	
	private String repoName;
	
	private String storeName;

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getRepoName() {
		return repoName;
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoId() {
		return repoId;
	}

	public void setRepoId(String repoId) {
		this.repoId = repoId;
	}

	private String officeId;
	
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	private int userid;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}
	
	
	private int repoMetaType;

	public int getRepoMetaType() {
		return repoMetaType;
	}

	public void setRepoMetaType(int repoMetaType) {
		this.repoMetaType = repoMetaType;
	}

	private int[] repoMetaTypeArray;
	
	public int[] getRepoMetaTypeArray() {
		return repoMetaTypeArray;
	}

	public void setRepoMetaTypeArray(int[] repoMetaTypeArray) {
		this.repoMetaTypeArray = repoMetaTypeArray;
	}

	private int[] array;
	
	private int[] repoArray;

	public int[] getRepoArray() {
		return repoArray;
	}

	public void setRepoArray(int[] repoArray) {
		this.repoArray = repoArray;
	}

	public int[] getArray() {
		return array;
	}

	public void setArray(int[] array) {
		this.array = array;
	}

	// 数据库名 即表中的store_dir
	private String dir;

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	// 查询条件 输入数据源
	private String filter;

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	private int mrFunctionType;

	public int getMrFunctionType() {
		return mrFunctionType;
	}

	public void setMrFunctionType(int mrFunctionType) {
		this.mrFunctionType = mrFunctionType;
	}

	private int env_id;

	public int getEnv_id() {
		return env_id;
	}

	public void setEnv_id(int env_id) {
		this.env_id = env_id;
	}
	
	private int storeId;

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
	
	private int resultId;

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}
	
	private String dbname;
	private String tbname;

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getTbname() {
		return tbname;
	}

	public void setTbname(String tbname) {
		this.tbname = tbname;
	}
	
	
	
	private String createUser;

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	private int taskId;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
}
