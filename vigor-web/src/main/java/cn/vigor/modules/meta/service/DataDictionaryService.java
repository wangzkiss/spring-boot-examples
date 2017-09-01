package cn.vigor.modules.meta.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.modules.meta.dao.DataDictionaryDao;
import cn.vigor.modules.meta.entity.DataDictionary;

/**
 * 数据字典Service
 * @author kiss
 * @version 2016-06-13
 */
@Service
@Transactional(readOnly = true)
public class DataDictionaryService extends CrudService<DataDictionaryDao, DataDictionary> {

	
	public List<DataDictionary> findClums(DataDictionary dataDictionary) {
		return dao.findClums(dataDictionary);
	}
	
	public Page<DataDictionary> findPage(Page<DataDictionary> page, DataDictionary dataDictionary) {
		return super.findPage(page, dataDictionary);
	}

    public Page<DataDictionary> findallPage(Page<DataDictionary> page,
            DataDictionary dataDictionary)
    {
        dataDictionary.setPage(page);
        dataDictionary.preInsert();
        page.setList(dao.findAllList(dataDictionary));
        return page;
    }
	
	
	
}