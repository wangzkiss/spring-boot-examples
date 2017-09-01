package cn.vigor.modules.tji.util;

import java.util.List;

import com.google.common.collect.Lists;

import cn.vigor.common.utils.SpringContextHolder;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.service.MetaSourceService;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.tji.dao.TaskDao;
import cn.vigor.modules.tji.dao.TaskGroupDao;
import cn.vigor.modules.tji.entity.TaskBaseType;
import cn.vigor.modules.tji.entity.TaskGroup;

public class TaskUitl{
    
    private static TaskGroupDao taskGroupDao = SpringContextHolder.getBean(TaskGroupDao.class);
    
    private static TaskDao taskDao = SpringContextHolder.getBean(TaskDao.class);
    
    private static MetaSourceService metaSourceService=SpringContextHolder.getBean(MetaSourceService.class);
    
    /**
     * 平台存储
     */
    private static MetaStoreService metaStoreService=SpringContextHolder.getBean(MetaStoreService.class);
    /**
     * 根据分组类型获取分组信息
     * @param type
     * @return List<TaskGroup>
     */
    public static List<TaskGroup> getGroupInfoByType(Integer type){
        List<TaskGroup> taskGroupList = taskGroupDao.getGroupInfoByType(type);
        if (taskGroupList == null){
            taskGroupList = Lists.newArrayList();
        }
        return taskGroupList;
    }
    
    /**
     * 获取所有的任务类型信息
     * @param type
     * @return List<TaskGroup>
     */
    public static List<TaskBaseType> getAllTaskType(){
        List<TaskBaseType> taskTypeList = taskDao.getAllTaskType();
        if (taskTypeList == null){
            taskTypeList = Lists.newArrayList();
        }
        return taskTypeList;
    }
    
    /**
     * 获取所有平台存储信息
     * @param type
     * @return List<TaskGroup>
     */
    public static List<MetaStore> findAllMetaStore(Integer storeType){
        return null;
     /*   List<MetaStore>  list//=  metaStoreService.findAllMetaStore(storeType);
        if (list == null){
            list = Lists.newArrayList();
        }
        return list;*/
    }
    
    /**
     * 获取所有外部数据源信息
     * @param type
     * @return List<TaskGroup>
     */
    public static List<MetaSource> findAllMetaSource(Integer sourceType){
        return null;
       /* List<MetaSource>  list=  metaSourceService.findAllMetaSource(sourceType);
        if (list == null){
            list = Lists.newArrayList();
        }
        return list;*/
    }
}
