package cn.vigor.modules.compute.bean;


public class SourceField extends ModelObject implements Comparable<SourceField>{
	private int id;                  	//id
	private String name = "";      		//名称
	private String aliasName = ""; 		//别名
	private String type = "";      		//类型
	private int index;             		//索引
	private String desc = "";      		//描述
	private int storeId;           		//存储id
	private int sourceId;          		//数据源id
	private boolean isFuction=false; 	// 是否为函数
	private String fuctionFields=""; 	//函数段
	private String dataFormat="";    	//数据格式
	
	public String getDataFormat() {
		return dataFormat;
	}

	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
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
		firePropertyChange("isFuction", this.isFuction, this.isFuction = isFuction);
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
		firePropertyChange("aliasName", this.aliasName, this.aliasName = aliasName);
	}

	public int getStoreId() {
		return storeId;
	}

	public void setStoreId(int storeId) {
		this.storeId = storeId;
	}

	public int getSourceId() {
		return sourceId;
	}

	public void setSourceId(int sourceId) {
		firePropertyChange("sourceId", this.sourceId, this.sourceId = sourceId);
	}
	
	@Override
	public int compareTo(SourceField sf) {
		int flag = Integer.valueOf(this.getIndex()).compareTo(
				Integer.valueOf(sf.getIndex()));
			return flag;
    }
}
