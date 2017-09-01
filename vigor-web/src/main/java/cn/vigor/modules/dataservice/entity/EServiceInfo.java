/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.entity;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 数据服务管理Entity
 * @author liminfang
 * @version 2016-06-28
 */
public class EServiceInfo extends DataEntity<EServiceInfo> {
	
	private static final long serialVersionUID = 1L;
	private Integer serviceId;		// 服务id
	private String serviceName;		// 服务名
	private Integer serviceType;		// 服务类型
	private String serviceDesc;		// 服务描述
	private Integer fromType;		// 来源类型：1为平台存储，2为结果集
	private Integer dataId;		// 数据集id，根据来源类型关联存储表或结果集表
	private Integer dataType;		// 数据类型
	private List<EServiceFunction> eServiceFunctionList = Lists.newArrayList();		// 子表列表
	private List<EServicePro> eServiceProList = Lists.newArrayList();		// 子表列表
	private List<EServiceInfoMeta> metaList = Lists.newArrayList();

	
	public List<EServiceFunction> geteServiceFunctionList()
    {
        return eServiceFunctionList;
    }

    public void seteServiceFunctionList(List<EServiceFunction> eServiceFunctionList)
    {
        this.eServiceFunctionList = eServiceFunctionList;
    }

    public List<EServicePro> geteServiceProList()
    {
        return eServiceProList;
    }

    public void seteServiceProList(List<EServicePro> eServiceProList)
    {
        this.eServiceProList = eServiceProList;
    }

    public EServiceInfo() {
		super();
	}

	@NotNull(message="服务id不能为空")
	@ExcelField(title="服务id", align=2, sort=0)
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getId() {
	    return (serviceId == null) ? "" : "" + serviceId;
	}
	
	@Length(min=1, max=50, message="服务名长度必须介于 1 和 50 之间")
	@ExcelField(title="服务名", align=2, sort=1)
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	@ExcelField(title="服务类型", dictType="service_type", align=2, sort=2)
	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}
	
	@Length(min=0, max=500, message="服务描述长度必须介于 0 和 500 之间")
	@ExcelField(title="服务描述", align=2, sort=3)
	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}
	
	@ExcelField(title="来源类型：1为平台存储，2为结果集", dictType="service_from_type", align=2, sort=4)
	public Integer getFromType() {
		return fromType;
	}

	public void setFromType(Integer fromType) {
		this.fromType = fromType;
	}
	
	@NotNull(message="数据集id，根据来源类型关联存储表或结果集表不能为空")
	@ExcelField(title="数据集id，根据来源类型关联存储表或结果集表", dictType="", align=2, sort=5)
	public Integer getDataId() {
		return dataId;
	}

	public void setDataId(Integer dataId) {
		this.dataId = dataId;
	}
	
	@ExcelField(title="数据类型", align=2, sort=6)
	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

    public List<EServiceInfoMeta> getMetaList()
    {
        return metaList;
    }

    public void setMetaList(List<EServiceInfoMeta> metaList)
    {
        this.metaList = metaList;
    }
	
	
}