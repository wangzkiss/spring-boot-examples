/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.dataservice.entity.EServiceFunction;

/**
 * 数据服务管理DAO接口
 * @author liminfang
 * @version 2016-06-28
 */
@MyBatisDao
public interface EServiceFunctionDao extends CrudDao<EServiceFunction> {

    public int deleteServiceFuncByServiceId(int serviceId);
    
}