package cn.vigor.modules.compute.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 任务 实体类
 * 
 * @author hewei
 * 
 */
public class ComputeTask extends ModelObject {
	public final static int SPARK_TASK_TYPE = 4;
	public final static int HIVE_TASK_TYPE = 3;
	public final static int JAR_MR_TASK_TYPE = 6;
	public final static int STORM_TASK_TYPE = 4;
	public final static int STREAM_ETL_TASK_TYPE = 1;
	public static final int R_TASK_TYPE = 9;
	public static final int BATCH_ETL_TYPE = 2;
	public static final int FLUME_TASK_TYPE = 12;
	public final static int TRAFODION_TASK_TYPE = 10;
	
	public static final int JAR_STORM_TASK_TYPE = 18;
	
	public static final int JAR_Model_TASK_TYPE = 21;
	
	public static final int JAR_SPARK_TASK_TYPE = 5;
	
	public static final int SQL_SCRIPT= 8;
	
	public static final int SHELL_SCRIPT= 11;
	//CHENYAN
	public static final int INTERFACE_SCRIPT= 25;

	public static final int ALGORITHM_TYPE=13;
	
	public static final int PIG_TASK_TYPE=15;
	
	public static final int DATA_QUALITY=7;
	
	public static final int ALGORITHM_RECOMMENDITEMBASED_TASK_TYPE = 1;
	
	public static final int ALGORITHM_SEQDIRECTORY_TASK_TYPE=2;
	
	public static final int ALGORITHM_KMEANS_TASK_TYPE=3;
	
	public static final int ALGORITHM_CANOPY_TASK_TYPE=4;
	
	public static final int ALGORITHM_FKMEANS_TASK_TYPE = 5;
	
	public static final int ALGORITHM_SEQDUMPER_TASK_TYPE = 6;
	
	public static final int ALGORITHM_SEQ2PARSE_TASK_TYPE = 7;
	
	public static final int ALGORITHM_TRAINNB_TASK_TYPE = 8;
	
	public static final int ALGORITHM_TESTNB_TASK_TYPE = 9;
	
	public static final int ALGORITHM_DESCRIBE_TASK_TYPE =10;
	
	public static final int ALGORITHM_BUILDFOREST_TASK_TYPE = 11;
	
	public static final int ALGORITHM_TESTFOREST_TASK_TYPE = 12;
	
	public static final int ALGORITHM_SPLIT_TASK_TYPE = 13;
	
	public static final int ALGORITHM_BAYESEQ_TASK_TYPE =20;
	
	/**
	 * 任务Id
	 */
	private int taskId;

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		firePropertyChange("taskId", this.taskId, this.taskId = taskId);
	}

	/**
	 * 任务名称
	 */
	private String taskName;

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		firePropertyChange("taskName", this.taskName, this.taskName = taskName);
	}

	/**
	 * 任务类型
	 */
	private int taskType;

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		firePropertyChange("taskType", this.taskType, this.taskType = taskType);
	}

	/**
	 * 类型名称
	 */
	private String typeName;
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		firePropertyChange("typeName", this.typeName, this.typeName = typeName);
	}

	/**
	 * 任务描述
	 */
	private String taskDesc;

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		firePropertyChange("taskDesc", this.taskDesc, this.taskDesc = taskDesc);
	}

	private String functionName;
	
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		firePropertyChange("functionName", this.functionName,
				this.functionName = functionName);
	}

	/**
	 * 创建日期
	 */
	private Date createTime;
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		firePropertyChange("createDate", this.createTime,this.createTime = createTime);
		String createDateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(createTime);
		setCreateDateString(createDateString);
	}

	private String createDateString;

	public String getCreateDateString() {
		return createDateString;
	}

	public void setCreateDateString(String createDateString) {
		firePropertyChange("createDateString", this.createDateString,
				this.createDateString = createDateString);
	}

	/**
	 * 创建人
	 */
	private String createUser;

	/**
	 * 获取创建人
	 * @return
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * 设置创建人
	 * @param createUser
	 */
	public void setCreateUser(String createUser) {
		firePropertyChange("createUser", this.createUser, this.createUser = createUser);
	}
	
	/**
	 * 任务的xml信息，用于配置工具
	 */
	private String xmlData;

	public String getXmlData() {
		return xmlData;
	}

	public void setXmlData(String xmlData) {
		firePropertyChange("xmlData", this.xmlData, this.xmlData = xmlData);
	}
	
	/**
	 * 任务状态
	 */
	private int taskStatus;

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		firePropertyChange("taskStatus", this.taskStatus, this.taskStatus = taskStatus);
		if(taskStatus == 0){
			setStatusString("未执行");
		}
		else if(taskStatus ==1){
			setStatusString("执行已通过");
		}
		else if(taskStatus ==2){
			setStatusString("执行不通过");
		}
		else if(taskStatus ==3){
			setStatusString("停止");
		}
	}
	
	private String statusString;
	
	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		firePropertyChange("statusString", this.statusString, this.statusString = statusString);
	}
	
	/**
	 * 任务当前目录,跟组织架构有关联
	 */
	private String dirPath;

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		firePropertyChange("dirPath", this.dirPath, this.dirPath = dirPath);
	}
	
	/**
	 * 任务来源平台,1：配置工具； 2：数据服务
	 */
	private int comeFrom=1;

	public int getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(int comeFrom) {
		firePropertyChange("comeFrom", this.comeFrom, this.comeFrom = comeFrom);
	}
	
	/**
	 *  分组Id
	 */
	private int groupId;

	/**
	 * 获取 分组Id
	 * @return groupId 分组Id
	 */
	public int getGroupId() {
		return groupId;
	}

	/**
	 * 设置 分组Id
	 * @param groupId 分组Id
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
}
