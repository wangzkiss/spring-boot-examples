package cn.vigor.modules.sys.entity;

import javax.validation.constraints.NotNull;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 邮件模板Entity
 * @author zhangfeng
 * @version 2016-08-12
 */
public class MailTemplate extends DataEntity<MailTemplate>
{
    
    private static final long serialVersionUID = 1L;
    
    private Integer mtId; // 主键
    
    private Integer mtType; // 模板类型 1任务失败邮件
    
    private String mtContent; // 模板内容
    
    private Integer pitchOn; // 是否被选中，0否，1是
    
    public MailTemplate()
    {
        super();
    }
    
    public MailTemplate(String id)
    {
        super(id);
    }
    
    @NotNull(message = "主键不能为空")
    @ExcelField(title = "主键", align = 2, sort = 0)
    public Integer getMtId()
    {
        return mtId;
    }
    
    public void setMtId(Integer mtId)
    {
        this.mtId = mtId;
    }
    
    @NotNull(message = "模板类型 1任务失败邮件不能为空")
    @ExcelField(title = "模板类型 1任务失败邮件", align = 2, sort = 1)
    public Integer getMtType()
    {
        return mtType;
    }
    
    public void setMtType(Integer mtType)
    {
        this.mtType = mtType;
    }
    
    @ExcelField(title = "模板内容", align = 2, sort = 2)
    public String getMtContent()
    {
        return mtContent;
    }
    
    public void setMtContent(String mtContent)
    {
        this.mtContent = mtContent;
    }
    
    @NotNull(message = "是否被选中，0否，1是不能为空")
    @ExcelField(title = "是否被选中，0否，1是", align = 2, sort = 3)
    public Integer getPitchOn()
    {
        return pitchOn;
    }
    
    public void setPitchOn(Integer pitchOn)
    {
        this.pitchOn = pitchOn;
    }
    
}