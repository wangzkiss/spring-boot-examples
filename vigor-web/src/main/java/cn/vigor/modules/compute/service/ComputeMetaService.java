package cn.vigor.modules.compute.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.modules.compute.bean.ComputeMetaResult;
import cn.vigor.modules.compute.bean.ComputeMetaStore;
import cn.vigor.modules.compute.bean.InParam;
import cn.vigor.modules.compute.bean.Repositories;
import cn.vigor.modules.compute.bean.TmpTable;
import cn.vigor.modules.compute.dao.ComputeJobDao;
import cn.vigor.modules.compute.dao.ComputeMetaDao;
import cn.vigor.modules.compute.dao.ComputeTaskDao;

@Service
@Transactional(readOnly = true)
public class ComputeMetaService
{
    @Autowired
    public ComputeTaskDao computeTaskDao;
    @Autowired
    public ComputeJobDao computeJobDao;
    @Autowired
    public ComputeMetaDao computeMetaDao;
    
    public List<Repositories> getAllRepo(InParam param){
        return computeMetaDao.getAllRepo(param);
    }
    
    public List<ComputeMetaStore> getMetaStoresByTypes(InParam param){
        return computeMetaDao.getMetaStoresByTypes(param);
    }
    
    public List<ComputeMetaResult> getMetaResultByTypes(InParam param){
        return computeMetaDao.getMetaResultByTypes(param);
    }

    public List<TmpTable> getAllTableName(InParam inparam)
    {
        return computeMetaDao.getAllTableName(inparam);
    }

    public Integer getTaskId(String dbname, String s, int userid, boolean admin)
    {
        return computeMetaDao.getTaskId(dbname, s, userid, admin);
    }
}
