package cn.vigor.modules.server.dao;


import java.util.List;
import java.util.Map;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.server.entity.ClusterParam;
import cn.vigor.modules.server.entity.Platform;

/**
 * 集群信息DAO接口
 * @author kiss
 * @version 2016-06-30
 */
@MyBatisDao
public interface PlatformDao extends CrudDao<Platform> {
    public Platform getOne(Platform entity);
    public int updateStatus(Platform entity);
    public List< Map<String, String>> etlclusterFields(ClusterParam param);
    public Map<String, String> findClusterStatus(String clsterId);
    public Map<String, String> findJobStatus(Platform entity);
    public Map<String, Object> findBasicInfo();
    
}