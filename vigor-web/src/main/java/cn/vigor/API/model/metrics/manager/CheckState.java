package cn.vigor.API.model.metrics.manager;

public class CheckState {
	
	private String state;
	private String progress_percent;
	private String id;
	private String href;
	
	
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getProgress_percent() {
		return progress_percent;
	}
	public void setProgress_percent(String progress_percent) {
		this.progress_percent = progress_percent;
	}
	

}
