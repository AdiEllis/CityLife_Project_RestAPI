package com.project.Controllers;

import com.project.Models.User;
import com.project.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Query;
import java.util.List;


@RestController
@Transactional
public class UsersController {
    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() { }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public User addUser(@ModelAttribute("User") User user) { //apply all User's props auto.
        persist.save(user);
        return user;
    }

    @RequestMapping(value = "/users/update", method = RequestMethod.POST)
    public User updateUser(@ModelAttribute("User") User user) {
        //persist.save(user);
        return user;
    }

    @GetMapping("/users/getUser")
    public User getUser(@RequestParam int id) {
        return persist.loadObject(User.class, id);
    }

    @GetMapping("/users/getAllUsers")
    public List<User> getAllUsers() {
        List<User> allUsers = persist.getQuerySession().createQuery("FROM User").list();
        return allUsers;
    }

    public User getUserByPhone (String phone) {
        return (User)persist.getQuerySession().createQuery("FROM User WHERE phone = :phone")
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
