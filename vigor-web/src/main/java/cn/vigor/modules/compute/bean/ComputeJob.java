package cn.vigor.modules.compute.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;



/**
 * 数据输出
 * @author yangtao
 *
 */
public class ComputeJob extends ModelObject{
	
	private int id;
	
	private String name;
	
	private int type;
	
	private String typeName;
	
	private String desc;
	
	private int status;
	
	private String statusString;
	
	private int scheduleId;
	
	private int taskId;
	
	private Date createDate;
	
	private String createDateString;
	
	private int taskType;
	
	private int tallyDown;
	
	private int createUserid=-1;
	
	private int ownerUserid=-1;
	
	public int getOwnerUserid() {
		return ownerUserid;
	}

	public void setOwnerUserid(int ownerUserid) {
		this.ownerUserid = ownerUserid;
	}

	public int getCreateUserid() {
		return createUserid;
	}

	public void setCreateUserid(int createUserid) {
		this.createUserid = createUserid;
	}

	public int getTallyDown() {
		return tallyDown;
	}

	public void setTallyDown(int tallyDown) {
		this.tallyDown = tallyDown;
	}

	public String getRelationGoal() {
		return relationGoal;
	}

	public void setRelationGoal(String relationGoal) {
		this.relationGoal = relationGoal;
	}

	private String relationGoal="";
	private List<ComputeJob> dependJobList;
	
	public List<ComputeJob> getDependJobList() {
		return dependJobList;
	}

	public void setDependJobList(List<ComputeJob> dependJobList) {
		this.dependJobList = dependJobList;
	}

	/**
	 * 是否是导入的任务
	 */
	private boolean importJob=false;

	public boolean isImportJob() {
		return importJob;
	}

	public void setImportJob(boolean importJob) {
		this.importJob = importJob;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		firePropertyChange("id", this.id, this.id = id);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		firePropertyChange("type", this.type, this.type = type);
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		firePropertyChange("desc", this.desc, this.desc = desc);
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		firePropertyChange("status", this.status, this.status = status);
		
		if(status == 0){
			setStatusString("未审核");
		}
		else if(status ==1){
			setStatusString("审核已通过");
		}
		else if(status ==2){
			setStatusString("审核不通过");
		}
		else if(status ==3){
			setStatusString("停止");
		}
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		firePropertyChange("scheduleId", this.scheduleId, this.scheduleId = scheduleId);
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		firePropertyChange("createDate", this.createDate, this.createDate = createDate);
		String createDateString = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(createDate);
		setCreateDateString(createDateString);
	}

	public String getCreateDateString() {
		return createDateString;
	}

	public void setCreateDateString(String createDateString) {
		firePropertyChange("createDateString", this.createDateString, this.createDateString = createDateString);
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		firePropertyChange("statusString", this.statusString, this.statusString = statusString);
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
}
