package com.avorona;

import com.avorona.schedule.CountriesScheduler;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
@EnableScheduling
public class Application {

//  @Bean
//  public DataSource dataSource() {
//    SimpleDriverDataSource ds = new SimpleDriverDataSource();
//    ds.setDriverClass(com.mysql.jdbc.Driver.class);
//    ds.setUrl("jdbc:mysql://localhost:3306/spring-hibernate-demo-1?useSSL=false");
//    ds.setUsername("user");
//    ds.setPassword("password");
//    return ds;
//  }

  @Bean
//  @Autowired
  public SessionFactory sessionFactory(/*DataSource dataSource*/) throws IOException {
    LocalSessionFactoryBean sf = new LocalSessionFactoryBean();
    sf.setPackagesToScan("com.avorona.model");
    sf.setConfigLocation(new ClassPathResource("hibernate.cfg.xml"));
    sf.afterPropertiesSet();

    return sf.getObject();
  }

  @Bean
  @Scope("prototype")
  @Autowired
  public Session session(SessionFactory sessionFactory) throws IOException {
    try {
      return sessionFactory.getCurrentSession();
    } catch (HibernateException e) {
      return sessionFactory.openSession();
    }
  }

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
    CountriesScheduler countriesScheduler = context.getBean(CountriesScheduler.class);
    countriesScheduler.getCountries();
  }
}
