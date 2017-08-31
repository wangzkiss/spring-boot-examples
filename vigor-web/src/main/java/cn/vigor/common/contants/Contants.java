package cn.vigor.common.contants;

/**
 * 常量类
 * @author zhangfeng
 *
 */
public class Contants
{
    /*-------------外部数据源类型-----------------*/
    public static final int SOURCE_TYPE_FTP = 2;
    
    public static final int SOURCE_TYPE_HDFS = 3;
    
    public static final int SOURCE_TYPE_HBASE = 4;
    
    public static final int SOURCE_TYPE_HIVE = 5;
    
    public static final int SOURCE_TYPE_MYSQL = 6;
    
    public static final int SOURCE_TYPE_ORACLE = 7;
    
    public static final int SOURCE_TYPE_SQLSERVER = 10;
    
    /*-------------平台存储类型-----------------*/
    public static final int STORE_TYPE_HDFS = 3;
    
    public static final int STORE_TYPE_HBASE = 4;
    
    public static final int STORE_TYPE_HIVE = 5;
    
    public static final int STORE_TYPE_MYSQL = 6;
    
    public static final int STORE_TYPE_ORACLE = 7;
    
    public static final int STORE_TYPE_SQLSERVER = 10;
    
    public static final int STORE_TYPE_TRAFODION = 11;
    
    /*-------------任务类型-----------------*/
    public static final int TASK_TYPE_ETL = 1;
    
    public static final int TASK_TYPE_ETL_TMP = 2;
    
    public static final int TASK_TYPE_HIVE = 3;
    
    public static final int TASK_TYPE_SPARK = 4;
    
    public static final int TASK_TYPE_SPARKZDY = 5;
    
    public static final int TASK_TYPE_MR = 6;
    
    public static final int TASK_TYPE_SJZL = 7;
    
    public static final int TASK_TYPE_PROCEDURE = 8;
    
    public static final int TASK_TYPE_ACT = 9;
    
    public static final int TASK_TYPE_TRAFODION = 10;
    
    public static final int TASK_TYPE_SHELL = 11;
    
    public static final int TASK_TYPE_KYLIN = 12;
    
    public static final int TASK_TYPE_FLUME = 13;
    
    /*-----------------结果集类型------------------*/
    
    public static final int RESULT_TYPE_MYSQL = 6;
    
    public static final int RESULT_TYPE_FTP = 2;
    
    public static final int RESULT_TYPE_HIVE = 5;
    
    public static final int RESULT_TYPE_TRAFODION = 11;
    
    
    /*-----------------函数类型------------------*/
    
    public static final int HIVE_FUNCTION_TYPE = 2;
	public static final int TRAFODION_FUNCTION_TYPE=7;
    
    
    /**
     *  0为外部数据源 ，1，为平台存储， 2为结果集
     */
    public static final int SOURCE = 0; 
    
    /**
     *  0为外部数据源 ，1，为平台存储， 2为结果集
     */
    public static final int STORE = 1;
    
    /**
     *  0为外部数据源 ，1，为平台存储， 2为结果集
     */
    public static final int RESULT = 2;
    
    
}
