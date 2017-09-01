package cn.vigor.modules.meta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.meta.dao.DataExploreLogDao;
import cn.vigor.modules.meta.entity.DataExploreLog;

@Service
@Transactional(readOnly = true)
public class DataExploreLogService extends CrudService<DataExploreLogDao, DataExploreLog> {
	
	public Page<DataExploreLog> findPage(Page<DataExploreLog> page, DataExploreLog dataExploreLog) {
		return super.findPage(page, dataExploreLog);
	}
	
	public Page<DataExploreLog> findallPage(Page<DataExploreLog> page,
		 DataExploreLog dataExploreLog)
    {
	 	dataExploreLog.setPage(page);
	 	dataExploreLog.preInsert();
        page.setList(dao.findAllList(dataExploreLog));
        return page;
    }
}
