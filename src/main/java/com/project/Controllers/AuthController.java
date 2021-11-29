package com.project.Controllers;

import com.project.Models.User;
import com.project.Objects.Entities.AuthUser;
import com.project.Objects.Entities.BasicResponseModel;
import com.project.Persist;
import com.project.Utils.Definitions;
import com.project.Utils.PasswordAuthentication;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Transactional
public class AuthController extends BaseController {

    @Autowired private Persist persist;

    EmailValidator validator = EmailValidator.getInstance();

    @RequestMapping(value = "/authenticate/login", method = RequestMethod.POST)
    public BasicResponseModel getUser(@RequestParam String email, @RequestParam char[] password) {
        BasicResponseModel responseModel;
        if (email.length() == 0 || password.length == 0) {
            responseModel = new BasicResponseModel(Definitions.MISSING_FIELDS, Definitions.MISSING_FIELDS_MSG);
        } else {
            if (!validator.isValid(email)) {
                responseModel = new BasicResponseModel(Definitions.INVALID_EMAIL, Definitions.INVALID_EMAIL_MSG);
            }else{
                List<User> user = persist.getQuerySession().createQuery("FROM User WHERE email = :email")
                        .setParameter("email", email)
                        .list();
                if (user.isEmpty()) {
                    responseModel = new BasicResponseModel(Definitions.LOGIN_FAILED_NO_EXISTS, Definitions.LOGIN_FAILED_NO_EXISTS_MSG);
                } else {
                    if (user.size() > 1) {
                        responseModel = new BasicResponseModel(Definitions.MULTI_RECORD, Definitions.MULTI_RECORD_MSG);
                    } else {
                        if (PasswordAuthentication.hashPassword(password).equals(user.get(0).getPassword())) { //login succsessful
                            String newToken = PasswordAuthentication.createLoginToken(user.get(0).getEmail(), user.get(0).getPassword());
                            User userRow = persist.loadObject(User.class, user.get(0).getOid());
                            userRow.setToken(newToken);
                            persist.save(userRow);
                            AuthUser authUser = new AuthUser(
                                    userRow.getOid(),
                                    userRow.getToken(),
                                    userRow.getEmail(),
                                    userRow.getColonyID(),
                                    userRow.getIsAdmin()
                            );
                            responseModel = new BasicResponseModel(authUser);
                        } else {
                            responseModel = new BasicResponseModel(Definitions.LOGIN_FAILED_WRONG_PASSWORD, Definitions.LOGIN_FAILED_WRONG_PASSWORD_MSG);
                        }
                    }
                }
            }
        }
        return responseModel;
    }
}
