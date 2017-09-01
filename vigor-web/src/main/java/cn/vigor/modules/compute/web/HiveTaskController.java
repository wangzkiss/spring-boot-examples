package cn.vigor.modules.compute.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.contants.Contants;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.common.web.BaseController;
import cn.vigor.modules.compute.bean.AssociationCondition;
import cn.vigor.modules.compute.bean.ComputeFunction;
import cn.vigor.modules.compute.bean.ComputeFunctionParam;
import cn.vigor.modules.compute.bean.ComputeMetaResult;
import cn.vigor.modules.compute.bean.ComputeMetaStore;
import cn.vigor.modules.compute.bean.ComputeTaskGroup;
import cn.vigor.modules.compute.bean.HiveSql;
import cn.vigor.modules.compute.bean.InParam;
import cn.vigor.modules.compute.bean.LogicCond;
import cn.vigor.modules.compute.bean.MetaStoreField;
import cn.vigor.modules.compute.bean.Output;
import cn.vigor.modules.compute.bean.OutputField;
import cn.vigor.modules.compute.bean.Repositories;
import cn.vigor.modules.compute.bean.TableAssociation;
import cn.vigor.modules.compute.bean.TmpTable;
import cn.vigor.modules.compute.entity.ComputeTaskDetailBean;
import cn.vigor.modules.compute.service.CommonService;
import cn.vigor.modules.compute.service.ComputeMetaService;
import cn.vigor.modules.compute.service.ComputeTaskService;
import cn.vigor.modules.compute.utils.TestUtil;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.service.MetaRepoService;
/**
 * 任务配置接口Controller
 * @author chenyan-38342
 * @version v1.0
 * @date 2016-12-27
 *
 */
@Controller
@RequestMapping(value="${adminPath}/compute/computetask")
public class HiveTaskController extends BaseController{
    
    @Autowired
    private ComputeTaskService computeTaskService;
    @Autowired
    private ComputeMetaService computeMetaService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private MetaRepoService metaRepoService;
    
    
    private Map<String,MetaStoreField>  mapTableToStoreField ;
    
    private Map<String, ComputeMetaStore> storeMapFields ;
    
    
    /**
     * 根据taskId编辑任务
     * @param taskId
     * @return
     */
    @RequestMapping(value="/editTask",method = RequestMethod.GET)
    public String editTask(@RequestParam(value="taskId",required=true)int taskId,ComputeTaskDetailBean detailBean,Model model,ComputeTaskDetailBean taskDetail){
        try
        {
            taskDetail = getBaseData(taskDetail);
            Output outputFromXml = commonService.getOutputFromXML(taskId);
            if (outputFromXml != null) {
                
                outputFromXml.setFunctions(detailBean.getFunctions());
                outputFromXml.setTaskGroups(detailBean.getTaskGroups());
                outputFromXml.setInputResources(detailBean.getInputResources());
                outputFromXml.setTaskId(taskId);
                JSONObject json = (JSONObject)JSONObject.toJSON(outputFromXml);
                System.out.println(json);
                model.addAttribute("data",taskDetail);
                model.addAttribute("data",json);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "modules/tji/taskconfigure/hiveTaskForm";
    }
    
    /**
     * 添加hive/spark任务
     */
    @RequestMapping(value = "/showHiveTask",method = RequestMethod.GET)
    public String showHiveTask(ComputeTaskDetailBean taskDetail,Model model) throws Exception{
        taskDetail = getBaseData(taskDetail);
        model.addAttribute("data", taskDetail);
        return "modules/tji/taskconfigure/hiveTaskForm";
    }
    
    /**
     * 根据资源库查询数据源列表
     * @param resourceName 资源库名
     * @return 结果
     */
    @RequestMapping(value="/getDataSourceByRepo",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getDataSourceByDatabase(@RequestParam(value="repoId",required=true)String repoId,
    		ComputeTaskDetailBean detailBean ){
        JSONObject jsonObject = new JSONObject();
        try
        {
        	String[] repoIds = {};
        	if(repoId.contains(",")){
        		repoIds = repoId.split(",");
        	}else{
        		repoIds = new String[]{repoId};
        	}
        	//输入字段信息
            mapTableToStoreField = new HashMap<String,MetaStoreField>();
            storeMapFields = new HashMap<String, ComputeMetaStore>();
            List<ComputeMetaStore> reInputMetaStores = new ArrayList<ComputeMetaStore>();
        	for (String id : repoIds) {
        		InParam  param = new InParam();
                param.setRepoArray(new int[] { Contants.SOURCE_TYPE_HIVE });
                param.setRepoMetaTypeArray(new int[]{Contants.STORE});
                if(detailBean.getIsAdmin()==0){
                    param.setAdmin(false); 
                }else{
                    param.setAdmin(true);
                }
                param.setUserid(Integer.parseInt(detailBean.getLoginUser().getId()));
                param.setArray(new int[]{Contants.SOURCE_TYPE_HIVE});
                param.setRepoId(id);
                
                //输入数据源信息
                List<ComputeMetaStore> inputMetaStores = computeMetaService.getMetaStoresByTypes(param);
                reInputMetaStores.addAll(inputMetaStores);
                if(inputMetaStores.size()>0){
                	MetaRepo metaRepo = metaRepoService.get(id);
                    for(ComputeMetaStore cms:inputMetaStores){
                        List<MetaStoreField> storeFields = cms.getMetaStoreFields();
                        for(MetaStoreField sf:storeFields){
                            mapTableToStoreField.put(metaRepo.getRepoName() + "." + cms.getStoreFile()+"."+sf.getProName(),sf);
                            storeMapFields.put(metaRepo.getRepoName() + "." + cms.getStoreFile(), cms);
                        }
                    }
                }
			}
        	getSession().setAttribute("mapTableToStoreField", mapTableToStoreField);
            getSession().setAttribute("storeMapFields", storeMapFields);
            detailBean.setMapTableToSourceField(mapTableToStoreField);
            detailBean.setStoreMapFields(storeMapFields);
            detailBean.setInputMetaStores(reInputMetaStores);
            jsonObject.put("success", true);
            jsonObject.put("msg", "成功");
            jsonObject.put("data", reInputMetaStores);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "失败");
        }
        return jsonObject;
    }
    
    /**
     * 根据输出资源库id,输出类型,结果类型获取输出
     * @param typeId mysql 6  hive  5   ftp 2  trafodion 11
     * @param resultType  1 平台存储  0 结果集
     * @return
     */
    @RequestMapping(value="/getOutputRepo",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getOutputRepo(
            @RequestParam(value="typeId",required=true)Integer typeId,
            @RequestParam(value="resultType",required=true)Integer resultType,ComputeTaskDetailBean detailBean){
        JSONObject jsonObject = new JSONObject();
        try
        {
            List<Repositories> repo =null;
            InParam param = new InParam();
            param.setRepoArray(new int[] { typeId});
            if(detailBean.getIsAdmin()==0){
                param.setAdmin(false); 
            }else{
                param.setAdmin(true);
            }
            param.setUserid(Integer.parseInt(detailBean.getLoginUser().getId()));
            
            
            if(resultType==1){
                param.setRepoMetaTypeArray(new int[]{Contants.STORE});
                repo = computeMetaService.getAllRepo(param);
            }else if(resultType==0){
                param.setRepoMetaTypeArray(new int[]{Contants.RESULT});
                repo = computeMetaService.getAllRepo(param);
            }
            List<Repositories> list = new ArrayList<Repositories>();
            for(int i=0;i<repo.size();i++){
                if(!repo.get(i).getRepoName().contains("${")){
                    list.add(repo.get(i));
                }
            }
            
            jsonObject.put("success", true);
            jsonObject.put("data", list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "失敗");
        }
        return jsonObject;
    }
    /**
     * 根据输出资源库id,输出类型,结果类型获取输出
     * @param outRepoId
     * @param typeId mysql 6  hive  5   ftp 2  trafodion 11
     * @param resultType  1 平台存储  0 结果集
     * @return
     */
    @RequestMapping(value="/getOutputs",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getOutputs(@RequestParam(value="outRepoId",required=true)String outRepoId,
            @RequestParam(value="typeId",required=true)Integer typeId,
            @RequestParam(value="resultType",required=true)Integer resultType,ComputeTaskDetailBean detailBean){
        JSONObject jsonObject = new JSONObject();
        try
        {
            
            InParam param = new InParam();
            param.setRepoArray(new int[] { Contants.SOURCE_TYPE_HIVE});
            if(detailBean.getIsAdmin()==0){
                param.setAdmin(false); 
            }else{
                param.setAdmin(true);
            }
            param.setUserid(Integer.parseInt(detailBean.getLoginUser().getId()));
            param.setArray(new int[]{typeId});
            param.setRepoId(outRepoId);
            
            List<ComputeMetaStore> mateStorelist = null;
            List<ComputeMetaResult> mateResultlist = null;
            
            if(resultType==1){
                param.setRepoMetaTypeArray(new int[]{Contants.STORE});
                mateStorelist = computeMetaService.getMetaStoresByTypes(param);
            }else if(resultType==0){
                param.setRepoMetaTypeArray(new int[]{Contants.RESULT});
                mateResultlist = computeMetaService.getMetaResultByTypes(param);
            }
            if(mateStorelist!=null&&mateStorelist.size()>0){
                jsonObject.put("data", mateStorelist);
                jsonObject.put("success", true);
                jsonObject.put("msg", "成功");
            }else if(mateResultlist!=null&&mateResultlist.size()>0){
                jsonObject.put("data", mateResultlist);
                jsonObject.put("success", true);
                jsonObject.put("msg", "成功");
            }else{
                jsonObject.put("data", "");
                jsonObject.put("success", true);
                jsonObject.put("msg", "成功");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("msg", "失敗");
        }
        return jsonObject;
    }
    /**
     * 添加自定义mr,spark任务的基础数据
     * @param detailBean
     * @return
     */
    private ComputeTaskDetailBean getBaseData(ComputeTaskDetailBean detailBean){
        InParam  param = new InParam();
        param.setRepoArray(new int[] { Contants.SOURCE_TYPE_HIVE });
        param.setRepoMetaTypeArray(new int[]{Contants.STORE});
        if(detailBean.getIsAdmin()==1){
            param.setAdmin(true); 
        }else{
            param.setAdmin(false);
        }
        param.setUserid(Integer.parseInt(detailBean.getLoginUser().getId()));
        
        List<Repositories> list = new ArrayList<Repositories>();
        
        List<Repositories> listRepo = computeMetaService.getAllRepo(param);
        for(int i=0;i<listRepo.size();i++){
            if(!listRepo.get(i).getRepoName().contains("${")){
                list.add(listRepo.get(i));
            }
        }
        detailBean.setInputResources(list);
        //根据组的类型获取分组(这里获取计算任务的分组)
        List<ComputeTaskGroup> groups = computeTaskService.getTaskGroup();
        detailBean.setTaskGroups(groups);
        //函数类型列表
        List<ComputeFunction> functions = computeTaskService.getFunctionsByType(Contants.HIVE_FUNCTION_TYPE);
        detailBean.setFunctions(functions);
        
        getSession().setAttribute("detailBean", detailBean);
        return detailBean;
    }
    
    
    /**
     * 添加hive计算任务
     * @param taskDetail
     * @param model
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/addHiveTask",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addHiveTask(Output params,Model model,ComputeTaskDetailBean detailBean) throws Exception{
        HiveSql hiveSql = new HiveSql();
        
        Output output = new Output();
        
        Map<String, MetaStoreField> mapTableToStoreField = (Map<String, MetaStoreField>) getSession().getAttribute("mapTableToStoreField");
        Map<String, ComputeMetaStore> storeMapFields =  (Map<String, ComputeMetaStore>) getSession().getAttribute("storeMapFields");
        detailBean = (ComputeTaskDetailBean) getSession().getAttribute("detailBean");
        
        String tname = StringEscapeUtils.unescapeHtml(params.getTaskName());
        String repoId = params.getRepoId();
        output.setTaskName(tname);    /*任务名称*/
        output.setTaskDesc(params.getTaskDesc());    /*任务描述*/
        output.setTaskType(Contants.TASK_TYPE_HIVE); /*任务类型*/
        output.setRepoId(repoId);         /*输入资源id*/
        output.setOutRepoId(params.getOutRepoId());   /*输出资源id*/
        output.setGroupId(params.getGroupId());       /*组id*/
        output.setOutputString(params.getOutputString());  /*输出名称   mysql hdfs  trafodion*/
        output.setStoragType(params.getOutputType());
        output.setOutputType(params.getOutputType());  /*输出类型  0 结果集   1 平台存储*/
        output.setOutputId(params.getOutputId());     /*输出id   storeId   resultId*/
        output.setCreateUser(detailBean.getLoginUser().getLoginName());
        output.setInputDataBase(params.getRepoName());
        output.setOutputDataBase(params.getOutRepoName());
        output.setTaskId(params.getTaskId());
        
        if(params.getBtnHiveSql()==1){
            List<OutputField> outputFields = new ArrayList<OutputField>();
            boolean isFunction=false;
            OutputField newOutputField = null;
            String functionName="";
            String type = null;
            String field=params.getChooseFields(); 
            
            if(field!=null&&!"".equals(field)){
                field = StringEscapeUtils.unescapeHtml(field);
                JSONArray tbNameArray = JSONArray.parseArray(field);
                Object[] obje = tbNameArray.toArray();
                for (Object object : obje) {
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    String fieldname = jsonObject.getString("fieldname");
                    String aliasname = jsonObject.getString("aliasname");
                    if(fieldname!=null&&!"".equals(fieldname)){
                        if(fieldname.contains("(")&&fieldname.contains(")")){     //是否为函数
                            isFunction=true;
                            functionName=fieldname.substring(0, fieldname.lastIndexOf("("));
                            for(int j=0;j<detailBean.getFunctions().size();j++){
                                if(functionName.equals(detailBean.getFunctions().get(j).getFunctionClassName())){
                                    newOutputField = new OutputField();
                                    ComputeFunction f2=detailBean.getFunctions().get(j);
                                    newOutputField.setName(f2.getName()+fieldname.substring(fieldname.lastIndexOf("("),fieldname.lastIndexOf(")")+1));
                                    newOutputField.setFuctionId(f2.getId());
                                    newOutputField.setFunctionName(f2.getName());
                                    newOutputField.setFuctionAlias(aliasname);
                                    newOutputField.setFuctionDesc(f2.getDesc());
                                    newOutputField.setFuctionFields(fieldname.substring(fieldname.lastIndexOf("(")+1,fieldname.lastIndexOf(")")));
                                    newOutputField.setFunctionClassName(f2.getFunctionClassName());
                                    newOutputField.setType(f2.getReturnType());
                                    newOutputField.setFuction(true);
                                    newOutputField.setIndex(outputFields.size()+1);
                                    outputFields.add(newOutputField);
                                    output.getDimensionFields().add(newOutputField);//将 newOutputField加入     list
                                }
                            }
                        }else{
                            isFunction=false;                           
                            newOutputField = new OutputField();
                            for(Map.Entry<String,MetaStoreField> entry:mapTableToStoreField.entrySet()){
                                String key=entry.getKey();                 //获得键
                                if(fieldname.trim().equals(key)){ //若fieldname 与键相等   
                                    type=entry.getValue().getProType();      //根据键值  获得 字段类型
                                }
                            }
                            newOutputField.setName(fieldname);
                            newOutputField.setCompleteFieldName(fieldname);                        //将 name设为   fieldname             //设置别名
                            newOutputField.setFieldAlias(aliasname);
                            newOutputField.setIndex(outputFields.size()+1);                     //自定义index加1
                            newOutputField.setType(type);                           //类型设置
                            newOutputField.setFuction(isFunction); 
                            outputFields.add(newOutputField);//是否为函数设置
                            output.getDimensionFields().add(newOutputField);//将 newOutputField加入     list
                        }
                    }
                }
                output.setFields(outputFields);               //设置进入  output
            }
            
            //获得字段设置字符串数组
            LogicCond listLogicCond = null ;                
            List<LogicCond> list=new ArrayList<LogicCond>();
            
            String fieldType = null;    
            String fieldsValue=params.getFieldValue();
            if(!fieldsValue.equals("")&&fieldsValue!=null){
                fieldsValue = StringEscapeUtils.unescapeHtml(fieldsValue); 
                JSONArray jsonArrayField = JSONArray.parseArray(fieldsValue);
                Object[] objects = jsonArrayField.toArray();
                for (Object object : objects) {
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    listLogicCond = new LogicCond();
                    listLogicCond.setLeft(jsonObject.getString("left"));
                    listLogicCond.setAndor(jsonObject.getString("andor"));
                    listLogicCond.setName(jsonObject.getString("name"));
                    for(Map.Entry<String,MetaStoreField> entry:mapTableToStoreField.entrySet()){
                        String key=entry.getKey();                 //获得键
                        if(jsonObject.getString("name").trim().equals(key)){ //若fieldname 与键相等   
                            fieldType=entry.getValue().getProType();      //根据键值  获得 字段类型
                        }
                    }
                    listLogicCond.setType(fieldType);
                    listLogicCond.setSourceName(jsonObject.getString("name")+" "+fieldType);
                    listLogicCond.setOp(jsonObject.getString("op"));
                    listLogicCond.setValue(jsonObject.getString("value"));
                    listLogicCond.setRight(jsonObject.getString("right"));
                    list.add(listLogicCond);
                }
                output.getCondFields().addAll(list);
            }
            
            String functionId="";        //函数id
            
            if(output.getDimensionFields()!=null&&!output.getDimensionFields().isEmpty()){
                for(int t=0;t<output.getDimensionFields().size();t++){
                    OutputField outputField=output.getDimensionFields().get(t);
                    if(outputField.isFuction()){
                        //如果得到的outputField是函数
                        for(ComputeFunction fun:detailBean.getFunctions()){
                            if(fun.getName().equals(outputField.getFunctionName())){
                                if(fun!=null){
                                    functionId+=fun.getId();
                                    if(t==output.getDimensionFields().size()-1){
                                        functionId += "";
                                    }else{
                                        functionId += ",";
                                    }
                                }
                            }
                        }
                    }
                }
                output.setFunctionId(functionId);   
            }//设置函数id
            
            List<TableAssociation> tableAssociationList = new ArrayList<TableAssociation>();
            
            String assJosn = params.getAssJson();
            
            if(!("").equals(assJosn)&&assJosn!=null){
                assJosn= StringEscapeUtils.unescapeHtml(assJosn);
                JSONArray jsonArray = JSONArray.parseArray(assJosn);
                Object[] objs = jsonArray.toArray();
                for (Object object : objs) {
                    TableAssociation ta = new TableAssociation();
                    
                    List<AssociationCondition> asslist = new ArrayList<AssociationCondition>();
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    ta.setMasterTable(jsonObject.getString("mtb"));
                    String sql = jsonObject.getString("condition");
                    String sqls[] = sql.split(",");
                    for(String s:sqls){
                        AssociationCondition assocition = new AssociationCondition();
                        assocition.setAssLogic(jsonObject.getString("asslogic"));
                        assocition.setOperation(jsonObject.getString("operation"));
                        assocition.setCondition(s); 
                        asslist.add(assocition);
                    }
                    ta.setFromTableType(jsonObject.getString("dtb"));
                    ta.setLogic(jsonObject.getString("logic"));
                    ta.setAssociationConditionList(asslist);
                    tableAssociationList.add(ta);
                }
            }
            
            String storeIdString="";                  //storeIdString
            String tableNames=params.getTableNames();
            
            if(tableNames!=null&&!"".equals(tableNames)){
                String tableNameArray[]=tableNames.split(",");
                for(int i=0;i<tableNameArray.length;i++){     //{dhsf=cn.richinfo.hdp2.rcp.model.Source@5dbab9df, adb_20151222bb=cn.richinfo.hdp2.rcp.model.Source@e010f8c, ad_20151222aa=cn.richinfo.hdp2.rcp.model.Source@2c8aca1a, a=cn.richinfo.hdp2.rcp.model.Source@67a3b782, asdf=cn.richinfo.hdp2.rcp.model.Source@2d64ecf6, mysql_job_hive=cn.richinfo.hdp2.rcp.model.Source@3d0121c2, youhivesql=cn.richinfo.hdp2.rcp.model.Source@154f66d8, mysql_job_33=cn.richinfo.hdp2.rcp.model.Source@fe8f9d6, dgh=cn.richinfo.hdp2.rcp.model.Source@86a58c3, lxl_testww50=cn.richinfo.hdp2.rcp.model.Source@7de96e85, rereyyy=cn.richinfo.hdp2.rcp.model.Source@68571e57, dfg=cn.richinfo.hdp2.rcp.model.Source@2f891745, test_1217aa=cn.richinfo.hdp2.rcp.model.Source@5156cb64, lxl_testww50_g=cn.richinfo.hdp2.rcp.model.Source@19433bf4}
                    ComputeMetaStore s =storeMapFields.get(tableNameArray[i]);
                    if(s!=null){
                        if (i != 0) {
                            storeIdString += ",";
                        }
                        storeIdString+=s.getStoreId();
                    }
                }
                output.setStoreIdString(storeIdString);           //设置storeIdString
            }
            
            
            String dTable="";
            StringBuffer sb = new StringBuffer();
            
            if(!tableNames.contains(",")){
                hiveSql.setMasterTable(tableNames); 
                hiveSql.setDependentTable(dTable); 
            }else{
                String tableName[] =tableNames.split(",");      // [表名 字段名]
                hiveSql.setMasterTable(tableName[0]); 
                for(int i=1;i<tableName.length;i++){
                    sb.append(tableName[i]).append(",");
                }
                int a = sb.toString().lastIndexOf(",");
                hiveSql.setDependentTable(sb.toString().substring(0,a));
            }
            //依赖表
            hiveSql.setType(1);
            
            String sqlValue = StringEscapeUtils.unescapeHtml(params.getSqlValue());
            hiveSql.setValue(sqlValue);
            hiveSql.setAssociationList(tableAssociationList);
            List<String> repoIds = Arrays.asList(params.getRepoId().split(","));
            String dbName = null;
            for(int i=0;i<detailBean.getInputResources().size();i++){
            	if(repoIds.contains(detailBean.getInputResources().get(i).getId())){
            		dbName = (StringUtils.isEmpty(dbName)?dbName:dbName+",") + detailBean.getInputResources().get(i).getRepoName();
            	}
            }
            hiveSql.setDataBaseName(dbName);
            output.setHiveSql(hiveSql);
        }else if(params.getBtnHiveSql()==2){                        //自定义sql
            hiveSql.setType(2);                                     //类型为2
            String sqlSelf = StringEscapeUtils.unescapeHtml(params.getSelfSql());
            hiveSql.setValue(TestUtil.repaceWhiteSapce(sqlSelf));
            hiveSql.setAssociationList(null);
            List<String> repoIds = Arrays.asList(params.getRepoId().split(","));
            String dbName = null;
            for(int i=0;i<detailBean.getInputResources().size();i++){
            	if(repoIds.contains(detailBean.getInputResources().get(i).getId())){
            		dbName = (StringUtils.isEmpty(dbName)?dbName:dbName+",") + detailBean.getInputResources().get(i).getRepoName();
            	}
            }
            hiveSql.setDataBaseName(dbName);
            output.setHiveSql(hiveSql);    //set到output中
            
            Map<String, Integer> otherTab = new HashMap<String,Integer>();
            String tabName = null;
            int tbId = 0;
            String os = StringEscapeUtils.unescapeHtml(params.getOtable());
            if(os!=null&&!os.equals("")&&!os.equals("null")&&!os.equals("{}")){
                os = os.substring(os.indexOf("{")+1, os.lastIndexOf("}"));
                String[]  ots = os.split(",");
                for(String otss :  ots){
                    String[] ot= otss.split(":");
                    tabName = ot[0];
                    tbId = Integer.parseInt(ot[1].trim());
                    otherTab.put(tabName, tbId);
                }
                output.setOtherTable(otherTab);
            }
        }else if(params.getBtnHiveSql()==3){                        //hive - e  sql
            hiveSql.setType(3);            //类型为3
            String eSql = StringEscapeUtils.unescapeHtml(params.getHiveEsql());
            hiveSql.setValue(TestUtil.repaceWhiteSapce(eSql));
            List<String> repoIds = Arrays.asList(params.getRepoId().split(","));
            String dbName = "";
            List<Repositories> repositories = detailBean.getInputResources();
            for(int i=0;i<detailBean.getInputResources().size();i++){
            	if(repoIds.contains(String.valueOf(repositories.get(i).getId()))){
            		dbName = (StringUtils.isEmpty(dbName)?dbName:dbName+",") + repositories.get(i).getRepoName();
            	}
            }
            hiveSql.setDataBaseName(dbName);
            output.setHiveSql(hiveSql);    //set到output中
            Map<String, Integer> otherTab = new HashMap<String,Integer>();
            
            String tabName = null;
            int tbId = 0;
            String os = StringEscapeUtils.unescapeHtml(params.getOtable());
            if(os!=null&&!os.equals("")&&!os.equals("null")&&!os.equals("{}")){
                os = os.substring(os.indexOf("{")+1, os.lastIndexOf("}"));
                String[]  ots = os.split(",");
                for(String otss :  ots){
                    String[] ot= otss.split(":");
                    tabName = ot[0];
                    tbId = Integer.parseInt(ot[1].trim());
                    otherTab.put(tabName, tbId);
                }
                output.setOtherTable(otherTab);
            }
            
            List<InParam> inlist = new ArrayList<InParam>();
            String tmpList = StringEscapeUtils.unescapeHtml(params.getTmplist());
            if(tmpList!=null&&!tmpList.equals("")&&!tmpList.equals("null")&&!tmpList.equals("\"\"")){
                JSONArray jsonArrayTmp= JSONArray.parseArray(tmpList);
                Object[] objts = jsonArrayTmp.toArray();
                for (Object object : objts) {
                    JSONObject jsonObject = JSONObject.parseObject(object.toString());
                    InParam fs =new InParam();
                    fs.setAdmin(Boolean.parseBoolean(jsonObject.getString("admin")));
                    fs.setDbname(jsonObject.getString("dbname"));
                    fs.setTbname(jsonObject.getString("tbname"));
                    fs.setUserid(Integer.parseInt(jsonObject.getString("userid")));
                    inlist.add(fs);
                }
                output.setFlist(inlist);
            }
        }
        
        output.getXmlString2();
        JSONObject data = new JSONObject();
        if (params.getTaskId() == 0) {
            try {
                output.setTaskStatus(0);
                commonService.createTask(output);
                data.put("success", false);
                data.put("msg", "计算任务保存成功");
            } catch (DuplicateKeyException ed) {
                data.put("success", false);
                data.put("msg", "有重复数据插入");
            } catch (Exception e) {
                e.printStackTrace();
                data.put("success", false);
                data.put("msg", "计算任务保存失败");
            }
        } else {
            try {
                commonService.updateTask(output);
                data.put("success", false);
                data.put("msg", "计算任务修改成功");
            } catch (DuplicateKeyException ed) {
                data.put("success", false);
                data.put("msg", "有重复数据插入");
            } catch (Exception e) {
                e.printStackTrace();
                data.put("success", false);
                data.put("msg", "计算任务修改失败");
            } 
        }
        return data;
    }
    
    
    
    
    
    /**
     * 添加脚本任务
     * @param taskDetail
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/checkSql",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject checkSql(Output params,ComputeTaskDetailBean detailBean) throws Exception{
        Output ou = new Output(); 
        List<String> storefile =new ArrayList<String>();
        List<String> createTable = new ArrayList<String>();      //创建表
        List<String> otherTable = new ArrayList<String>();       //操作表
        Map<String,Integer> tbs = new HashMap<String,Integer>();
        List<InParam> flist = new ArrayList<InParam>() ;
        JSONObject  obj = new JSONObject();
        //TODO sql验证
        if(params.getBtnHiveSql()==2){
            
            String selfSql = StringEscapeUtils.unescapeHtml(params.getSelfSql().toLowerCase());
            if(!selfSql.trim().startsWith("select")){  
                obj.put("success", "false");
                obj.put("msg", "仅支持select操作！");
                return obj;
            }
            
            if(selfSql.trim().contains(";")){  
                obj.put("success", "false");
                obj.put("msg", "sql语句不能包含分号！");
                return obj;
            }
            
            List<String> tableNames =  TestUtil.getAllTableNames(selfSql);           //获得sql语句中所有的表名
            if(tableNames==null || tableNames.size()==0){
            	obj.put("success", "false");
                obj.put("msg", "sql语句不正确,无表名！");
                return obj;
            }
            InParam inparam = new InParam();
            String repoId = params.getRepoId();
            String repoName = params.getRepoName();
            String[] repoNames = repoName.split(",");
            String[] repoIds;
            if(repoId!=null && repoId.contains(",")){
            	repoIds = repoId.split(",");
            }else{
            	repoIds = new String[]{repoId};
            }
            List<ComputeMetaStore> allStores = new ArrayList<ComputeMetaStore>();
            Map<String,List<String>> map = new HashMap<String,List<String>>();
            for(int i=0;i<repoIds.length;i++){
            	String rn = repoNames[i];
            	inparam.setRepoId(repoIds[i]);
                inparam.setArray(new int[] { Contants.STORE_TYPE_HIVE });
                inparam.setUserid(Integer.parseInt(detailBean.getLoginUser().getId()));
                
                if(detailBean.getIsAdmin()==0){
                    inparam.setAdmin(false); 
                }else{
                    inparam.setAdmin(true);
                }
                //查询出该连接对应库的所有表
                List<ComputeMetaStore> stores = computeMetaService.getMetaStoresByTypes(inparam);
                if(stores!=null && stores.size()>0){
                	allStores.addAll(stores);
                }
                if(stores.size()>0){
                    for (ComputeMetaStore ms : stores) {
                    	if(!map.containsKey(rn)){
                    		List<String> a = new ArrayList<String>();
                    		a.add(ms.getStoreFile());
                    		map.put(rn, a);
                    	}else{
                    		List<String> ll = map.get(rn);
                    		if(ll!=null){
                    			ll.add(ms.getStoreFile());
                    			map.put(rn, ll);
                    		}else{
                    			ll = new ArrayList<String>();
                    			ll.add(ms.getStoreFile());
                    			map.put(rn, ll);
                    		}
                    	}
                    	if(repoIds.length>1){
                    		if(!storefile.contains(rn + "." + ms.getStoreFile())){
                        		storefile.add(rn + "." + ms.getStoreFile());
                        	}
                    	}else{
                    		if(!storefile.contains(ms.getStoreFile())){
                        		storefile.add(ms.getStoreFile());
                        	}
                    	}
                    }
                }
                //获取库下的所有的临时表
                inparam.setDbname(rn);
                List<TmpTable> tb = computeMetaService.getAllTableName(inparam);
                if(tb.size()>0){
                    for (TmpTable ms : tb) {
                    	if(repoIds.length>1){
                    		if(!storefile.contains(ms.getDbname() + "." + ms.getTbname())){
                        		storefile.add(ms.getDbname() + "." + ms.getTbname());
                        	}
                    	}else{
                    		if(!storefile.contains(ms.getTbname())){
                        		storefile.add(ms.getTbname());
                        	}
                    	}
                    }
                }
            }
            //循环出所有的表 类似于   t_student.select   根据点进行切割  获得表名
            for(String tablename:tableNames){
            	if(storefile.size()==0){  
                    obj.put("success", "false");
                    obj.put("msg", "没有该库"+params.getRepoName()+"下的表权限");
                    return obj;
                }
            	String table = tablename.substring(0, tablename.lastIndexOf("."));
            	if(repoIds.length==1){
            		//如果只是单个库,sql中表可以带库名和不带库名
            		if(table.contains(".")){
            			String dbName = table.split("\\.")[0];
            			String tbName = table.split("\\.")[1];
            			List<String> list = map.get(dbName);
            			if(list!=null && list.size()>0 && !list.contains(tbName)){
            				obj.put("success", "false");
                            obj.put("msg", "数据库"+dbName+"不存在"+tbName+"表");
                            return obj;
            			}else if(!storefile.contains(tbName)){
            				obj.put("success", "false");
                            obj.put("msg", "无表："+table+"的操作权限");
                            return obj;
            			}else{
            				for (ComputeMetaStore ms : allStores) {
                                if((ms.getStoreFile()).equals(tbName)){
                                    tbs.put(tablename, ms.getStoreId());
                                    break;
                                }
                            }
                            ou.setOtherTable(tbs);
            			}
            		}else{
            			if(!storefile.contains(table)){
            				obj.put("success", "false");
                            obj.put("msg", "无表："+table+"的操作权限");
                            return obj;
            			}else{
            				for (ComputeMetaStore ms : allStores) {
                                if((ms.getStoreFile()).equals(table)){
                                    tbs.put(tablename, ms.getStoreId());
                                    break;
                                }
                            }
                            ou.setOtherTable(tbs);
            			}
            		}
            	}else{
            		if(!table.contains(".")){
            			obj.put("success", "false");
                        obj.put("msg", "跨库查询必须带库名"+table);
                        return obj;
            		}else{
            			String dbName = table.split("\\.")[0];
            			String tbName = table.split("\\.")[1];
            			List<String> list = map.get(dbName);
            			if(list!=null && list.size()>0 && !list.contains(tbName)){
            				obj.put("success", "false");
                            obj.put("msg", "数据库"+dbName+"不存在"+tbName+"表");
                            return obj;
            			}else if(!storefile.contains(table)){
            				obj.put("success", "false");
                            obj.put("msg", "无表："+tbName+"的操作权限");
                            return obj;
            			}else{
            				for (ComputeMetaStore ms : allStores) {
                                if((ms.getStoreFile()).equals(tbName)){
                                    tbs.put(tablename, ms.getStoreId());
                                    break;
                                }
                            }
                            ou.setOtherTable(tbs);
            			}
            		}
            	}
            }
        }else if(params.getBtnHiveSql()==3){
            String hiveESql = StringEscapeUtils.unescapeHtml(params.getHiveEsql());
            List<ComputeMetaStore> allStores = new ArrayList<ComputeMetaStore>();
            List<String> tableNames = TestUtil.getAllTableNames(hiveESql);
            if(tableNames.size()==0){
                obj.put("success", "false");
                obj.put("msg", "校验失败，无表名");
                return obj;
            }
            String repoId = params.getRepoId();
            String repoName = params.getRepoName();
            String[] repoNames = repoName.split(",");
            String[] repoIds = {};
            if(repoId!=null && repoId.contains(",")){
            	repoIds = repoId.split(",");
            }else if(repoId!=null){
            	repoIds = new String[]{repoId};
            }
            Map<String,String> mp = new HashMap<String,String>();
        	for(int i=0;i<repoIds.length;i++){
        		mp.put(repoIds[i], repoNames[i]);
        		InParam inparam = new InParam();
                inparam.setDbname(repoNames[i]);
                inparam.setRepoId(repoIds[i]);
                inparam.setArray(new int[] { Contants.STORE_TYPE_HIVE });
                inparam.setUserid(Integer.parseInt(detailBean.getLoginUser().getId()));
                if(detailBean.getIsAdmin()==0){
                    inparam.setAdmin(false); 
                }else{
                    inparam.setAdmin(true);
                }
                //获得所选库下所有的表
                List<ComputeMetaStore> stores = computeMetaService.getMetaStoresByTypes(inparam);
                allStores.addAll(stores);
                if(stores.size()>0){
                    for (ComputeMetaStore ms : stores) {
                    	//将该库下的表名存入storefile
                    	if(repoIds.length>1){
                    		if(!storefile.contains(repoNames[i] + "." + ms.getStoreFile())){
                        		storefile.add(repoNames[i] + "." + ms.getStoreFile());
                        	}
                    	}else{
                    		if(!storefile.contains(ms.getStoreFile())){
                        		storefile.add(ms.getStoreFile());
                        	}
                    	}
                    }
                }
                List<TmpTable> tb = computeMetaService.getAllTableName(inparam);
                if(tb.size()>0){
                    for (TmpTable ms : tb) {
                    	//将该库下的临时表存入storefile
                    	if(repoIds.length>1){
                    		if(!storefile.contains(ms.getDbname() + "." + ms.getTbname())){
                        		storefile.add(ms.getDbname() + "." + ms.getTbname());
                        	}
                    	}else{
                    		if(!storefile.contains(ms.getTbname())){
                        		storefile.add(ms.getTbname());
                        	}
                    	}
                    }
                }
        	}
        	for(String tablename:tableNames){
                if(tablename.contains("create")){
                	String tbName = tablename.substring(0, tablename.lastIndexOf("."));
                	if(repoIds.length>1){
                		if(!tbName.contains(".")){
                			obj.put("success", "false");
                            obj.put("msg", "跨库查询表"+tbName+"必须带库名");
                            return obj;
                		}
                		createTable.add(tbName);
                	}else{
                		//不涉及跨库时,createTable只存放表名
                		if(tbName.contains(".")){
                			tbName = tbName.split("\\.")[1];
                		}
                		createTable.add(tbName);
                	}
                }else{
                	//其他表操作存放入otherTable中
                    otherTable.add(tablename);
                }
            }
            boolean isAdmin = true;
            if(detailBean.getIsAdmin()==0){
            	isAdmin = false;
            }
            int userId = Integer.parseInt(detailBean.getLoginUser().getId());
            //新增任务
            if(params.getTaskId()==0){                     
                //如果有表创建语句    先进行判断所有的表名是否满足权限后   在进行插入
                if(createTable.size()>0){
                	for(String ctable : createTable){
                		if(storefile.contains(ctable)){
                			obj.put("success", "false");
                			if(repoIds.length>1){
                				obj.put("msg", "该表"+ctable.split("\\.")[0]+"已经存在"+ctable.split("\\.")[1]+"中,请重命名");
                			}else{
                				obj.put("msg", "该表"+ctable+"已经存在"+repoName+"中,请重命名");
                			}
                            return obj;
                		}
                	}
                    //循环插入表格
                    for(String s :createTable){
                        InParam fs =new InParam();
                        if(s.contains(".")){
                        	fs.setDbname(s.split("\\.")[0]);
                        	fs.setTbname(s.split("\\.")[1]);
                        }else{
                        	fs.setDbname(repoName);
                        	fs.setTbname(s);
                        }
                        fs.setAdmin(isAdmin);
                        fs.setUserid(userId);
                        flist.add(fs);
                    }
                }
            }else{   //编辑任务
                if(createTable.size()>0){
                    //循环插入表格
                    for(String s :createTable){
                        //查询taskId  判断时候编辑为同一任务
                    	String dbName = null;
                    	String tnName = null;
                    	if(s.contains(".")){
                    		dbName = s.split("\\.")[0];
                    		tnName = s.split("\\.")[1];
                        }else{
                        	dbName = repoName;
                        	tnName = s;
                        }
                        Integer taskid = computeMetaService.getTaskId(dbName,tnName,userId,isAdmin);
                        InParam fs =new InParam();
                        fs.setDbname(dbName);
                        fs.setTbname(tnName);
                        fs.setAdmin(isAdmin);
                        fs.setUserid(userId);
                        //若taskId不为空  并且taskId相等    则为编辑同一任务
                        if(taskid !=null && params.getTaskId()==taskid){
                            if(!storefile.contains(dbName + "." + tnName)){
                                flist.add(fs);
                            }
                        }else{
                            //编辑不同任务
                            if(storefile.contains(dbName + "." + tnName)){
                                obj.put("success", "false");
                                obj.put("msg", "该表"+tnName+"已经存在"+dbName+"中,请重命名");
                                return obj;
                            }else{
                                flist.add(fs);
                            }
                        }
                    }
                }
            }
            ou.setFlist(flist);
            //如果有其他的表操作sql语句   类似于   insert delete  update select alter 
            if(otherTable.size()>0){
                for(String ot : otherTable){
            		String otable = ot.substring(0, ot.lastIndexOf("."));
            		//如果是跨库,则需要将表名前的库名去掉
            		if(repoIds.length==1 && otable.contains(".")){
            			otable = otable.split("\\.")[1];
            		}
            		if(createTable.size()>0){
                        if(!storefile.contains(otable)&&!createTable.contains(otable)){
                            obj.put("success", "false");
                            obj.put("msg", "无表："+otable+"的操作权限");
                            return obj;
                        }
                    }else{
                        if(!storefile.contains(otable)){
                            obj.put("success", "false");
                            obj.put("msg", "无表："+otable+"的操作权限");
                            return obj;
                        }
                    }
                	for (ComputeMetaStore ms : allStores) {
                		if(repoIds.length>1){
                			String s = mp.get(String.valueOf(ms.getRepoId()));
                			if(s!=null && (s + "." + ms.getStoreFile()).equals(otable)){
                                tbs.put(ot, ms.getStoreId());
                                break;
                            }
                		}else{
                			if(ms.getStoreFile().equals(otable)){
                                tbs.put(ot, ms.getStoreId());
                                break;
                            }
                		}
                    }
                }
                ou.setOtherTable(tbs);
            }
        }
        obj.put("success", "true");
        obj.put("msg", "校验成功");
        obj.put("data", ou);
        return obj;
    }
    
    
    @RequestMapping(value = "/getFunc",method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getFunc(ComputeTaskDetailBean detailBean,@RequestParam(value="funcParamField",required=true)String funcParamField) throws Exception{
        JSONObject json = new JSONObject();
        
        detailBean = (ComputeTaskDetailBean) getSession().getAttribute("detailBean");
        
        List<String> list = new ArrayList<String>();
        List<ComputeFunction> functions = detailBean.getFunctions();
        Map<String,MetaStoreField> sf = (Map<String, MetaStoreField>) getSession().getAttribute("mapTableToStoreField");
        
        
        String[] fp = funcParamField.split(",");
        MetaStoreField s=sf.get(fp[0]);
        String[] params = null;
        String[] functionParams=null;
        if (functions != null && functions.size() > 0) {
            for (int i = 0; i < functions.size(); i++) {
                ComputeFunction fuction=functions.get(i);
                List<ComputeFunctionParam> listFunctionParam= computeTaskService.getFunctionParams(Integer.parseInt(fuction.getId()));
                if(listFunctionParam!=null&&fp.length==listFunctionParam.size()){
                    params=new String[fp.length];
                    functionParams=new String[fp.length];
                    for(int j=0;j<fp.length;j++){
                        MetaStoreField f=sf.get(fp[j]);
                        params[j]=f.getProType();
                    }
                    for(int t=0;t<listFunctionParam.size();t++){
                        ComputeFunctionParam cfp=(ComputeFunctionParam) listFunctionParam.get(t);
                        functionParams[t]=cfp.getParamType();
                    }
                }
                
                if(fp.length<=1&&(listFunctionParam!=null&&listFunctionParam.size()==1)){//fuction.getParaType()!=null&&s.getType().equalsIgnoreCase(fuction.getParaType())
                    //if(s.getType().equalsIgnoreCase(fuction.getParaType())){
                    //makeMenuItem(joinMenu, tree, i);
                    //}else
                    ComputeFunctionParam comfun=listFunctionParam.get(0);
                    if("int".equalsIgnoreCase(s.getProType())&&
                            (comfun.getParamType().toLowerCase().contains("int")||"number".contains(comfun.getParamType().toLowerCase())||"double".contains(comfun.getParamType().toLowerCase()))
                            ){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("REAL".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("REAL")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"float".contains(comfun.getParamType().toLowerCase())
                            ||"int".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("INTEGER".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("INTEGER")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"double".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("NUMERIC".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("NUMERIC")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"int".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("TINYINT".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("TINYINT")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"double".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("SMALLINT".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("SMALLINT")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"double".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("MEDIUMINT".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("MEDIUMINT")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"double".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("BITINT".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("BITINT")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"double".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("LARGEINT".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("LARGEINT")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"double".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("BIT".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("BIT")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"double".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("double".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("double")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"int".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("float".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("float")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"int".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("decimal".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("decimal")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            ||"int".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("long".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("long")
                            ||"number".contains(comfun.getParamType().toLowerCase())
                            )){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("string".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("string")
                            ||"varchar".contains(comfun.getParamType().toLowerCase()))){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("varchar".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("varchar")
                            ||"string".contains(comfun.getParamType().toLowerCase()))){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("char".equalsIgnoreCase(s.getProType())&&(comfun.getParamType().toLowerCase().contains("char")
                            ||"string".contains(comfun.getParamType().toLowerCase()))){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }else if("ALL".equalsIgnoreCase(comfun.getParamType())){
                        list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                    }
                }else if(fp.length>1&&listFunctionParam.size()==fp.length){//&&ConstantUtil.compareStrings(params, fuction.getParaType().split(","))
                    list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                }else if(listFunctionParam.size()==fp.length&&"ALL".equalsIgnoreCase(fuction.getParaType())){
                    list.addAll(makeMenuItem(i,listFunctionParam,detailBean));
                }
            }
        }
        json.put("success", true);
        json.put("data", list);
        return json;
    }
    
    
    private List<String> makeMenuItem( int i,final List<ComputeFunctionParam> listFunctionParam,ComputeTaskDetailBean detailBean) {
        List<String> cf = new ArrayList<String>();
        String functionParamType="";
        if (!listFunctionParam.isEmpty()) {
            for(int j=0;j<listFunctionParam.size();j++){
                ComputeFunctionParam fpp=listFunctionParam.get(j);
                if (j != 0) {
                    functionParamType += ", ";
                }
                functionParamType += fpp.getParamType();
            }
        }
        List<ComputeFunction> funcs = detailBean.getFunctions();
        
        if(funcs.get(i).getName().endsWith("hive_distinctcount")){
            cf.add(funcs.get(i).getFunctionClassName()+"( distinct "+functionParamType+")");
        }else{
            cf.add(funcs.get(i).getFunctionClassName()+"("+functionParamType+")");
        }
        
        return cf;
    }
    
    
    public static HttpSession getSession() { 
        HttpSession session = null; 
        try { 
            session = getRequest().getSession(); 
        } catch (Exception e) {} 
        return session; 
    } 
    
    public static HttpServletRequest getRequest() { 
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes(); 
        return attrs.getRequest(); 
    } 
    
}
