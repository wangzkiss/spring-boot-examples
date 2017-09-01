/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 接入方管理Entity
 * @author liminfang
 * @version 2016-07-06
 */
public class Partner extends DataEntity<Partner> {
	
	private static final long serialVersionUID = 1L;
	private Integer partnerId;		// 接入商唯一标识，自增主键
	private String partnerName;		// 接入商名，唯一
	private Integer partnerType;		// 接入商类型id，参照t_base_partner_type中type_id值说明：
	private String partnerDesc;		// 接入商中文名全称
	private Integer bindUserid;		// bind_userid
	private String appKey;		// 接入商应用key
	private String secretKey;		// 接入商密钥
	private Integer partnerStatus;		// 接入商状态，值说明：0：新建待审核；1：已审核
	private String contactName;		// 接入商联系人名
	private String contactEmail;		// 联系人电子邮件
	private String contactPhone;		// 联系人电话
	private String ftpIp;		// ftp服务器ip
	private Integer ftpPort;		// ftp端口
	private String ftpPath;
	private String ftpUser;		// ftp用户名
	private String ftpPassword;		// ftp密码
	private String createUser;		// 接入商创建人
	private Date createTime;		// 接入商创建时间
	private String modifyUser;		// 接入商修改人
	private Date modifyTime;		// 接入商修改时间
	private List<PartnerServ> partnerServiceList = Lists.newArrayList();		// 子表列表
    private List<PartnerIp> partnerIpList = Lists.newArrayList();        // 子表列表
    private int handleType;// 1 白名单才做 2 授权服务操作
	
	public Partner() {
		super();
	}

	public Partner(String id){
		super(id);
	}

   public String getId() {
        return (partnerId == null) ? "" : "" + partnerId;
    }
   
	@NotNull(message="接入商唯一标识，自增主键不能为空")
	@ExcelField(title="接入商唯一标识，自增主键", align=2, sort=0)
	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	
	@Length(min=1, max=50, message="接入商名，唯一长度必须介于 1 和 50 之间")
	@ExcelField(title="接入商名，唯一", align=2, sort=1)
	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	
	@NotNull(message="接入商类型id，参照t_base_partner_type中type_id值说明：不能为空")
	@ExcelField(title="接入商类型id，参照t_base_partner_type中type_id值说明：", dictType="partner_type", align=2, sort=2)
	public Integer getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(Integer partnerType) {
		this.partnerType = partnerType;
	}
	
	@Length(min=1, max=500, message="接入商中文名全称长度必须介于 1 和 500 之间")
	@ExcelField(title="接入商中文名全称", align=2, sort=3)
	public String getPartnerDesc() {
		return partnerDesc;
	}

	public void setPartnerDesc(String partnerDesc) {
		this.partnerDesc = partnerDesc;
	}
	
	@ExcelField(title="bind_userid", align=2, sort=4)
	public Integer getBindUserid() {
		return bindUserid;
	}

	public void setBindUserid(Integer bindUserid) {
		this.bindUserid = bindUserid;
	}
	
	@Length(min=1, max=500, message="接入商应用key长度必须介于 1 和 500 之间")
	@ExcelField(title="接入商应用key", align=2, sort=5)
	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	@Length(min=1, max=500, message="接入商密钥长度必须介于 1 和 500 之间")
	@ExcelField(title="接入商密钥", align=2, sort=6)
	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	@NotNull(message="接入商状态，值说明：0：新建待审核；1：已审核不能为空")
	@ExcelField(title="接入商状态，值说明：0：新建待审核；1：已审核", align=2, sort=7)
	public Integer getPartnerStatus() {
		return partnerStatus;
	}

	public void setPartnerStatus(Integer partnerStatus) {
		this.partnerStatus = partnerStatus;
	}
	
	@Length(min=1, max=50, message="接入商联系人名长度必须介于 1 和 50 之间")
	@ExcelField(title="接入商联系人名", align=2, sort=8)
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	
	@Length(min=1, max=50, message="联系人电子邮件长度必须介于 1 和 50 之间")
	@ExcelField(title="联系人电子邮件", align=2, sort=9)
	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
	@Length(min=1, max=11, message="联系人电话长度必须介于 1 和 11 之间")
	@ExcelField(title="联系人电话", align=2, sort=10)
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	
	@Length(min=0, max=20, message="ftp服务器ip长度必须介于 0 和 20 之间")
	@ExcelField(title="ftp服务器ip", align=2, sort=11)
	public String getFtpIp() {
		return ftpIp;
	}

	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}
	
	@ExcelField(title="ftp端口", align=2, sort=12)
	public Integer getFtpPort() {
		return ftpPort;
	}

	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}
	
	public String getFtpPath() {
        return ftpPath;
    }

    public void setFtpPath(String ftpPath) {
        this.ftpPath = ftpPath;
    }
	
	@Length(min=0, max=50, message="ftp用户名长度必须介于 0 和 50 之间")
	@ExcelField(title="ftp用户名", align=2, sort=13)
	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}
	
	@Length(min=0, max=50, message="ftp密码长度必须介于 0 和 50 之间")
	@ExcelField(title="ftp密码", align=2, sort=14)
	public String getFtpPassword() {
		return ftpPassword;
	}

	public void setFtpPassword(String ftpPassword) {
		this.ftpPassword = ftpPassword;
	}
	
	@Length(min=0, max=50, message="接入商创建人长度必须介于 0 和 50 之间")
	@ExcelField(title="接入商创建人", align=2, sort=15)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="接入商创建时间", align=2, sort=16)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Length(min=0, max=50, message="接入商修改人长度必须介于 0 和 50 之间")
	@ExcelField(title="接入商修改人", align=2, sort=17)
	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="接入商修改时间", align=2, sort=18)
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public List<PartnerServ> getPartnerServiceList() {
		return partnerServiceList;
	}

	public void setPartnerServiceList(List<PartnerServ> partnerServiceList) {
		this.partnerServiceList = partnerServiceList;
	}
	
	public List<PartnerIp> getPartnerIpList() {
        return partnerIpList;
    }

    public void setPartnerIpList(List<PartnerIp> partnerIpList) {
        this.partnerIpList = partnerIpList;
    }

    public int getHandleType()
    {
        return handleType;
    }

    public void setHandleType(int handleType)
    {
        this.handleType = handleType;
    }
}