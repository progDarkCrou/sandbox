package vorona.andriy;

import org.hibernate.SessionFactory;
import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by avorona on 28.12.15.
 */

@Configuration
@EnableJpaRepositories(basePackages = "vorona.andriy.repositories")
public class MainConfiguration {

    @Bean
    @Scope("singleton")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/hibernate?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }

    @Bean
    @Autowired
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactory.setEntityManagerInterface(HibernateEntityManager.class);
        entityManagerFactory.setDataSource(dataSource);

        Properties properties = new Properties();

        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.cache.provider_class",
                org.hibernate.cache.ehcache.StrategyRegistrationProviderImpl.class.getName()
        );
        properties.setProperty("hibernate.cache.region.factory_class",
                org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory.class.getName()
        );

        entityManagerFactory.setJpaProperties(properties);

        entityManagerFactory.setPackagesToScan("vorona.andriy.model");

        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory.getObject();
    }

    @Bean
    @Autowired
    public SessionFactory sessionFactory(EntityManagerFactory entityManagerFactory) {
        HibernateJpaSessionFactoryBean sessionFactoryBean = new HibernateJpaSessionFactoryBean();
        sessionFactoryBean.setEntityManagerFactory(entityManagerFactory);
        return sessionFactoryBean.getObject();
    }

//    @Bean
//    @Autowired
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
//        transactionManager.setRollbackOnCommitFailure(true);
//        transactionManager.setNestedTransactionAllowed(true);
//
//        transactionManager.afterPropertiesSet();
//
//        return transactionManager;
//    }

    @Bean
    @Autowired
    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory, DataSource dataSource) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);

        transactionManager.setDataSource(dataSource);
        transactionManager.setNestedTransactionAllowed(true);
        transactionManager.setRollbackOnCommitFailure(true);
        transactionManager.setHibernateManagedSession(false);
        transactionManager.afterPropertiesSet();

        return transactionManager;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxInMemorySize(10000000);
        return multipartResolver;
    }

}
