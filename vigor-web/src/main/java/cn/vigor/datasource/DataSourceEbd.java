package cn.vigor.datasource;


import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import cn.vigor.common.persistence.annotation.MyBatisDao;

//import cn.vigor.common.persistence.annotation.MyBatisDao; annotationClass = MyBatisDao.class

/**
 * Created by summer on 2016/11/25.
 */
@Configuration
@MapperScan(basePackages = "cn.vigor.modules", sqlSessionTemplateRef  = "ebdSqlSessionTemplate" ,annotationClass=MyBatisDao.class)
public class DataSourceEbd {

    @Bean(name = "ebdDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.ebd")
    @Primary
    public DataSource ebdDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "ebdSqlSessionFactory")
    @Primary
    public SqlSessionFactory ebdSqlSessionFactory(@Qualifier("ebdDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/ebd/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "ebdTransactionManager")
    @Primary
    public DataSourceTransactionManager ebdTransactionManager(@Qualifier("ebdDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "ebdSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate ebdSqlSessionTemplate(@Qualifier("ebdSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
