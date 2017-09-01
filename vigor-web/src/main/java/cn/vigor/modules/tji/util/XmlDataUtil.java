package cn.vigor.modules.tji.util;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import cn.vigor.common.contants.Contants;
import cn.vigor.common.utils.SpringContextHolder;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.jars.entity.Expension;
import cn.vigor.modules.jars.entity.Function;
import cn.vigor.modules.meta.dao.MetaStoreDao;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.tji.bean.InputDsfRelations;
import cn.vigor.modules.tji.entity.FunctionParamBean;
import cn.vigor.modules.tji.entity.TaskDetailBean;

/**
 * 生成xmldata字段数据工具类
 * @author 38342
 *
 */
public class XmlDataUtil
{
    private static final String CDATA_START = "<![CDATA[";
    
    private static final String CDATA_END = "]]>";

    /**
     * 获取自定义计算任务中xmldata数据
     * @param taskDetailBean
     * @return
     */
    public static String getCustomComXmlData(TaskDetailBean taskDetailBean){
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("\n<task>");
        sb.append("\n\t<taskId>").append(taskDetailBean.getTaskId()).append("</taskId>");
        sb.append("\n\t<taskType>").append(taskDetailBean.getTaskType()).append("</taskType>");
        sb.append("\n\t<taskName>").append(CDATA_START).append(taskDetailBean.getTaskName()).append(CDATA_END).append("</taskName>");
        sb.append("\n\t<taskDesc>").append(CDATA_START).append(taskDetailBean.getTaskDesc()).append(CDATA_END).append("</taskDesc>");
        sb.append("\n\t<inputDataBase>").append(CDATA_START).append("").append(CDATA_END).append("</inputDataBase>");//TODO
        sb.append("\n\t<outputDataBase>").append(CDATA_START).append("").append(CDATA_END).append("</outputDataBase>");//TODO
        sb.append("\n\t<repoId>").append(taskDetailBean.getInputResourceId()==null?0:taskDetailBean.getInputResourceId()).append("</repoId>");
        sb.append("\n\t<outRepoId>").append(taskDetailBean.getOutputResourceId()==null?0:taskDetailBean.getOutputResourceId()).append("</outRepoId>");
        sb.append("\n\t<storagType>").append(taskDetailBean.getResultType()==null?0:taskDetailBean.getResultType()).append("</storagType>");
        String outputType = taskDetailBean.getOutputType();
        if(outputType!=null && outputType.equals("2")){
            outputType = "FTP";
        }else if(outputType!=null && outputType.equals("3")){
            outputType = "HDFS";
        }else if(outputType!=null && outputType.equals("4")){
            outputType = "HBASE";
        }else if(outputType!=null && outputType.equals("11")){
            outputType = "X-OLTP";
        }else{
            outputType = "0";
        }
        sb.append("\n\t<outputString>").append(outputType).append("</outputString>");
        sb.append("\n\t<groupId>").append(taskDetailBean.getGroupId()==null?0:taskDetailBean.getGroupId()).append("</groupId>");
//        List<Map<String, Object>> list = taskDetailBean.getOutputMetaList();
//        if(list!=null && list.size()>0){
//            sb.append("\n\t<outputId>").append(list.get(0).get("id")).append("</outputId>");
//        }else{
//            sb.append("\n\t<outputId>").append(0).append("</outputId>");
//        }
        sb.append("\n\t<outputId>").append(StringUtils.isEmpty(taskDetailBean.getOutputDataSourceId())?0:taskDetailBean.getOutputDataSourceId()).append("</outputId>");
        sb.append("\n\t<createUserid>").append(CDATA_START).append(taskDetailBean.getCurrentUser().getId()).append(CDATA_END).append("</createUserid>");
        sb.append("\n\t<ownerUserid>").append(CDATA_START).append(taskDetailBean.getCurrentUser().getId()).append(CDATA_END).append("</ownerUserid>");
        
        //
        sb.append("\n\t<version>").append(CDATA_START).append("").append(CDATA_END).append("</version>");
        sb.append("\n\t<storeId>").append(taskDetailBean.getInputDataSourceId()==null?0:taskDetailBean.getInputDataSourceId()).append("</storeId>");
        sb.append("\n\t<functionId>").append(taskDetailBean.getFunctionId()).append("</functionId>");
        sb.append("\n\t<outputType>").append(taskDetailBean.getResultType()==null?0:taskDetailBean.getResultType()).append("</outputType>");
        sb.append("\n\t<inputType>").append(1).append("</inputType>");//TODO
        sb.append("\n\t<partitionField>").append("").append("</partitionField>");//TODO
        
        sb.append("\n\t<hiveTime>").append("").append("</hiveTime>");
        sb.append("\n\t<hiveTimeUnit>").append("").append("</hiveTimeUnit>");
        sb.append("\n\t<hivefileSize>").append(CDATA_START).append("").append(CDATA_END).append("</hivefileSize>");
       /* if (StringUtils.isNotBlank(taskDetailBean.ge)) {
            sb.append("\n\t<delimiter>").append(CDATA_START).append(delimiter).append(CDATA_END).append("</delimiter>");
        }*/

        
        sb.append("\n\t\t<name>").append(CDATA_START).append("").append(CDATA_END).append("</name>");
        sb.append("\n\t\t<file>").append(CDATA_START).append("").append(CDATA_END).append("</file>");
        sb.append("\n\t\t<username>").append(CDATA_START).append("").append(CDATA_END).append("</username>");
        sb.append("\n\t\t<password>").append(CDATA_START).append("").append(CDATA_END).append("</password>");
        sb.append("\n\t\t<dir>").append(CDATA_START).append("").append(CDATA_END).append("</dir>");
        sb.append("\n\t\t<ip>").append(CDATA_START).append("").append(CDATA_END).append("</ip>");
        sb.append("\n\t\t<port>").append(0).append("</port>");
        sb.append("\n\t\t<resultType>").append(0).append("</resultType>");
        sb.append("\n\t\t<resultId>").append(CDATA_START).append(0).append(CDATA_END).append("</resultId>");
        
        sb.append("\n\t<result>");
            sb.append("\n\t\t<name>").append(CDATA_START).append("").append(CDATA_END).append("</name>");
            sb.append("\n\t\t<file>").append(CDATA_START).append("").append(CDATA_END).append("</file>");
            sb.append("\n\t\t<username>").append(CDATA_START).append("").append(CDATA_END).append("</username>");
            sb.append("\n\t\t<password>").append(CDATA_START).append("").append(CDATA_END).append("</password>");
            sb.append("\n\t\t<dir>").append(CDATA_START).append("").append(CDATA_END).append("</dir>");
            sb.append("\n\t\t<ip>").append(CDATA_START).append("").append(CDATA_END).append("</ip>");
            sb.append("\n\t\t<port>").append(0).append("</port>");
            sb.append("\n\t\t<resultType>").append(0).append("</resultType>");
//            if (StringUtils.isNotBlank(delimiter)) {
//                sb.append("\n\t<delimiter>").append(CDATA_START).append(delimiter).append(CDATA_END).append("</delimiter>");
//            }
        sb.append("\n\t</result>");
        
//        if (calField != null) {
//            sb.append("\n\t<calField>");
//            sb.append("\n\t\t<name>").append(CDATA_START).append(calField.getName()).append(CDATA_END).append("</name>");
//            sb.append("\n\t\t<aliasName>").append(CDATA_START).append(calField.getAliasName()).append(CDATA_END).append("</aliasName>");
//            sb.append("\n\t\t<type>").append(calField.getType()).append("</type>");
//            if (taskType == Task.R_TASK_TYPE) {
//                sb.append("\n\t\t<index>").append(calField.getIndex()).append("</index>");
//            } else {
//                sb.append("\n\t\t<index>").append(calField.getIndex()).append("</index>");
//            }
//            sb.append("\n\t</calField>");
//        }
        if(taskDetailBean.getTaskType()==Contants.TASK_TYPE_MR ||
                taskDetailBean.getTaskType()==Contants.TASK_TYPE_SPARKZDY){
            Function function = taskDetailBean.getFunction();
            sb.append("\n\t<mr>");
            String functionType = "";
            if(taskDetailBean.getFunctionType().equals("1")){
                functionType = "SPARK";
            }else if(taskDetailBean.getFunctionType().equals("4")){
                functionType = "MR";
            }else{
                functionType = taskDetailBean.getFunctionType();
            }
            sb.append("\n\t\t<functionType>").append(CDATA_START).append(functionType).append(CDATA_END).append("</functionType>");
            sb.append("\n\t\t<functionId>").append(CDATA_START).append(function==null?null:function.getId()).append(CDATA_END).append("</functionId>");
            sb.append("\n\t\t<functionJarName>").append(CDATA_START).append((function==null || function.getJarId()==null)?null:function.getJarId().getJarName()).append(CDATA_END).append("</functionJarName>");
            sb.append("\n\t\t<functionJarPath>").append(CDATA_START).append(function==null || function.getJarId()==null?null:function.getJarId().getJarPath()).append(CDATA_END).append("</functionJarPath>");
            sb.append("\n\t\t<functionName>").append(CDATA_START).append(function==null || function.getJarId()==null?null:function.getFunctionName()).append(CDATA_END).append("</functionName>");
            sb.append("\n\t\t<functionClass>").append(CDATA_START).append(function==null || function.getJarId()==null?null:function.getFunctionClass()).append(CDATA_END).append("</functionClass>");
            
            sb.append("\n\t\t<ip>").append(CDATA_START).append("").append(CDATA_END).append("</ip>");
            sb.append("\n\t\t<port>").append(CDATA_START).append(0).append(CDATA_END).append("</port>");
            sb.append("\n\t\t<userName>").append(CDATA_START).append("").append(CDATA_END).append("</userName>");
            sb.append("\n\t\t<passWord>").append(CDATA_START).append("").append(CDATA_END).append("</passWord>");
            sb.append("\n\t\t<dbName>").append(CDATA_START).append("").append(CDATA_END).append("</dbName>");
            sb.append("\n\t\t<dbType>").append(CDATA_START).append(0).append(CDATA_END).append("</dbType>");
            
            sb.append("\n\t\t<ruleExpression>").append(CDATA_START).append("").append(CDATA_END).append("</ruleExpression>");
            sb.append("\n\t\t<etlModeName>").append(CDATA_START).append("").append(CDATA_END).append("</etlModeName>");
            sb.append("\n\t\t<shellPath>").append(CDATA_START).append("").append(CDATA_END).append("</shellPath>");
            
            sb.append("\n\t\t<arguments>");
            if(taskDetailBean.getFunctionParams()!=null && !taskDetailBean.getFunctionParams().isEmpty()){
                for (FunctionParamBean arg : taskDetailBean.getFunctionParams()) {
                    sb.append("\n\t\t\t<arg>");
                    sb.append("\n\t\t\t\t<index>").append(arg.getParamIndex()).append("</index>");
                    sb.append("\n\t\t\t\t<name>").append(CDATA_START).append(arg.getParamName()).append(CDATA_END).append("</name>");
                    sb.append("\n\t\t\t\t<value>").append(CDATA_START).append(arg.getValue()).append(CDATA_END).append("</value>");
                    sb.append("\n\t\t\t\t<paramType>").append(CDATA_START).append(arg.getParamType()).append(CDATA_END).append("</paramType>");
                    //paramSelect参数的作用只要是作为计算子系统的存储过程任务的输入输出参数类型,由于计算子系统的类型与管理平台这边的类型反了,故存储时将值进行挑换顺序
                    //paramSelect的值只能是(1/2)
                    int paramSelect = 0;
                    if(arg.getParamSelect()!=null){
                        if(arg.getParamSelect()==1){
                            paramSelect=2;
                        }else{
                            paramSelect=1;
                        }
                    }
                    sb.append("\n\t\t\t\t<paramSelect>").append(CDATA_START).append(paramSelect).append(CDATA_END).append("</paramSelect>");
                    sb.append("\n\t\t\t</arg>");
                }
            }
            sb.append("\n\t\t</arguments>");
            sb.append("\n\t</mr>");
        }else if(taskDetailBean.getTaskType()==Contants.TASK_TYPE_SJZL){//数据质量
            sb.append("\n\t<dqs>");
            List<InputDsfRelations> inputDsfRelations = taskDetailBean.getInputDsfRelations();
            if(!inputDsfRelations.isEmpty()){
                for (InputDsfRelations inputDsfRelation : inputDsfRelations)
                {
                    sb.append("\n\t\t<dqm>");
                    sb.append("\n\t\t\t<fieldName>").append(CDATA_START).append(inputDsfRelation.getDataSourceProName()).append(CDATA_END).append("</fieldName>");
                    sb.append("\n\t\t\t<fieldIndex>").append(CDATA_START).append(inputDsfRelation.getDataSourceProIndex()).append(CDATA_END).append("</fieldIndex>");
                    sb.append("\n\t\t\t<fieldFormat>").append(CDATA_START).append(inputDsfRelation.getDataSourceProFormat()).append(CDATA_END).append("</fieldFormat>");
                    sb.append("\n\t\t\t<fieldType>").append(CDATA_START).append(inputDsfRelation.getDataSourceProType()).append(CDATA_END).append("</fieldType>");
                    
                    sb.append("\n\t\t\t<functionId>").append(CDATA_START).append(inputDsfRelation.getFunctionId()).append(CDATA_END).append("</functionId>");
                    sb.append("\n\t\t\t<functionName>").append(CDATA_START).append(inputDsfRelation.getFunctionName()).append(CDATA_END).append("</functionName>");
                    sb.append("\n\t\t\t<functionClass>").append(CDATA_START).append(inputDsfRelation.getFunctionClass()).append(CDATA_END).append("</functionClass>");
//                    MapreduceJarDao mapreduceJarDao = SpringContextHolder.getBean(MapreduceJarDao.class);
//                    mapreduceJar = mapreduceJarDao.get(inputDsfRelation.getFunctionJarId());
                    sb.append("\n\t\t\t<functionJarName>").append(CDATA_START).append(inputDsfRelation.getFunctionJarName()).append(CDATA_END).append("</functionJarName>");
                    sb.append("\n\t\t\t<functionJarPath>").append(CDATA_START).append(inputDsfRelation.getFunctionJarPath()).append(CDATA_END).append("</functionJarPath>");
                    sb.append("\n\t\t\t<functionExpr>").append(CDATA_START).append(StringEscapeUtils.unescapeXml(inputDsfRelation.getFunctionExpr())).append(CDATA_END).append("</functionExpr>");
                    sb.append("\n\t\t\t<exprType>").append(CDATA_START).append("").append(CDATA_END).append("</exprType>");
                    MetaStoreDao metaStoreDao = SpringContextHolder.getBean(MetaStoreDao.class);
                    MetaStore metaStore = metaStoreDao.get(taskDetailBean.getInputDataSourceId());
                    sb.append("\n\t\t\t<sourceId>").append(CDATA_START).append(metaStore.getId()).append(CDATA_END).append("</sourceId>");
                    sb.append("\n\t\t\t<sourceName>").append(CDATA_START).append(metaStore.getStoreName()).append(CDATA_END).append("</sourceName>");
                    sb.append("\n\t\t\t<arguments>");
                    if(!taskDetailBean.getFunctionParams().isEmpty()){
                        for (FunctionParamBean arg : taskDetailBean.getFunctionParams()) {
                            sb.append("\n\t\t\t<arg>");
                            sb.append("\n\t\t\t\t<name>").append(CDATA_START).append(arg.getParamName()).append(CDATA_END).append("</name>");
                            sb.append("\n\t\t\t\t<value>").append(CDATA_START).append(arg.getValue()).append(CDATA_END).append("</value>");
                            sb.append("\n\t\t\t\t<index>").append(arg.getParamIndex()).append("</index>");
                            sb.append("\n\t\t\t</arg>");
                        }
                    }
                    sb.append("\n\t\t\t</arguments>");
                    sb.append("\n\t\t</dqm>");
                }
            }
            sb.append("\n\t</dqs>");
        }else if(taskDetailBean.getTaskType()==Contants.TASK_TYPE_SHELL || 
                taskDetailBean.getTaskType()==Contants.TASK_TYPE_PROCEDURE){//shell脚本和存储过程
            sb.append("\n\t<mr>");
            Expension expension = null;
            String expansionFileld = taskDetailBean.getFunction().getExpansionField();
            if(expansionFileld!=null){
                expension = new Gson().fromJson(expansionFileld, Expension.class);
            }
            String functionType = "";
            if(taskDetailBean.getFunctionType().equals("9")){
                functionType = "Shell";
            }else if(taskDetailBean.getFunctionType().equals("8")){
                functionType = "SQL";
            }else{
                functionType = taskDetailBean.getFunctionType();
            }
            sb.append("\n\t\t<functionType>").append(CDATA_START).append(functionType).append(CDATA_END).append("</functionType>");
            sb.append("\n\t\t<functionId>").append(CDATA_START).append(taskDetailBean.getFunction().getId()).append(CDATA_END).append("</functionId>");
            sb.append("\n\t\t<functionJarName>").append(CDATA_START).append("").append(CDATA_END).append("</functionJarName>");
            sb.append("\n\t\t<functionJarPath>").append(CDATA_START).append("").append(CDATA_END).append("</functionJarPath>");
            sb.append("\n\t\t<functionName>").append(CDATA_START).append(taskDetailBean.getFunction().getFunctionName()).append(CDATA_END).append("</functionName>");
            sb.append("\n\t\t<functionClass>").append(CDATA_START).append(taskDetailBean.getFunction().getFunctionClass()).append(CDATA_END).append("</functionClass>");
            sb.append("\n\t\t<ip>").append(CDATA_START).append(expension.getIp()).append(CDATA_END).append("</ip>");
            sb.append("\n\t\t<port>").append(CDATA_START).append(expension.getPort()).append(CDATA_END).append("</port>");
            sb.append("\n\t\t<userName>").append(CDATA_START).append(expension.getUserName()).append(CDATA_END).append("</userName>");
            sb.append("\n\t\t<passWord>").append(CDATA_START).append(expension.getPassword()).append(CDATA_END).append("</passWord>");
            sb.append("\n\t\t<dbName>").append(CDATA_START).append(expension.getDbname()).append(CDATA_END).append("</dbName>");
            sb.append("\n\t\t<dbType>").append(CDATA_START).append(expension.getDbType()).append(CDATA_END).append("</dbType>");
            sb.append("\n\t\t<ruleExpression>").append(CDATA_START).append(expension.getRuleExpression()).append(CDATA_END).append("</ruleExpression>");
            sb.append("\n\t\t<etlModeName>").append(CDATA_START).append(expension.getEtlModeName()).append(CDATA_END).append("</etlModeName>");
            sb.append("\n\t\t<shellPath>").append(CDATA_START).append(expension.getShellPath()).append(CDATA_END).append("</shellPath>");
            sb.append("\n\t\t<arguments>");
            if(taskDetailBean.getFunctionParams()!=null && !taskDetailBean.getFunctionParams().isEmpty()){
                for (FunctionParamBean arg : taskDetailBean.getFunctionParams()) {
                    sb.append("\n\t\t\t<arg>");
                    sb.append("\n\t\t\t\t<index>").append(arg.getParamIndex()).append("</index>");
                    sb.append("\n\t\t\t\t<name>").append(CDATA_START).append(arg.getParamName()).append(CDATA_END).append("</name>");
                    sb.append("\n\t\t\t\t<value>").append(CDATA_START).append(arg.getValue()).append(CDATA_END).append("</value>");
                    sb.append("\n\t\t\t\t<paramType>").append(CDATA_START).append(arg.getParamType()).append(CDATA_END).append("</paramType>");
                    int paramSelect = 0;
                    if(arg.getParamSelect()!=null){
                        if(arg.getParamSelect()==1){
                            paramSelect=2;
                        }else{
                            paramSelect=1;
                        }
                    }
                    sb.append("\n\t\t\t\t<paramSelect>").append(CDATA_START).append(paramSelect).append(CDATA_END).append("</paramSelect>");
                    sb.append("\n\t\t\t</arg>");
                }
            }
            sb.append("\n\t\t</arguments>");
            sb.append("\n\t</mr>");
        }
        sb.append("\n\t<outputStores>");
        sb.append("\n\t</outputStores>");
        sb.append("\n\t<sources>");
        sb.append("\n\t</sources>");
        sb.append("\n\t</task>");
        return sb.toString();
    }
}
