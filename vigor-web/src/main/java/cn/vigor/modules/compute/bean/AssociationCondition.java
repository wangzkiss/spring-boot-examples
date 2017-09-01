package cn.vigor.modules.compute.bean;

public class AssociationCondition {
	private String asslogic = "";

	private String operation ="";
	
	private String condition ="";
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getAssLogic() {
		return asslogic;
	}

	public void setAssLogic(String asslogic) {
		this.asslogic = asslogic;
	}
}
