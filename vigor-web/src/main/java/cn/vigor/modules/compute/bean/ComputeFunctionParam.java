package cn.vigor.modules.compute.bean;


/**
 * 函数参数实体类
 * @author HW
 *
 */
public class ComputeFunctionParam extends ModelObject{

	/**
	 * 参数Id
	 */
	private int paramId;
	
	/**
	 * 函数Id
	 */
	private int functionId;
	
	/**
	 * 参数名称
	 */
	private String paramName;
	
	/**
	 * 参数顺序
	 */
	private int paramIndex;
	
	/**
	 * 参数选择位置
	 */
	private int paramSelect;
	
	/**
	 * 参数类型
	 */
	private String paramType;
	
	/**
	 * 参数描述
	 */
	private String paramDesc;
	
	/**
	 * 获得参数Id
	 * @return paramId
	 */
	public int getParamId() {
		return paramId;
	}

	/**
	 * 设置参数ID
	 * @param paramId
	 */
	public void setParamId(int paramId) {
		this.paramId = paramId;
	}

	/**
	 * 获取函数Id
	 * @return
	 */
	public int getFunctionId() {
		return functionId;
	}

	/**
	 * 设置函数Id
	 * @param functionId
	 */
	public void setFunctionId(int functionId) {
		this.functionId = functionId;
	}

	/**
	 * 设置函数名称
	 * @return paramName
	 */
	public String getParamName() {
		return paramName;
	}

	/**
	 * 设置函数名称
	 * @param paramName
	 */
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	/**
	 * 获得参数顺序
	 * @return paramIndex
	 */
	public int getParamIndex() {
		return paramIndex;
	}

	/**
	 * 设置参数顺序
	 * @param paramIndex
	 */
	public void setParamIndex(int paramIndex) {
		this.paramIndex = paramIndex;
	}

	/**
	 * 获取参数选择顺序
	 * @return paramSelect
	 */
	public int getParamSelect() {
		return paramSelect;
	}

	/**
	 * 设置参数选择顺序
	 * @param paramSelect
	 */
	public void setParamSelect(int paramSelect) {
		this.paramSelect = paramSelect;
	}

	/**
	 * 获取参数类型
	 * @return paramType
	 */
	public String getParamType() {
		return paramType;
	}

	/**
	 * 设置参数类型
	 * @param paramType
	 */
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	/**
	 * 过去参数描述
	 * @return paramDesc
	 */
	public String getParamDesc() {
		return paramDesc;
	}

	/**
	 * 设置参数描述
	 * @param paramDesc
	 */
	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
}
