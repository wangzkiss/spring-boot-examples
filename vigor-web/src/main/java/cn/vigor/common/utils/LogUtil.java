package cn.vigor.common.utils;

import org.apache.log4j.Logger;

/**
 * 日志，统一日志输出规范
 * @author  zhangfeng
 */
public class LogUtil
{
    private Logger log;
    
    private static LogUtil logUtil;
    
    //使用空格分隔message关键字段信息
    private static final String SPLIT_KEY = " ";
    
    private static final String JOBNAME_NULL_VALUE = "-1";
    
    private static final int JOBID_NULL_VALUE = -1;
    
    private static final int TRACKID_NULL_VALUE = -1;
    
    public LogUtil(Class<?> clazz)
    {
        log = Logger.getLogger(clazz);
    }
    
    public static LogUtil getInstance(Class<?> clazz)
    {
        if (logUtil == null)
        {
            logUtil = new LogUtil(clazz);
        }
        return logUtil;
        
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param trackId
     * @param message
     * @param t
     */
    public void debug(String jobName, int jobId, int trackId, Object message,
            Throwable t)
    {
        String information = jobName + SPLIT_KEY + jobId + SPLIT_KEY + trackId
                + SPLIT_KEY + message;
        log.debug(information, t);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param trackId
     * @param message
     */
    public void debug(String jobName, int jobId, int trackId, Object message)
    {
        this.debug(jobName, jobId, trackId, message, null);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param message
     */
    public void debug(String jobName, int jobId, Object message)
    {
        this.debug(jobName, jobId, TRACKID_NULL_VALUE, message, null);
    }
    
    /**
     * 
     * @param jobName
     * @param message
     */
    public void debug(String jobName, Object message)
    {
        this.debug(jobName,
                JOBID_NULL_VALUE,
                TRACKID_NULL_VALUE,
                message,
                null);
    }
    
    /**
     * 
     * @param jobName
     * @param message
     */
    public void debug(Object message)
    {
        this.debug(JOBNAME_NULL_VALUE,
                JOBID_NULL_VALUE,
                TRACKID_NULL_VALUE,
                message,
                null);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param trackId
     * @param message
     * @param t
     */
    public void info(String jobName, int jobId, int trackId, Object message,
            Throwable t)
    {
        String information = jobName + SPLIT_KEY + jobId + SPLIT_KEY + trackId
                + SPLIT_KEY + message;
        log.info(information, t);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param trackId
     * @param message
     */
    public void info(String jobName, int jobId, int trackId, Object message)
    {
        this.info(jobName, jobId, trackId, message, null);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param message
     */
    public void info(String jobName, int jobId, Object message)
    {
        this.info(jobName, jobId, TRACKID_NULL_VALUE, message, null);
    }
    
    /**
     * 
     * @param jobName
     * @param message
     */
    public void info(String jobName, Object message)
    {
        this.info(jobName, JOBID_NULL_VALUE, TRACKID_NULL_VALUE, message, null);
    }
    
    /**
     * 
     * @param jobName
     * @param message
     */
    public void info(Object message)
    {
        this.info(JOBNAME_NULL_VALUE,
                JOBID_NULL_VALUE,
                TRACKID_NULL_VALUE,
                message,
                null);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param trackId
     * @param message
     * @param t
     */
    public void error(String jobName, int jobId, int trackId, Object message,
            Throwable t)
    {
        String information = jobName + SPLIT_KEY + jobId + SPLIT_KEY + trackId
                + SPLIT_KEY + message;
        log.error(information, t);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param trackId
     * @param message
     */
    public void error(String jobName, int jobId, int trackId, Object message)
    {
        this.error(jobName, jobId, trackId, message, null);
    }
    
    /**
     * 
     * @param jobName
     * @param jobId
     * @param message
     */
    public void error(String jobName, int jobId, Object message)
    {
        this.error(jobName, jobId, TRACKID_NULL_VALUE, message, null);
    }
    
    /**
     * 
     * @param jobName
     * @param message
     */
    public void error(String jobName, Object message)
    {
        this.error(jobName,
                JOBID_NULL_VALUE,
                TRACKID_NULL_VALUE,
                message,
                null);
    }
    
    /**
     * 
     * @param jobName
     * @param message
     */
    public void error(Object message)
    {
        this.error(JOBNAME_NULL_VALUE,
                JOBID_NULL_VALUE,
                TRACKID_NULL_VALUE,
                message,
                null);
    }
    
}
