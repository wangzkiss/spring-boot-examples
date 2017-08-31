/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.sys.dao;


import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.sys.entity.Log;

/**
 * 日志DAO接口
 * @author jeeplus
 * @version 2014-05-16
 */
@MyBatisDao
public interface LogDao extends CrudDao<Log> {

	public void empty();
}
