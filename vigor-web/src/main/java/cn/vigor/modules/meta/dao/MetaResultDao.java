package cn.vigor.modules.meta.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.sys.entity.User;

/**
 * 数据库连接信息DAO接口
 * @author kiss
 * @version 2016-05-13
 */
@MyBatisDao
public interface MetaResultDao extends CrudDao<MetaResult> {
    List<HashMap<String, Object>> findEtlFlow(String id);
    List<HashMap<String, Object>> findCalFlow(String id);
    List<Map<String,Object>> getMetaResultByType(@Param("repoId")String repoId,@Param("types")List<Integer> types,@Param("user")User user);
}