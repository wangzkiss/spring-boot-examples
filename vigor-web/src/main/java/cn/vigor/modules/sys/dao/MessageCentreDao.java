/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.sys.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.sys.entity.MessageCentre;

/**
 * 邮件DAO接口
 * @author zhangfeng
 * @version 2016-07-05
 */
@MyBatisDao
public interface MessageCentreDao extends CrudDao<MessageCentre> {

    /**
     * 更改消息状态
     * @param status
     * @return
     */
    public int updateStatus(int messageId);
	
}