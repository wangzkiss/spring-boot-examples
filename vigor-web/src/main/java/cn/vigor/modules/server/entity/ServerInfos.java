package cn.vigor.modules.server.entity;


import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 物理节点Entity
 * @author kiss
 * @version 2016-06-21
 */
public class ServerInfos extends DataEntity<ServerInfos> {
	
	private static final long serialVersionUID = 1L;
	private String hostName;		// 主机名
	private String ip;		// 主机ip
	private String userName;		// 主机登录用户名
	private String userPwd;		// 主机登录密码
	private Integer port;		// ssh 端口
    private Integer cpuCount;       // cup个数
    private String cpuInfo;     // cpu信息
    private String discoveryStatus;     // discovery_status
    private String hostAttributes;      // host_attributes
    private String ipv4;        // ipv4
    private String ipv6;        // ipv6
    private Long lastRegistrationTime;      // 注册时间
    private String osArch;      // os_arch
    private String osInfo;      // os_info
    private String osType;      // os_type
    private Integer phCpuCount;     // ph_cpu_count
    private String publicHostName;      // public_host_name
    private String rackInfo;        // 机架信息
    private float totalMem;      // 总内存
	private String diskTotal;  //总硬盘
	private String metrics;   //所有指标json串
	private String sshKey;
	private String diskFree;//可用内存
	private double diskUsedBit;//内存使用率 
	private String hostStatus;//主机状态
	private String diskUsed;//已用内存

    public String getSshKey()
    {
        return sshKey;
    }

    public void setSshKey(String sshKey)
    {
        this.sshKey = sshKey;
    }

    public String getMetrics()
    {
        return metrics;
    }

    public void setMetrics(String metrics)
    {
        this.metrics = metrics;
    }

    public String getDiskTotal()
    {
        return diskTotal;
    }

    public void setDiskTotal(String diskTotal)
    {
        this.diskTotal = diskTotal;
    }

    private List<Map<String,String>> componetList = Lists.newArrayList();     
	
	public List<Map<String, String>> getComponetList()
    {
        return componetList;
    }

    public void setComponetList(List<Map<String, String>> componetList)
    {
        this.componetList = componetList;
    }

    public ServerInfos() {
		super();
	}

	public ServerInfos(String id){
		super(id);
	}

	@Length(min=1, max=50, message="主机名长度必须介于 1 和 50 之间")
	@ExcelField(title="主机名", align=2, sort=1)
	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	@Length(min=1, max=50, message="主机ip长度必须介于 1 和 50 之间")
	@ExcelField(title="主机ip", align=2, sort=2)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Length(min=1, max=50, message="主机登录用户名长度必须介于 1 和 50 之间")
	@ExcelField(title="主机登录用户名", align=2, sort=3)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=1, max=50, message="登录密码长度必须介于 1 和 50 之间")
	@ExcelField(title="登录密码", align=2, sort=4)
	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	
	@NotNull(message="ssh 端口不能为空")
	@ExcelField(title="ssh 端口", align=2, sort=5)
	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

    public Integer getCpuCount()
    {
        return cpuCount;
    }

    public void setCpuCount(Integer cpuCount)
    {
        this.cpuCount = cpuCount;
    }

    public String getCpuInfo()
    {
        return cpuInfo;
    }

    public void setCpuInfo(String cpuInfo)
    {
        this.cpuInfo = cpuInfo;
    }

    public String getDiscoveryStatus()
    {
        return discoveryStatus;
    }

    public void setDiscoveryStatus(String discoveryStatus)
    {
        this.discoveryStatus = discoveryStatus;
    }

    public String getHostAttributes()
    {
        return hostAttributes;
    }

    public void setHostAttributes(String hostAttributes)
    {
        this.hostAttributes = hostAttributes;
    }

    public String getIpv4()
    {
        return ipv4;
    }

    public void setIpv4(String ipv4)
    {
        this.ipv4 = ipv4;
    }

    public String getIpv6()
    {
        return ipv6;
    }

    public void setIpv6(String ipv6)
    {
        this.ipv6 = ipv6;
    }

    public Long getLastRegistrationTime()
    {
        return lastRegistrationTime;
    }

    public void setLastRegistrationTime(Long lastRegistrationTime)
    {
        this.lastRegistrationTime = lastRegistrationTime;
    }

    public String getOsArch()
    {
        return osArch;
    }

    public void setOsArch(String osArch)
    {
        this.osArch = osArch;
    }

    public String getOsInfo()
    {
        return osInfo;
    }

    public void setOsInfo(String osInfo)
    {
        this.osInfo = osInfo;
    }

    public String getOsType()
    {
        return osType;
    }

    public void setOsType(String osType)
    {
        this.osType = osType;
    }

    public Integer getPhCpuCount()
    {
        return phCpuCount;
    }

    public void setPhCpuCount(Integer phCpuCount)
    {
        this.phCpuCount = phCpuCount;
    }

    public String getPublicHostName()
    {
        return publicHostName;
    }

    public void setPublicHostName(String publicHostName)
    {
        this.publicHostName = publicHostName;
    }

    public String getRackInfo()
    {
        return rackInfo;
    }

    public void setRackInfo(String rackInfo)
    {
        this.rackInfo = rackInfo;
    }

    public float getTotalMem()
    {
        return totalMem;
    }

    public void setTotalMem(float totalMem)
    {
        this.totalMem = totalMem;
    }

    public String getDiskFree()
    {
        return diskFree;
    }

    public void setDiskFree(String diskFree)
    {
        this.diskFree = diskFree;
    }

    public double getDiskUsedBit()
    {
        return diskUsedBit;
    }

    public void setDiskUsedBit(double diskUsedBit)
    {
        this.diskUsedBit = diskUsedBit;
    }

	public String getHostStatus() {
		return hostStatus;
	}

	public void setHostStatus(String hostStatus) {
		this.hostStatus = hostStatus;
	}

	public String getDiskUsed() {
		return diskUsed;
	}

	public void setDiskUsed(String diskUsed) {
		this.diskUsed = diskUsed;
	}
}