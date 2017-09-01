package cn.vigor.modules.server.dao;

import java.util.List;
import java.util.Map;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.AmbariBatisDao;
import cn.vigor.modules.server.entity.Component;
import cn.vigor.modules.server.entity.ServerInfos;

/**
 * 物理节点DAO接口
 * @author kiss
 * @version 2016-06-21
 */
@AmbariBatisDao
public interface ServerInfosDao extends CrudDao<ServerInfos>
{
    
    /**
     * 查询服务器上所有组件
     * @param id 服务器id
     * @return
     */
    public List<Map<String, String>> findComponentByHostId(String id);
    
    /**
     * 集群节点信息
     * @param component
     * @return
     */
    public List<Component> findComponentByServerName(Component component);
    
    /**
     * 查询所有集群信息
     * @param component
     * @return
     */
    public List<Component> findCluseServer();

    public List<ServerInfos> findNewByServerName(String name);
    
    public ServerInfos findByHostName(String hostName);
    
}