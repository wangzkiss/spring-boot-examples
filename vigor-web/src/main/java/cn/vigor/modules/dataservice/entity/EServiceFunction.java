/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package cn.vigor.modules.dataservice.entity;


import cn.vigor.common.persistence.DataEntity;
import cn.vigor.modules.dataservice.utils.Constants;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.sys.utils.DictUtils;

/**
 * 数据服务管理Entity
 * @author liminfang
 * @version 2016-06-28
 */
public class EServiceFunction extends DataEntity<EServiceFunction> {
	
	private static final long serialVersionUID = 1L;
	private Integer serviceId;		// 服务id，关联服务信息表id 父类
	private Function func;     // 函数，通过函数id关联函数信息表
	private String funcTypeLabel;
	private int flag;
	
	public EServiceFunction() {
		super();
	}

	public EServiceFunction(String id){
		super(id);
	}

	public EServiceFunction(int serviceId){
		this.serviceId = serviceId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

    public Function getFunc()
    {
        return func;
    }

    public void setFunc(Function func)
    {
        this.func = func;
   }

    public String getFuncTypeLabel()
    {
        funcTypeLabel = DictUtils.getDictLabel("" + func.getFunctionType(), Constants.DICT_FUNC_TYPE, "未知");
        return funcTypeLabel;
    }

    public void setFuncTypeLabel(String funcTypeLabel)
    {
        this.funcTypeLabel = funcTypeLabel;
    }

    public int getFlag()
    {
        return flag;
    }

    public void setFlag(int flag)
    {
        this.flag = flag;
    }
	
}