package cn.vigor.modules.compute.bean;

/**
 * 	任务依赖实体类
 * @author HW
 *
 */
public class TableDependency {
	private int jobId;

	private int preposedJobId;
	
	private int tallyDown;

	private String relationGoal="";
	
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
	
	public int getJobId() {
		return jobId;
	}
	
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	public int getPreposedJobId() {
		return preposedJobId;
	}
	
	public void setPreposedJobId(int preposedJobId) {
		this.preposedJobId = preposedJobId;
	}
}
