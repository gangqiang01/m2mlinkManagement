package com.m2m.management.controller;

import com.m2m.management.entity.User;
import com.m2m.management.repository.IUserBean;
import com.m2m.management.utils.response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    IUserBean userService;


    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity(response.success(users), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        System.out.println("Fetching User with id " + id);
        Optional<User> opuser  = userService.findById(id);

        if (opuser.get() == null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(response.success(opuser.get()), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + user.getName());

        if (userService.existsById(user.getUid())) {
            System.out.println("A User with name " + user.getName() + " already exist");
            return new ResponseEntity(response.error("username alread exit"), HttpStatus.CONFLICT);
        }
        user.setPasswd(DigestUtils.md5DigestAsHex(user.getPasswd().getBytes()));

        userService.save(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getUid()).toUri());
        return new ResponseEntity(response.success(), headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        System.out.println("Updating User " + id);

        Optional<User> opuser = userService.findById(id);


        if (opuser==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity(response.error("user not found"), HttpStatus.NOT_FOUND);
        }
        User currentUser = opuser.get();
        currentUser.setName(user.getName());
        currentUser.setPasswd(DigestUtils.md5DigestAsHex(user.getPasswd().getBytes()));
        currentUser.setTs(user.getTs());

        userService.save(currentUser);
        return new ResponseEntity(response.success(currentUser), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting User with id " + id);
        Optional<User> user = userService.findById(id);
        if(user == null){
            return new ResponseEntity(response.error("user not found"), HttpStatus.NOT_FOUND);
        }
        userService.deleteById(id);
        return new ResponseEntity(response.success(), HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
        System.out.println("Deleting All Users");

        userService.deleteAll();
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
}
