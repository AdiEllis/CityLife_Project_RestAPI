package com.project.Controllers;

import com.project.Models.User;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.Definitions;
import com.project.Utils.PasswordAuthentication;
import com.project.Utils.Validator;
import org.apache.commons.validator.routines.EmailValidator;
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
    EmailValidator validator = EmailValidator.getInstance();

    @PostConstruct
    public void init() {}

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public BasicResponseModel addUser(@ModelAttribute("User") User user) { //apply all User's props auto.
        BasicResponseModel responseModel;
        if (user.objectIsEmpty()) {
            responseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
        } else {
            if (!validator.isValid(user.getEmail())) {
                responseModel = new BasicResponseModel(Definitions.INVALID_EMAIL, Definitions.INVALID_EMAIL_MSG);
            } else {
                List<User> userList = persist.getQuerySession().createQuery("FROM User WHERE email = :email")
                        .setParameter("email", user.getEmail())
                        .list();
                if (userList.isEmpty()) {
                    user.setPassword(PasswordAuthentication.hashPassword(user.getPassword()));
                    persist.save(user);
                    responseModel = new BasicResponseModel(user);
                } else {
                    responseModel = new BasicResponseModel(Definitions.EMAIL_EXISTS, Definitions.EMAIL_EXISTS_MSG);
                }
            }
        }
        return responseModel;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public BasicResponseModel updateUser(@ModelAttribute("User") User user) {
        BasicResponseModel responseModel;
        user.setPassword(null); //unable to update password in this route.

        List<User> userRow = persist.getQuerySession().createQuery("FROM User WHERE id = :id")
                .setParameter("id", user.getId())
                .list();
        if (userRow.isEmpty()) {
            responseModel = new BasicResponseModel(Definitions.USER_NOT_FOUND, Definitions.USER_NOT_FOUND_MSG);
        } else if (userRow.size() > 1) {
            responseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
        } else {
            User oldUser = persist.loadObject(User.class, user.getId());
            if (user.getEmail() != null && !user.getEmail().equals(userRow.get(0).getEmail())) {
                if (!validator.isValid(user.getEmail())) {
                    responseModel = new BasicResponseModel(Definitions.INVALID_EMAIL, Definitions.INVALID_EMAIL_MSG);
                } else {
                    List<User> userList = persist.getQuerySession().createQuery("FROM User WHERE email = :email")
                            .setParameter("email", user.getEmail())
                            .list();
                    if (userList.size() > 0) {
                        responseModel = new BasicResponseModel(Definitions.EMAIL_EXISTS, Definitions.EMAIL_EXISTS_MSG);
                    } else {
                        oldUser.setObject(user);
                        persist.save(oldUser);
                        responseModel = new BasicResponseModel(oldUser);
                    }
                }
            } else {
                oldUser.setObject(user);
                persist.save(oldUser);
                responseModel = new BasicResponseModel(oldUser);
            }
        }
        return responseModel;
    }

    @RequestMapping(value = "/users/updateUserPassword", method = RequestMethod.POST)
    public BasicResponseModel updateUserPassword(@RequestParam int id, @RequestParam String password) {
        BasicResponseModel responseModel;
        if (id < 0 || password.length() == 0) {
            responseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
        } else {

            List<User> userRow = persist.getQuerySession().createQuery("FROM User WHERE id = :id")
                    .setParameter("id", id)
                    .list();
            if (userRow.isEmpty()) {
                responseModel = new BasicResponseModel(Definitions.USER_NOT_FOUND, Definitions.USER_NOT_FOUND_MSG);
            } else if (userRow.size() > 1) {
                responseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                User oldUser = persist.loadObject(User.class, id);
                oldUser.setPassword(PasswordAuthentication.hashPassword(password));
                responseModel = new BasicResponseModel(oldUser);
            }
        }
        return responseModel;
    }

    @RequestMapping(value = "/users/getUser", method = RequestMethod.GET)
    public BasicResponseModel getUser(@RequestParam int id) {
        BasicResponseModel responseModel;
        if(id < 0){
            responseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
        }else{
            List<User> userRow = persist.getQuerySession().createQuery("FROM User WHERE id = :id")
                    .setParameter("id", id)
                    .list();
            if (userRow.isEmpty()) {
                responseModel = new BasicResponseModel(Definitions.USER_NOT_FOUND, Definitions.USER_NOT_FOUND_MSG);
            } else if (userRow.size() > 1) {
                responseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                responseModel = new BasicResponseModel(persist.loadObject(User.class, id));
            }
        }
        return responseModel;
    }


    @RequestMapping(value = "/users/login", method = RequestMethod.POST)
    public BasicResponseModel getUser(@RequestParam String email, @RequestParam char[] password) {
        BasicResponseModel responseModel;
        if(email.length() == 0 || password.length == 0){
            responseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
        }else {
            List<User> user = persist.getQuerySession().createQuery("FROM User WHERE email = :email")
                    .setParameter("email", email)
                    .list();
            if (user.isEmpty()) {
                responseModel = new BasicResponseModel(Definitions.LOGIN_FAILED_NO_EXISTS, Definitions.LOGIN_FAILED_NO_EXISTS_MSG);
            } else {
                if (user.size() > 1) {
                    responseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                } else {
                    if (PasswordAuthentication.hashPassword(password).equals(user.get(0).getPassword())) {
                        String newToken = PasswordAuthentication.createLoginToken(user.get(0).getEmail(), user.get(0).getPassword());
                        User userRow = persist.loadObject(User.class, user.get(0).getId());
                        userRow.setToken(newToken);
                        responseModel = new BasicResponseModel(newToken);
                    } else {
                        responseModel = new BasicResponseModel(Definitions.LOGIN_FAILED_WRONG_PASSWORD, Definitions.LOGIN_FAILED_WRONG_PASSWORD_MSG);
                    }
                }
            }
        }
        return responseModel;
    }

    @RequestMapping(value = "/users/getAllUsers", method = RequestMethod.GET)
    public BasicResponseModel getAllUsers() {
        BasicResponseModel responseModel;
        List<User> allUsers = persist.getQuerySession().createQuery("FROM User").list();
        if (allUsers.isEmpty()) {
            responseModel = new BasicResponseModel(Definitions.EMPTY_LIST, Definitions.EMPTY_LIST_MSG);
        } else {
            responseModel = new BasicResponseModel(allUsers);
        }
        return responseModel;
    }

    @RequestMapping(value = "/users/deleteUser", method = RequestMethod.GET)
    public BasicResponseModel deleteUser(@RequestParam int id, @RequestParam boolean delete) {
        BasicResponseModel responseModel;
        if(id < 0){
            responseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
        }else{
            List<User> userRow = persist.getQuerySession().createQuery("FROM User WHERE id = :id")
                    .setParameter("id", id)
                    .list();
            if (userRow.isEmpty()) {
                responseModel = new BasicResponseModel(Definitions.USER_NOT_FOUND, Definitions.USER_NOT_FOUND_MSG);
            } else if (userRow.size() > 1) {
                responseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
            } else {
                User user = persist.loadObject(User.class, id);
                user.setDeleted(delete);
                persist.save(user);
                responseModel = new BasicResponseModel(user);
            }
        }
        return responseModel;
    }

}
