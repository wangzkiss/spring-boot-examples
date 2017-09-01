package cn.vigor.modules.tji.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.thoughtworks.xstream.annotations.XStreamOmitField;

public class ModelObject {
	@XStreamOmitField
	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);

	public void setChangeSupport(PropertyChangeSupport changeSupport) {
		this.changeSupport = changeSupport;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		try {
			changeSupport.addPropertyChangeListener(propertyName, listener);
		} catch (Exception e) {
		}
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		try {
			changeSupport.removePropertyChangeListener(propertyName, listener);
		} catch (Exception e) {
		}
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		if (changeSupport != null) {
			changeSupport.firePropertyChange(propertyName, oldValue, newValue);
		}
	}
}