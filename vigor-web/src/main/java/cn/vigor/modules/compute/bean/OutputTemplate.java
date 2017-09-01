package cn.vigor.modules.compute.bean;



/**
 * 数据存储模板
 * @author yangtao
 *
 */
public class OutputTemplate extends ModelObject{
	private int type;
	
	private String ip;
	
	private int id;
	
	private int port;
	
	private String db;
	
	private String typeText;
	
	private String username = "";
	
	private String password = "";
	
	public void setType(int type) {
		firePropertyChange("type", this.type, this.type = type);
	}
	
	public int getType() {
		return type;
	}
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		firePropertyChange("ip", this.ip, this.ip = ip);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		firePropertyChange("port", this.port, this.port = port);
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		firePropertyChange("db", this.db, this.db = db);
	}
	
	public String getTypeText() {
		return typeText;
	}
	
	public void setTypeText(String typeText) {
		firePropertyChange("typeText", this.typeText, this.typeText = typeText);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		if(username != null) this.username = username;
	}

	public String getPassword() {
			return password;
	}

	public void setPassword(String password) {
		if(password != null) this.password = password;
	}
}
