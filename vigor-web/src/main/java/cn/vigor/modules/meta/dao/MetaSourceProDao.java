package cn.vigor.modules.meta.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.MetaSourcePro;

/**
 * 源数据表信息DAO接口
 * @author kiss
 * @version 2016-05-17
 */
@MyBatisDao
public interface MetaSourceProDao extends CrudDao<MetaSourcePro> {
	
    public List<MetaSourcePro> findSourceProBySourceId(@Param("sourceId")Integer sourceId);
}