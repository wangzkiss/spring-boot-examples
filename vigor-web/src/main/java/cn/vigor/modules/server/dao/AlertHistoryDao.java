package cn.vigor.modules.server.dao;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.AmbariBatisDao;
import cn.vigor.modules.server.entity.AlertHistory;

@AmbariBatisDao
public interface AlertHistoryDao extends CrudDao<AlertHistory>{

}
