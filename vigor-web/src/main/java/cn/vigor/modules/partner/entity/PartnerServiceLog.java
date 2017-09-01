/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 接入方管理Entity
 * @author liminfang
 * @version 2016-07-06
 */
public class PartnerServiceLog extends DataEntity<PartnerServiceLog> {
	
	private static final long serialVersionUID = 1L;
	private Integer partnerId;		// 接入商id
	private String partnerName;
	private Integer serviceId;		// 服务id
	private String serviceName;
	private Date serviceTime;		// 服务时间
	private int serviceResult;		// 服务结果
	private String resultDesc;		// 服务i结果描述
	private Integer jobId;		// 对应离线任务的jobid,实时任务为0
	
	public PartnerServiceLog() {
		super();
	}

	public PartnerServiceLog(String id){
		super(id);
	}

	@NotNull(message="接入商id不能为空")
	@ExcelField(title="接入商id", dictType="", align=2, sort=0)
	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	
	@NotNull(message="服务id不能为空")
	@ExcelField(title="服务id", dictType="", align=2, sort=1)
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="服务时间不能为空")
	@ExcelField(title="服务时间", align=2, sort=2)
	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	@Length(min=1, max=50, message="服务结果长度必须介于 1 和 50 之间")
	@ExcelField(title="服务结果", align=2, sort=3)
	public int getServiceResult() {
		return serviceResult;
	}

	public void setServiceResult(int serviceResult) {
		this.serviceResult = serviceResult;
	}
	
    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }
	
	public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
	
	@Length(min=1, max=500, message="服务i结果描述长度必须介于 1 和 500 之间")
	@ExcelField(title="服务i结果描述", align=2, sort=4)
	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	
	@NotNull(message="对应离线任务的jobid,实时任务为0不能为空")
	@ExcelField(title="对应离线任务的jobid,实时任务为0", align=2, sort=6)
	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
}