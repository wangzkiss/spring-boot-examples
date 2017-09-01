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

import cn.vigor.common.persistence.annotation.ActDbDao;

/**
 * Created by summer on 2016/11/25.
 */
@Configuration
@MapperScan(basePackages = "cn.vigor.modules", sqlSessionTemplateRef  = "ActSqlSessionTemplate",annotationClass=ActDbDao.class)
public class DataSourceAct {

    @Bean(name = "actDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.act")
    public DataSource actDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "actSqlSessionFactory")
    @Primary
    public SqlSessionFactory actSqlSessionFactory(@Qualifier("actDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/act/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "actTransactionManager")
    @Primary
    public DataSourceTransactionManager actTransactionManager(@Qualifier("actDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "actSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate actSqlSessionTemplate(@Qualifier("actSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
