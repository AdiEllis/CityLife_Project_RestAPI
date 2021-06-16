package com.project.Controllers;

import com.project.Main;
import com.project.Models.User;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.Definitions;
import com.project.Utils.PasswordAuthentication;
import com.project.Utils.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.util.List;


@RestController
@Transactional
public class UsersController implements Validator {
    @Autowired
    private Persist persist;
    private PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @PostConstruct
    public void init() {

    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public User addUser(@ModelAttribute("User") User user) { //apply all User's props auto.
        persist.save(user);
        return user;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public User updateUser(@ModelAttribute("User") User user) {
        User oldUser = persist.loadObject(User.class, user.getId());
        oldUser.setObject(user);
        persist.save(oldUser);
        return oldUser;
    }

    @GetMapping("/users/getUser")
    public User getUser(@RequestParam int id) {
        return persist.loadObject(User.class, id);
    }


    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public BasicResponseModel getUser(@RequestParam String email, @RequestParam char[] password) {
        BasicResponseModel responseModel;
        List<User> user = persist.getQuerySession().createQuery("FROM User WHERE email = :email")
                .setParameter("email", email)
                .list();
        if (user.isEmpty()) {
            responseModel = new BasicResponseModel(Definitions.LOGIN_FAILED_NO_EXISTS, Definitions.LOGIN_FAILED_NO_EXISTS_MSG);
        } else {
            if (user.size() > 1) {
                responseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                if (passwordAuthentication.authenticate(password, user.get(0).getPassword().toString())) {
                    responseModel = new BasicResponseModel(passwordAuthentication.createLoginToken());
                } else {
                    responseModel = new BasicResponseModel(Definitions.LOGIN_FAILED_WRONG_PASSWORD, Definitions.LOGIN_FAILED_WRONG_PASSWORD_MSG);
                }
            }
        }
        LOGGER.info(String.valueOf(passwordAuthentication.authenticate(password, passwordAuthentication.hash(password))));
        return responseModel;
    }

    @GetMapping("/users/getAllUsers")
    public List<User> getAllUsers() {
        List<User> allUsers = persist.getQuerySession().createQuery("FROM User").list();
        return allUsers;
    }

    public User getUserByPhone(String phone) {
        return (User) persist.getQuerySession().createQuery("FROM User WHERE phone = :phone")
                .setParameter("phone", phone)
                .uniqueResult();
    }

    @GetMapping("/users/deleteUser")
    public User deleteUser(@RequestParam int id, @RequestParam boolean delete) {
        User user = persist.loadObject(User.class, id);
        user.setDeleted(delete);
        persist.save(user);
        return user;
    }

}
