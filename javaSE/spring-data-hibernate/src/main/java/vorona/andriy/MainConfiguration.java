package vorona.andriy;

import org.hibernate.jpa.HibernateEntityManager;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by avorona on 28.12.15.
 */

@Configuration
@EnableTransactionManagement(mode = AdviceMode.PROXY, proxyTargetClass = true)
@EnableJpaRepositories(basePackages = "vorona.andriy.repositories")
public class MainConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    @Scope("singleton")
    public DataSource dataSource() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/hibernate?useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setSuppressClose(true);
        return dataSource;
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManagerFactory.setEntityManagerInterface(HibernateEntityManager.class);
        entityManagerFactory.setDataSource(dataSource);

        Properties properties = new Properties();

        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        properties.setProperty("hibernate.cache.provider_class",
                org.hibernate.cache.ehcache.StrategyRegistrationProviderImpl.class.getName()
        );
        properties.setProperty("hibernate.cache.region.factory_class",
                org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory.class.getName()
        );

        entityManagerFactory.setJpaProperties(properties);

        entityManagerFactory.setPackagesToScan("vorona.andriy.model");

        entityManagerFactory.afterPropertiesSet();
        return entityManagerFactory;
    }

    @Bean
    @Autowired
    public HibernateJpaSessionFactoryBean sessionFactory(EntityManagerFactory entityManagerFactory) {
        HibernateJpaSessionFactoryBean sessionFactoryBean = new HibernateJpaSessionFactoryBean();
        sessionFactoryBean.setEntityManagerFactory(entityManagerFactory);
        return sessionFactoryBean;
    }

//    @Bean
//    @Autowired
//    public Session session(SessionFactory sessionFactory) {
//        try {
//            return sessionFactory.getCurrentSession();
//        } catch (HibernateException e) {
//            return sessionFactory.openSession();
//        }
//    }

//    @Bean
//    @Autowired
//    public PlatformTransactionManager transactionManager(SessionFactory sessionFactory, DataSource dataSource) {
//        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
//
//        transactionManager.setDataSource(dataSource);
//        transactionManager.setNestedTransactionAllowed(true);
//        transactionManager.setRollbackOnCommitFailure(true);
//        transactionManager.afterPropertiesSet();
//
//        return transactionManager;
//    }

    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager(entityManagerFactory);
        transactionManager.setGlobalRollbackOnParticipationFailure(true);
        transactionManager.setRollbackOnCommitFailure(true);
        transactionManager.setTransactionSynchronization(AbstractPlatformTransactionManager.SYNCHRONIZATION_ALWAYS);
        transactionManager.setFailEarlyOnGlobalRollbackOnly(true);
        transactionManager.setNestedTransactionAllowed(true);
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
