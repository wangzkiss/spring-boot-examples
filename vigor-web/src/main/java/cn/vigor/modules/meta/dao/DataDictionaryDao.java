package cn.vigor.modules.meta.dao;
import java.util.List;

import cn.vigor.common.persistence.CrudDao;
import cn.vigor.common.persistence.annotation.MyBatisDao;
import cn.vigor.modules.meta.entity.DataDictionary;
/**
 * 数据字典DAO接口
 * @author kiss
 * @version 2016-06-13
 */
@MyBatisDao
public interface DataDictionaryDao extends CrudDao<DataDictionary> {

    List<DataDictionary> findClums(DataDictionary dataDictionary);
}