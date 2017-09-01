package cn.vigor.modules.meta.entity;

import cn.vigor.common.persistence.DataEntity;

public class TaskHourReport extends DataEntity<TaskHourReport>{

	private static final long serialVersionUID = 3172271525206144103L;
	
	/**
	 * 任务类型
	 */
	private int taskType;
	
	/**
	 * 失败数
	 */
	private int failCount;
	
	/**
	 * 成功数
	 */
	private int successCount;
	
	/**
	 * 暂停数
	 */
	private int pauseCount;
	
	/**
	 * 运行中数
	 */
	private int runningCount;
	
	/**
	 * 时间(年-月-日 时)
	 */
	private String sdate;
	
	/**
	 * 时间点(小时)
	 */
	private int shour;
	
	/**
	 * 任务耗时(单位:秒)
	 */
	private int timeConsuming;
	
	/**
	 * 统计开始时间
	 */
	private String cBeginDate;
	
	/**
	 * 统计结束时间
	 */
	private String cEndDate;

	public int getTaskType() {
		return taskType;
	}

	public int getFailCount() {
		return failCount;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public int getPauseCount() {
		return pauseCount;
	}

	public int getRunningCount() {
		return runningCount;
	}

	public String getSdate() {
		return sdate;
	}

	public int getShour() {
		return shour;
	}

	public int getTimeConsuming() {
		return timeConsuming;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}

	public void setPauseCount(int pauseCount) {
		this.pauseCount = pauseCount;
	}

	public void setRunningCount(int runningCount) {
		this.runningCount = runningCount;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public void setShour(int shour) {
		this.shour = shour;
	}

	public void setTimeConsuming(int timeConsuming) {
		this.timeConsuming = timeConsuming;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TaskHourReport [taskType=");
		builder.append(taskType);
		builder.append(", failCount=");
		builder.append(failCount);
		builder.append(", successCount=");
		builder.append(successCount);
		builder.append(", pauseCount=");
		builder.append(pauseCount);
		builder.append(", runningCount=");
		builder.append(runningCount);
		builder.append(", sdate=");
		builder.append(sdate);
		builder.append(", shour=");
		builder.append(shour);
		builder.append(", timeConsuming=");
		builder.append(timeConsuming);
		builder.append("]");
		return builder.toString();
	}

	public String getcBeginDate() {
		return cBeginDate;
	}

	public void setcBeginDate(String cBeginDate) {
		this.cBeginDate = cBeginDate;
	}

	public String getcEndDate() {
		return cEndDate;
	}

	public void setcEndDate(String cEndDate) {
		this.cEndDate = cEndDate;
	}
}
