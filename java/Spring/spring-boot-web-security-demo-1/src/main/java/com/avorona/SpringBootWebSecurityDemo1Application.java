package com.avorona;

import com.mysql.jdbc.Driver;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.sql.DataSource;

@SpringBootApplication
@EnableGlobalMethodSecurity(securedEnabled = true)
@ImportResource("classpath:context.xml")
@EnableJpaRepositories(basePackages = "com.avorona.domain.repo")
public class SpringBootWebSecurityDemo1Application extends WebSecurityConfigurerAdapter {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.httpBasic()
            .and()
            .authorizeRequests()
                .anyRequest().authenticated()
            .and()
            .logout()
                .permitAll()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
            .and()
            .csrf();
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:4306/spring-test-db");

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan("com.avorona.domain.model");
        factory.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();
        return factory;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebSecurityDemo1Application.class, args);
    }
}
