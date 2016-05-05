package com.avorona;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.net.UnknownHostException;
import java.text.DateFormat;

@SpringBootApplication
@EnableMongoRepositories
public class SpringInitializerDemoApplication {

    @Bean
    public MongoClient mongoClient() throws UnknownHostException {
        return new MongoClient();
    }

    @Bean
    public MongoTemplate mongoTemplate() throws UnknownHostException {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), "image_db");
        mongoTemplate.setWriteConcern(WriteConcern.ACKNOWLEDGED);
        mongoTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
        return mongoTemplate;
    }

    @Bean
    @Order(-999999999)
    public CharacterEncodingFilter characterEncodingFilter(){
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(false);

        return characterEncodingFilter;
    }

    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.dateFormat(DateFormat.getDateTimeInstance());
        return builder.build();
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringInitializerDemoApplication.class, args);
	}
}
