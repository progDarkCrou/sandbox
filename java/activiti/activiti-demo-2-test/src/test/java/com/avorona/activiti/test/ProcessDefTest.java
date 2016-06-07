package com.avorona.activiti.test;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by avorona on 27.05.16.
 */
public class ProcessDefTest extends TestBase {

    @Test
    public void can_deploy_one_definition() {
        RepositoryService repositoryService = rule.getRepositoryService();

        long beforeCount = repositoryService.createDeploymentQuery().count();
        Deployment deploy = repositoryService
                .createDeployment()
                .addClasspathResource("test-proc-def.bpmn20.xml")
                .category("http://www.activiti.org/processdef")
                .name("Test deployment 1")
                .deploy();
        long afterCount = repositoryService.createDeploymentQuery().count();
        assertThat(afterCount - beforeCount).isEqualTo(1);
        assertThat(repositoryService.createDeploymentQuery().deploymentId(deploy.getId()).count()).isEqualTo(1);
    }

}
