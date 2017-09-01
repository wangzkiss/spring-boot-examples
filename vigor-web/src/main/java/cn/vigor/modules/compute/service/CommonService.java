package cn.vigor.modules.compute.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.thoughtworks.xstream.XStream;

import cn.vigor.common.contants.Contants;
import cn.vigor.common.utils.StringUtils;
import cn.vigor.modules.compute.bean.AssociationCondition;
import cn.vigor.modules.compute.bean.ComputeFunction;
import cn.vigor.modules.compute.bean.FTP4MR;
import cn.vigor.modules.compute.bean.HiveSql;
import cn.vigor.modules.compute.bean.InParam;
import cn.vigor.modules.compute.bean.LogicCond;
import cn.vigor.modules.compute.bean.Output;
import cn.vigor.modules.compute.bean.OutputField;
import cn.vigor.modules.compute.bean.OutputTemplate;
import cn.vigor.modules.compute.bean.SelfProgramArgument;
import cn.vigor.modules.compute.bean.Source;
import cn.vigor.modules.compute.bean.TableAssociation;
import cn.vigor.modules.compute.dao.ComputeJobDao;
import cn.vigor.modules.compute.dao.ComputeMetaDao;
import cn.vigor.modules.compute.dao.ComputeTaskDao;
import cn.vigor.modules.compute.dao.DateConverter;

@Service
@Transactional(readOnly = true)
public class CommonService
{
    @Autowired
    public ComputeTaskDao computeTaskDao;
    @Autowired
    public ComputeJobDao computeJobDao;
    @Autowired
    public ComputeMetaDao computeMetaDao;
    
    @Transactional(propagation = Propagation.REQUIRED)
    public int  createTask(Output output) {
        String taskName=output.getTaskName();
        output.setInputType(1);
        String newTaskName="";
        if(!output.getTaskName().startsWith("TASK_")){
            newTaskName="TASK_"+taskName;
            output.setTaskName(newTaskName);
        }
        computeTaskDao.insertTask(output);
        
        if(output.getFlist()!=null&&!output.getFlist().isEmpty()){
            for(InParam fs:output.getFlist()){
                fs.setTaskId(output.getTaskId());
                computeMetaDao.insertTmpTable(fs);
            }
        }

        // 关于Hive任务和Storm任务存储多数据源
        if ((output.getTaskType() == Contants.TASK_TYPE_HIVE || output.getTaskType() == Contants.TASK_TYPE_TRAFODION  || output.getTaskType() == Contants.TASK_TYPE_SPARK  )) {
            if (output.getStoreIdString() != null&& output.getStoreIdString().split(",").length > 1) {
                String[] storeIdStrings = output.getStoreIdString().split(",");
                for (String storeId : storeIdStrings) {
                    output.setStoreId(Integer.valueOf(storeId));
                    computeTaskDao.insertTaskInput(output);
                }
            } else if (StringUtils.isNotEmpty(output.getStoreIdString()) && output.getStoreIdString().split(",").length == 1) {
                output.setStoreId(Integer.valueOf(output.getStoreIdString()));
                computeTaskDao.insertTaskInput(output);
            } else if (StringUtils.isEmpty(output.getStoreIdString()) && output.getStoreId()>0) {
                computeTaskDao.insertTaskInput(output);
            }
        }else if(output.getTaskType() ==Contants.TASK_TYPE_MR||output.getTaskType()==Contants.TASK_TYPE_SPARKZDY){
            computeTaskDao.insertTaskInput(output);
        }
        if(output.getHiveSql().getType()==1 ||  output.getHiveSql().getType()==2 || output.getTaskType() == Contants.TASK_TYPE_MR ){
            computeTaskDao.insertTaskOutput(output);
        }
        Map<String,Integer> tbs = output.getOtherTable();
        if(tbs!=null&&!tbs.isEmpty()){
            for (Map.Entry<String, Integer> entry : tbs.entrySet()) {
                if(entry.getKey().contains("insert")){
                    output.setOutputId(entry.getValue());
                    output.setOutputType(1);
                    computeTaskDao.insertTaskOutput(output);
                }else if(entry.getKey().contains("select")){
                    output.setInputType(1);
                    output.setStoreId(entry.getValue());
                    computeTaskDao.insertTaskInput(output);
                }
            }
        }
        return output.getTaskId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTask(Output output) {
        String taskName=output.getTaskName();
        output.setInputType(1);
        String newTaskName="";
        if(!output.getTaskName().startsWith("TASK_")){
            newTaskName="TASK_"+taskName;
            output.setTaskName(newTaskName);
        }

        if(output.getFlist()!=null&&!output.getFlist().isEmpty()){
            for(InParam fs:output.getFlist()){
                fs.setTaskId(output.getTaskId());
                computeMetaDao.insertTmpTable(fs);
            }
        }
        
        computeTaskDao.deleteTask(output.getTaskId());
        computeTaskDao.deleteTaskInput(output.getTaskId());
        computeTaskDao.deleteTaskOutput(output.getTaskId());
        computeTaskDao.insertTask(output);
        
        // taskDao.insertTaskInput(output);
        // 关于Hive任务和Storm任务存储多数据源
        if ((output.getTaskType() == Contants.TASK_TYPE_HIVE || output.getTaskType() == Contants.TASK_TYPE_TRAFODION  || output.getTaskType() == Contants.TASK_TYPE_SPARK   )) {
            if (output.getStoreIdString() != null && output.getStoreIdString().split(",").length > 1) {
                String[] storeIdStrings = output.getStoreIdString().split(",");
                for (String storeId : storeIdStrings) {
                    output.setStoreId(Integer.valueOf(storeId));
                    computeTaskDao.insertTaskInput(output);
                }
            } else if (output.getStoreIdString() != null && output.getStoreIdString().split(",").length == 1) {
                if (output.getStoreIdString() != null) {
                    output.setStoreId(Integer.valueOf(output.getStoreIdString()));
                }
                computeTaskDao.insertTaskInput(output);
            } else if (output.getStoreIdString() == null && output.getStoreId()>0) {
                computeTaskDao.insertTaskInput(output);
            }
        }else if(output.getTaskType() ==Contants.TASK_TYPE_MR||output.getTaskType()==Contants.TASK_TYPE_SPARKZDY){
            computeTaskDao.insertTaskInput(output);
        }
        if(output.getHiveSql().getType()==1 ||  output.getHiveSql().getType()==2 ||  output.getTaskType() == Contants.TASK_TYPE_MR ){
            computeTaskDao.insertTaskOutput(output);
        }


        Map<String,Integer> tbs = output.getOtherTable();

        if(tbs!=null&&!tbs.isEmpty()){
            for (Map.Entry<String, Integer> entry : tbs.entrySet()) {
                if(entry.getKey().contains("insert")){
                    output.setOutputId(entry.getValue());
                    output.setOutputType(1);
                    computeTaskDao.insertTaskOutput(output);
                }else if(entry.getKey().contains("select")){
                    output.setInputType(1);
                    output.setStoreId(entry.getValue());
                    computeTaskDao.insertTaskInput(output);
                }
            }
        }
    }
    
    
    public Output getOutputFromXML(int taskId) {
        String xml = computeTaskDao.getTaskXmlData(taskId);
        XStream xstream = new XStream();
        xstream.alias("task", Output.class);
        xstream.alias("calField", OutputField.class);
        xstream.alias("hiveSql", HiveSql.class);
        xstream.alias("functions", ArrayList.class);
        xstream.alias("function", ComputeFunction.class);
        xstream.alias("associationList", ArrayList.class);
        xstream.alias("associationConditionList", ArrayList.class);
        xstream.alias("association", TableAssociation.class);
        xstream.alias("condition", AssociationCondition.class);
        xstream.alias("condFields", ArrayList.class);
        xstream.alias("condField", LogicCond.class);
        xstream.alias("dimensionFields", ArrayList.class);
        xstream.alias("dimensionField", OutputField.class);
        xstream.alias("dqs", ArrayList.class);

        xstream.alias("hbs", ArrayList.class);

        xstream.alias("outputStores", ArrayList.class);
        xstream.alias("outputStore", OutputTemplate.class);

        xstream.alias("sources", ArrayList.class);
        xstream.alias("source", Source.class);

        xstream.alias("startTime", Date.class);
        xstream.alias("mr", FTP4MR.class);
        xstream.alias("jarftp", String.class);
        xstream.alias("arguments", ArrayList.class);
        xstream.alias("arg", SelfProgramArgument.class);
        xstream.alias("result", String.class);
        xstream.registerConverter(new DateConverter());
        xstream.ignoreUnknownElements();
        Output output = null;
        if(xml!=null&&!xml.isEmpty()){
            output = (Output) xstream.fromXML(xml);
            return output;
        }
        return output;
    }
    
}
