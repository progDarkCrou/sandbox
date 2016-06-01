package com.avorona.service;

import com.avorona.domain.model.User;
import com.avorona.exception.AlreadyDefined;
import com.avorona.exception.UnableToFindException;
import com.avorona.web.UserRequest;

/**
 * Created by avorona on 01.06.16.
 */
public interface UserService {

    boolean exists(String id);

    User find(Long id) throws UnableToFindException;

    User create(UserRequest request) throws AlreadyDefined;

    User update(Long id, UserRequest request) throws UnableToFindException;

    void delete(User user) throws UnableToFindException;

    void delete(Long id) throws UnableToFindException;
}
