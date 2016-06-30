package com.avorona.schedule;

import com.avorona.model.CountryEntity;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by avorona on 30.06.16.
 */
@Service
public class CountriesScheduler {

  private static final Logger log = LoggerFactory.getLogger(CountriesScheduler.class);

  @Autowired
  private Session session;

  //  @Scheduled(fixedRate = 11 * 1000)
  public void getCountries() {
    while (true) {
      try {
        Thread.sleep(11 * 1000);
        CountryEntity country = session.get(CountryEntity.class, 1L);
        log.info("Fetched country: {}", country);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

//  @Scheduled(fixedRate = 11 * 1000)
//  public void getCountriesConcurrently() {
//    CompletableFuture.supplyAsync(() -> {
//      CountryEntity country = session.get(CountryEntity.class, 1L);
//      return country;
//    }).thenAccept(country -> {
//      log.info("Fetched country concurrently: {}", country);
//    });
//  }
}
