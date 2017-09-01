package cn.vigor.modules.meta.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.BaseDateParams;

/**
 * 基础日期参数DAO
 * @author huzeyuan
 * @version v3.0
 * 2016-10-28
 *
 */
@MyBatisDao
public interface BaseDateParamsDao extends CrudDao<BaseDateParams>{
    
}
