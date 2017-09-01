package cn.vigor.modules.compute.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 数据输出
 * 
 * @author yangtao
 */
@XStreamAlias("task")
public class Output extends ModelObject {
    public static final int RESULT_TYPE_MONGODB = 16;
    public static final int RESULT_TYPE_HDFS = 4;
    public static final int RESULT_TYPE_MYSQL = 17;
    public static final int RESULT_TYPE_FTP = 9;
    
    private int inputType;
    
    private int resultId;
    
    public int getResultId() {
        return resultId;
    }
    
    public void setResultId(int resultId) {
        this.resultId = resultId;
    }
    
    public int getInputType() {
        return inputType;
    }
    
    public void setInputType(int inputType) {
        this.inputType = inputType;
    }
    
    private boolean isSaveAs=false;
    
    public boolean isSaveAs() {
        return isSaveAs;
    }
    
    public void setSaveAs(boolean isSaveAs) {
        this.isSaveAs = isSaveAs;
    }
    
    private String version="";
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * 主数据源信息
     */
    private Source mainSource;
    
    public Source getMainSource() {
        return mainSource;
    }
    
    public void setMainSource(Source mainSource) {
        this.mainSource = mainSource;
    }
    
    private List<OutputField> fields = new ArrayList<OutputField>();
    
    /**
     * 多个输出的存储实体类实例集合
     */
    private List<OutputTemplate> outputStores = new ArrayList<OutputTemplate>();
    
    /**
     * 获取多个输出的存储实体类实例集合
     * @return outputStores
     */
    public List<OutputTemplate> getOutputStores() {
        return outputStores;
    }
    
    public void setOutputStores(List<OutputTemplate> outputStores) {
        this.outputStores = outputStores;
    }
    
    /**
     * 多个数据源的存储实体类实例集合
     */
    private List<Source> sources = new ArrayList<Source>();
    
    /**
     * 获取多个数据源的存储实体类实例集合
     * @return sources
     */
    public List<Source> getSources() {
        return sources;
    }
    
    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
    
    private List<ComputeFunction> functions=new ArrayList<ComputeFunction>();
    
    public List<ComputeFunction> getFunctions() {
        return functions;
    }
    
    public void setFunctions(List<ComputeFunction> functions) {
        this.functions = functions;
    }
    
    private List<OutputField> dimensionFields = new ArrayList<OutputField>();
    private OutputField calField = new OutputField();
    private int sourceId;
    private HiveSql hiveSql = new HiveSql();
    private int jobId;
    
    private FTP4MR mr;
    
    private List<DataQualityMR>  dqs;
    
    private String hiveTime;
    
    private String hiveTimeUnit;
    
    private String hivefileSize;
    
    public String getHiveTime() {
        return hiveTime;
    }
    
    public void setHiveTime(String hiveTime) {
        this.hiveTime = hiveTime;
    }
    
    public String getHiveTimeUnit() {
        return hiveTimeUnit;
    }
    
    public void setHiveTimeUnit(String hiveTimeUnit) {
        this.hiveTimeUnit = hiveTimeUnit;
    }
    
    public String getHivefileSize() {
        return hivefileSize;
    }
    
    public void setHivefileSize(String hivefileSize) {
        this.hivefileSize = hivefileSize;
    }
    
    /*public List<DataQualityMR> getDqs() {
		return dqs;
	}

	public void setDqs(List<DataQualityMR> dqs) {
		this.dqs = dqs;
	}
     */
    /*private List<HamrBean>  hbs;

	public List<HamrBean> getHbs() {
		return hbs;
	}

	public void setHbs(List<HamrBean> hbs) {
		this.hbs = hbs;
	}
     */
    /*private Pig pig;
	
	public Pig getPig() {
		return pig;
	}

	public void setPig(Pig pig) {
		this.pig = pig;
	}
     */
    /**
     * 输出类型，输出到哪
     */
    private int outputType;
    
    public int getOutputType() {
        return outputType;
    }
    
    public void setOutputType(int outputType) {
        this.outputType = outputType;
    }
    
    /**
     * 算法模型实体类
     *//*
	private AlgorithmModel algorithmModel;
	
      *//**
      * 获取算法模型实体类实例
      * @return AlgorithmModel 
      *//*
	public AlgorithmModel getAlgorithmModel() {
		return algorithmModel;
	}
       */
    /**
     * 设置算法模型实体类实例
     * @param algorithmModel
     */
    /*public void setAlgorithmModel(AlgorithmModel algorithmModel) {
		this.algorithmModel = algorithmModel;
	}
     */
    private Schedule schedule;
    public Schedule getSchedule()
    {
        return schedule;
    }
    
    public void setSchedule(Schedule schedule)
    {
        this.schedule = schedule;
    }
    
    public OutputField getCalField() {
        return calField;
    }
    
    public void setCalField(OutputField calField) {
        this.calField = calField;
    }
    
    public List<OutputField> getDimensionFields() {
        return dimensionFields;
    }
    
    
    /**
     * 是否是导入的任务
     */
    private boolean importJob=false;
    
    public boolean isImportJob() {
        return importJob;
    }
    
    public void setImportJob(boolean importJob) {
        this.importJob = importJob;
    }
    
    private List<LogicCond> condFields = new ArrayList<LogicCond>();
    
    /**
     * 输出表的Id
     */
    private int outputId;
    
    public int getOutputId() {
        return outputId;
    }
    
    public void setOutputId(int outputId) {
        this.outputId = outputId;
    }
    
    private String name="";
    
    private String metaStoreName;
    
    private String file;
    
    private String metaStoreFile;
    
    private int type;
    
    private String desc;
    
    private String ip;
    
    private int port;
    
    private String dir;
    
    private String username;
    
    private String password;
    
    private String delimiter;
    
    private int resultType;
    
    private String partitionField="";
    
    public String getPartitionField() {
        return partitionField;
    }
    
    public void setPartitionField(String partitionField) {
        this.partitionField = partitionField;
    }
    
    private ComputeJob job ;
    
    private ComputeTask task;
    
    public ComputeTask getTask() {
        return task;
    }
    
    public void setTask(ComputeTask task) {
        this.task = task;
    }
    
    public String getMetaStoreName() {
        return metaStoreName;
    }
    
    public void setMetaStoreName(String metaStoreName) {
        this.metaStoreName = metaStoreName;
    }
    
    public String getMetaStoreFile() {
        return metaStoreFile;
    }
    
    public void setMetaStoreFile(String metaStoreFile) {
        this.metaStoreFile = metaStoreFile;
    }
    
    public ComputeJob getJob() {
        return job;
    }
    
    public void setJob(ComputeJob job) {
        this.job = job;
    }
    
    public String getDelimiter() {
        return delimiter;
    }
    
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    /**
     * 任务状态
     */
    private int taskStatus;
    
    public int getTaskStatus() {
        return taskStatus;
    }
    
    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }
    
    private String taskName;
    
    private String taskDesc;
    
    private int taskId;
    
    private int taskType;
    
    private int storeId;
    
    private String storeIdString;
    
    private String dataBaseName="";
    
    public String getDataBaseName() {
        return dataBaseName;
    }
    
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }
    
    public String getStoreIdString() {
        return storeIdString;
    }
    
    public void setStoreIdString(String storeIdString) {
        this.storeIdString = storeIdString;
    }
    
    private String functionId="";
    
    private Map<String, List<MetaStoreField>> sourceNameKeyMapFields;
    
    public Map<String, List<MetaStoreField>> getSourceNameKeyMapFields() {
        return sourceNameKeyMapFields;
    }
    
    public void setSourceNameKeyMapFields(Map<String, List<MetaStoreField>> sourceNameKeyMapFields) {
        this.sourceNameKeyMapFields = sourceNameKeyMapFields;
    }
    
    /**
     * 一个使用实体类Source的Name+TypeName作为Key，自己本身作为Value的映射.
     */
    private Map<String, Source> sourceNameTypeKeyMapFields;
    
    public Map<String, Source> getSourceNameTypeKeyMapFields() {
        return sourceNameTypeKeyMapFields;
    }
    
    public void setSourceNameTypeKeyMapFields(
            Map<String, Source> sourceNameTypeKeyMapFields) {
        this.sourceNameTypeKeyMapFields = sourceNameTypeKeyMapFields;
    }
    
    private static final String CDATA_START = "<![CDATA[";
    private static final String CDATA_END = "]]>";
    
    /**
     * 当前Kettle登录用户信息
     *//*
	private LoginUserInfo loginUser;
	
	public LoginUserInfo getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(LoginUserInfo loginUser) {
		this.loginUser = loginUser;
	}*/
    
    /**
     * 创建用户名称
     */
    private String createUser;
    
    public String getCreateUser() {
        return createUser;
    }
    
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    
    /**
     * 任务来源平台,1：配置工具； 2：数据服务
     */
    private int comeFrom=1;
    
    public int getComeFrom() {
        return comeFrom;
    }
    
    public void setComeFrom(int comeFrom) {
        firePropertyChange("comeFrom", this.comeFrom, this.comeFrom = comeFrom);
    }
    
    /**
     * 创建用户Id
     */
    private int createUserid;
    
    private int ownerUserid;
    
    public int getOwnerUserid() {
        return ownerUserid;
    }
    
    public void setOwnerUserid(int ownerUserid) {
        this.ownerUserid = ownerUserid;
    }
    
    public int getCreateUserid() {
        return createUserid;
    }
    
    public void setCreateUserid(int createUserid) {
        this.createUserid = createUserid;
    }
    
    /**
     * 选择的资源库id(支持多选,多选情况下id用逗号,分隔)
     */
    private String repoId;
    
    public String getRepoId() {
        return repoId;
    }
    
    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }
    
    public String getXmlString2() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        sb.append("\n<task>");
        sb.append("\n\t<taskId>").append(taskId).append("</taskId>");
        sb.append("\n\t<taskType>").append(taskType).append("</taskType>");
        sb.append("\n\t<taskName>").append(CDATA_START).append(taskName).append(CDATA_END).append("</taskName>");
        sb.append("\n\t<taskDesc>").append(CDATA_START).append(taskDesc).append(CDATA_END).append("</taskDesc>");
        sb.append("\n\t<inputDataBase>").append(CDATA_START).append(inputDataBase).append(CDATA_END).append("</inputDataBase>");
        sb.append("\n\t<outputDataBase>").append(CDATA_START).append(outputDataBase).append(CDATA_END).append("</outputDataBase>");
        sb.append("\n\t<repoId>").append(repoId).append("</repoId>");
        sb.append("\n\t<outRepoId>").append(outRepoId).append("</outRepoId>");
        sb.append("\n\t<storagType>").append(storagType).append("</storagType>");
        sb.append("\n\t<outputString>").append(outputString).append("</outputString>");
        sb.append("\n\t<groupId>").append(groupId).append("</groupId>");
        sb.append("\n\t<outputId>").append(outputId).append("</outputId>");
        
        sb.append("\n\t<createUserid>").append(CDATA_START).append(createUserid).append(CDATA_END).append("</createUserid>");
        sb.append("\n\t<ownerUserid>").append(CDATA_START).append(ownerUserid).append(CDATA_END).append("</ownerUserid>");
        
        //
        sb.append("\n\t<version>").append(CDATA_START).append(version).append(CDATA_END).append("</version>");
        sb.append("\n\t<storeId>").append(storeId).append("</storeId>");
        sb.append("\n\t<functionId>").append(functionId).append("</functionId>");
        sb.append("\n\t<outputType>").append(outputType).append("</outputType>");
        sb.append("\n\t<inputType>").append(inputType).append("</inputType>");
        sb.append("\n\t<partitionField>").append(partitionField).append("</partitionField>");
        
        sb.append("\n\t<hiveTime>").append(this.getHiveTime()).append("</hiveTime>");
        sb.append("\n\t<hiveTimeUnit>").append(this.getHiveTimeUnit()).append("</hiveTimeUnit>");
        sb.append("\n\t<hivefileSize>").append(CDATA_START).append(this.getHivefileSize()).append(CDATA_END).append("</hivefileSize>");
        if (StringUtils.isNotBlank(delimiter)) {
            sb.append("\n\t<delimiter>").append(CDATA_START).append(delimiter).append(CDATA_END).append("</delimiter>");
        }
        //		if (hiveSql.getType() > 0) {
        sb.append("\n\t<hiveSql>");
        sb.append("\n\t\t<type>").append(hiveSql.getType()).append("</type>");
        sb.append("\n\t\t<masterTable>").append(hiveSql.getMasterTable()).append("</masterTable>");
        sb.append("\n\t\t<dependentTable>").append(hiveSql.getDependentTable()).append("</dependentTable>");
        sb.append("\n\t\t<value>").append(CDATA_START).append(hiveSql.getValue()).append(CDATA_END).append("</value>");
        sb.append("\n\t\t<dataBaseName>").append(CDATA_START).append(hiveSql.getDataBaseName()).append(CDATA_END).append("</dataBaseName>");
        sb.append("\n\t\t\t<associationList>");
        
        if(hiveSql.getAssociationList()!=null&&!hiveSql.getAssociationList().isEmpty()){
            
            for (TableAssociation tableAssociation : hiveSql.getAssociationList()) {
                sb.append("\n\t\t\t\t<association>");
                sb.append("\n\t\t\t\t\t<masterTable>").append(CDATA_START).append(tableAssociation.getMasterTable()).append(CDATA_END).append("</masterTable>");
                sb.append("\n\t\t\t\t\t<masterTableType>").append(CDATA_START).append(tableAssociation.getMasterTableType()).append(CDATA_END).append("</masterTableType>");
                sb.append("\n\t\t\t\t\t\t<tableName>").append(CDATA_START).append(tableAssociation.getTableName()).append(CDATA_END).append("</tableName>");
                sb.append("\n\t\t\t\t\t\t<fromTableType>").append(CDATA_START).append(tableAssociation.getFromTableType()).append(CDATA_END).append("</fromTableType>");
                sb.append("\n\t\t\t\t\t\t<logic>").append(CDATA_START).append(tableAssociation.getLogic()).append(CDATA_END).append("</logic>");
                if(!tableAssociation.getAssociationConditionList().isEmpty()){
                    sb.append("\n\t\t\t\t\t\t<associationConditionList>");
                    for(AssociationCondition associationCondition : tableAssociation.getAssociationConditionList()){
                        sb.append("\n\t\t\t\t\t\t\t<condition>");
                        sb.append("\n\t\t\t\t\t\t\t\t<condition>").append(CDATA_START).append(associationCondition.getCondition()).append(CDATA_END).append("</condition>");
                        sb.append("\n\t\t\t\t\t\t\t\t<asslogic>").append(CDATA_START).append(associationCondition.getAssLogic()).append(CDATA_END).append("</asslogic>");
                        sb.append("\n\t\t\t\t\t\t\t\t<operation>").append(CDATA_START).append(associationCondition.getOperation()).append(CDATA_END).append("</operation>");
                        sb.append("\n\t\t\t\t\t\t\t</condition>");
                    }
                    sb.append("\n\t\t\t\t\t\t</associationConditionList>");
                }
                sb.append("\n\t\t\t\t</association>");
            }
        }
        sb.append("\n\t\t\t</associationList>");
        sb.append("\n\t</hiveSql>");
        //		}
        
        
        
        
        if(this.getTaskType()==ComputeTask.HIVE_TASK_TYPE||this.getTaskType()==ComputeTask.SPARK_TASK_TYPE){
            if(hiveSql.getAssociationList()!=null&&!hiveSql.getAssociationList().isEmpty()){
                //Hive或者Shark计算任务时才写入
                sb.append("\n\t\t<joinTables>");
                
                
                
                for (TableAssociation tableAssociation : hiveSql.getAssociationList()) {
                    sb.append("\n\t\t\t<joinTable>");
                    sb.append("\n\t\t\t\t<joinType>").append(CDATA_START).append(tableAssociation.getLogic()).append(CDATA_END).append("</joinType>");
                    sb.append("\n\t\t\t\t<joinTable>").append(CDATA_START).append(tableAssociation.getTableName()).append(CDATA_END).append("</joinTable>");
                    sb.append("\n\t\t\t\t<tableAlias>").append(CDATA_START).append(tableAssociation.getTableName()).append(CDATA_END).append("</tableAlias>");
                    if(!tableAssociation.getAssociationConditionList().isEmpty()){
                        StringBuilder condition = new StringBuilder("ON ");
                        int i = 0;
                        for(AssociationCondition associationCondition : tableAssociation.getAssociationConditionList()){
                            associationCondition.getCondition();
                            if (i != 0) {
                                condition.append(associationCondition.getAssLogic()).append(" ");
                            }
                            condition.append(associationCondition.getCondition()).append(" ");
                            i++;
                        }
                        sb.append("\n\t\t\t\t<joinCondition>").append(CDATA_START).append(condition).append(CDATA_END).append("</joinCondition>");
                    }
                    sb.append("\n\t\t\t</joinTable>");
                }
                sb.append("\n\t\t</joinTables>");
            }
        }
        
        
        
        sb.append("\n\t\t<name>").append(CDATA_START).append(name).append(CDATA_END).append("</name>");
        sb.append("\n\t\t<file>").append(CDATA_START).append(file).append(CDATA_END).append("</file>");
        sb.append("\n\t\t<username>").append(CDATA_START).append(username).append(CDATA_END).append("</username>");
        sb.append("\n\t\t<password>").append(CDATA_START).append(password).append(CDATA_END).append("</password>");
        sb.append("\n\t\t<dir>").append(CDATA_START).append(dir).append(CDATA_END).append("</dir>");
        sb.append("\n\t\t<ip>").append(CDATA_START).append(ip).append(CDATA_END).append("</ip>");
        sb.append("\n\t\t<port>").append(port).append("</port>");
        sb.append("\n\t\t<resultType>").append(resultType).append("</resultType>");
        sb.append("\n\t\t<resultId>").append(CDATA_START).append(resultId).append(CDATA_END).append("</resultId>");
        
        sb.append("\n\t<result>");
        sb.append("\n\t\t<name>").append(CDATA_START).append(name).append(CDATA_END).append("</name>");
        sb.append("\n\t\t<file>").append(CDATA_START).append(file).append(CDATA_END).append("</file>");
        sb.append("\n\t\t<username>").append(CDATA_START).append(username).append(CDATA_END).append("</username>");
        sb.append("\n\t\t<password>").append(CDATA_START).append(password).append(CDATA_END).append("</password>");
        sb.append("\n\t\t<dir>").append(CDATA_START).append(dir).append(CDATA_END).append("</dir>");
        sb.append("\n\t\t<ip>").append(CDATA_START).append(ip).append(CDATA_END).append("</ip>");
        sb.append("\n\t\t<port>").append(port).append("</port>");
        sb.append("\n\t\t<resultType>").append(resultType).append("</resultType>");
        if (StringUtils.isNotBlank(delimiter)) {
            sb.append("\n\t<delimiter>").append(CDATA_START).append(delimiter).append(CDATA_END).append("</delimiter>");
        }
        sb.append("\n\t</result>");
        
        if (calField != null) {
            sb.append("\n\t<calField>");
            sb.append("\n\t\t<name>").append(CDATA_START).append(calField.getName()).append(CDATA_END).append("</name>");
            sb.append("\n\t\t<aliasName>").append(CDATA_START).append(calField.getAliasName()).append(CDATA_END).append("</aliasName>");
            sb.append("\n\t\t<type>").append(calField.getType()).append("</type>");
            if (taskType == ComputeTask.R_TASK_TYPE) {
                sb.append("\n\t\t<index>").append(calField.getIndex()).append("</index>");
            } else {
                sb.append("\n\t\t<index>").append(calField.getIndex()).append("</index>");
            }
            sb.append("\n\t</calField>");
        }
        
        sb.append("\n\t<dimensionFields>");
        for (OutputField f : getDimensionFields()) {
            sb.append("\n\t\t<dimensionField>");
            if(f.isFuction()){
                sb.append("\n\t\t\t<name>").append(CDATA_START).append(f.getCompleteFunction()).append(CDATA_END).append("</name>");
                sb.append("\n\t\t\t<fuctionId>").append(f.getFuctionId()).append("</fuctionId>");
                sb.append("\n\t\t\t<functionName>").append(f.getFunctionName()).append("</functionName>");
                sb.append("\n\t\t\t<fuctionAlias>").append(f.getFuctionAlias()).append("</fuctionAlias>");
                sb.append("\n\t\t\t<functionClassName>").append(f.getFunctionClassName()).append("</functionClassName>");
                sb.append("\n\t\t\t<fuctionDesc>").append(f.getFuctionDesc()).append("</fuctionDesc>");
                sb.append("\n\t\t\t<fuctionFields>").append(f.getFuctionFields()).append("</fuctionFields>");
                sb.append("\n\t\t\t<fuctionType>").append(f.getFuctionType()).append("</fuctionType>");
            }else{
                sb.append("\n\t\t\t<name>").append(CDATA_START).append(f.getCompleteFieldName()).append(CDATA_END).append("</name>");
                sb.append("\n\t\t\t<fieldAlias>").append(f.getFieldAlias()).append("</fieldAlias>");
            }
            sb.append("\n\t\t\t<isFuction>").append(f.isFuction()).append("</isFuction>");
            sb.append("\n\t\t\t<type>").append(f.getType()).append("</type>");
            sb.append("\n\t\t\t<index>").append(f.getIndex()).append("</index>");
            sb.append("\n\t\t</dimensionField>");
        }
        sb.append("\n\t</dimensionFields>");
        
        sb.append("\n\t<functions>");
        for (ComputeFunction f : getFunctions()) {
            sb.append("\n\t\t<function>");
            sb.append("\n\t\t\t<calculateField>").append(CDATA_START).append(f.getCalculateField()).append(CDATA_END).append("</calculateField>");
            sb.append("\n\t\t\t<calculateFieldAliasName>").append(f.getAlias()).append("</calculateFieldAliasName>");
            sb.append("\n\t\t\t<functionClassName>").append(f.getFunctionClassName()).append("</functionClassName>");
            sb.append("\n\t\t\t<type>").append(f.getType()).append("</type>");
            sb.append("\n\t\t</function>");
        }
        sb.append("\n\t</functions>");
        
        sb.append("\n\t<condFields>");
        int j = 0;
        for (LogicCond lc : getCondFields()) {
            // 字段和值不能为空
            if (StringUtils.isNotBlank(lc.getName())) {
                // 条件式中除第一个语句外，其他语句都要有and或or
                if (j > 0 && StringUtils.isBlank(lc.getAndor())){
                    continue;
                }
                sb.append("\n\t\t<condField>");
                sb.append("\n\t\t\t<name>").append(CDATA_START).append(lc.getName().split(" ")[0]).append(CDATA_END).append("</name>");
                sb.append("\n\t\t\t<sourceName>").append(CDATA_START).append(lc.getSourceName()).append(CDATA_END).append("</sourceName>");
                sb.append("\n\t\t\t<type>").append(lc.getType()).append("</type>");
                if(lc.getType()!=null && lc.getType().equalsIgnoreCase("date")){
                    sb.append("\n\t\t\t<dataFormat>").append(lc.getDataFormat()==null?"":lc.getDataFormat()).append("</dataFormat>");
                }
                sb.append("\n\t\t\t<left>").append(lc.getLeft()).append("</left>");
                sb.append("\n\t\t\t<right>").append(lc.getRight()).append("</right>");
                if (j > 0){
                    sb.append("\n\t\t\t<andor>").append(lc.getAndor()).append("</andor>");
                }
                else{
                    sb.append("\n\t\t\t<andor>").append("").append("</andor>");
                }
                sb.append("\n\t\t\t<isReEexc>").append(CDATA_START).append(lc.isReEexc()).append(CDATA_END).append("</isReEexc>");
                sb.append("\n\t\t\t<op>").append(CDATA_START).append(lc.getOp()).append(CDATA_END).append("</op>");
                sb.append("\n\t\t\t<value>").append(CDATA_START).append(lc.getValue()).append(CDATA_END).append("</value>");
                sb.append("\n\t\t</condField>");
                j++;
            }
        }
        sb.append("\n\t</condFields>");
        
        if (mr != null) {
            sb.append("\n\t<mr>");
            //          sb.append("\n\t<jarftp>");
            //          sb.append("\n\t\t<ip>").append(mr.getIp()).append("</ip>");
            //          sb.append("\n\t\t<port>").append(mr.getPort()).append("</port>");
            //          sb.append("\n\t\t<path>").append(mr.getPath()).append("</path>");
            //          sb.append("\n\t\t<fileName>").append(mr.getFileName()).append("</fileName>");
            //          sb.append("\n\t\t<user>").append("mr_user").append("</user>");
            ////            sb.append("\n\t\t<user>").append(mr.getUser()).append("</user>");
            //          sb.append("\n\t\t<password>").append("mr_pwd").append("</password>");
            ////            sb.append("\n\t\t<password>").append(mr.getPassword()).append("</password>");
            //          sb.append("\n\t</jarftp>");
            //          sb.append("\n\t\t<mainClass>").append(CDATA_START).append(mr.getMainClass()).append(CDATA_END).append("</mainClass>");
            
            sb.append("\n\t\t<functionType>").append(CDATA_START).append(mr.getFunctionType()).append(CDATA_END).append("</functionType>");
            sb.append("\n\t\t<functionId>").append(CDATA_START).append(mr.getFunctionId()).append(CDATA_END).append("</functionId>");
            sb.append("\n\t\t<functionJarName>").append(CDATA_START).append(mr.getFunctionJarName()).append(CDATA_END).append("</functionJarName>");
            sb.append("\n\t\t<functionJarPath>").append(CDATA_START).append(mr.getFunctionJarPath()).append(CDATA_END).append("</functionJarPath>");
            sb.append("\n\t\t<functionName>").append(CDATA_START).append(mr.getFunctionName()).append(CDATA_END).append("</functionName>");
            sb.append("\n\t\t<functionClass>").append(CDATA_START).append(mr.getFunctionClass()).append(CDATA_END).append("</functionClass>");
            
            sb.append("\n\t\t<ip>").append(CDATA_START).append(mr.getIp()==null?"":mr.getIp()).append(CDATA_END).append("</ip>");
            sb.append("\n\t\t<port>").append(CDATA_START).append(mr.getPort()).append(CDATA_END).append("</port>");
            sb.append("\n\t\t<userName>").append(CDATA_START).append(mr.getUserName()==null?"":mr.getUserName()).append(CDATA_END).append("</userName>");
            sb.append("\n\t\t<passWord>").append(CDATA_START).append(mr.getPassWord()==null?"":mr.getPassWord()).append(CDATA_END).append("</passWord>");
            sb.append("\n\t\t<dbName>").append(CDATA_START).append(mr.getDbName()==null?"":mr.getDbName()).append(CDATA_END).append("</dbName>");
            sb.append("\n\t\t<dbType>").append(CDATA_START).append(mr.getDbType()).append(CDATA_END).append("</dbType>");
            
            sb.append("\n\t\t<ruleExpression>").append(CDATA_START).append(mr.getRuleExpression()).append(CDATA_END).append("</ruleExpression>");
            sb.append("\n\t\t<etlModeName>").append(CDATA_START).append(mr.getEtlModeName()).append(CDATA_END).append("</etlModeName>");
            sb.append("\n\t\t<shellPath>").append(CDATA_START).append(mr.getShellPath()).append(CDATA_END).append("</shellPath>");
            
            sb.append("\n\t\t<arguments>");
            
            
            if(!mr.getArguments().isEmpty()){
                for (SelfProgramArgument arg : mr.getArguments()) {
                    sb.append("\n\t\t\t<arg>");
                    sb.append("\n\t\t\t\t<index>").append(arg.getIndex()).append("</index>");
                    sb.append("\n\t\t\t\t<name>").append(CDATA_START).append(arg.getName()).append(CDATA_END).append("</name>");
                    sb.append("\n\t\t\t\t<value>").append(CDATA_START).append(arg.getValue()).append(CDATA_END).append("</value>");
                    sb.append("\n\t\t\t\t<paramType>").append(CDATA_START).append(arg.getParamType()).append(CDATA_END).append("</paramType>");
                    sb.append("\n\t\t\t\t<paramSelect>").append(CDATA_START).append(arg.getParamSelect()).append(CDATA_END).append("</paramSelect>");
                    sb.append("\n\t\t\t</arg>");
                }
            }
            /*else{
                if(mr.getModelArguments()!=null&&!mr.getModelArguments().isEmpty()){
                    for (ModelProgramArgument arg : mr.getModelArguments()) {
                        sb.append("\n\t\t\t<arg>");
                        sb.append("\n\t\t\t\t<index>").append(arg.getIndex()).append("</index>");
                        sb.append("\n\t\t\t\t<name>").append(CDATA_START).append(arg.getName()).append(CDATA_END).append("</name>");
                        sb.append("\n\t\t\t\t<value>").append(CDATA_START).append(arg.getValue()).append(CDATA_END).append("</value>");
                        sb.append("\n\t\t\t</arg>");
                    }
                }
            }*/
            sb.append("\n\t\t</arguments>");
            sb.append("\n\t</mr>");
        }
        
        if (dqs != null&&!dqs.isEmpty()) {
            sb.append("\n\t<dqs>");
            for(DataQualityMR dq:dqs){
                sb.append("\n\t\t<dqm>");
                sb.append("\n\t\t\t<fieldName>").append(CDATA_START).append(dq.getFieldName()).append(CDATA_END).append("</fieldName>");
                sb.append("\n\t\t\t<fieldIndex>").append(CDATA_START).append(dq.getFieldIndex()).append(CDATA_END).append("</fieldIndex>");
                sb.append("\n\t\t\t<fieldFormat>").append(CDATA_START).append(dq.getFieldFormat()).append(CDATA_END).append("</fieldFormat>");
                sb.append("\n\t\t\t<fieldType>").append(CDATA_START).append(dq.getFieldType()).append(CDATA_END).append("</fieldType>");
                
                sb.append("\n\t\t\t<functionId>").append(CDATA_START).append(dq.getFunctionId()).append(CDATA_END).append("</functionId>");
                sb.append("\n\t\t\t<functionName>").append(CDATA_START).append(dq.getFunctionName()).append(CDATA_END).append("</functionName>");
                sb.append("\n\t\t\t<functionClass>").append(CDATA_START).append(dq.getFunctionClass()).append(CDATA_END).append("</functionClass>");
                
                sb.append("\n\t\t\t<functionJarName>").append(CDATA_START).append(dq.getFunctionJarName()).append(CDATA_END).append("</functionJarName>");
                sb.append("\n\t\t\t<functionJarPath>").append(CDATA_START).append(dq.getFunctionJarPath()).append(CDATA_END).append("</functionJarPath>");
                sb.append("\n\t\t\t<functionExpr>").append(CDATA_START).append(dq.getFunctionExpr()).append(CDATA_END).append("</functionExpr>");
                sb.append("\n\t\t\t<exprType>").append(CDATA_START).append(dq.getExprType()).append(CDATA_END).append("</exprType>");
                sb.append("\n\t\t\t<sourceId>").append(CDATA_START).append(dq.getSourceId()).append(CDATA_END).append("</sourceId>");
                sb.append("\n\t\t\t<sourceName>").append(CDATA_START).append(dq.getSourceName()).append(CDATA_END).append("</sourceName>");
                sb.append("\n\t\t\t<arguments>");
                if(!dq.getArguments().isEmpty()){
                    for (SelfProgramArgument arg : dq.getArguments()) {
                        sb.append("\n\t\t\t<arg>");
                        sb.append("\n\t\t\t\t<name>").append(CDATA_START).append(arg.getName()).append(CDATA_END).append("</name>");
                        sb.append("\n\t\t\t\t<value>").append(CDATA_START).append(arg.getValue()).append(CDATA_END).append("</value>");
                        sb.append("\n\t\t\t\t<index>").append(arg.getIndex()).append("</index>");
                        sb.append("\n\t\t\t</arg>");
                    }
                }
                sb.append("\n\t\t\t</arguments>");
                sb.append("\n\t\t</dqm>");
            }
            sb.append("\n\t</dqs>");
        }
        
        
        sb.append("\n\t<outputStores>");
        if(this.outputStores!=null&&!this.outputStores.isEmpty()){
            for(OutputTemplate ot:outputStores){
                sb.append("\n\t\t<outputStore>");
                sb.append("\n\t\t\t<id>").append(CDATA_START).append(ot.getId()).append(CDATA_END).append("</id>");
                sb.append("\n\t\t\t<db>").append(CDATA_START).append(ot.getDb()).append(CDATA_END).append("</db>");
                sb.append("\n\t\t\t<ip>").append(ot.getIp()).append("</ip>");
                sb.append("\n\t\t\t<port>").append(ot.getPort()).append("</port>");
                sb.append("\n\t\t\t<username>").append(ot.getUsername()).append("</username>");
                sb.append("\n\t\t\t<password>").append(ot.getPassword()).append("</password>");
                sb.append("\n\t\t\t<type>").append(ot.getType()).append("</type>");
                sb.append("\n\t\t\t<typeText>").append(ot.getTypeText()).append("</typeText>");
                sb.append("\n\t\t</outputStore>");
            }
        }
        sb.append("\n\t</outputStores>");
        
        sb.append("\n\t<sources>");
        if(this.getSources()!=null&&!this.getSources().isEmpty()){
            for(Source source:getSources()){
                sb.append("\n\t\t<source>");
                sb.append("\n\t\t\t<id>").append(CDATA_START).append(source.getId()).append(CDATA_END).append("</id>");
                sb.append("\n\t\t\t<name>").append(CDATA_START).append(source.getName()==null?"":source.getName()).append(CDATA_END).append("</name>");
                sb.append("\n\t\t\t<desc>").append(CDATA_START).append(source.getDesc()==null?"":source.getDesc()).append(CDATA_END).append("</desc>");
                sb.append("\n\t\t\t<ip>").append(source.getIp()==null?"":source.getIp()).append("</ip>");
                sb.append("\n\t\t\t<port>").append(source.getPort()==null?"":source.getPort()).append("</port>");
                sb.append("\n\t\t\t<dir>").append(source.getDir()==null?"":source.getDir()).append("</dir>");
                sb.append("\n\t\t\t<file>").append(source.getFile()==null?"":source.getFile()).append("</file>");
                sb.append("\n\t\t\t<type>").append(source.getType()==null?"":source.getType()).append("</type>");
                sb.append("\n\t\t\t<typeName>").append(source.getTypeName()==null?"":source.getTypeName()).append("</typeName>");
                sb.append("\n\t\t</source>");
            }
        }
        sb.append("\n\t</sources>");
        sb.append("\n</task>");
        return sb.toString();
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        firePropertyChange("name", this.name, this.name = name);
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        firePropertyChange("type", this.type, this.type = type);
    }
    
    public String getDesc() {
        return desc;
    }
    
    public void setDesc(String desc) {
        firePropertyChange("desc", this.desc, this.desc = desc);
    }
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        firePropertyChange("ip", this.ip, this.ip = ip);
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        firePropertyChange("port", this.port, this.port = port);
    }
    
    public String getFile() {
        return file;
    }
    
    public void setFile(String file) {
        firePropertyChange("file", this.file, this.file = file);
    }
    
    public String getDir() {
        return dir;
    }
    
    public void setDir(String dir) {
        firePropertyChange("dir", this.dir, this.dir = dir);
    }
    
    public List<OutputField> getFields() {
        return fields;
    }
    
    public void setFields(List<OutputField> fields) {
        this.fields = fields;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        firePropertyChange("username", this.username, this.username = username);
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        firePropertyChange("password", this.password, this.password = password);
    }
    
    /*
     * public void setDataInfoId(int dataInfoId) { this.dataInfoId = dataInfoId;
     * }
     */
    
    //	public void setDemiliter(String delimiter) {
    //		firePropertyChange("delimiter", this.delimiter,
    //				this.delimiter = delimiter);
    //	}
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String taskName) {
        firePropertyChange("taskName", this.taskName, this.taskName = taskName);
    }
    
    public String getTaskDesc() {
        return taskDesc;
    }
    
    public void setTaskDesc(String taskDesc) {
        firePropertyChange("taskDesc", this.taskDesc, this.taskDesc = taskDesc);
    }
    
    public int getTaskId() {
        return taskId;
    }
    
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    
    public String getFunctionId() {
        return functionId;
    }
    
    public void setFunctionId(String functionId) {
        this.functionId = functionId;
    }
    
    public int getTaskType() {
        return taskType;
    }
    
    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
    
    public HiveSql getHiveSql() {
        return hiveSql;
    }
    
    public void setHiveSql(HiveSql hiveSql) {
        this.hiveSql = hiveSql;
    }
    
    public int getStoreId() {
        return storeId;
    }
    
    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }
    
    public List<LogicCond> getCondFields() {
        return condFields;
    }
    
    public void setCondFields(List<LogicCond> condFields) {
        this.condFields = condFields;
    }
    
    public int getResultType() {
        return resultType;
    }
    
    public void setResultType(int resultType) {
        this.resultType = resultType;
    }
    
    public int getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
    
    
    public int getJobId() {
        return jobId;
    }
    
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
    
    
    /**
     * 存储类型 0 结果集 1 为平台存储
     */
    private int storagType;
    
    /**
     * 获取 存储类型
     * @return int storagType
     */
    public int getStoragType() {
        return storagType;
    }
    
    /**
     * 设置 存储类型 
     * @param storagType 存储类型
     */
    public void setStoragType(int storagType) {
        this.storagType = storagType;
    }
    /**
     * 输入资源库Id
     */
    private int inRepoId;
    
    /**
     * 输出资源库Id
     */
    private int outRepoId;
    
    /**
     * 获取 输入资源库Id
     * @return inRepoId 输入资源库Id
     */
    public int getInRepoId() {
        return inRepoId;
    }
    
    /**
     * 设置 输入资源库ID
     * @param inRepoId 输出资源库ID
     */
    public void setInRepoId(int inRepoId) {
        this.inRepoId = inRepoId;
    }
    
    /**
     * 获取 输出资源库Id
     * @return outRepoId 输出资源库Id
     */
    public int getOutRepoId() {
        return outRepoId;
    }
    
    /**
     * 设置 输出资源库ID
     * @param outRepoId 输入资源库ID
     */
    public void setOutRepoId(int outRepoId) {
        this.outRepoId = outRepoId;
    }
    
    /**
     * 输出类型 标志
     */
    private String outputString;
    
    /**
     * 获取 输出类型 标志
     * @return outputString 输出类型 标志
     */
    public String getOutputString() {
        return outputString;
    }
    
    /**
     * 设置 输出类型 标志
     * @param outputString 输出类型 标志
     */
    public void setOutputString(String outputString) {
        this.outputString = outputString;
    }
    
    /**
     *  分组Id
     */
    private int groupId;
    
    /**
     * 获取 分组Id
     * @return groupId 分组Id
     */
    public int getGroupId() {
        return groupId;
    }
    
    /**
     * 设置 分组Id
     * @param groupId 分组Id
     */
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    
    
    public Map<String,Integer> otherTable;
    
    public Map<String, Integer> getOtherTable() {
        return otherTable;
    }
    
    public void setOtherTable(Map<String, Integer> otherTable) {
        this.otherTable = otherTable;
    }
    
    public String inputDataBase;
    
    public String getInputDataBase() {
        return inputDataBase;
    }
    
    public void setInputDataBase(String inputDataBase) {
        this.inputDataBase = inputDataBase;
    }
    
    public String outputDataBase;
    
    public String getOutputDataBase() {
        return outputDataBase;
    }
    
    public void setOutputDataBase(String outputDataBase) {
        this.outputDataBase = outputDataBase;
    }
    
    
    /*-------------------------bs------------------------------*/
    
    /**
     * 前台所选按钮   配置hivesql  1  自定义hivesql 2   hive -e 3
     */
    public int btnHiveSql;
    
    public int getBtnHiveSql()
    {
        return btnHiveSql;
    }
    
    public void setBtnHiveSql(int btnHiveSql)
    {
        this.btnHiveSql = btnHiveSql;
    }
    
    /**
     * 所选字段
     */
    private String chooseFields;
    
    public String getChooseFields()
    {
        return chooseFields;
    }
    
    public void setChooseFields(String chooseFields)
    {
        this.chooseFields = chooseFields;
    }
    
    /**
     * 所选中字段的表名
     */
    private String tableNames;
    
    public String getTableNames()
    {
        return tableNames;
    }
    
    public void setTableNames(String tableNames)
    {
        this.tableNames = tableNames;
    }
    
    /**
     * 生成的sql
     */
    private String sqlValue;
    
    public String getSqlValue()
    {
        return sqlValue;
    }
    
    public void setSqlValue(String sqlValue)
    {
        this.sqlValue = sqlValue;
    }
    
    /**
     * 添加字段条件
     */
    private String fieldValue;
    
    public String getFieldValue()
    {
        return fieldValue;
    }
    
    public void setFieldValue(String fieldValue)
    {
        this.fieldValue = fieldValue;
    }
    
    
    /**
     *自定义hiveSql
     */
    private String selfSql;
    public String getSelfSql()
    {
        return selfSql;
    }
    
    public void setSelfSql(String selfSql)
    {
        this.selfSql = selfSql;
    }
    
    /**
     * 
     * hive -e sql
     */
    
    private String hiveEsql;
    public String getHiveEsql()
    {
        return hiveEsql;
    }
    
    public void setHiveEsql(String hiveEsql)
    {
        this.hiveEsql = hiveEsql;
    }
    
    /**
     * 输入连接名称
     */
    
    private String repoName;
    public String getRepoName()
    {
        return repoName;
    }
    
    public void setRepoName(String repoName)
    {
        this.repoName = repoName;
    }
    
    
    /**
     * 输出连接名称
     */
    
    private String outRepoName;
    public String getOutRepoName()
    {
        return outRepoName;
    }
    
    public void setOutRepoName(String outRepoName)
    {
        this.outRepoName = outRepoName;
    }
    
    /**
     * 设置连接关系的json字符串
     */
    
    private String assJson;
    
    
    
    public String getAssJson()
    {
        return assJson;
    }
    
    public void setAssJson(String assJson)
    {
        this.assJson = assJson;
    }
    
    /*
     * 设置连接关系字段
     */
    private String mtb;   /*主表*/
    private String dtb;   /*从表*/
    private String logic;    /*join left join*/
    private String conSql;  /*sql*/   
    private String asslogic;  /*and*/
    private String op;      /*=  操作符*/
    
    public FTP4MR getMr()
    {
        return mr;
    }
    
    public void setMr(FTP4MR mr)
    {
        this.mr = mr;
    }
    
    public String getMtb()
    {
        return mtb;
    }
    
    public void setMtb(String mtb)
    {
        this.mtb = mtb;
    }
    
    public String getDtb()
    {
        return dtb;
    }
    
    public void setDtb(String dtb)
    {
        this.dtb = dtb;
    }
    
    public String getLogic()
    {
        return logic;
    }
    
    public void setLogic(String logic)
    {
        this.logic = logic;
    }
    
    public String getOp()
    {
        return op;
    }
    
    public void setOp(String op)
    {
        this.op = op;
    }
    
    public String getConSql()
    {
        return conSql;
    }
    
    public void setConSql(String conSql)
    {
        this.conSql = conSql;
    }
    
    public String getAsslogic()
    {
        return asslogic;
    }
    
    public void setAsslogic(String asslogic)
    {
        this.asslogic = asslogic;
    }
    
    /**
     *sql语句的表list
     */
    private List<InParam> flist  ;
    
    
    
    public List<InParam> getFlist()
    {
        return flist;
    }
    
    public void setFlist(List<InParam> flist)
    {
        this.flist = flist;
    }
    
    
    
    /**
     * 输入资源库
     */
    private List<Repositories> inputResources;
    
    public List<Repositories> getInputResources()
    {
        return inputResources;
    }
    
    public void setInputResources(List<Repositories> inputResources)
    {
        this.inputResources = inputResources;
    }
    
    
    /**
     * 任务分组列表
     */
    private List<ComputeTaskGroup> taskGroups;
    
    public List<ComputeTaskGroup> getTaskGroups()
    {
        return taskGroups;
    }
    
    public void setTaskGroups(List<ComputeTaskGroup> taskGroups)
    {
        this.taskGroups = taskGroups;
    }
    
    
    
    private String tmplist;
    
    private String otable;

    public String getTmplist()
    {
        return tmplist;
    }

    public void setTmplist(String tmplist)
    {
        this.tmplist = tmplist;
    }

    public String getOtable()
    {
        return otable;
    }

    public void setOtable(String otable)
    {
        this.otable = otable;
    }
    
}
