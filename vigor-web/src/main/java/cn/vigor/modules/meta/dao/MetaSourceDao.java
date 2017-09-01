package cn.vigor.modules.meta.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.MetaSource;

/**
 * 数据库连接信息DAO接口
 * @author kiss
 * @version 2016-05-13
 */
@MyBatisDao
public interface MetaSourceDao extends CrudDao<MetaSource> {
    List<HashMap<String, Object>> findEtlFlow(String id);
    List<HashMap<String, Object>> findCalFlow(String id);
    
    /**
     * 找到所有的外部数据源信息
     * @param map
     * @return
     */
    List<HashMap<String,Object>> findAllMetaSource(Map<String, Object> map);
    
    MetaSource getBySourceName(@Param("sourceName") String sourceName);
}