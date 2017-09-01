package cn.vigor.modules.compute.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.modules.compute.dao.ComputeJobDao;
import cn.vigor.modules.compute.dao.ComputeMetaDao;
import cn.vigor.modules.compute.dao.ComputeTaskDao;

@Service
@Transactional(readOnly = true)
public class ComputeJobService
{
    @Autowired
    public ComputeTaskDao computeTaskDao;
    
    @Autowired
    public ComputeJobDao computeJobDao;

    @Autowired
    public ComputeMetaDao computeMetaDao;
    
    
}
