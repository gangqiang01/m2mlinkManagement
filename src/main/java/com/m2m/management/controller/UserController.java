package com.m2m.management.controller;

import com.m2m.management.entity.User;
import com.m2m.management.repository.IUserBean;
import com.m2m.management.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.util.DigestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    IUserBean userService;


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers(
            @RequestParam(name="keywords", required = false, defaultValue ="") String keywords,
            @RequestParam(name="currentpage", required = false, defaultValue ="0") int currentpage,
            @RequestParam(name="limit", required = false, defaultValue ="10") int limit) {
        logger.info(keywords);
        Pageable pageable = new PageRequest(currentpage, limit, Sort.Direction.ASC, "ts");
        List<User> users = userService.findByNameContaining(keywords, pageable);
        return new ResponseEntity(Response.success(users), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser(@PathVariable("id") long id) {
        logger.info("Fetching User with id " + id);
        try{
            Optional<User> opuser  = userService.findById(id);
            try{
                User user = opuser.get();
                return new ResponseEntity(Response.success(user), HttpStatus.OK);
            }catch(NoSuchElementException e){
                return new ResponseEntity(Response.error("uid is error"), HttpStatus.NOT_FOUND);
            }
        }catch(Exception e){
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User " + user.getName());
        User u = null;
        try{
            user.setPasswd(DigestUtils.md5DigestAsHex(user.getPasswd().getBytes()));
            user.setTs(new Date().getTime());
            userService.save(user);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("username already exists"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        logger.info("Updating User " + id);
        try{
            Optional<User> opuser = userService.findById(id);
            User currentUser = opuser.get();
            currentUser.setName(user.getName());
            currentUser.setPasswd(DigestUtils.md5DigestAsHex(user.getPasswd().getBytes()));
            currentUser.setTs(new Date().getTime());
            userService.save(currentUser);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("user not found"), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) {
        logger.info("Fetching & Deleting User with id " + id);
        try{
            userService.deleteById(id);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch(NullPointerException e){
            return new ResponseEntity(Response.error("user not found"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAllUsers() {
        logger.info("Deleting All Users");

        userService.deleteAll();
        return new ResponseEntity(Response.success(), HttpStatus.OK);
    }
}
