package cn.vigor.modules.compute.bean;


public class OutputField extends ModelObject {
	private int id;
	private String name = "";
	
	private String aliasName = "";
	private String type = "";
	private int index;
	private String desc = "";
	private int resultId;
	private int storeId;
	private boolean isFuction = false;
	private String fuctionFields = "";
	private String fuctionId = "";
	private String fuctionDesc = "";
	private String functionClassName = "";
	private String fuctionType = "";
	private String dataFormat="";
	private String fieldAlias;
	private String completeFieldName;
	
	
	public String getCompleteFieldName() {
		return completeFieldName;
	}

	public void setCompleteFieldName(String completeFieldName) {
		this.completeFieldName = completeFieldName;
	}

	public String getFieldAlias() {
		return fieldAlias;
	}

	public void setFieldAlias(String fieldAlias) {
		firePropertyChange("fieldAlias", this.fieldAlias,
				this.fieldAlias = fieldAlias);
	}

	public String getCompleteFunction(){
		return functionName+"("+fuctionFields+")";
	}
	/**
	 * 函数别名
	 */
	private String fuctionAlias="";
	
	public String getFuctionAlias() {
		return fuctionAlias;
	
	}

	public void setFuctionAlias(String fuctionAlias) {
		firePropertyChange("fuctionAlias", this.fuctionAlias,
				this.fuctionAlias = fuctionAlias);
	}

	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public String getFuctionType() {
		return fuctionType;
	}

	public void setFuctionType(String fuctionType) {
		this.fuctionType = fuctionType;
	}

	public String getFunctionClassName() {
		return functionClassName;
	}

	public void setFunctionClassName(String fuctionClass) {
		this.functionClassName = fuctionClass;
	}

	public String getFuctionDesc() {
		return fuctionDesc;
	}

	public void setFuctionDesc(String fuctionDesc) {
		this.fuctionDesc = fuctionDesc;
	}

	public String getFuctionId() {
		return fuctionId;
	}

	public void setFuctionId(String fuctionId) {
		this.fuctionId = fuctionId;
	}

	private String functionName = "";

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		firePropertyChange("functionName", this.functionName,
				this.functionName = functionName);
	}

	public String getFuctionFields() {
		return fuctionFields;
	}

	public void setFuctionFields(String fuctionFields) {
		this.fuctionFields = fuctionFields;
	}

	public boolean isFuction() {
		return isFuction;
	}

	public void setFuction(boolean isFuction) {
		firePropertyChange("isFuction", this.isFuction,
				this.isFuction = isFuction);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		firePropertyChange("type", this.type, this.type = type);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		firePropertyChange("id", this.id, this.id = id);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		firePropertyChange("index", this.index, this.index = index);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		firePropertyChange("desc", this.desc, this.desc = desc);
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		firePropertyChange("aliasName", this.aliasName,
				this.aliasName = aliasName);
	}

	public int getResultId() {
		return resultId;
	}

	public void setResultId(int resultId) {
		this.resultId = resultId;
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}
}
