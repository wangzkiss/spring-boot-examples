package cn.vigor.API.model.host;

import java.util.List;

public class HostComponent {
	
	private String href;
	
	private List<HostComponentItems> items;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public List<HostComponentItems> getItems() {
		return items;
	}

	public void setItems(List<HostComponentItems> items) {
		this.items = items;
	}
	
}
