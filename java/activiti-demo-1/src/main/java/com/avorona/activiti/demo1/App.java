package com.avorona.activiti.demo1;

import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

/**
 * Created by avorona on 24.05.16.
 */
public class App {
    public static void main(String[] args) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        IdentityService identityService = engine.getIdentityService();
        TaskService taskService = engine.getTaskService();
        RuntimeService runtimeService = engine.getRuntimeService();

        //Get all process definitions and display them
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
        System.out.println("Processes definitions: ");
        processDefinitions.forEach(App::printProcesses);

        //Get all users info
        List<User> users = identityService.createUserQuery().list();
        System.out.println("Existing users: ");
        users.forEach(App::printUser);

        System.out.println(identityService.createUserQuery().userId("1000").list());

        Optional<Map.Entry<String, List<ProcessDefinition>>> demoProcOpt = processDefinitions.stream()
                .collect(Collectors.groupingBy(ProcessDefinition::getName))
                .entrySet()
                .parallelStream()
                .filter(definition -> definition.getKey().toLowerCase().contains("demo"))
                .findFirst();

        if (!demoProcOpt.isPresent()) {
            System.out.println("Cannot find process with \"demo\" string in name");
            System.exit(1);
        }

        //Sorting for further simplicity
        List<ProcessDefinition> demoProcDefVersion = demoProcOpt.get().getValue();
        demoProcDefVersion
                .sort(Collections.reverseOrder(comparing(ProcessDefinition::getVersion)));

        ProcessDefinition demoProc = demoProcDefVersion.get(0);

        ProcessInstance demoProcInst = runtimeService.createProcessInstanceBuilder()
                .processDefinitionId(demoProc.getId())
                .start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Integer claimUserId;
        try {
            System.out.println("Please, provide user id, to which you want to assign a task: ");
            claimUserId = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            engine.close();
            throw new RuntimeException(e);
        }

        Task task = null;
        try {
            task = taskService.createTaskQuery()
                    .processInstanceId(demoProcInst.getId())
                    .singleResult();
        } catch (ActivitiException e) {
            System.out.println(String.format("Process #%s has no tasks", demoProc.getId()));
            engine.close();
            System.exit(2);
        }
        taskService.claim(task.getId(), claimUserId.toString());

        System.out.println(task.getAssignee());

        engine.close();
    }

    private static void printProcesses(ProcessDefinition definition) {
        System.out.println("\tProcess id: \t" + definition.getId());
        System.out.println("\tProcess title: \t" + definition.getName());
        System.out.println();
    }

    private static void printUser(User user) {
        System.out.println("\tUser name: \t" + user.getFirstName());
        System.out.println("\tUser last name: \t" + user.getLastName());
        System.out.println();
    }
}
