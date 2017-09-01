package cn.vigor.modules.compute.bean;

/**
 * 
 * @author HW 
 *
 */
public class TableAssociationField {

	/**
	 * //and or
	 */
	private String andor = "and";
	
	public String getAndor() {
		return andor;
	}

	public void setAndor(String andor) {
		this.andor = andor;
	}

	/**
	 * 操作符
	 */
	private String op = "=";

	/**
	 * 左表字段
	 */
	private String leftTableField = "";
	
	/**
	 * 右表字段
	 */
	private String rightTableField = "";

	/**
	 * 获取操作符
	 * @return
	 */
	public String getOp() {
		return op;
	}

	/**
	 * 设置操作符
	 * @param op
	 */
	public void setOp(String op) {
		this.op = op;
	}
	
	/**
	 * 获取左表关联字段
	 * @return
	 */
	public String getLeftTableField() {
		return leftTableField;
	}

	/**
	 * 设置左表关联字段
	 * @param leftTableField
	 */
	public void setLeftTableField(String leftTableField) {
		this.leftTableField = leftTableField;
	}

	/**
	 * 获取右表关联字段
	 * @return
	 */
	public String getRightTableField() {
		return rightTableField;
	}

	/**
	 * 设置右表关联字段
	 * @param leftTableField
	 */
	public void setRightTableField(String rightTableField) {
		this.rightTableField = rightTableField;
	}
}
