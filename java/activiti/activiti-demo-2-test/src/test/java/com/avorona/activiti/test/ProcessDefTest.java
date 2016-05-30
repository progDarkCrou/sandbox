package com.avorona.activiti.test;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.test.ActivitiRule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;

/**
 * Created by avorona on 27.05.16.
 */
public class ProcessDefTest {

    private Logger logger = LogManager.getLogger(ProcessInstTest.class);

    @Rule
    public ActivitiRule rule = new ActivitiRule();

    @Test
    public void can_deploy_one_definition() {
        Deployment deploy = rule.getRepositoryService()
                .createDeployment()
                .addClasspathResource("test-proc-def.xml")
                .name("Test deployment 1")
                .deploy();
    }

}
