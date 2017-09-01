/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.entity;

import cn.vigor.common.persistence.DataEntity;

/**
 * 接入方管理Entity
 * @author liminfang
 * @version 2016-07-06
 */
public class PartnerIp extends DataEntity<PartnerIp> {
	
	private static final long serialVersionUID = 1L;
	private Integer partnerId;		// 接入id，关联 接入表信息id 父类
	private String ipStart;
	private String ipEnd;
	private String ipStr;
	private int flag;
	
	public PartnerIp() {
		super();
	}

	public PartnerIp(String id){
		super(id);
	}

	public PartnerIp(Integer partnerId){
		this.partnerId = partnerId;
	}
	
	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	
	public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
    
    public String getIpEnd()
    {
        return ipEnd;
    }

    public void setIpEnd(String ipEnd) 
    {
        this.ipEnd = ipEnd;
    }
    

    public String getIpStart()
    {
        return ipStart;
    }

    public void setIpStart(String ipStart) 
    {
        this.ipStart = ipStart;
    }
	
    public String getIpStr()
    {
        if(ipStr == null || ipStr.length() == 0) {
            ipStr = ipStart + "-" + ipEnd;
        }
        return ipStr;
    }

    public void setIpStr(String ipStr) 
    {
        this.ipStr = ipStr;
    }
}