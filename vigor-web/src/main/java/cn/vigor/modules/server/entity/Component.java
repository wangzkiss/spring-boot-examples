package cn.vigor.modules.server.entity;

import cn.vigor.common.persistence.DataEntity;

public class Component extends DataEntity<Component>
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String hostName;        // 主机名
    private String ip;      // 主机ip
    private float totalMem;      // 总内存
    private String serviceName;
    private String componentName;
    private String currentState;
    private String cpuCount;
    private String displayName;
    private String nameNodeStatus;
    public String getHostName()
    {
        return hostName;
    }
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    public String getIp()
    {
        return ip;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }
    public float getTotalMem()
    {
        return totalMem;
    }
    public void setTotalMem(float totalMem)
    {
        this.totalMem = totalMem;
    }
    public String getServiceName()
    {
        return serviceName;
    }
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }
    public String getComponentName()
    {
        return componentName;
    }
    public void setComponentName(String componentName)
    {
        this.componentName = componentName;
    }
    public String getCurrentState()
    {
        return currentState;
    }
    public void setCurrentState(String currentState)
    {
        this.currentState = currentState;
    }
    public String getCpuCount()
    {
        return cpuCount;
    }
    public void setCpuCount(String cpuCount)
    {
        this.cpuCount = cpuCount;
    }
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getNameNodeStatus() {
		return nameNodeStatus;
	}
	public void setNameNodeStatus(String nameNodeStatus) {
		this.nameNodeStatus = nameNodeStatus;
	}
    
    
   
}
