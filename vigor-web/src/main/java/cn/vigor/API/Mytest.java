package cn.vigor.API;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.common.collect.Lists;

import cn.vigor.modules.meta.entity.MetaStorePro;

public class Mytest {

	public static void main(String[] args) throws Exception {
	    String tableName="testkiss22"; 
	    String external="testFamily22"; 
        String zkConn="172.18.84.67:2181,172.18.84.68:2181,172.18.84.69:2181"; 
        String mappingName="kiss002";
        List<MetaStorePro> metaStoreProList=Lists.newArrayList();
       
        MetaStorePro pro1=new MetaStorePro();
        pro1.setProName("id");
        pro1.setProType("int");
        pro1.setType(3);
        metaStoreProList.add(pro1);
        MetaStorePro pro2=new MetaStorePro();
        pro2.setProName("name");
        pro2.setProType("string");
        metaStoreProList.add(pro2);
        boolean op =true;
	    HBaseAdmin hBaseAdmin = null;
        try
        {
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.property.clientPort", "2181");
            configuration.set("hbase.zookeeper.quorum", zkConn);
            hBaseAdmin = new HBaseAdmin(configuration);
            if (hBaseAdmin.tableExists(tableName) )
            {// 
                hBaseAdmin.disableTable(tableName);
                hBaseAdmin.deleteTable(tableName);
            }
            if(op)
            {
                @SuppressWarnings("deprecation")
                HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
                tableDescriptor.addFamily(new HColumnDescriptor(external));
                hBaseAdmin.createTable(tableDescriptor); 
                if (mappingName!=null)
                {
                    @SuppressWarnings("resource")
                    HTable htable = new HTable(configuration, "pentaho_mappings");
                    for (MetaStorePro metaStorePro : metaStoreProList)
                    {
                        /**
                         * String qualifier = vm.getColumnFamily() + HBaseValueMeta.SEPARATOR vm.getColumnName() + HBaseValueMeta.SEPARATOR + alias;
                         */
                        if(metaStorePro.getType()==3)
                        {
                            Put put = new Put(Bytes.toBytes(tableName+","+mappingName));
                            String qualifier =metaStorePro.getProName();
                            String family="key";
                            String type=metaStorePro.getProType();
                            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(type));
                            htable.put(put);
                        }else{
                            Put put = new Put(Bytes.toBytes(tableName+","+mappingName));
                            String qualifier =external+","+metaStorePro.getProName()+","+metaStorePro.getProName();
                            String family="columns";
                            String type=metaStorePro.getProType();
                            put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(type));
                            htable.put(put);
                        }
                        
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        finally
        {
            if (hBaseAdmin != null)
            {
                hBaseAdmin.close();
            }
        }
	}

}
