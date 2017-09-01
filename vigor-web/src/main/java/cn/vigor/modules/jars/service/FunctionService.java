package cn.vigor.modules.jars.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.persistence.Page;
import cn.vigor.common.service.CrudService;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.jars.dao.FunctionDao;
import cn.vigor.modules.jars.dao.FunctionParamDao;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.jars.entity.FunctionParam;
import cn.vigor.modules.jars.entity.FunctionType;
import cn.vigor.modules.meta.dao.DataPermissionDao;
import cn.vigor.modules.meta.entity.DataPermission;

/**
 * 函数管理Service
 * @author kiss
 * @version 2016-06-13
 */
@Service
@Transactional(readOnly = true)
public class FunctionService extends CrudService<FunctionDao, Function> {

	@Autowired
	private FunctionParamDao functionParamDao;
	@Autowired
    private DataPermissionDao dataPermissionDao;
	public Function get(String id) {
		Function function = super.get(id);
		function.setFunctionParamList(functionParamDao.findList(new FunctionParam(function)));
		return function;
	}
	
	public List<Function> findList(Function function) {
		return super.findList(function);
	}
	
	public Page<Function> findPage(Page<Function> page, Function function) {
	    if(page.getOrderBy()==null|| "".equals(page.getOrderBy()))
        {
            page.setOrderBy("id desc");
        }
		return super.findPage(page, function);
	}
	
	@Transactional(readOnly = false)
	public void save(Function function) {
		super.save(function);
		//添加数据权限信息
        DataPermission datap=new DataPermission();
        datap.setAll("Y");//设置拥有所有
        datap.setUser(function.getCurrentUser());//得到创建者
        datap.setDataType("function");//数据类型
        datap.setOfficeId(function.getCurrentUser().getOffice().getId());
        datap.setDataId(function.getId()); //设置数据
        if (function.getIsNewRecord()){//标示新增
            dataPermissionDao.insert(datap);//插入权限信息
        }
		
		for (FunctionParam functionParam : function.getFunctionParamList()){
			if (functionParam.getId() == null){
				continue;
			}
			if (FunctionParam.DEL_FLAG_NORMAL.equals(functionParam.getDelFlag())){
				if (StringUtils.isBlank(functionParam.getId())){
					functionParam.setFunctionId(function);
					functionParam.preInsert();
					functionParamDao.insert(functionParam);
				}else{
					functionParam.preUpdate();
					functionParamDao.update(functionParam);
				}
			}else{
				functionParamDao.delete(functionParam);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(Function function) {
		super.delete(function);
		DataPermission datap=new DataPermission();
        datap.setUser(function.getCurrentUser());//得到创建者
        datap.setDataType("function");//数据类型
        datap.setDataId(function.getId()); //设置数据
        dataPermissionDao.delete(datap);//插入权限信息
		functionParamDao.delete(new FunctionParam(function));
	}
	/**
     * 找到所有的函数类型信息
     * @return List<FunctionType>
     */
    public List<FunctionType> getFunctionType(){
        return this.dao.getFunctionType();
    }
	
}