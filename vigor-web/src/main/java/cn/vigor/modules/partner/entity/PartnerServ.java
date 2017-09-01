/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.entity;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.modules.dataservice.entity.EServiceInfo;
import cn.vigor.modules.dataservice.utils.Constants;
import cn.vigor.modules.sys.utils.DictUtils;

/**
 * 接入方管理Entity
 * @author liminfang
 * @version 2016-07-06
 */
public class PartnerServ extends DataEntity<PartnerServ> {
	
	private static final long serialVersionUID = 1L;
	private EServiceInfo service;		// 服务id，关联服务信息表
	private Integer partnerId;		// 接入id，关联 接入表信息id 父类
	private String serviceTypeLabel;
	private int flag;
	
	public PartnerServ() {
		super();
	}

	public PartnerServ(String id){
		super(id);
	}

	public PartnerServ(Integer partnerId){
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

    public EServiceInfo getService()
    {
        return service;
    }

    public void setService(EServiceInfo service)
    {
        this.service = service;
    }

    public String getServiceTypeLabel()
    {
        return DictUtils.getDictLabel("" + service.getServiceType(), Constants.DICT_SERVICE_TYPE, "未知类型");
    }

    public void setServiceTypeLabel(String serviceTypeLabel)
    {
        this.serviceTypeLabel = serviceTypeLabel;
    }
	
}