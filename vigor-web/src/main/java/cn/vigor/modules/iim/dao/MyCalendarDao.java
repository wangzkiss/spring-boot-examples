/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.iim.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.iim.entity.MyCalendar;


/**
 * 日历DAO接口
 * @author liugf
 * @version 2016-04-19
 */
@MyBatisDao
public interface MyCalendarDao extends CrudDao<MyCalendar> {
	
}