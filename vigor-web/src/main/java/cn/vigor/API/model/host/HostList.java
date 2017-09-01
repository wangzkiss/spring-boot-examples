package cn.vigor.API.model.host;

import java.util.List;

public class HostList {
	private String href;
	private List<Items> items;
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public List<Items> getItems() {
		return items;
	}
	public void setItems(List<Items> items) {
		this.items = items;
	}
	

}
