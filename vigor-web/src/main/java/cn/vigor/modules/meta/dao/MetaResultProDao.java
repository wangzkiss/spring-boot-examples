package cn.vigor.modules.meta.dao;


import org.apache.ibatis.annotations.Param;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.MetaResultPro;

/**
 * 结果数据集DAO接口
 * @author kiss
 * @version 2016-05-17
 */
@MyBatisDao
public interface MetaResultProDao extends CrudDao<MetaResultPro> {
	public MetaResultPro getByResultIdAndProName(@Param("resultId")String resultId,@Param("proName")String proName);
}