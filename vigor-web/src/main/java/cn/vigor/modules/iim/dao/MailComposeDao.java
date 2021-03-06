/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.iim.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.iim.entity.MailCompose;

/**
 * 发件箱DAO接口
 * @author jeeplus
 * @version 2015-11-15
 */
@MyBatisDao
public interface MailComposeDao extends CrudDao<MailCompose> {
	public int getCount(MailCompose entity);
}