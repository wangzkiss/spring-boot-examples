package cn.vigor.modules.server.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.server.dao.AlertHistoryDao;
import cn.vigor.modules.server.dao.ClusterServicesDao;
import cn.vigor.modules.server.dao.ServerInfosDao;
import cn.vigor.modules.server.entity.AlertHistory;
import cn.vigor.modules.server.entity.ClusterServices;
import cn.vigor.modules.server.entity.Component;
import cn.vigor.modules.server.entity.ServerInfos;

/**
 * 物理节点Service
 * @author kiss
 * @version 2016-06-21
 */
@Service
@Transactional(readOnly = true)
public class ServerInfosService extends CrudService<ServerInfosDao, ServerInfos> {
	
	@Autowired
	private ClusterServicesDao clusterServicesDao;
	
	@Autowired
	private AlertHistoryDao alertHistoryDao;

	public ServerInfos get(String id) {
	    ServerInfos serverInfos=super.get(id);
	    serverInfos.setComponetList(dao.findComponentByHostId(id));
		return serverInfos;
	}
	
	public List<ServerInfos> findList(ServerInfos serverInfos) {
		return super.findList(serverInfos);
	}
	
	public Page<ServerInfos> findPage(Page<ServerInfos> page, ServerInfos serverInfos) {
		return super.findPage(page, serverInfos);
	}
	
	@Transactional(readOnly = false)
	public void save(ServerInfos serverInfos) {
		super.save(serverInfos);
	}
	
	@Transactional(readOnly = false)
	public void delete(ServerInfos serverInfos) {
		super.delete(serverInfos);
	}
	public Page<Component> findComponentByServerName(Page<Component> page, Component component) {
	    component.setPage(page);
	    component.preInsert();
        page.setList(dao.findComponentByServerName(component));
        return page;
    }

    public List<Component> findCluseServer()
    {
        return  dao.findCluseServer();
    }

    public List<ServerInfos> findNewByServerName(String name)
    {
       
        return  dao.findNewByServerName(name);
    }
	
    public List<ClusterServices> findAllClusterServices(){
    	return clusterServicesDao.findAllList(new ClusterServices());
    }
	
    public Page<AlertHistory> findPage(Page<AlertHistory> page,AlertHistory alertHistory){
    	alertHistory.setPage(page);
    	alertHistory.preInsert();
        page.setList(alertHistoryDao.findList(alertHistory));
        return page;
    }
    
    public ServerInfos findHostInfoByHostName(String hostName){
    	return dao.findByHostName(hostName);
    }
}