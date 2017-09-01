package cn.vigor.modules.compute.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.modules.compute.bean.ComputeFunction;
import cn.vigor.modules.compute.bean.ComputeFunctionParam;
import cn.vigor.modules.compute.bean.ComputeTaskGroup;
import cn.vigor.modules.compute.dao.ComputeJobDao;
import cn.vigor.modules.compute.dao.ComputeMetaDao;
import cn.vigor.modules.compute.dao.ComputeTaskDao;

@Service
@Transactional(readOnly = true)
public class ComputeTaskService
{
    @Autowired
    public ComputeTaskDao computeTaskDao;
    @Autowired
    public ComputeJobDao computeJobDao;
    @Autowired
    public ComputeMetaDao computeMetaDao;

    public List<ComputeFunction> getFunctionsByType(int type){
        return computeTaskDao.getFunctionsByType(type);
    }
    
    public List<ComputeTaskGroup> getTaskGroup(){
        return computeTaskDao.getTaskGroup();
    }

    public List<ComputeFunctionParam> getFunctionParams(int parseInt)
    {
        return computeTaskDao.getFunctionParams(parseInt);
    }
    
    public int getTaskSameName(String taskName)
    {
        return computeTaskDao.getTaskSameName(taskName);
    }
}
