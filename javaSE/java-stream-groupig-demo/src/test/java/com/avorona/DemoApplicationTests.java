package com.avorona;

import com.avorona.model.Country;
import com.avorona.model.State;
import com.avorona.repository.CountryRepository;
import com.avorona.repository.StateRepository;
import com.sun.xml.internal.stream.StaxErrorReporter;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class DemoApplicationTests {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    @Before
    public void beforeFillData() {
        countryRepository.deleteAll();
        stateRepository.deleteAll();
        for (int i = 0; i < 10; i++) {
            Country country = new Country();
            country.setYear((int) (Math.random() * 10 + i));
            country.setTitle("Title " + country.getYear());
            country.setCode(String.valueOf(i));

            country.setStates(new ArrayList<>(20));

            countryRepository.save(country);

            for (int i1 = 0; i1 < 20; i1++) {
                State state = new State();

                state.setYear((int) (Math.random() * 8 + i1));
                state.setTitle("State " + state.getYear());
                state.setCountry(country);

                country.getStates().add(state);
                stateRepository.save(state);
            }

            countryRepository.save(country);
        }
    }

    @Test
    public void savedTest() {
        assertThat(countryRepository.findAll()).hasSize(100);
        assertThat(stateRepository.findAll()).hasSize(2000);
    }

    @Test
    public void groupingTest() {
        List<State> all = stateRepository.findAll();

        Map<BigInteger, LinkedList<LinkedList<State>>> groupedStates = all.stream()
                .collect(Collectors.groupingBy(t -> t.getCountry().getId(), Collector.of(() -> {
                            LinkedList<LinkedList<State>> objects = new LinkedList<>();
                            objects.add(new LinkedList<>());
                            return objects;
                        },
                        (objects, state) -> {
                            LinkedList<State> last = objects.getLast();
                            if (last.size() > 0 && last.getLast().getYear() - state.getYear() >= 4) {
                                last = new LinkedList<>();
                                objects.add(last);
                            }
                            last.add(state);
                        }, (objects, objects2) -> {
                            objects.addAll(objects2);
                            return objects;
                        })));

        assertThat(groupedStates).hasSize(10);

        assertThat(groupedStates.entrySet()
                .parallelStream()
                .peek(group -> {
                    System.out.println("Country #" + group.getKey() + "\n\t group count - " + group.getValue().size());
                })
                .flatMap(group -> group.getValue().stream())
                .flatMap(Collection::parallelStream)
                .collect(Collectors.toList())).hasSize(200);
    }
}
