package com.avorona.activiti.test;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.logging.log4j.message.FormattedMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by avorona on 27.05.16.
 */

public class ProcessInstTest extends TestBase {

    @Before
    public void before_create_1_process_instance() {
        List<ProcessDefinition> definitions = rule.getRepositoryService()
                .createProcessDefinitionQuery()
                .orderByProcessDefinitionKey().asc()
                .orderByProcessDefinitionVersion().desc()
                .list();

        logger.info(new FormattedMessage("Process definitions amount: %s", definitions.size()));
        ProcessDefinition processDefinition = definitions.get(0);

        logger.info("Going to create process instance for definition id: " + processDefinition.getId());
        rule.getRuntimeService().startProcessInstanceById(processDefinition.getId());
    }

    @Test
    public void is_at_least_1_process_instance() {
        List<ProcessInstance> processInstances = rule.getRuntimeService().createProcessInstanceQuery().list();
        assertThat(processInstances.size()).isGreaterThanOrEqualTo(1);
        logger.info("Processes instances are: " + processInstances.size());
    }

    @Test
    public void can_delete_1_process_instance() {
        List<ProcessInstance> processInstances1 = rule.getRuntimeService().createProcessInstanceQuery().list();
        logger.info("Processes instances: " + processInstances1);

        if (processInstances1.size() > 0) {
            ProcessInstance processInstance = processInstances1.get(0);
            logger.info(new FormattedMessage("Deleting process instance with id: %s", processInstance.getId()));
            rule.getRuntimeService().deleteProcessInstance(processInstance.getId(), null);

            List<ProcessInstance> processInstances2 = rule.getRuntimeService().createProcessInstanceQuery().list();
            assertThat(processInstances1.size()).isGreaterThan(processInstances2.size());
        } else {
            logger.info("No process instances to delete. Skipping test...");
        }
    }

    @Test
    public void can_delete_all_process_instances() {
        List<ProcessInstance> processInstances = rule.getRuntimeService().createProcessInstanceQuery().list();

        processInstances.forEach(processInstance -> {
            rule.getRuntimeService().deleteProcessInstance(processInstance.getId(), null);
        });

        List<ProcessInstance> processInstances1 = rule.getRuntimeService().createProcessInstanceQuery().list();

        assertThat(processInstances1.size()).isEqualTo(0);
        assertThat(processInstances.size()).isGreaterThan(processInstances1.size());
    }
}
