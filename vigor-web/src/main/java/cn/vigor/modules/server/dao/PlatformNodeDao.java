package cn.vigor.modules.server.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.server.entity.PlatformNode;

/**
 * 集群信息DAO接口
 * @author kiss
 * @version 2016-06-30
 */
@MyBatisDao
public interface PlatformNodeDao extends CrudDao<PlatformNode>
{
    public int updateStatus(PlatformNode entity);
    
}