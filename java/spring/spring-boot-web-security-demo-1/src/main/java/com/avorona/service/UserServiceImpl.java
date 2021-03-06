package com.avorona.service;

import com.avorona.domain.model.User;
import com.avorona.domain.repo.UserRepository;
import com.avorona.exception.AlreadyDefined;
import com.avorona.exception.UnableToFindException;
import com.avorona.exception.UserAlreadyDefined;
import com.avorona.web.UserRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * Created by avorona on 01.06.16.
 */
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Session session;

    @Override
    public boolean exists(String username) {
        return entityManager.createQuery("select count(u) from User u where u.username = :username", Long.class)
                .setParameter("username", username).getSingleResult() > 0;
    }

    @Override
    public User find(Long id) throws UnableToFindException {
        return entityManager.find(User.class, id);
    }

    @Override
    public User find(String username) throws UnableToFindException {
        return session.byNaturalId(User.class).using("username", username).load();
    }

    @Override
    public User create(UserRequest request) throws AlreadyDefined {
        if (exists(request.getUsername())) throw new UserAlreadyDefined(request.getUsername());
        User user = map(request);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    @Override
    public User update(Long id, UserRequest request) throws UnableToFindException {
        return map(request);
    }

    @Override
    public void delete(User user) throws UnableToFindException {
        if (user == null) {
            throw new UnableToFindException();
        }
        entityManager.remove(user);
    }

    @Override
    public void delete(Long id) throws UnableToFindException {
        this.delete(entityManager.getReference(User.class, id));
    }

    private User map(UserRequest userRequest) {
        User user = new User();
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(userRequest.getPassword());
        return user;
    }
}
