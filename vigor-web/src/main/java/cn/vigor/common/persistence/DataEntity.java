/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.common.persistence;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.vigor.common.config.Global;
import cn.vigor.common.utils.DateUtils;
import cn.vigor.common.utils.IdGen;
import cn.vigor.modules.sys.entity.Role;
import cn.vigor.modules.sys.entity.User;
import cn.vigor.modules.sys.utils.UserUtils;

/**
 * 数据Entity类
 * @author jeeplus
 * @version 2014-05-16
 */
public abstract class DataEntity<T> extends BaseEntity<T>
{
    
    private static final long serialVersionUID = 1L;
    
    protected String remarks; // 备注
    
    protected User createBy; // 创建者
    
    protected Date createDate; // 创建日期
    
    protected User updateBy; // 更新者
    
    protected Date updateDate; // 更新日期
    
    protected String delFlag; // 删除标记（0：正常；1：删除；2：审核）
    
    private int enable;//0 未启用  1 启用  
    
    protected String createDateString;
    
    protected String updateDateString;
    
    protected String share; // 是否开启共享权限，Y 表示开 ；N 表示关  （特殊表的信息才用到）
    
    protected User loginUser;
    
    protected int isAdmin = 0;
    
    /**
     * 类型(vdi vdp)根据类型过滤数据
     */
    private String vtype;
    
    @JsonIgnore
    public int getIsAdmin()
    {
        int flag = 0;
        List<Role> roleList = UserUtils.getRoleListByCreuntUser();
        for (Role role : roleList)
        {
            if ("system".equals(role.getEnname()))
            {
                flag = 1;
                break;
            }
        }
        isAdmin = flag;
        return isAdmin;
    }
    
    public void setIsAdmin(int isAdmin)
    {
        this.isAdmin = isAdmin;
    }
    
    @JsonIgnore
    public User getLoginUser()
    {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId()))
        {
            this.loginUser = user;
        }
        return loginUser;
    }
    
    public String getShare()
    {
        return share;
    }
    
    public void setShare(String share)
    {
        this.share = share;
    }
    
    /**
     * 获取创建时间字符串
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String getCreateDateString()
    {
        if (createDate == null)
        {
            createDate = new Date();
        }
        return DateUtils.formatDateTime(createDate);
    }
    
    /**
     * 获取修改时间字符串
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String getUpdateDateString()
    {
        if (updateDate == null)
        {
            updateDate = new Date();
        }
        return DateUtils.formatDateTime(updateDate);
    }
    
    public int getEnable()
    {
        return enable;
    }
    
    public void setEnable(int enable)
    {
        this.enable = enable;
    }
    
    public DataEntity()
    {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }
    
    public DataEntity(String id)
    {
        super(id);
    }
    
    /**
     * 插入之前执行方法，需要手动调用
     */
    @JsonIgnore
    @Override
    public void preInsert()
    {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if (!this.isNewRecord)
        {
            setId(IdGen.uuid());
        }
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId()))
        {
            this.updateBy = user;
            this.createBy = user;
        }
        this.updateDate = new Date();
        this.createDate = this.updateDate;
    }
    
    /**
     * 更新之前执行方法，需要手动调用
     */
    @Override
    public void preUpdate()
    {
        User user = UserUtils.getUser();
        if (StringUtils.isNotBlank(user.getId()))
        {
            this.updateBy = user;
        }
        this.updateDate = new Date();
    }
    
    @Length(min = 0, max = 255)
    public String getRemarks()
    {
        return remarks;
    }
    
    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }
    
    @JsonIgnore
    public User getCreateBy()
    {
        return createBy;
    }
    
    public void setCreateBy(User createBy)
    {
        this.createBy = createBy;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate()
    {
        return createDate;
    }
    
    public void setCreateDate(Date createDate)
    {
        this.createDate = createDate;
    }
    
    @JsonIgnore
    public User getUpdateBy()
    {
        return updateBy;
    }
    
    public void setUpdateBy(User updateBy)
    {
        this.updateBy = updateBy;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate()
    {
        return updateDate;
    }
    
    public void setUpdateDate(Date updateDate)
    {
        this.updateDate = updateDate;
    }
    
    @JsonIgnore
    @Length(min = 1, max = 1)
    public String getDelFlag()
    {
        return delFlag;
    }
    
    public void setDelFlag(String delFlag)
    {
        this.delFlag = delFlag;
    }

	public String getVtype() {
		if(StringUtils.isEmpty(vtype)){
			return Global.getConfig("sys_flag");
		}
		return vtype;
	}

	public void setVtype(String vtype) {
		this.vtype = vtype;
	}
}
