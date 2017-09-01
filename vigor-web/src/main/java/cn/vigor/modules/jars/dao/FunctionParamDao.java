package cn.vigor.modules.jars.dao;


import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.jars.entity.FunctionParam;

/**
 * 函数管理DAO接口
 * @author kiss
 * @version 2016-06-13
 */
@MyBatisDao
public interface FunctionParamDao extends CrudDao<FunctionParam> {

    public List<HashMap<String,Object>> findParamsByFunctionId(String id);
    
    /**
     * 获取参数名
     * @param id
     * @return
     */
    public List<String> findParamsNameByFunctionId(@Param("functionId") int id,
            @Param("flag") int flag);
}