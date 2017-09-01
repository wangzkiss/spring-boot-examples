/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.partner.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.partner.entity.PartnerServiceLog;

/**
 * 接入方管理DAO接口
 * @author liminfang
 * @version 2016-07-06
 */
@MyBatisDao
public interface PartnerServiceLogDao extends CrudDao<PartnerServiceLog> {

	
}