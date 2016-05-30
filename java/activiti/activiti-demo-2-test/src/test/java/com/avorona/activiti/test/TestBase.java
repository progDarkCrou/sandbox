package com.avorona.activiti.test;

import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Rule;

import java.util.List;

/**
 * Created by avorona on 30.05.16.
 */
public class TestBase {

    protected Logger logger = LogManager.getLogger(this.getClass());

    @Rule
    public ActivitiRule rule = new ActivitiRule();

    @Before
    public void before_authenticate_user() {
        List<User> userList = rule.getIdentityService().createUserQuery().list();
        if (userList.size() > 0) {
            User user = userList.get(0);
            logger.info("Authenticated user id: " + user.getId());
            rule.getIdentityService().setAuthenticatedUserId(user.getId());
        } else {
            logger.info("No user to authenticate. Be careful in the further tests!!!");
        }
    }
}
