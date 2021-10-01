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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
public class EmptyUserListController extends BaseController implements Validator {
    @Autowired
    private Persist persist;
    EmailValidator validator = EmailValidator.getInstance();

    @RequestMapping(value = "/user/add", method = RequestMethod.POST)
    public BasicResponseModel addUser(@ModelAttribute("User") User user) {
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
}
