package cn.vigor.modules.sys.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import cn.vigor.common.persistence.DataEntity;
import cn.vigor.common.utils.excel.annotation.ExcelField;

/**
 * 邮件Entity
 * @author zhangfeng
 * @version 2016-07-05
 */
public class MessageCentre extends DataEntity<MessageCentre>
{
    
    private static final long serialVersionUID = 1L;
    
    private Long messageId; // 消息id
    
    private Integer messageType; // 消息类型。1：邮件、2：短信
    
    private Integer messageStatus; // 消息状态。-1：未发送、0：发送失败、1：发送成功
    
    private Integer messageTemplate; // 消息模板。
    
    private Date finishTime; // 消息发送完成时间
    
    private String receiveNum; // 消息接收人。可以为多个，以逗号分隔
    
    private String copytoNum; // 消息抄送人。可以为多个，以逗号分隔
    
    private String recipientName; // 邮件正文昵称。
    
    private String messageTitle; // 邮件标题
    
    private String messageContent; // 邮件正文。
    
    private Date startTime;
    
    private Date endTime;
    
    public MessageCentre()
    {
        super();
    }
    
    public MessageCentre(String id)
    {
        super(id);
    }
    
    @NotNull(message = "消息id不能为空")
    @ExcelField(title = "消息id", align = 2, sort = 0)
    public Long getMessageId()
    {
        return messageId;
    }
    
    public void setMessageId(Long messageId)
    {
        this.messageId = messageId;
    }
    
    @NotNull(message = "消息类型。1：邮件、2：短信不能为空")
    @ExcelField(title = "消息类型。1：邮件、2：短信", align = 2, sort = 1)
    public Integer getMessageType()
    {
        return messageType;
    }
    
    public void setMessageType(Integer messageType)
    {
        this.messageType = messageType;
    }
    
    @NotNull(message = "消息状态。-1：未发送、0：发送失败、1：发送成功不能为空")
    @ExcelField(title = "消息状态。-1：未发送、0：发送失败、1：发送成功", align = 2, sort = 2)
    public Integer getMessageStatus()
    {
        return messageStatus;
    }
    
    public void setMessageStatus(Integer messageStatus)
    {
        this.messageStatus = messageStatus;
    }
    
    @NotNull(message = "消息模板。不能为空")
    @ExcelField(title = "消息模板。", align = 2, sort = 3)
    public Integer getMessageTemplate()
    {
        return messageTemplate;
    }
    
    public void setMessageTemplate(Integer messageTemplate)
    {
        this.messageTemplate = messageTemplate;
    }
    
    public Date getFinishTime()
    {
        return finishTime;
    }
    
    public void setFinishTime(Date finishTime)
    {
        this.finishTime = finishTime;
    }
    
    public Date getStartTime()
    {
        return startTime;
    }
    
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }
    
    public Date getEndTime()
    {
        return endTime;
    }
    
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }
    
    @Length(min = 1, max = 200, message = "消息接收人。可以为多个，以逗号分隔长度必须介于 1 和 200 之间")
    @ExcelField(title = "消息接收人。可以为多个，以逗号分隔", align = 2, sort = 5)
    public String getReceiveNum()
    {
        return receiveNum;
    }
    
    public void setReceiveNum(String receiveNum)
    {
        this.receiveNum = receiveNum;
    }
    
    @Length(min = 0, max = 200, message = "消息抄送人。可以为多个，以逗号分隔长度必须介于 0 和 200 之间")
    @ExcelField(title = "消息抄送人。可以为多个，以逗号分隔", align = 2, sort = 6)
    public String getCopytoNum()
    {
        return copytoNum;
    }
    
    public void setCopytoNum(String copytoNum)
    {
        this.copytoNum = copytoNum;
    }
    
    @Length(min = 0, max = 20, message = "邮件正文昵称。长度必须介于 0 和 20 之间")
    @ExcelField(title = "邮件正文昵称。", align = 2, sort = 7)
    public String getRecipientName()
    {
        return recipientName;
    }
    
    public void setRecipientName(String recipientName)
    {
        this.recipientName = recipientName;
    }
    
    @Length(min = 0, max = 100, message = "邮件标题长度必须介于 0 和 100 之间")
    @ExcelField(title = "邮件标题", align = 2, sort = 8)
    public String getMessageTitle()
    {
        return messageTitle;
    }
    
    public void setMessageTitle(String messageTitle)
    {
        this.messageTitle = messageTitle;
    }
    
    @ExcelField(title = "邮件正文。", align = 2, sort = 9)
    public String getMessageContent()
    {
        return messageContent;
    }
    
    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }
    
}