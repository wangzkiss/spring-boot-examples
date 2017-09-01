package cn.vigor.modules.compute.bean;

import java.util.List;

/**
 * 数据来源
 * @author yangtao
 *
 */
public class Source extends ModelObject{
	public static final int SOURCE_TYPE_HDFS = 3;
	public static final int SOURCE_TYPE_HBASE = 4;
	public static final int SOURCE_TYPE_KAFKA = 9;
	public static final int SOURCE_TYPE_HIVE = 5;
//	public static final int SOURCE_TYPE_HIVE = 8;
	
	private List<SourceField> fields ;//= new ArrayList<SourceField>()
	
	private int id;
	
	private String name;
	
	private String type;
	
	private String typeName;
	
	private String desc;
	
	private String ip;
	
	private String port;
	
	private String file;
	
	private String dir;
	
	private int storeDataInfoId;
	
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		firePropertyChange("type", this.type, this.type = type);
	}
	
	public String getDesc() {
		return desc;
	}
	
	public void setDesc(String desc) {
		firePropertyChange("desc", this.desc, this.desc = desc);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		firePropertyChange("ip", this.ip, this.ip = ip);
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		firePropertyChange("port", this.port, this.port = port);
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		firePropertyChange("file", this.file, this.file = file);
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		firePropertyChange("dir", this.dir, this.dir = dir);
	}

	public List<SourceField> getFields() {
		return fields;
	}

	public void setFields(List<SourceField> fields) {
		this.fields = fields;
	}

	public int getStoreDataInfoId() {
		return storeDataInfoId;
	}

	public void setStoreDataInfoId(int storeDataInfoId) {
		this.storeDataInfoId = storeDataInfoId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
