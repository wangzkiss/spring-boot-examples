package cn.vigor.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.sys.dao.MessageCentreDao;
import cn.vigor.modules.sys.entity.MessageCentre;

/**
 * 邮件Service
 * @author zhangfeng
 * @version 2016-07-05
 */
@Service
@Transactional(readOnly = true)
public class MessageCentreService
        extends CrudService<MessageCentreDao, MessageCentre>
{
    
    public List<MessageCentre> findList(MessageCentre messageCentre)
    {
        return super.findList(messageCentre);
    }
    
    public Page<MessageCentre> findPage(Page<MessageCentre> page,
            MessageCentre messageCentre)
    {
        return super.findPage(page, messageCentre);
    }
    
    /**
     * 更改消息状态
     * @param status
     * @return
     */
    @Transactional(readOnly = false)
    public int updateStatus(int messageId)
    {
        return dao.updateStatus(messageId);
    }
}