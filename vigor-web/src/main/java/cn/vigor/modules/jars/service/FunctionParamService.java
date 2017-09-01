package cn.vigor.modules.jars.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.vigor.common.service.CrudService;
import cn.vigor.modules.jars.dao.FunctionParamDao;
import cn.vigor.modules.jars.entity.FunctionParam;

@Service
@Transactional(readOnly = true)
public class FunctionParamService
        extends CrudService<FunctionParamDao, FunctionParam>
{
    /**
     * 根据Id 获取 函数参数信息
     */
    public List<HashMap<String, Object>> findParamsByFunctionId(String id)
    {
      //参数做排序  安输入 输出排序
        List<HashMap<String, Object>> newparams=new ArrayList<HashMap<String,Object>>();
        List<HashMap<String, Object>> params= this.dao.findParamsByFunctionId(id);
        for (HashMap<String, Object> map : params) {
            if(map.get("paramName").toString().contains("input_source_name")){
                newparams.add(map);
            }
        }
        for (HashMap<String, Object> map : params) {
            if(!map.get("paramName").toString().contains("input_source_name")&& map.get("paramName").toString().contains("input_") ){
                newparams.add(map);
            }
            
        }
        for (HashMap<String, Object> map : params) {
             if(map.get("paramName").toString().contains("time") || 
                    map.get("paramName").toString().contains("date")||
                    map.get("paramName").toString().contains("day")||
                    map.get("paramName").toString().equals("hour")||
                    map.get("paramName").toString().contains("condition")
            ){
                newparams.add(map);
            }
        }
        for (HashMap<String, Object> map : params) {
            if(map.get("paramName").toString().contains("output_store_name")){
                newparams.add(map);
            }
        }
        for (HashMap<String, Object> map : params) {
            if(!map.get("paramName").toString().contains("output_store_name")&& map.get("paramName").toString().contains("output_") ){
                newparams.add(map);
            }
        }
        return newparams;
    }
    
    /**
     * 获取参数名
     * @param id
     * @return
     */
    public List<String> findParamsNameByFunctionId(int id, int flag)
    {
        return dao.findParamsNameByFunctionId(id, flag);
    }
    
    public List<HashMap<String, Object>> getParamsByFunctionId(String id){
        return this.dao.findParamsByFunctionId(id);
    }
}
