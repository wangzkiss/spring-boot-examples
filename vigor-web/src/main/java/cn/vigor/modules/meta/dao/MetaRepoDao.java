package cn.vigor.modules.meta.dao;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.sys.entity.User;

/**
 * 数据库连接信息DAO接口
 * @author kiss
 * @version 2016-05-13
 */
@MyBatisDao
public interface MetaRepoDao extends CrudDao<MetaRepo> {

    List<HashMap<String,String>> findListByRepoType(MetaRepo repo);

    String getHiveDataType(@Param("dataType")String lowerCase,@Param("type")Integer type);
    
    List<MetaRepo> getHdfsAndFtpSources(@Param("metaTypes")List<Integer> metaTypes,
            @Param("repoTypes")List<Integer> repoTypes,@Param("isAdmin")int isAdmin,@Param("createBy")User user);
    
    List<MetaRepo> getAllRepo(@Param("metaTypes")List<Integer> metaTypes,
            @Param("repoTypes")List<Integer> repoTypes,@Param("isAdmin")int isAdmin);
    List<MetaRepo> findListByType(MetaRepo metaRepo);

    MetaRepo findExistRepo(MetaRepo repo);
}