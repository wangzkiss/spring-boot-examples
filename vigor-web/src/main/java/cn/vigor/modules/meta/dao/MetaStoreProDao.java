package cn.vigor.modules.meta.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.MetaStorePro;

/**
 * 存储数据信息DAO接口
 * @author kiss
 * @version 2016-05-17
 */
@MyBatisDao
public interface MetaStoreProDao extends CrudDao<MetaStorePro> {
    
    public List<MetaStorePro> findByStoreIds(@Param("ids")List<Integer> ids);

	
}