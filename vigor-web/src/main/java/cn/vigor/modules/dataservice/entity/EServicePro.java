/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.entity;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 数据服务管理Entity
 * @author liminfang
 * @version 2016-06-28
 */
public class EServicePro extends DataEntity<EServicePro> {
	
	private static final long serialVersionUID = 1L;
	private Integer serviceId;		// 服务id，关联服务信息表 父类
	private String proName;		// 属性名称
	private String proType;		// 属性类型
	private String proDesc;		// 描述信息
	
	public EServicePro() {
		super();
	}

	public EServicePro(String id){
		super(id);
	}

	public EServicePro(Integer serviceId){
		this.serviceId = serviceId;
	}

	@NotNull(message="服务id，关联服务信息表不能为空")
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	
	@Length(min=1, max=50, message="属性名称长度必须介于 1 和 50 之间")
	@ExcelField(title="属性名称", align=2, sort=2)
	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}
	
	@Length(min=0, max=50, message="属性类型长度必须介于 0 和 50 之间")
	@ExcelField(title="属性类型", align=2, sort=3)
	public String getProType() {
		return proType;
	}

	public void setProType(String proType) {
		this.proType = proType;
	}
	
	@Length(min=0, max=50, message="描述信息长度必须介于 0 和 50 之间")
	@ExcelField(title="描述信息", align=2, sort=4)
	public String getProDesc() {
		return proDesc;
	}

	public void setProDesc(String proDesc) {
		this.proDesc = proDesc;
	}
	
}