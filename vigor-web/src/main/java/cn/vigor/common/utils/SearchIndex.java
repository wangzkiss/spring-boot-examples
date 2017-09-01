package cn.vigor.common.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import cn.vigor.modules.tji.entity.JobLog;
import net.sf.json.JSONArray;


public class SearchIndex
{
    public final static String CLUSTERNAME = "yhm-elk";
    
    private String indexName = "logstash-xdata*";
    
    private String esAddress;
    
    private int esPort;
    
    public DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private SortOrder sortOrder = SortOrder.ASC;
    
    public static TransportClient client = null;
    
    private int pageSize = 100;
    
    private int pageNo = 1;
    
    public SearchIndex(){
    }
    
    public SearchIndex(String esAddress){
        this.esAddress = esAddress;
    }
    
    public SearchIndex(String esAddress, int esPort)
    {
        this.esAddress = esAddress;
        this.esPort = esPort;
    }
    
    public SearchIndex(String esAddress, int esPort, int pageSize, int pageNo){
        this.esAddress = esAddress;
        this.esPort = esPort;
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }
    
    public long getJobLogCount(JobLog jobLog){
        long count = 0;
        try {
            if(client == null){
                buildClientByTransport();              
            }
            QueryBuilder queryBuilder = buildQuery(jobLog);
            SearchResponse countresponse = client.prepareSearch(indexName)
                    .setQuery(queryBuilder).setSize(0)
                    .execute()
                    .actionGet();
            count = countresponse.getHits().totalHits();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    
    public String getJobLogs(String trackId)
    {
        List<String> list = new ArrayList<String>();
        
        try
        {
            if (client == null)
            {
                buildClientByTransport();
            }
            
            QueryBuilder queryBuilder = buildQuery(trackId);
            SearchResponse response = client.prepareSearch(indexName)
                    .setQuery(queryBuilder)
                    .addSort("datetime.keyword", sortOrder)
                    .setFrom(0)
                    .setSize(500)
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();
            
            if (response != null)
            {
                for (SearchHit hit : response.getHits().getHits())
                {
                    if (hit.getSource() != null && hit.getSource().size() > 0)
                    {
                        Map<String, Object> hitMap = hit.getSource();
                        
                        list.add(hitMap.get("datetime") + " "
                                + hitMap.get("message"));
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return JSONArray.fromObject(list).toString();
    }
    
    public List<JobLog> getJobLogs(JobLog jobLog){
        List<JobLog> list = new ArrayList<JobLog>();
        try {
            if(client == null){
                buildClientByTransport();              
            }
            QueryBuilder queryBuilder = buildQuery(jobLog);
            SortOrder sortOrder = SortOrder.ASC;
            SearchResponse response = client.prepareSearch(indexName)
                    .setQuery(queryBuilder)
                    .addSort("@timestamp",sortOrder)
                    .setFrom((pageNo-1)*pageSize)
                    .setSize(pageSize)
                    .setScroll(new TimeValue(60000))
                    .execute()
                    .actionGet();
            if (response != null) {
                for (SearchHit hit : response.getHits().getHits()) {
                    if(hit.getSource() != null && hit.getSource().size() > 0){
                        JobLog log = new JobLog();
                        if(hit.getSource().get("message") != null){
                            log.setLogMessage(hit.getSource().get("message").toString());                           
                        }
                        if(hit.getSource().get("jobName") != null){
                            log.setJobName(hit.getSource().get("jobName").toString());                          
                        }
                        if(hit.getSource().get("jobId") != null){
                            log.setJobId(Integer.parseInt(hit.getSource().get("jobId").toString()));                            
                        }
                        if(hit.getSource().get("trackId") != null){
                            log.setTrackId(Integer.parseInt(hit.getSource().get("trackId").toString()));                            
                        }
                        if(hit.getSource().get("datetime") != null){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = hit.getSource().get("datetime").toString();
                            log.setLogTime(sdf.parse(date));                     
                        }
                        if(hit.getSource().get("type") != null){
                            log.setSysName(hit.getSource().get("type").toString());                         
                        }
                        list.add(log);
                    }
                }
            }           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /* slow */
    @SuppressWarnings("resource")
    public void buildClientByTransport() throws UnknownHostException
    {
        Settings settings = Settings.builder()
                .put("cluster.name", CLUSTERNAME)
                .build();
        client = new PreBuiltTransportClient(settings).addTransportAddress(
                new InetSocketTransportAddress(InetAddress.getByName(esAddress),
                        esPort));
    }
    
    private QueryBuilder buildQuery(String trackId)
    {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("trackId", trackId));
        
        return queryBuilder;
    }
    
    private QueryBuilder buildQuery(JobLog jobLog) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if(jobLog != null){
            if(!StringUtils.isEmpty(jobLog.getJobName())){
                queryBuilder.must(new QueryStringQueryBuilder(jobLog.getJobName()).field("jobName"));        
            }
            if(jobLog.getJobId()!=null && jobLog.getJobId() > 0){
                queryBuilder.must(new QueryStringQueryBuilder(jobLog.getJobId() + "").field("jobId"));       
            }
            if(jobLog.getTrackId()!=null && jobLog.getTrackId() > 0){
                queryBuilder.must(new QueryStringQueryBuilder(jobLog.getTrackId() + "").field("trackId"));        
            }
            if(!StringUtils.isEmpty(jobLog.getSysName())){
                queryBuilder.must(QueryBuilders.termQuery("type", jobLog.getSysName()));          
            }           
        }
        return queryBuilder;
    }
    
    public void close()
    {
        if (client != null)
        {
            client.close();
            client = null;
        }
    }
    
    public static void main(String[] args) throws Exception
    {
        SearchIndex search = new SearchIndex("172.18.84.66", 9300);
        String str = search.getJobLogs("9139");
        System.out.println(StringEscapeUtils.unescapeHtml(str));
    }
}