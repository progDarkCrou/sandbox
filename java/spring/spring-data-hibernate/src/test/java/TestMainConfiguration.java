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

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by avorona on 28.12.15.
 */

@Configuration
@EnableTransactionManagement(mode = AdviceMode.PROXY, proxyTargetClass = true)
@EnableJpaRepositories
public class TestMainConfiguration {

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

    properties.setProperty("hibernate.dialect", org.hibernate.dialect.MySQL5InnoDBDialect.class.getName());
    properties.setProperty("hibernate.show_sql", "true");
    properties.setProperty("hibernate.format_sql", "true");
    properties.setProperty("hibernate.use_sql_comments", "true");

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
}
