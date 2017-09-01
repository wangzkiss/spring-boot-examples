package cn.vigor.modules.meta.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.kylin.jdbc.Driver;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.vigor.common.config.Global;
import cn.vigor.common.utils.AESUtil;
import cn.vigor.common.utils.SpringContextHolder;
import cn.vigor.modules.meta.entity.MetaRepo;
import cn.vigor.modules.meta.entity.MetaResult;
import cn.vigor.modules.meta.entity.MetaResultPro;
import cn.vigor.modules.meta.entity.MetaSource;
import cn.vigor.modules.meta.entity.MetaSourcePro;
import cn.vigor.modules.meta.entity.MetaStore;
import cn.vigor.modules.meta.entity.MetaStorePro;
import cn.vigor.modules.meta.entity.MetaTable;
import cn.vigor.modules.meta.service.MetaStoreService;
import cn.vigor.modules.tji.util.KylinHttpRequest;

public class DBUtils
{

	private static Logger log = Logger.getLogger(DBUtils.class);

	//org.apache.hadoop.hive.jdbc.HiveDriver
	private static String driverName = "org.apache.hive.jdbc.HiveDriver";

	// Hive2 db driver for spark
	private static String driverName2 = "org.apache.hive.jdbc.HiveDriver";

	private static String dbDriverName = "com.mysql.jdbc.Driver";

	private static String oracleDriverName = "oracle.jdbc.driver.OracleDriver";
	
	public static Connection getHiveConnection(String url, String user,
			String passwd) throws SQLException
			{
		try
		{
			Class.forName(driverName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Class forName driverName:" + driverName + "exception : "
					+ e);
		}
		Connection hiveConnection = DriverManager.getConnection(url,
				user,
				passwd);
		return hiveConnection;
			}

	public static Connection getHiveConnection(String url) throws SQLException
	{
		try
		{
			Class.forName(driverName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Class forName driverName:" + driverName + "exception : "
					+ e);
		}
		Connection hiveConnection = DriverManager.getConnection(url, "", "");
		return hiveConnection;
	}

	public static Connection getHiveConnection2(String url, String user,
			String passwd) throws SQLException
			{
		try
		{
			Class.forName(driverName2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Class forName driverName2:" + driverName2
					+ "exception : " + e);
		}
		DriverManager.setLoginTimeout(5);
		Connection hiveConnection = DriverManager.getConnection(url,
				user,
				passwd);
		return hiveConnection;
			}

	public static Connection getHiveConnection2(String url) throws SQLException
	{
		try
		{
			Class.forName(driverName2);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Class forName driverName2:" + driverName2
					+ "exception : " + e);
		}
		Connection hiveConnection = DriverManager.getConnection(url, "", "");
		return hiveConnection;
	}

	public static Connection getDBConnection(String url, String user,
			String passwd) throws SQLException
			{
		try
		{
			Class.forName(dbDriverName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Class forName dbDriverName:" + dbDriverName
					+ "exception : " + e);
		}
		Connection dbConnection = DriverManager.getConnection(url,
				user,
				passwd);
		return dbConnection;
			}

	public static Connection getOrcleConnection(String url, String user,
			String passwd) throws SQLException
			{
		try
		{
			Class.forName(oracleDriverName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Class forName oracleDriverName:" + oracleDriverName
					+ "exception : " + e);
		}
		Connection dbConnection = DriverManager.getConnection(url,
				user,
				passwd);
		return dbConnection;
			}

	public static Connection getDBConnection(String url) throws SQLException
	{
		try
		{
			Class.forName(dbDriverName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.error("Class forName driverName:" + driverName + "exception : "
					+ e);
		}
		Connection dbConnection = DriverManager.getConnection(url);
		return dbConnection;
	}

	// mysql �õ���ݿ�
	public List<String> getDataBase(String user, String pwd, String port,
			String ip) throws Exception
			{
		String mysqlUrl = "jdbc:mysql://" + ip + ":" + port
				+ "?characterEncoding=UTF-8";
		Connection hiveConnection = getDBConnection(mysqlUrl, user, pwd);
		ResultSet rs = null;
		String sql = "SELECT `SCHEMA_NAME` FROM `information_schema`.`SCHEMATA`";
		List<String> list = new ArrayList<String>();
		try
		{
			PreparedStatement pre = hiveConnection.prepareStatement(sql);
			rs = pre.executeQuery();
			while (rs.next())
			{
				list.add(rs.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
			}

	// oracle �õ���
	public List<String> getOracleDataBase(String user, String pwd, String port,
			String ip, String database) throws Exception
			{
		String oracleUrl = "jdbc:oracle:thin:@" + ip + ":" + port + ":"
				+ database;
		List<String> list = new ArrayList<String>();
		// jdbc:oracle:thin:@192.168.2.66:1521:orcl66
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection OracleConnection = DriverManager.getConnection(oracleUrl,
					user,
					pwd);
			ResultSet rs = null;
			String sql = "select Table_NAME from user_tables";
			PreparedStatement pre = OracleConnection.prepareStatement(sql);
			rs = pre.executeQuery();
			while (rs.next())
			{
				list.add(rs.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Class forName oracleDriverName:" + oracleDriverName
					+ "exception : " + e);
		}
		return list;
			}

	/**
	 * 查询hive数据库的所有表信息
	 * @param user 用户名
	 * @param pwd 密码
	 * @param port 端口
	 * @param ip ip地址
	 * @param database 数据库
	 * @return
	 * @throws Exception
	 */
	public List<MetaTable> getHiveTables(String user, String pwd, String port,
			String ip, String database) throws Exception{

		String hiveUrl = "jdbc:hive2://" + ip + ":" + port + "/"+((database==null||database.equals(""))?"default":database)+"?characterEncoding=UTF-8";
		Connection hiveConnection = getHiveConnection(hiveUrl, user, pwd);
		ResultSet resultSet = null;
		String sql = "show tables";
		List<MetaTable> tables = new ArrayList<MetaTable>();
		try
		{
			PreparedStatement statement = hiveConnection.prepareStatement(sql);
			resultSet = statement.executeQuery();
			int index = 1;
			while(resultSet.next()){
				MetaTable table = new MetaTable();
				table.setTableName(resultSet.getString(1));
				if(resultSet.getString(1).toLowerCase().lastIndexOf("_view")>0){
					table.setType(2);
				}
				table.setIndex(index);
				tables.add(table);
				index++;
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if(hiveConnection != null){
				hiveConnection.close();
			}
		}
		return tables;
	}

	// mysql �õ� ��
	public List<MetaTable> getMysqlTables(String user, String pwd, String port,
			String ip, String database) throws Exception
			{
		String mysqlUrl = "jdbc:mysql://" + ip + ":" + port + "/" + database
				+ "?characterEncoding=UTF-8";
		Connection hiveConnection = getDBConnection(mysqlUrl, user, pwd);
		ResultSet rs = null;
		String sql = "select table_name,table_comment from information_schema.tables where table_schema = '"
				+ database + "'";
		//添加查询视图的sql,因为视图信息中没有table_comment字段,无法直接进行union all,故做单独处理
		//        String mysqlView_sql = "select table_name from information_schema.VIEWS where table_schema = '" + database + "'";
		List<MetaTable> tables = new ArrayList<MetaTable>();
		try
		{
			PreparedStatement pre = hiveConnection.prepareStatement(sql);
			rs = pre.executeQuery();
			int index = 1;
			while (rs.next())
			{
				MetaTable table = new MetaTable();
				table.setTableName(rs.getString(1));
				table.setTableDesc(rs.getString(2));
				if(rs.getString(2).equals("VIEW")){
					table.setType(2);
				}
				table.setIndex(index);
				tables.add(table);
				index++;
			}
			//            //获取mysql视图
			//            PreparedStatement statement = hiveConnection.prepareStatement(mysqlView_sql);
			//            rs = statement.executeQuery();
			//            while(rs.next()){
			//                MetaTable table = new MetaTable();
			//                table.setTableName(rs.getString(1));
			//                table.setIndex(index);
			//                table.setType(2);
			//                tables.add(table);
			//                index++;
			//            }
		}catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if(hiveConnection != null){
				hiveConnection.close();
			}
		}
		return tables;
			}

	// oralce
	public List<MetaTable> getOralceTables(String user, String pwd, String port,
			String ip, String database,String repoInstance) throws Exception
			{
		String oracleUrl = "jdbc:oracle:thin:@" + ip + ":" + port + ":"
				+ database;
		List<MetaTable> tables = new ArrayList<MetaTable>();
		Connection oracleConnection = null;
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			oracleConnection = DriverManager.getConnection(oracleUrl,user,pwd);
			ResultSet rs = null;
			String sql = "select * from all_tab_comments where owner = '" + repoInstance.toUpperCase() + "'";
			PreparedStatement pre = oracleConnection.prepareStatement(sql);
			rs = pre.executeQuery();
			int index = 1;
			while (rs.next())
			{
				MetaTable table = new MetaTable();
				table.setTableName(rs.getString(2));
				if(rs.getString(3).equals("TABLE")){
					table.setType(1);
				}else{
					table.setType(2);
				}
				table.setTableDesc("");
				table.setIndex(index);
				tables.add(table);
				index++;
			}
			//获取视图
			//            String oraView_sql = "select * from user_views";
			//            PreparedStatement statement = oracleConnection.prepareStatement(oraView_sql);
			//            rs = statement.executeQuery();
			//            while (rs.next()){
			//                MetaTable table = new MetaTable();
			//                table.setTableName(rs.getString(1));
			//                table.setTableDesc("");
			//                table.setType(2);
			//                table.setIndex(index);
			//                tables.add(table);
			//                index++;
			//            }
		}catch (SQLException e)
		{
			e.printStackTrace();
		}finally {
			if(oracleConnection != null){
				oracleConnection.close();
			}
		}
		return tables;
			}

	public List<MetaTable> getTrafodionTables(String user, String pwd, String port,
			String ip, String database) throws Exception{
		List<MetaTable> tables = new ArrayList<MetaTable>();
		Class.forName("org.trafodion.jdbc.t4.T4Driver");
		String url = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
		Connection connection = DriverManager.getConnection(url, user, pwd);
		String userDbase = "set schema " + database;
		Statement pre = connection.createStatement();
		boolean flag = pre.execute(userDbase);
		log.info("use " + userDbase + " is : " + flag);
		ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
		ResultSet resultSet2 = connection.getMetaData().getTables(null, null, "%", new String[]{"VIEW"});
		try
		{
			int index = 0;
			while(resultSet.next()) {
				MetaTable table = new MetaTable();
				String tableName = resultSet.getString("TABLE_NAME");
				table.setTableName(tableName);
				table.setIndex(index);
				tables.add(table);
				index++;
			}
			while(resultSet2.next()){
				MetaTable metaTable = new MetaTable();
				String viewName = resultSet2.getString("TABLE_NAME");
				metaTable.setType(2);
				metaTable.setTableName(viewName);
				metaTable.setIndex(index);
				tables.add(metaTable);
				index++;
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if(connection!=null){
				connection.close();
			}
		}
		return tables;
	}

	// SQL SERVER
	public List<MetaTable> getSqlserverTables(String user, String pwd, String port,
			String ip, String database) throws Exception
			{

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String URI = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName="
				+ database;
		Connection conn= DriverManager.getConnection(URI, user, pwd);
		ResultSet rs = null;
		//SELECT name FROM test..SysObjects Where XType='U' ORDER BY Name ;
		String sql = "SELECT name,XType FROM "+database+"..SysObjects Where XType in ('U','V','AF','P') ORDER BY Name";//获取视图和表
		List<MetaTable> tables = new ArrayList<MetaTable>();
		try
		{
			PreparedStatement pre = conn.prepareStatement(sql);
			rs = pre.executeQuery();
			int index = 0;
			while (rs.next())
			{
				MetaTable table = new MetaTable();
				table.setTableName(rs.getString(1));
				String type = rs.getString(2).trim();
				//默认是表U(type=1)
				if(type.equals("V")){//视图
					table.setType(2);
				}else if(type.equals("P")){//存储过程
					table.setType(3);
				}else if(type.equals("AF")){//聚合函数
					table.setType(4);
				}
				table.setTableDesc("");
				table.setIndex(index);
				tables.add(table);
				index++;
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			if(conn!=null){
				conn.close();
			}
		}
		return tables;
			}
	public List<MetaSourcePro> getSqlServerMetaSourcePro(String user, String pwd,
			String port, String ip, String database, String tablename)
					throws Exception
					{
		ResultSet rs = null;
		String sql = "select b.name 字段名,c.name 类型,b.prec 长度,b.isnullable 是否为空,[是否主键]=case when exists(SELECT 1 FROM sysobjects where xtype='PK' and parent_obj=b.id and name in (SELECT name FROM sysindexes WHERE indid in(SELECT indid FROM sysindexkeys WHERE id = b.id AND colid=b.colid))) then '1' else '0' end from sysobjects a,syscolumns b,systypes c where a.id=b.id and a.name='"+tablename+"' and b.xtype=c.xusertype and a.type in ('u','v')";
		List<MetaSourcePro> list = new ArrayList<MetaSourcePro>();
		try
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String URI = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName="
					+ database;
			Connection conn= DriverManager.getConnection(URI, user, pwd);
			PreparedStatement  pre = conn.prepareStatement(sql);
			rs = pre.executeQuery();
			int num = 1;
			while (rs.next())
			{
				MetaSourcePro column = new MetaSourcePro();
				column.setId("");
				column.setProName(rs.getString(1));
				column.setProType(rs.getString(2).trim().toLowerCase());
				column.setProIndex(num);
				column.setColumnSize(rs.getString(3));
				column.setType(rs.getString(5).equals("1")?3:2);
				column.setRemarks("长度："+rs.getString(3));
				list.add(column);
				num++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Class forName oracleDriverName:" + oracleDriverName
					+ "exception : " + e);
		}
		return list;
					}
	/**
	 * mysql �õ��ֶ�
	 * 
	 * @param user 
	 * @param pwd
	 * @param port
	 * @param ip
	 * @param database
	 * @param tablename
	 * @return
	 * @throws Exception
	 */
	public List<MetaSourcePro> getMetaSourcePro(String user, String pwd,
			String port, String ip, String database, String tablename)
					throws Exception
					{
		String mysqlUrl = "jdbc:mysql://" + ip + ":" + port + "/" + database
				+ "?characterEncoding=UTF-8";
		Connection hiveConnection = getDBConnection(mysqlUrl, user, pwd);
		ResultSet rs = null;
		String sql = "select column_name ,column_type,column_comment,column_key,column_default from information_schema.columns where table_name='"
				+ tablename + "' and table_schema='" + database + "'";
		// String sql = "show columns from "+tablename;
		List<MetaSourcePro> list = new ArrayList<MetaSourcePro>();
		try
		{
			PreparedStatement pre = hiveConnection.prepareStatement(sql);
			rs = pre.executeQuery();
			int num = 1;
			while (rs.next())
			{
				MetaSourcePro column = new MetaSourcePro();
				column.setId("");
				column.setProName(rs.getString(1));
				column.setProIndex(num);
				String columnType = rs.getString(2);
				if (columnType.contains("(") && columnType.contains(")"))
				{
					int index = columnType.indexOf("(");
					String attribute = columnType.substring(0, index);
					column.setProType(attribute.toLowerCase());
					column.setColumnSize(columnType.substring(index+1,columnType.indexOf(")")));
				}
				else
				{
					column.setProType(rs.getString(2).trim().toLowerCase());
				}
				column.setRemarks(rs.getString(3));
				String columnKey = rs.getString(4);
				if(columnKey!=null && !columnKey.equals("")){
					if(columnKey.equals("PRI")){
						column.setType(3);
					}else{
						column.setType(2);
					}
				}else{
					column.setType(2);
				}
				list.add(column);
				num++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Class forName oracleDriverName:" + oracleDriverName
					+ "exception : " + e);
		}
		return list;
					}

	/**
	 * 获取hive表结构信息
	 * @param user hive数据库用户名
	 * @param pwd hive数据库密码
	 * @param port 端口
	 * @param ip 地址
	 * @param database 数据库名称
	 * @param tablename 表名
	 * @return
	 * @throws Exception
	 */
	public List<MetaSourcePro> getHiveMetaSourcePro(String user, String pwd,
			String port, String ip, String database, String tablename) throws Exception{
		String hiveUrl = "jdbc:hive2://" + ip + ":" + port + "/" + database + "?characterEncoding=UTF-8";
		Connection hiveConnection = getHiveConnection(hiveUrl, user, pwd);
		ResultSet rs = null;
		String sql = "describe " + tablename;
		List<MetaSourcePro> list = new ArrayList<MetaSourcePro>();
		try
		{
			PreparedStatement pre = hiveConnection.prepareStatement(sql);
			rs = pre.executeQuery();
			int num = 1;
			while (rs.next())
			{
				MetaSourcePro column = new MetaSourcePro();
				if(StringUtils.isEmpty(rs.getString(1))){
					break;
				}
				column.setId("");
				column.setType(2);
				column.setProName(rs.getString(1));
				column.setProIndex(num);
				String columnType = rs.getString(2);
				if (columnType.contains("(") && columnType.contains(")"))
				{
					int index = columnType.indexOf("(");
					String attribute = columnType.substring(0, index);
					column.setProType(attribute.toLowerCase().trim());
					column.setColumnSize(columnType.substring(index+1,columnType.indexOf(")")));
				}
				else
				{
					column.setProType(rs.getString(2).trim().toLowerCase());
				}
				String remark = rs.getString(3);
				//                if(StringUtils.isNotEmpty(remark)){
				//                    remark = new String(remark.getBytes(), "IS8859-1");
				//                }
				column.setRemarks(remark);
				list.add(column);
				num++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Class forName oracleDriverName:" + oracleDriverName
					+ "exception : " + e);
		}
		return list;
	}

	/**
	 * 获取trafodion表的字段信息
	 * @param user trafodion用户名
	 * @param pwd trafodion密码
	 * @param port trafodion端口
	 * @param ip trafodion的ip
	 * @param database trafodion的数据库名 
	 * @param tablename trafodion的表名
	 * @return
	 * @throws Exception
	 */
	public List<MetaSourcePro> getTrafodionMetaSourcePro(String user, String pwd,
			String port, String ip, String database, String tablename) throws Exception{
		List<MetaSourcePro> list = new ArrayList<MetaSourcePro>();
		Class.forName("org.trafodion.jdbc.t4.T4Driver");
		String url = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
		Connection connection = DriverManager.getConnection(url, user, pwd);
		String userDbase = "set schema " + database;
		Statement pre = connection.createStatement();
		boolean flag = pre.execute(userDbase);
		log.info("use " + userDbase + " is : " + flag);
		ResultSet resultSet = connection.getMetaData().getColumns(null, null, tablename, "%");
		int index = 0;
		while(resultSet.next()){
			MetaSourcePro metaSourcePro = new MetaSourcePro();
			String name = resultSet.getString("COLUMN_NAME");
			String remark = resultSet.getString("REMARKS");
			String type = resultSet.getString("TYPE_NAME");
			String columnSize = resultSet.getString("COLUMN_SIZE");
			metaSourcePro.setId("");
			metaSourcePro.setProName(name);
			metaSourcePro.setProType(type);
			metaSourcePro.setColumnSize(columnSize);
			metaSourcePro.setRemarks(remark);
			metaSourcePro.setProIndex(index);
			index++;
			list.add(metaSourcePro);
		}
		return list;
	}


	// oracle �õ��ֶ�
	public List<MetaSourcePro> getOracleMetaSourcePro(String user, String pwd,
			String port, String ip, String database, String tablename,String repoInstance)
					throws Exception
					{
		String oracleUrl = "jdbc:oracle:thin:@" + ip + ":" + port + ":"
				+ database;
		ResultSet rs = null;
		String sql = "select * from all_tab_columns where TABLE_NAME = '" + tablename + "' and owner = '" + repoInstance.toUpperCase() + "'";
		List<MetaSourcePro> list = new ArrayList<MetaSourcePro>();
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			Connection OracleConnection = DriverManager.getConnection(oracleUrl,
					user,pwd);
			//查询主键
			String sql_p = "select cu.COLUMN_NAME from user_cons_columns cu, user_constraints au where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' AND cu.table_name = '"+tablename+"'";
			PreparedStatement pre1 = OracleConnection.prepareStatement(sql_p);
			ResultSet rs1 = pre1.executeQuery();
			List<String> ps = new ArrayList<String>();
			while (rs1.next())
			{
				ps.add(rs1.getString(1));
			}

			PreparedStatement pre = OracleConnection.prepareStatement(sql);
			rs = pre.executeQuery();

			int num = 1;
			while (rs.next())
			{
				MetaSourcePro column = new MetaSourcePro();
				column.setId("");
				column.setProName(rs.getString(3));
				column.setProIndex(num);
				column.setColumnSize(rs.getString(7));
				column.setProType(rs.getString(4));
				if(ps.size()>0 && ps.contains(rs.getString(3))){
					column.setType(3);
				}else{
					column.setType(2);
				}
				column.setRemarks("");
				list.add(column);
				num++;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			log.error("Class forName oracleDriverName:" + oracleDriverName
					+ "exception : " + e);
		}
		return list;
					}

	/**
	 * 
	 * @param repo ������Ϣ
	 * @param result 
	 * @throws Exception
	 */
	public static void excuteOracleSql(MetaRepo repo, MetaStore result,
			boolean op,Connection oracleConnection) throws Exception
			{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		String port = repo.getPort().toString();
		String dataBase = repo.getRepoName();

		String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dataBase;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		boolean f = false;
		if(oracleConnection==null){
			f = true;
			oracleConnection = DriverManager.getConnection(url,user,pwd);
		}
		Statement preparedStatement = oracleConnection.createStatement();
		if (op)
		{
			/* String sql = "create database if not exists " + dataBase;
             boolean flag = preparedStatement.execute(sql);
             log.info("create " + dataBase + " is : " + flag);
			 */
			if (result != null)
			{
				String tableName = result.getStoreFile();
				/* String userDbase = "use " + dataBase;
                boolean flag1 = preparedStatement.execute(userDbase);
                log.info("use " + dataBase + " is : " + flag1);*/

				String createTable = "create table  " + tableName
						+ "(";
				//boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				for (MetaStorePro colum : result.getMetaStoreProList())
				{
					String name = colum.getProName();
					String type = colum.getProType();
					String columnSize = colum.getColumnSize();
					if(cn.vigor.common.utils.StringUtils.isNotEmpty(columnSize)){
						type = type + "("+columnSize+")";
					}
					//                    type = type.toLowerCase().contains("varchar") ? "varchar(64)" : type;
					clms = clms + "\"" + name.toUpperCase() + "\" " + " " + type + " " + null + ",";
				}
				clms = clms.substring(0, clms.lastIndexOf(","));// ȥ�����һ��','��
				createTable = createTable + clms
						+ ")";
				log.info("sql  is : " + createTable);
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}
		}
		else
		{
			if (result == null)
			{
				/* String deleteBase = "drop database if exists " + dataBase;
                 boolean flag = preparedStatement.execute(deleteBase);
                 log.info("delete " + dataBase + " is : " + flag);*/
			}
			else
			{
				/* String userDbase = "use " + dataBase;
                 boolean flag = preparedStatement.execute(userDbase);
                 log.info("use " + dataBase + " is : " + flag);*/

				String deleteTable = "drop table "
						+ result.getStoreFile();
				boolean flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}

		preparedStatement.close();
		if(f){
			oracleConnection.close();
		}
			}
	/**
	 * 
	 * @param repo ������Ϣ
	 * @param result 
	 * @throws Exception
	 */
	public static void excuteOracleSql(MetaRepo repo, MetaResult result,
			boolean op) throws Exception
			{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		String port = repo.getPort().toString();
		String dataBase = repo.getRepoName();

		String url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dataBase;
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection OracleConnection = DriverManager.getConnection(url,
				user,
				pwd);
		Statement preparedStatement = OracleConnection.createStatement();
		if (op)
		{
			/*String sql = "create database if not exists " + dataBase;
            boolean flag = preparedStatement.execute(sql);
            log.info("create " + dataBase + " is : " + flag);*/

			if(result!=null){
				String tableName = result.getResultFile();
				/*  String tablesDesc = result.getResultDesc();
            String userDbase = "use " + dataBase;
            boolean flag1 = preparedStatement.execute(userDbase);
            log.info("use " + dataBase + " is : " + flag1);*/

				String createTable = "create table   " + tableName
						+ "(";
				// boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				//            String desc = "";// COMMENT 'asdad'
				for (MetaResultPro colum : result.getMetaResultProList())
				{
					String name = colum.getProName();
					String type = colum.getProType();
					//                desc = colum.getProDesc() != null
					//                        ? "comment '" + colum.getProDesc() + "'" : "";
					String columnSize = colum.getColumnSize();
					if(cn.vigor.common.utils.StringUtils.isNotEmpty(columnSize)){
						type = type + "("+columnSize+")";
					}
					//                type = type.toLowerCase().contains("varchar") ? "varchar(64)"
					//                        : type;
					clms = clms + "\"" + name.toUpperCase() + "\" " + " " + type + " " + null + " ,";
				}
				clms = clms.substring(0, clms.lastIndexOf(","));// ȥ�����һ��','��
				createTable = createTable + clms
						+ ")";
				log.info("sql  is : " + createTable);
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}
		} else {
			if (result == null)
			{
				/*String deleteBase = "drop database if exists " + dataBase;
                boolean flag = preparedStatement.execute(deleteBase);
                log.info("delete " + dataBase + " is : " + flag);*/
			}
			else
			{
				/*String userDbase = "use " + dataBase;
                boolean flag = preparedStatement.execute(userDbase);
                log.info("use " + dataBase + " is : " + flag);*/

				String deleteTable = "drop table"
						+ result.getResultFile();
				boolean flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}

		preparedStatement.close();
			}
	/**
	 * 
	 * @param repo ������Ϣ
	 * @param result 
	 * @throws Exception
	 */
	public static void excuteMysqlSql(MetaRepo repo, MetaStore result,
			boolean op,Connection connection) throws Exception
			{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		String port = repo.getPort().toString();
		String dataBase = repo.getRepoName();
		String url = "jdbc:mysql://" + ip + ":" + port + "/information_schema?characterEncoding=UTF-8";
		//如果是取消启用,这时候我们直接连接dataBase
		if(!op){
			url = "jdbc:mysql://" + ip + ":" + port + "/" + dataBase + "?characterEncoding=UTF-8";
		}
		boolean f = false;
		if(connection==null){
			f = true;
			connection = getDBConnection(url, user, pwd);
		}
		String sql = "create database if not exists " + dataBase;
		Statement preparedStatement = connection.createStatement();
		if (op)
		{
			boolean flag = preparedStatement.execute(sql);
			log.info("create " + dataBase + " is : " + flag);
			if (result != null)
			{
				//TODO 判断数据库表是否已经启用过了,如果该表已经和其他存储信息关联,该当前需要启用的存储信息表无法启用,返回页面提示信息
				String tableName =result.getStoreFile();
				String tablesDesc = result.getStoreDesc();
				String userDbase = "use " + dataBase;
				boolean flag1 = preparedStatement.execute(userDbase);
				preparedStatement.close();
				log.info("use " + dataBase + " is : " + flag1);
				log.info("check table" + tableName + "is exist or not.");
				String qsql = "select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='"+dataBase+"' and TABLE_NAME='"+tableName+"'";
				if(preparedStatement.isClosed()){
					preparedStatement = connection.createStatement();
				}
				ResultSet  resultSet = preparedStatement.executeQuery(qsql);
				if(resultSet.next()){
					return;
				}
				preparedStatement.close();
				String createTable = "create table if not exists " + tableName + "(";
				// boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				String desc = "";// COMMENT 'asdad'
				String pk = "";
				for (MetaStorePro colum : result.getMetaStoreProList())
				{
					int type = colum.getType();//类型
					String name = colum.getProName();
					String protype = colum.getProType();//字段数据类型
					String columnSize = colum.getColumnSize();
					if(cn.vigor.common.utils.StringUtils.isNotEmpty(columnSize)){
						protype = protype + "("+columnSize+")";
					}else{
						//如果字段类型为varchar,长度为空,则赋值默认长度64
						if(protype.toLowerCase().contains("char")){
							protype = protype + "(64)";
						}
					}
					desc = colum.getProDesc() != null
							? "comment '" + colum.getProDesc() + "'" : "";
					//                    type = type.toLowerCase().contains("char") || type.toLowerCase().contains("string")
					//                            ? "varchar(64)" : type;
					if(type==3){//主键
						pk = pk + "`" + name + "`,";
						clms = clms + "`" + name + "` " + protype + " not null " + desc + ",";
					}else{
						clms = clms + "`" + name + "` " + protype + " null " + desc + ",";
					}
				}
				//判断该建表语句是否有主键字段
				if(!pk.equals("")){
					pk = "PRIMARY KEY (" + pk.substring(0, pk.lastIndexOf(",")) + ")";
					clms = clms + pk;
				}else{
					clms = clms.substring(0, clms.lastIndexOf(","));//去掉最后一个逗号
				}
				/*  clms = clms + "track_id" +  " int  null comment '批次id' ,";*/
				createTable = createTable + clms
						+ ") default charset=utf8 comment='" + tablesDesc + "'";
				log.info("sql  is : " + createTable);
				if(preparedStatement.isClosed()){
					preparedStatement = connection.createStatement();
				}
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}

		}
		else
		{
			//        	String userDbase = "use " + dataBase;
			//            boolean flag = preparedStatement.execute(userDbase);
			//            preparedStatement.close();
			//            log.info("use " + dataBase + " is : " + flag);
			if (result == null)
			{
				//判断该库下是否存在表,如果存在,则不drop该库
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				while(resultSet.next()) {
					return;
				}
				String deleteBase = "drop database if exists " + dataBase;
				boolean flag = preparedStatement.execute(deleteBase);
				log.info("delete " + dataBase + " is : " + flag);
			}
			else
			{
				String deleteTable = "drop table if exists " + result.getStoreFile();
				boolean flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}
		preparedStatement.close();
		if(f){
			connection.close();
		}

			}
	/**
	 * 
	 * @param repo ������Ϣ
	 * @param result 
	 * @throws Exception
	 */
	public static void excuteMysqlSql(MetaRepo repo, MetaResult result,
			boolean op) throws Exception
			{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		String port = repo.getPort().toString();
		String dataBase = repo.getRepoName();
		String url = "jdbc:mysql://" + ip + ":" + port + "/information_schema?characterEncoding=UTF-8";
		//如果是取消启用,则直接连接dataBase
		if(!op){
			url = "jdbc:mysql://" + ip + ":" + port + "/" + dataBase + "?characterEncoding=UTF-8";
		}
		Connection connection = getDBConnection(url, user, pwd);
		String sql = "create database if not exists " + dataBase;
		Statement preparedStatement = connection.createStatement();
		if (op)
		{
			boolean flag = preparedStatement.execute(sql);
			log.info("create " + dataBase + " is : " + flag);
			if (result != null)
			{
				String tableName = result.getResultFile();
				String tablesDesc = result.getResultDesc();
				String userDbase = "use " + dataBase;
				boolean flag1 = preparedStatement.execute(userDbase);
				log.info("use " + dataBase + " is : " + flag1);
				//TODO 判断表是否已经启用
				log.info("check table" + tableName + "is exist or not.");
				String qsql = "select TABLE_NAME from INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='"+dataBase+"' and TABLE_NAME='"+tableName+"'";
				ResultSet  resultSet = preparedStatement.executeQuery(qsql);
				if(resultSet.next()){
					return;
				}
				String createTable = "create table if not exists " + tableName
						+ "(";
				// boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				String desc = "";// COMMENT 'asdad'
				String pk = "";
				//                clms = clms + "track_id" +  " int  null comment '批次id' ,";
				for (MetaResultPro colum : result.getMetaResultProList())
				{
					int type = colum.getType();//类型
					String name = colum.getProName();
					String protype = colum.getProType();
					String columnSize = colum.getColumnSize();
					//结果集的track_id一律放在表的末尾,之前mysql的是放在第一个字段的位置,为了保证
					if(cn.vigor.common.utils.StringUtils.isNotEmpty(columnSize)){
						protype = protype + "("+columnSize+")";
					}else{
						//如果字段类型为varchar,长度为空,则赋值默认长度64
						if(protype.toLowerCase().contains("char")){
							protype = protype + "(64)";
						}
					}
					desc = colum.getProDesc() != null
							? "comment '" + colum.getProDesc() + "'" : "";
					//                    type = type.toLowerCase().contains("char") || type.toLowerCase().contains("string")
					//                            ? "varchar(64)" : type;
					if(type==3){//主键
						pk = pk + "`" + name + "`,";
						clms = clms + "`" + name + "` " + protype + " not null " + desc + ",";
					}else{
						clms = clms + "`" + name + "` " + protype + " null " + desc + ",";
					}
				}
				//                clms = clms.substring(0, clms.lastIndexOf(","));//去掉最后一个逗号
				//判断该建表语句是否有主键字段
				if(!pk.equals("")){
					pk = "PRIMARY KEY (" + pk.substring(0, pk.lastIndexOf(",")) + ")";
					clms = clms + pk;
				}else{
					clms = clms.substring(0, clms.lastIndexOf(","));//去掉最后一个逗号
				}
				createTable = createTable + clms
						+ ") default charset=utf8 comment='" + tablesDesc + "'";
				log.info("sql  is : " + createTable);
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}

		}
		else
		{
			if (result == null)
			{
				//判断该库下是否存在表,如果存在,则不drop该库
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				while(resultSet.next()) {
					return;
				}
				String deleteBase = "drop database if exists " + dataBase;
				boolean flag = preparedStatement.execute(deleteBase);
				log.info("delete " + dataBase + " is : " + flag);
			}
			else
			{
				//                String userDbase = "use " + dataBase;
				//                boolean flag = preparedStatement.execute(userDbase);
				//                log.info("use " + dataBase + " is : " + flag);
				String deleteTable = "drop table if exists "
						+ result.getResultFile();
				boolean flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}
		preparedStatement.close();

			}

	public static void excuteSqlServer(MetaRepo repo, MetaStore result,
			boolean op,Connection connection) throws Exception
			{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		String port = repo.getPort().toString();
		String dataBase = repo.getRepoName();
		//private static final String DRIVER="com.mysql.jdbc.Driver";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String URI  = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName=master";
		if(!op){
			URI  = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName=" + dataBase;
		}
		boolean f = false;
		if(connection==null){
			f = true;
			connection = DriverManager.getConnection(URI, user, pwd);
		}
		Statement preparedStatement = connection.createStatement();
		if (op)
		{
			if(result==null){
				//判断数据库是否存在
				boolean bol = checkSqlserverDatabase(dataBase,preparedStatement);
				if(!bol){
					String sql = "create database " + dataBase;
					boolean flag = preparedStatement.execute(sql);
					log.info("create " + dataBase + " is : " + flag);
				}
			}
			if (result != null)
			{
				//判断数据库是否存在
				boolean bol = checkSqlserverDatabase(dataBase,preparedStatement);
				if(!bol){
					String sql = "create database " + dataBase;
					boolean flag = preparedStatement.execute(sql);
					log.info("create " + dataBase + " is : " + flag);
				}
				String tableName = result.getStoreFile();
				String userDbase = "use " + dataBase;
				boolean flag1 = preparedStatement.execute(userDbase);
				preparedStatement.close();
				log.info("use " + dataBase + " is : " + flag1);
				String createTable = "create table  " + tableName
						+ "(";
				// boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				for (MetaStorePro colum : result.getMetaStoreProList())
				{
					String name = colum.getProName();
					String type = colum.getProType();
					String columnSize = colum.getColumnSize();
					//sqlserver在建表时,int,tinyint,smallint,bigint等整型类型时,不能指定其长度,即使我们通过源抽取过来的字段带长度
					//,我们建表时需要忽略
					if(StringUtils.isNotEmpty(columnSize) && !columnSize.equals("0")){
						if(!type.equals("int")&&!type.equals("tinyint")&&
								!type.equals("smallint")&&!type.equals("bigint")){
							type = type + "("+columnSize+")";
						}
					}
					//                    type = type.toLowerCase().contains("varchar")
					//                            ? "varchar(64)" : type;
					clms = clms + "\"" + name + "\"" + " " + type + " " + null + " " + ",";
				}
				clms = clms.substring(0, clms.lastIndexOf(","));//
				createTable = createTable + clms
						+ ")";
				log.info("sql  is : " + createTable);
				if(preparedStatement.isClosed()){
					preparedStatement = connection.createStatement();
				}
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}

		}
		else
		{
			if (result == null)
			{
				//判断该库下是否存在表,如果存在,则不drop该库
				String userDbase = "use " + dataBase;
				preparedStatement.execute(userDbase);
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				while(resultSet.next()) {
					return;
				}
				//在drop数据库时,需要关闭之前的连接,从新开启之后才能对dataBase进行drop,否则会出现"dataBase当前正在使用而无法删除"
				preparedStatement.close();
				connection.close();
				connection = DriverManager.getConnection(URI, user, pwd);
				preparedStatement = connection.createStatement();
				String deleteBase = "drop database " + dataBase;
				boolean flag = preparedStatement.execute(deleteBase);
				log.info("delete " + dataBase + " is : " + flag);
			}
			else
			{
				String userDbase = "use " + dataBase;
				boolean flag = preparedStatement.execute(userDbase);
				preparedStatement.close();
				log.info("use " + dataBase + " is : " + flag);

				String deleteTable = "drop table  "
						+ result.getStoreFile();
				if(preparedStatement.isClosed()){
					preparedStatement = connection.createStatement();
				}
				flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}
		preparedStatement.close();
		if(f){
			connection.close();
		}

			}
	public static void excuteSqlServer(MetaRepo repo, MetaResult result,
			boolean op) throws Exception
			{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		String port = repo.getPort().toString();
		String dataBase = repo.getRepoName();
		//private static final String DRIVER="com.mysql.jdbc.Driver";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String URI = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName=master";
		if(!op){
			URI  = "jdbc:sqlserver://" + ip + ":" + port + "; DatabaseName=" + dataBase;
		}
		Connection connection = DriverManager.getConnection(URI, user, pwd);
		Statement preparedStatement = connection.createStatement();
		if (op)
		{
			if (result == null)
			{
				//判断数据库是否存在
				boolean bol = checkSqlserverDatabase(dataBase,preparedStatement);
				if(!bol){
					String sql = "create database " + dataBase;
					boolean flag = preparedStatement.execute(sql);
					log.info("create " + dataBase + " is : " + flag);
				}
			}
			if (result != null)
			{
				//判断数据库是否存在
				boolean bol = checkSqlserverDatabase(dataBase,preparedStatement);
				if(!bol){
					String sql = "create database " + dataBase;
					boolean flag = preparedStatement.execute(sql);
					log.info("create " + dataBase + " is : " + flag);
				}
				String tableName = result.getResultFile();
				String userDbase = "use " + dataBase;
				boolean flag1 = preparedStatement.execute(userDbase);
				log.info("use " + dataBase + " is : " + flag1);
				String createTable = "create table  " + tableName
						+ "(";
				// boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				for (MetaResultPro colum : result.getMetaResultProList())
				{
					String name = colum.getProName();
					String type = colum.getProType();
					String columnSize = colum.getColumnSize();
					//sqlserver在建表时,int,tinyint,smallint,bigint等整型类型时,不能指定其长度,即使我们通过源抽取过来的字段带长度
					//,我们建表时需要忽略
					if(StringUtils.isNotEmpty(columnSize) && !columnSize.equals("0")){
						if(!type.equals("int")&&!type.equals("tinyint")&&
								!type.equals("smallint")&&!type.equals("bigint")){
							type = type + "("+columnSize+")";
						}
					}
					//                    type = type.toLowerCase().contains("varchar")
					//                            ? "varchar(64)" : type;
					clms = clms + "\"" + name + "\"" + " " + type + " " + null + ",";
				}
				clms = clms.substring(0, clms.lastIndexOf(","));// 
				createTable = createTable + clms
						+ ")";
				log.info("sql  is : " + createTable);
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}

		}
		else
		{
			if (result == null)
			{
				//判断该库下是否存在表,如果存在,则不drop该库
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				while(resultSet.next()) {
					return;
				}
				//在drop数据库时,需要关闭之前的连接,从新开启之后才能对dataBase进行drop,否则会出现"dataBase当前正在使用而无法删除"
				preparedStatement.close();
				connection.close();
				connection = DriverManager.getConnection(URI, user, pwd);
				preparedStatement = connection.createStatement();
				String deleteBase = "drop database  " + dataBase;
				boolean flag = preparedStatement.execute(deleteBase);
				log.info("delete " + dataBase + " is : " + flag);
			}
			else
			{
				String userDbase = "use " + dataBase;
				boolean flag = preparedStatement.execute(userDbase);
				log.info("use " + dataBase + " is : " + flag);

				String deleteTable = "drop table  "
						+ result.getResultFile();
				flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}
		preparedStatement.close();

			}

	/**
	 * 启用trafodion（平台存储）
	 * @param repo 数据连接信息
	 * @param result 平台存储连接信息
	 * @param op op 启用/取消启用（true启用,false取消启用）
	 * @throws Exception
	 */
	public static void excuteTrafodion(MetaRepo repo, MetaStore result,
			boolean op,Connection connection) throws Exception{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String password = repo.getUserPwd();
		if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
			password = AESUtil.decForTD(password);
		}
		String port = repo.getPort().toString();
		String schema = repo.getRepoName();
		Class.forName("org.trafodion.jdbc.t4.T4Driver");
		String url = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
		boolean f = false;
		if(connection==null){
			connection = DriverManager.getConnection(url, user, password);
			f = true;
		}
		Statement preparedStatement = connection.createStatement();
		if (op)
		{
			if (result == null)
			{
				List<String> schemas = new ArrayList<String>();
				//判断schema是否存在
				ResultSet resultSet = connection.getMetaData().getSchemas();
				while(resultSet.next()){
					String sch = resultSet.getString(1);
					schemas.add(sch);
				}
				if(!schemas.contains(schema.toUpperCase())){
					String sql = "create schema IF NOT EXISTS " + schema;
					int flag = preparedStatement.executeUpdate(sql);
					preparedStatement.close();//执行完一次之后立即关闭
					log.info("create " + schema + " is : " + (flag==1?"success":"failed"));
				} else {
					log.info("schema " + schema + "exist");
				}
			}
			if (result != null)
			{
				String tableName = result.getStoreFile();
				if(preparedStatement.isClosed()){
					preparedStatement = connection.createStatement();
				}
				preparedStatement.execute("create schema IF NOT EXISTS "+schema);
				String userDbase = "set schema " + schema;
				boolean flag1 = preparedStatement.execute(userDbase);
				preparedStatement.close();
				log.info("use " + tableName + " is : " + flag1);
				if(tableName.toUpperCase().equals("TEST")){
					throw new Exception(tableName+"是关键字,请重新输入");
				}
				//在创建表时,需要判断表是否存在,存在的话抛出异常信息
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				List<String> tables = new  ArrayList<String>();
				while(resultSet.next()) {
					tables.add(resultSet.getString("TABLE_NAME"));
				}
				if(tables.contains(tableName.toUpperCase())){
					//表存在
					return;
				}
				String createTable = "create table \"" + tableName.toUpperCase() + "\"(";
				// boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				String primaryKeys = "";
				for (MetaStorePro colum : result.getMetaStoreProList())
				{
					int tp = colum.getType();
					String name = colum.getProName();
					String type = colum.getProType();
					String columnSize = colum.getColumnSize();
					if(tp==3){//判断该字段是否为主键,可以有多个,即联合主键
						primaryKeys = primaryKeys + "\"" + name.toUpperCase() + "\" " + ",";
					}
					if(!StringUtils.isEmpty(columnSize)){
						if(type.toLowerCase().equals("numeric")
								||type.toLowerCase().equals("decimal")){
							type = type +"("+columnSize+")";
						}else if(type.toLowerCase().contains("char")){
							type = type + "(" + columnSize + ") character set utf8";
						}
					}else{
						if(type.toLowerCase().contains("char")){//包括char,varchar,nchar
							type = type.toLowerCase() + "(2000) character set utf8";
						}
					}
					if(type.toLowerCase().equals("double")){//如果是double类型,需要指定为双精度
						type = "double precision";
					}else if(type.toLowerCase().equals("timestamp")){
						type = type + (StringUtils.isEmpty(columnSize)?"(0)":"("+(Integer.valueOf(columnSize)>0?6:0)+")");
					}
					clms = clms + "\"" + name.toUpperCase() + "\" " + type + (tp==3?" NO DEFAULT NOT NULL":"") + ",";
				}
				clms = clms.substring(0, clms.lastIndexOf(","));//
				if(!primaryKeys.equals("")){
					primaryKeys = primaryKeys.substring(0, primaryKeys.lastIndexOf(","));
				}
				createTable = createTable + clms + (primaryKeys.equals("")?") hbase_options(DATA_BLOCK_ENCODING = 'FAST_DIFF',COMPRESSION = 'SNAPPY',MEMSTORE_FLUSH_SIZE = '1073741824')":",PRIMARY KEY ("+primaryKeys+")" + ") hbase_options(DATA_BLOCK_ENCODING = 'FAST_DIFF',COMPRESSION = 'SNAPPY',MEMSTORE_FLUSH_SIZE = '1073741824') salt using 8 partitions on ("+primaryKeys+")");
				log.info("sql  is : " + createTable);
				if(preparedStatement.isClosed()){
					preparedStatement = connection.createStatement();
				}
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}

		}
		else
		{
			if (result == null)
			{
				boolean flag = true;
				//判断该库下是否存在表,如果存在,则不drop该库
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				while(resultSet.next()) {
					return;
				}
				//如果trafodion的库为默认库,则在取消启用时不能drop该默认库,直接返回,不对该默认库做drop操作(默认库为SEABASE)
				if(!schema.toUpperCase().equals("SEABASE")){
					String deleteBase = "drop schema " + schema;
					flag = preparedStatement.execute(deleteBase);
				}
				preparedStatement.close();
				log.info("delete " + schema + " is : " + flag);
			}
			else
			{
				String userDbase = "set schema " + schema;
				boolean flag = preparedStatement.execute(userDbase);
				preparedStatement.close();
				log.info("use " + schema + " is : " + flag);
				String deleteTable = "drop table " + result.getStoreFile();
				if(preparedStatement.isClosed()){
					preparedStatement = connection.createStatement();
				}
				flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}
		preparedStatement.close();
		if(f){//如果不是批量启用的话,每次执行完该方法之后,在结尾进行close
			connection.close();
		}
	}

	/**
	 * 启用trafodion（结果数据集）
	 * @param repo 数据连接信息
	 * @param result 结果连接信息
	 * @param op 启用/取消启用（true启用,false取消启用）
	 */
	public static void excuteTrafodion(MetaRepo repo, MetaResult result,
			boolean op) throws Exception{
		String ip = repo.getIp();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		String port = repo.getPort().toString();
		String schema = repo.getRepoName();
		Class.forName("org.trafodion.jdbc.t4.T4Driver");
		String URI = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
		Connection connection = DriverManager.getConnection(URI, user, pwd);
		Statement preparedStatement = connection.createStatement();
		if (op)
		{
			if (result == null)
			{
				List<String> schemas = new ArrayList<String>();
				//判断schema是否存在
				//                ResultSet resultSet = preparedStatement.executeQuery("show schemas");
				ResultSet resultSet = connection.getMetaData().getSchemas();
				while(resultSet.next()){
					String sch = resultSet.getString(1);
					schemas.add(sch);
				}
				if(!schemas.contains(schema.toUpperCase())){
					String sql = "create schema " + schema;
					boolean flag = preparedStatement.execute(sql);
					log.info("create " + schema + " is : " + flag);
				}else{
					log.info("schema " + schema + "exist");
				}
			}
			if (result != null)
			{
				String tableName = result.getResultFile();
				preparedStatement.execute("create schema IF NOT EXISTS "+schema);
				String userDbase = "set schema " + schema;
				boolean flag1 = preparedStatement.execute(userDbase);
				log.info("use " + schema + " is : " + flag1);
				//trafodion中一些关键字无法当做表名,给页面提示信息
				if(tableName.toUpperCase().equals("TEST")){
					throw new Exception(tableName+"是关键字,请重新输入");
				}
				//在创建表时,需要判断表是否存在,存在的话抛出异常信息
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				List<String> tables = new  ArrayList<String>();
				while(resultSet.next()) {
					tables.add(resultSet.getString("TABLE_NAME"));
				}
				if(tables.contains(tableName.toUpperCase())){
					//表存在
					return;
				}
				String createTable = "create table \"" + tableName.toUpperCase() + "\"(";
				// boolean flag2 = preparedStatement.execute(isDropTable);
				String clms = "";
				String primaryKeys = "";
				for (MetaResultPro colum : result.getMetaResultProList())
				{
					int tp = colum.getType();
					String name = colum.getProName();
					String type = colum.getProType();
					String columnSize = colum.getColumnSize();
					if(tp==3){//判断该字段是否为主键,可以有多个,即联合主键
						primaryKeys = primaryKeys + "\"" + name.toUpperCase() + "\" " + ",";
					}
					if(!StringUtils.isEmpty(columnSize)){
						if(type.toLowerCase().equals("numeric")
								||type.toLowerCase().equals("decimal")){
							type = type +"("+columnSize+")";
						}else if(type.toLowerCase().contains("char")){
							type = type + "(" + columnSize + ") character set utf8";
						}
					}else{
						if(type.toLowerCase().contains("char")){//包括char,varchar,nchar
							type = type.toLowerCase() + "(2000) character set utf8";
						}
					}
					if(type.toLowerCase().equals("double")){//如果是double类型,需要指定为双精度
						type = "double precision";
					}else if(type.toLowerCase().equals("timestamp")){
						type = type + (StringUtils.isEmpty(columnSize)?"(0)":"("+(Integer.valueOf(columnSize)>0?6:0)+")");
					}
					clms = clms + "\"" + name.toUpperCase() + "\" " + type + (tp==3?" NO DEFAULT NOT NULL":"") + ",";
				}
				clms = clms.substring(0, clms.lastIndexOf(","));// 
				if(!primaryKeys.equals("")){
					primaryKeys = primaryKeys.substring(0, primaryKeys.lastIndexOf(","));
				}
				createTable = createTable + clms + (primaryKeys.equals("")?") hbase_options(DATA_BLOCK_ENCODING = 'FAST_DIFF',COMPRESSION = 'SNAPPY',MEMSTORE_FLUSH_SIZE = '1073741824')":",PRIMARY KEY ("+primaryKeys+")" + ") hbase_options(DATA_BLOCK_ENCODING = 'FAST_DIFF',COMPRESSION = 'SNAPPY',MEMSTORE_FLUSH_SIZE = '1073741824') salt using 8 partitions on ("+primaryKeys+")");
				log.info("sql  is : " + createTable);
				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create  " + tableName + " is : " + flag3);
			}

		}else {
			if (result == null)
			{
				boolean flag = true;
				//判断该库下是否存在表,如果存在,则不drop该库
				ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				while(resultSet.next()) {
					return;
				}
				//如果trafodion的库为默认库,则在取消启用时不能drop该默认库,直接返回,不对该默认库做drop操作(默认库为SEABASE)
				if(!schema.toUpperCase().equals("SEABASE")){
					String deleteBase = "drop schema " + schema;
					flag = preparedStatement.execute(deleteBase);
				}
				log.info("drop schema " + schema + "is : " + flag);
			} else {
				String userDbase = "set schema " + schema;
				boolean flag = preparedStatement.execute(userDbase);
				log.info("use " + schema + " is : " + flag);

				String deleteTable = "drop table " + result.getResultFile();
				flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}
		preparedStatement.close();
	}

	/**
	 * 
	 * @param repo ������Ϣ
	 * @param store  hive��Ӧ��hdfsdir
	 * @throws Exception
	 */
	public static void excuteHiveSql(MetaRepo repo,MetaResult result, boolean op)
			throws Exception
			{
		String ip = repo.getIp();
		String port = repo.getPort().toString();
		String dataBase = repo.getRepoName();
		String userName = repo.getUserName();
		String password = repo.getUserPwd();
		if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
			password = AESUtil.decForTD(password);
		}
		String hiveUrl = "jdbc:hive2://" + ip + ":" + port
				+ "/default?characterEncoding=UTF-8";
		log.info("url " + hiveUrl);
		Connection hiveConnection = getHiveConnection(hiveUrl,userName,password);
		Statement preparedStatement = hiveConnection.createStatement();
		if (op )
		{
			String initDataBase = "create database if not exists " + dataBase;//+" location '"+hdfsDir+"'";
			boolean flag = preparedStatement.execute(initDataBase);
			log.info("create " + dataBase + " is : " + flag);
			if (result != null)
			{
				String tableName = result.getResultFile();
				String tablesDesc = result.getResultDesc();
				String delimiter = result.getDelimiter();
				String userDbase = "use " + dataBase;
				boolean flag1 = preparedStatement.execute(userDbase);
				log.info("use " + dataBase + " is : " + flag1);

				String createTable = "create table if not exists " + tableName
						+ "(";
				String parts = " partitioned by (";
				String clms = "";
				boolean isHaveP = false;//��ʾ����
				for (MetaResultPro colum : result.getMetaResultProList())
				{
					String name = colum.getProName();
					String type = colum.getProType();

					String columnSize = colum.getColumnSize();
					//在创建hive表时,需要对字段的类型进行hive转化,当时varchar或者char类型时,必须给定字段长度
					//如果此时没有给定长度,给默认长度500进行建表
					type = MetaUtil.getHiveDataType(type,5);
					if(type==null){
						type = colum.getProType();
					}else  if(type.toLowerCase().contains("char")){
						type = StringUtils.isEmpty(columnSize)?(type + "(500)"):(type + "(" + columnSize + ")");
					}else if(type.toLowerCase().trim().equals("decimal")){
						type = StringUtils.isEmpty(columnSize)?(type):(type + "(" + columnSize + ")");
					}
					/* type = type.toLowerCase().contains("char") ? "string"
                            : type;
                    type = type.toLowerCase().contains("int") ? "int" : type;
					 */


					String desc = colum.getProDesc() != null
							? "comment '" + colum.getProDesc() + "'" : "";

					if (colum.getType() == 1)//�жϷ����ֶ�
					{
						parts = parts + "`" + name + "` " + type + " " + desc
								+ ",";
						isHaveP = true;
					}else{
						clms = clms + "`" + name + "` " + type + " " + desc + ",";
					}                
				}
				clms = clms.substring(0, clms.lastIndexOf(","));// ȥ�����һ��','��
				createTable = createTable + clms + ") comment '" + tablesDesc
						+ "'";

				if (isHaveP) //判断是否有分区字段
				{
					parts = parts.substring(0, parts.lastIndexOf(","));
					parts = parts + ")";
					createTable = createTable + parts;
				}
				String ways=(result.getStoreWay()!=null&&"".equals(result.getStoreWay()))?result.getStoreWay():"textfile";

				if(!"\\t".equals(delimiter)){
					delimiter="\\"+delimiter;
				}
				createTable = createTable
						+ " row format delimited fields terminated by '"
						+ delimiter + "' stored as "+ways;// location '/user/hive/test'";
				log.info("sql   is : " + createTable);

				boolean flag3 = preparedStatement.execute(createTable);
				log.info("create " + dataBase + " is : " + flag3);
			}
		}
		else
		{
			if (result == null)
			{
				//判断该库下是否存在表,如果存在,则不drop该库
				ResultSet resultSet = hiveConnection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
				while(resultSet.next()) {
					return;
				}
				String deleteBase = "drop database if exists " + dataBase;
				boolean flag = preparedStatement.execute(deleteBase);
				log.info("delete " + dataBase + " is : " + flag);
			}
			else
			{
				String userDbase = "use " + dataBase;
				boolean flag = preparedStatement.execute(userDbase);
				log.info("use " + dataBase + " is : " + flag);

				String deleteTable = "drop table if exists "
						+ result.getResultFile();
				flag = preparedStatement.execute(deleteTable);
				log.info("drop table " + deleteTable + " is : " + flag);
			}

		}

		preparedStatement.close();
			}

	/**
	 * 
	 * @param repo ������Ϣ
	 * @param store  hive��Ӧ��hdfsdir
	 * @throws Exception
	 */
	public static String excuteHiveSql(MetaRepo repo, MetaStore store, boolean op,Connection hiveConnection)
			throws Exception
			{
		try
		{
			String ip = repo.getIp();
			String port = repo.getPort().toString();
			String dataBase = repo.getRepoName();
			String hiveUrl = "jdbc:hive2://" + ip + ":" + port + "/default?characterEncoding=UTF-8";
			log.info("url " + hiveUrl);
			boolean f = false;
			String pwd = repo.getUserPwd();
			if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
				pwd = AESUtil.decForTD(pwd);
			}
			if(hiveConnection==null){
				f = true;
				hiveConnection = getHiveConnection(hiveUrl,repo.getUserName(),pwd);
			}
			Statement preparedStatement = hiveConnection.createStatement();
			if (op)
			{
				String initDataBase = "create database if not exists " + dataBase;//+" location '"+hdfsDir+"'";
				boolean flag = preparedStatement.execute(initDataBase);
				preparedStatement.close();
				log.info("create " + dataBase + " is : " + flag);
				if (store != null)
				{
					String tableName = store.getStoreFile();
					String tablesDesc = store.getStoreDesc();
					String delimiter = store.getDelimiter();
					String userDbase = "use " + dataBase;
					if(preparedStatement.isClosed()){
						preparedStatement = hiveConnection.createStatement();
					}
					boolean flag1 = preparedStatement.execute(userDbase);
					preparedStatement.close();
					log.info("use " + dataBase + " is : " + flag1);

					String ways=store.getStoreWay();
					
					String createTable = "create table if not exists " + tableName
							+ "(";
					
					String  createTableTemp = "create table if not exists " + tableName+"_temp"+ "(";
					
					String parts = " partitioned by (";
					String clms = "";
					boolean isHaveP = false;
					for (MetaStorePro colum : store.getMetaStoreProList())
					{
						String name = colum.getProName();
						String type = colum.getProType();
						String columnSize = colum.getColumnSize();
						//在创建hive表时,需要对字段的类型进行hive转化,当时varchar或者char类型时,必须给定字段长度
						//如果此时没有给定长度,给默认长度500进行建表
						type = MetaUtil.getHiveDataType(type,5);
						if(type.toLowerCase().contains("char")){
							type = StringUtils.isEmpty(columnSize)?(type + "(500)"):(type + "(" + columnSize + ")");
						}else if(type.toLowerCase().trim().equals("decimal")){
							type = StringUtils.isEmpty(columnSize)?(type):(type + "(" + columnSize + ")");
						}
						/*type = type.toLowerCase().contains("char") ? "string"
                                : type;
                        type = type.toLowerCase().contains("int") ? "int" : type;*/
						String desc = colum.getProDesc() != null
								? "comment '" + colum.getProDesc() + "'" : "";

						if (colum.getType() == 1)
						{
							parts = parts + "`" + name + "` " + type + " " + desc
									+ ",";
							isHaveP = true;
						}else{
							clms = clms + "`" + name + "` " + type + " " + desc + ",";
						}                
					}
					clms = clms.substring(0, clms.lastIndexOf(","));
					createTable = createTable + clms + ") comment '" + tablesDesc
							+ "'";
					
					createTableTemp = createTableTemp+ clms + ") comment '" + tablesDesc+ "'";

					if (isHaveP) //判断是否有分区字段
					{
						parts = parts.substring(0, parts.lastIndexOf(","));
						parts = parts + ")";
						createTable = createTable + parts;
//						createTableTemp = createTableTemp + parts;
					}

					if(!"\\t".equals(delimiter)){
						delimiter="\\"+delimiter;
					}
					if(ways!=null&&!"".equals(ways)){
						createTable = createTable+ " row format delimited fields terminated by '"+ delimiter + "' stored as "+ways;// location '/user/hive/test'";
						log.info("sql   is : " + createTable);
						createTableTemp = createTableTemp+ " row format delimited fields terminated by '"+ delimiter + "' stored as textfile";// location '/user/hive/test'";
						log.info("sql   is : " + createTableTemp);
						
					}
					if(preparedStatement.isClosed()){
						preparedStatement = hiveConnection.createStatement();
					}
					boolean flag3 = preparedStatement.execute(createTable);
					log.info("create " + dataBase + " is : " + flag3);
					return createTableTemp;
				}
			}
			else
			{
				if (store == null)
				{
					//判断该库下是否存在表,如果存在,则不drop该库
					ResultSet resultSet = hiveConnection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
					while(resultSet.next()) {
						return null;
					}
					String deleteBase = "drop database if exists " + dataBase;
					boolean flag = preparedStatement.execute(deleteBase);
					log.info("delete " + dataBase + " is : " + flag);
				}
				else
				{
					String userDbase = "use " + dataBase;
					boolean flag = preparedStatement.execute(userDbase);
					preparedStatement.close();
					log.info("use " + dataBase + " is : " + flag);
					String dt = "drop table if exists " + store.getStoreFile();
					if(preparedStatement.isClosed()){
						preparedStatement = hiveConnection.createStatement();
					}
					int flag1 = preparedStatement.executeUpdate(dt);
					log.info("drop table " + dt + " is : " + flag1);
				}

			}
			preparedStatement.close();
			if(f){
				hiveConnection.close();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return null;

			}
	
	
	

	public static boolean excuteHiveSql(String ip, String port, String user,
			String pwd, String database, String sql) {
		boolean f = false;
		Connection hiveConnection = null;
		try {
			String hiveUrl = "jdbc:hive2://" + ip + ":" + port
					+ "/default?characterEncoding=UTF-8";
			log.info("url " + hiveUrl);
			if (StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")) {
				pwd = AESUtil.decForTD(pwd);
			}
			hiveConnection = getHiveConnection(hiveUrl, user, pwd);
			Statement preparedStatement = hiveConnection.createStatement();
			String userDbase = "use " + database;
			preparedStatement.execute(userDbase);
			preparedStatement.execute(sql);
			f = true;
		} catch (Exception e) {
			e.printStackTrace();
			f= false;
		} finally {
			if (hiveConnection != null) {
				try {
					hiveConnection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return f;
	}
	

	/**
	 * 
	 * @param repo
	 * @param store 
	 * @throws Exception
	 */
	public static void createHdfsDir(MetaRepo repo, MetaStore store, boolean op)
			throws Exception
			{
		String ip = repo.getIp();
		String dataBase = repo.getRepoName();
		String user = repo.getUserName();
		String pwd = repo.getUserPwd();
		if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
			pwd = AESUtil.decForTD(pwd);
		}
		SshClient sshClient = new SshClient(ip, 22, user, pwd);
		log.info("craete hdfs dir :" + dataBase);
		String res = "";
		if (op)
		{
			res = sshClient.executeOnce(
					"source /etc/profile; hadoop fs -mkdir -p " + dataBase);
			if (store != null)
			{
				String touchFile = "source /etc/profile; hadoop fs -mkdir -p " + dataBase + "/"
						+ store.getStoreFile();
				res = sshClient.executeOnce(touchFile);

			}
		}
		else
		{
			if (store != null)
			{
				res = sshClient
						.executeOnce("source /etc/profile; hadoop fs -rm -r "
								+ dataBase + "/" + store.getStoreFile());
			}
			else
			{
				res = sshClient.executeOnce(
						"source /etc/profile; hadoop fs -rm -r " + dataBase);
			}
			log.info(res);
		}

			}

	/**
	 * ����kafka ��topic
	 * 
	 * 
	 */
	public static void createTopic(MetaRepo repo)
	{

	}

	/**
	 * ����hbase��Ҫ�ı�
	 * 
	 * @param tableName
	 *            hbase�ı����
	 * @param external
	 *            cf
	 * @param zkConn
	 *            ���ӵ�zookepper
	 */
	public static void createHbaseTable(MetaStore store,
			boolean op) throws Exception
			{
		String zkConn=Global.getConfig("zookeeper_ip");
		HBaseAdmin hBaseAdmin = null;
		String tableName=store.getStoreFile(); 
		String nameSpace = store.getRepoId().getRepoName();
		tableName = nameSpace + ":" + tableName;
		Boolean flag = true;
		String external=store.getStoreExternal(); 
		String mappingName=external.substring(external.indexOf(";")+1);
		external = external.substring(0, external.indexOf(";"));
		//        String firstFamily=null;
		try
		{
			Configuration configuration = HBaseConfiguration.create();
			configuration.set("hbase.zookeeper.property.clientPort", "2181");
			configuration.set("hbase.zookeeper.quorum", zkConn);
			hBaseAdmin = new HBaseAdmin(configuration);
			NamespaceDescriptor[] descriptors = hBaseAdmin.listNamespaceDescriptors();
			if(descriptors!=null && descriptors.length>0){
				for (NamespaceDescriptor namespaceDescriptor : descriptors){
					String name = namespaceDescriptor.getName();
					if(name.equals(nameSpace)){
						flag = false;
						break;
					}
				}
			}
			//创建命名空间
			if (flag) {
				try {
					hBaseAdmin.createNamespace(NamespaceDescriptor.create(nameSpace).build());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			if (hBaseAdmin.tableExists(tableName) )
			{// 
				hBaseAdmin.disableTable(tableName);
				hBaseAdmin.deleteTable(tableName);
				log.debug(tableName + " is exist,detele....");
			}
			//            if (StringUtils.isNotEmpty(external))
			//            {
			//                String[] cfs = external.split(",");
			//                if (cfs != null && cfs.length > 0)
			//                {
			//                    for (String cf : cfs)
			//                    {
			//                        if(null==firstFamily)
			//                        {
			//                           firstFamily=cf;
			//                        }
			//                        mappingName=cf;//取最后一个为列簇
			//                    }
			//                }
			//            }
			if(op)
			{
				@SuppressWarnings("deprecation")
				HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
				if (StringUtils.isNotEmpty(external))
				{
					String[] cfs = external.split(",");
					if (cfs != null && cfs.length > 0)
					{
						for (String cf : cfs)
						{
							tableDescriptor.addFamily(new HColumnDescriptor(cf));
						}
						hBaseAdmin.createTable(tableDescriptor);
					}
				}
				if (mappingName!=null)
				{
					List<MetaStorePro> metaStoreProList=store.getMetaStoreProList();
					if(!hBaseAdmin.tableExists("pentaho_mappings"))
					{
						@SuppressWarnings("deprecation")
						HTableDescriptor mappingtable = new HTableDescriptor("pentaho_mappings");
						mappingtable.addFamily(new HColumnDescriptor("columns"));
						mappingtable.addFamily(new HColumnDescriptor("key"));
						hBaseAdmin.createTable(mappingtable);
					}
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
							String qualifier =metaStorePro.getProExternal()+","+metaStorePro.getProName()+","+metaStorePro.getProName();
							String family="columns";
							String type=metaStorePro.getProType();
							put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(type));
							htable.put(put);
						}

					}
				}
			}else{
				@SuppressWarnings("resource")
				HTable htable = new HTable(configuration, "pentaho_mappings");
				if(hBaseAdmin.tableExists("pentaho_mappings"))
				{
					List<Delete> list = new ArrayList<Delete> ();  
					String rowkey=tableName + ","+mappingName;
					Delete d1 = new Delete(rowkey.getBytes());  
					list.add(d1);  

					htable.delete(list);  
					log.info("删除maping数据成功!rowkey为："+rowkey);  
				}
			}
		}
		catch (Exception e)
		{
			log.info(e.getMessage());
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

	/**
	 * 外部数据源信息,类型为hbase时,生成hbasetable
	 * @param metaSource
	 * @throws Exception
	 */
	public static void createHbaseTable(MetaSource metaSource) throws Exception{
		String zkConn=Global.getConfig("zookeeper_ip");
		HBaseAdmin hBaseAdmin = null;
		String nameSpace = metaSource.getRepoId().getRepoName();
		String tableName = metaSource.getSourceFile();
		tableName = nameSpace + ":" + tableName;
		Boolean flag = true;
		String external = metaSource.getExternal();
		String mappingName = external.substring(external.indexOf(";")+1);
		external = external.substring(0, external.indexOf(";"));
		try
		{
			Configuration configuration = HBaseConfiguration.create();
			configuration.set("hbase.zookeeper.property.clientPort", "2181");
			configuration.set("hbase.zookeeper.quorum", zkConn);
			hBaseAdmin = new HBaseAdmin(configuration);
			NamespaceDescriptor[] descriptors = hBaseAdmin.listNamespaceDescriptors();
			if(descriptors!=null && descriptors.length>0){
				for (NamespaceDescriptor namespaceDescriptor : descriptors){
					String name = namespaceDescriptor.getName();
					if(name.equals(nameSpace)){
						flag = false;
						break;
					}
				}
			}
			//创建命名空间
			if (flag) {
				try {
					hBaseAdmin.createNamespace(NamespaceDescriptor.create(nameSpace).build());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			if (hBaseAdmin.tableExists(tableName) )
			{
				hBaseAdmin.disableTable(tableName);
				hBaseAdmin.deleteTable(tableName);
				log.debug(tableName + " is exist,detele....");
			}
			@SuppressWarnings("deprecation")
			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			if (StringUtils.isNotEmpty(external))
			{
				String[] cfs = external.split(",");
				if (cfs != null && cfs.length > 0)
				{
					for (String cf : cfs)
					{
						tableDescriptor.addFamily(new HColumnDescriptor(cf));
					}
					hBaseAdmin.createTable(tableDescriptor);
				}
			}
			if (mappingName!=null)
			{
				List<MetaSourcePro> metaSourceProList=metaSource.getMetaSourceProList();
				if(!hBaseAdmin.tableExists("pentaho_mappings"))
				{
					@SuppressWarnings("deprecation")
					HTableDescriptor mappingtable = new HTableDescriptor("pentaho_mappings");
					mappingtable.addFamily(new HColumnDescriptor("columns"));
					mappingtable.addFamily(new HColumnDescriptor("key"));
					hBaseAdmin.createTable(mappingtable);
				}
				@SuppressWarnings("resource")
				HTable htable = new HTable(configuration, "pentaho_mappings");
				for (MetaSourcePro metaSourcePro : metaSourceProList)
				{
					if(metaSourcePro.getType()==3)
					{
						Put put = new Put(Bytes.toBytes(tableName+","+mappingName));
						String qualifier =metaSourcePro.getProName();
						String family="key";
						String type=metaSourcePro.getProType();
						put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(type));
						htable.put(put);
					}else{
						Put put = new Put(Bytes.toBytes(tableName+","+mappingName));
						String qualifier =metaSourcePro.getProExternal()+","+metaSourcePro.getProName()+","+metaSourcePro.getProName();
						String family="columns";
						String type=metaSourcePro.getProType();
						put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), Bytes.toBytes(type));
						htable.put(put);
					}

				}
			}
		}
		catch (Exception e)
		{
			log.info(e.getMessage());
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

	/**
    1	flume_exec
    2	ftp�ļ�
    3	hdfs
    4	hbase
    5	hive
    6	mysql
    7	oracle
    8	mongdb
    9	kafka
    10	sqlserver 
    11 trafodion
	 * @throws Exception 
	 */


	public static boolean enable(MetaRepo repo, MetaStore store,
			MetaResult result, boolean op) throws Exception
			{
		boolean flag = false;
		switch (repo.getRepoType())
		{
		case 2://ftp
			flag =createFtpDir(repo, result, op);
			break;
		case 3://hdfs
			createHdfsDir(repo, store, op);
			flag = true;
			break;
		case 4://hbase
			if (store != null)
			{
				createHbaseTable(store,op);
				flag = true;
			}
			break;
		case 5://hive
			if(repo.getMetaType()==1){
				MetaStoreService ms = SpringContextHolder.getBean(MetaStoreService.class);
				String createTableTempSql = excuteHiveSql(repo, store, op,null);
				if(createTableTempSql!=null){
					store.setStoreExternal(createTableTempSql);
					ms.updateMetaStore(store);
				}
				//load/unload加载hive表
				if(store!=null){
					kylinHiveTable(store.getRepoName()+"."+store.getStoreFile(), op);
				}
			}else if(repo.getMetaType()==2){
				excuteHiveSql(repo, result, op);
			}
			flag = true;
			break;
		case 6://mysql
			if(repo.getMetaType()==1){
				excuteMysqlSql(repo, store, op,null);
			}else if(repo.getMetaType()==2){
				excuteMysqlSql(repo, result, op);
			}
			flag = true;
			break;
		case 7://oracle
			if(repo.getMetaType()==1){
				excuteOracleSql(repo, store, op,null);
			}else if(repo.getMetaType()==2){
				excuteOracleSql(repo, result, op);
			}
			flag = true;
			break;
		case 9://kafka
			createTopic(repo);
			flag = true;
			break;
		case 10://sqlserver
			if(repo.getMetaType()==1){
				excuteSqlServer(repo, store, op,null);
			}else if(repo.getMetaType()==2){
				excuteSqlServer(repo, result, op);
			}
			flag = true;
			break;
		case 11://trafodion
			if(repo.getMetaType()==1){
				excuteTrafodion(repo, store, op,null);
			}else if(repo.getMetaType()==2){
				excuteTrafodion(repo, result, op);
			}
			flag = true;
			break;
		default:
			break;
		}
		return flag;
			}

	/**
	 * 操作kylin hive表
	 * @param tableName 表名
	 * @param op
	 */
	private static void kylinHiveTable(String tableName,boolean op){
		String baseUrl = Global.getConfig("kylin_base_api_url") + "tables/" + tableName + "/" + Global.getConfig("kylin_default_project");
		try
		{
			if(op){//load
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("calculate", true);
				String params = JSONObject.toJSONString(map);
				KylinHttpRequest.httpPost(params, baseUrl, "POST");
			}else{//unload
				KylinHttpRequest.httpGet(baseUrl, "DELETE");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			log.info((op?"load hive表"+tableName:"unload hive表"+tableName)+"失败,"+e.getMessage());
		}
	}

	private static boolean createFtpDir(MetaRepo repo, MetaResult result, boolean op)
	{
		boolean flag=true;
		try
		{
			String ip = repo.getIp();
			String user = repo.getUserName();
			String pwd = repo.getUserPwd();
			if(StringUtils.isNotEmpty(pwd) && pwd.endsWith("==")){
				pwd = AESUtil.decForTD(pwd);
			}
			String port = repo.getPort();
			String dataBase = repo.getRepoName();
			FtpUtil t = new FtpUtil();
			boolean res=t.connect("/", ip, Integer.parseInt(port), user,pwd);
			if(res){
				if(op)
				{
					t.mkdir(dataBase);
					if(result!=null&& result.getResultFile()!=null)
					{
						String filename=result.getResultFile();
						t.upload("", dataBase+"/"+filename);
					}
				}else{
					if(result!=null&& result.getResultFile()!=null)
					{
						String filename=result.getResultFile();
						flag=t.deleteFile(dataBase+"/"+filename);
					}else{
						flag=t.deleteDir(dataBase);                
					}
				} 
			}else{

			}
		}
		catch (Exception e)
		{
			flag=false;
			e.printStackTrace();
		}
		return flag;
	}

	public static void main(String[] args) throws Exception
	{
		String s="\t";
		System.out.println("\\t".equals(s));
		Connection connection = getDBConnection("jdbc:mysql://172.18.84.71:3306/te?characterEncoding=UTF-8", "root", "123456");
		ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
		while(resultSet.next()) {
			return;
		}

		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		String URI  = "jdbc:sqlserver://" + "172.18.84.53" + ":" + 1433 + ";DatabaseName=master";
		Connection dbConnection = DriverManager.getConnection(URI,"sa","Sql123");
		System.out.println(dbConnection);
		/* String dataBase="test";//trafodion_lixiaoli 
        String ip="172.18.84.68";//172.18.84.67
        String port="10015";//  23400
        String sql = "show  tables";
        //sql = "select * from customer";
        List < Map <String ,String>> beans=excuteSparkSql(ip, port, dataBase, "customer", sql,"","");
        for (Map<String, String> bean : beans)
        {
            System.out.println(bean.toString());
        }*/

		/* String dataBase="trafodion_lixiaoli";// 
        String ip="172.18.84.67";//
        String port="23400";//  
        String sql = "show tables";
        sql = "select * from j_kemu";
        MetaRepo repo=new MetaRepo();
        repo.setIp(ip);
        repo.setPort(port);
        repo.setUserName("zz");
        repo.setUserPwd("zz");
        repo.setRepoName(dataBase);
        try
        {
            List < Map <String ,String>> beans=excuteTrafodionSql(repo,"", sql) ;
            for (Map<String, String> bean : beans)
            {
                System.out.println(bean.toString());
            }
            List<MetaTable> list = new DBUtils().getHiveTables("hive", "hive", "10000", "172.18.84.68", "time_lee");
            for (MetaTable metaTable : list)
            {
                System.out.println(metaTable.getTableName());
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
		/*try
        {
            Class.forName("org.trafodion.jdbc.t4.T4Driver");
            String url = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
            Connection  connection = DriverManager.getConnection(url, "zz", "zz");
            Statement preparedStatement = connection.createStatement();
            String userDbase = "set schema trafodion_lixiaoli";
            boolean flag1 = preparedStatement.execute(userDbase);
            log.info("use " + dataBase + " is : " + flag1);
            ResultSet  resultSet = preparedStatement.executeQuery("select * from j_kemu");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }*/
	}
	/**
	 * 执行 spark sql语句方法
	 * @param ip
	 * @param port
	 * @param dataBase
	 * @param tableName
	 * @param sql
	 * @return
	 * @throws Exception 
	 */
	public static  List < Map <String ,String>> excuteSparkSql(String ip,String port,String dataBase,String tableName,String sql,String userName,String pwd) throws Exception{
		List < Map <String ,String>> beans=new ArrayList< Map <String ,String>>();//返回字段值信息
		String jdbcurl="jdbc:hive2://"+ip+":"+port; 
		String[] names=null;
		Connection connection=null;
		Statement preparedStatement=null;
		try
		{
			connection=DBUtils.getHiveConnection(jdbcurl,userName,pwd);
			preparedStatement = connection.createStatement();
			String userDbase = "use " + dataBase;
			boolean flag1 = preparedStatement.execute(userDbase);
			log.info("use " + dataBase + " is : " + flag1);

			log.info("sql is :{" + sql + "}");
			ResultSet  resultSet = preparedStatement.executeQuery(sql);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			//获取列名
			int count=rsmd.getColumnCount();
			names=new String[count];
			for(int i=0;i<count;i++){
				names[i]=rsmd.getColumnName(i+1);
			}
			while(resultSet.next()){
				Map <String ,String> bean= new LinkedHashMap<String ,String>();
				for (String name : names)
				{
					if(!name.contains("isTemporary")){
						log.info(name+"\t*********"+resultSet.getString(name));
						bean.put(name, resultSet.getString(name));
					}
				}
				beans.add(bean);
			}

		} catch (Exception e)
		{
			if(e!=null&&e.getMessage().contains("Table not found"))
			{
				log.info(tableName+"不存在");
			}else if(e!=null&&e.getMessage().contains("Input path does not exist")){
				log.info(tableName+"没有数据");
				Map <String ,String> bean= new HashMap<String ,String>();
				if(names!=null){
					for (String name : names)
					{
						if(!name.contains("isTemporary")){
							bean.put(name, "");
						}
					}
				}
				beans.add(bean);
			}else{
				e.printStackTrace();
				throw e;
			}
		} finally {
			try
			{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		} 
		return beans;
	}

	/**
	 * 启用trafodion（平台存储）
	 * @param repo 数据连接信息
	 * @param result 平台存储连接信息
	 * @param op op 启用/取消启用（true启用,false取消启用）
	 * @throws Exception
	 */
	public static List < Map <String ,String>>  excuteTrafodionSql(MetaRepo repo, String tableName,
			String sql) throws Exception  {
		List < Map <String ,String>> beans=new ArrayList< Map <String ,String>>();//返回字段值信息
		String[] names=null;
		Connection connection=null;
		Statement preparedStatement=null;
		try
		{
			String ip = repo.getIp();
			String user = repo.getUserName();
			String password = repo.getUserPwd();
			if(StringUtils.isNotEmpty(password) && password.endsWith("==")){
				password = AESUtil.decForTD(password);
			}
			String port = repo.getPort().toString();
			String dataBase = repo.getRepoName();
			Class.forName("org.trafodion.jdbc.t4.T4Driver");
			String url = "jdbc:t4jdbc://" + ip + ":" + port + "/:";
			connection = DriverManager.getConnection(url, user, password);
			preparedStatement = connection.createStatement();
			String userDbase = "set schema " + dataBase;
			boolean flag1 = preparedStatement.execute(userDbase);
			log.info("use " + dataBase + " is : " + flag1);
			ResultSet  resultSet = preparedStatement.executeQuery(sql);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			//获取列名
			int count=rsmd.getColumnCount();
			names=new String[count];
			for(int i=0;i<count;i++){
				names[i]=rsmd.getColumnName(i+1);
			}
			while(resultSet.next()){
				Map <String ,String> bean= new HashMap<String ,String>();
				for (String name : names)
				{
					if(!name.contains("isTemporary")){
						log.info(name+"\t*********"+resultSet.getString(name));
						bean.put(name, resultSet.getString(name));
					}
				}
				beans.add(bean);
			}
		}
		catch (Exception e)
		{
			if(e!=null&&e.getMessage().contains("Table not found"))
			{
				log.info(tableName+"不存在");
			}else if(e!=null&&e.getMessage().contains("Invalid authorization specification")){
				log.info(tableName+"密码错误");
				throw new RuntimeException("密码错误!");
			}else{
				e.printStackTrace();
				throw e;
			}
		} finally {
			try
			{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				throw e;
			}
		}
		return beans;
	}

	public static List<Map<String ,String>> executeKylinSql(String sql) throws Exception{
		List<Map<String ,String>> beans = new ArrayList<Map<String ,String>>();//返回字段值信息
		Driver driver = (Driver) Class.forName("org.apache.kylin.jdbc.Driver").newInstance();
		Properties properties = new Properties();
		properties.put("user", "ADMIN");
		properties.put("password", "KYLIN");

		String jdbcurl="jdbc:kylin://172.30.103.11:7070/" + Global.getConfig("kylin_default_project"); 
		String[] names=null;
		Connection connection=null;
		Statement preparedStatement=null;
		try
		{
			connection = driver.connect(jdbcurl, properties);
			preparedStatement = connection.createStatement();
			log.info("sql is :{" + sql + "]");
			ResultSet  resultSet = preparedStatement.executeQuery(sql);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			//获取列名
			int count=rsmd.getColumnCount();
			names=new String[count];
			for(int i=0;i<count;i++){
				names[i]=rsmd.getColumnName(i+1);
			}
			while(resultSet.next()){
				Map <String ,String> bean= new HashMap<String ,String>();
				for (String name : names)
				{
					if(!name.contains("isTemporary")){
						log.info(name+"\t*********"+resultSet.getString(name));
						bean.put(name, resultSet.getString(name));
					}
				}
				beans.add(bean);
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		} finally {
			try
			{
				if(preparedStatement!=null){
					preparedStatement.close();
				}
				if(connection!=null){
					connection.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		} 
		return beans;
	}


	public static boolean checkSqlserverDatabase(String databaseName,Statement preparedStatement) throws Exception{
		//判断数据库是否存在
		boolean bol = false;
		String qs = "select COUNT(*) as cc from Master..SysDatabases where Name = '" + databaseName + "'";
		ResultSet resultSet = preparedStatement.executeQuery(qs);
		while(resultSet.next()){
			String sch = resultSet.getString("cc");
			if(sch!=null && Integer.valueOf(sch)>0){
				bol = true;
			}else{
				bol = false;
			}
			break;
		}
		return bol;
	}

}
