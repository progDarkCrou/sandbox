package com.avorona.activiti.test;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.junit.Test;

/**
 * Created by avorona on 27.05.16.
 */
public class ProcessDefTest extends TestBase {

    @Test
    public void can_deploy_one_definition() {
        Deployment deploy = rule.getRepositoryService()
                .createDeployment()
                .addClasspathResource("test-proc-def.bpmn20.xml")
                .category("http://www.activiti.org/processdef")
                .name("Test deployment 1")
                .deploy();

        Model model = rule.getRepositoryService().newModel();
        model.setDeploymentId(deploy.getId());
        model.setName(deploy.getName() + " model");
        model.setKey(deploy.getId() + "key");
        rule.getRepositoryService().saveModel(model);
    }

}
