package com.m2m.management.controller;


import com.m2m.management.entity.User;
import com.m2m.management.repository.IUserBean;
import com.m2m.management.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class loginController {
    private static final Logger logger = LoggerFactory.getLogger(loginController.class);

    @Autowired
    IUserBean userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<Void> login(@RequestBody User user){

        String username = user.getName();
        String pwd = user.getPasswd();
        String md5pwd = DigestUtils.md5DigestAsHex(pwd.getBytes());
        try{
            User u = userService.findByName(username);
            if(u.getPasswd().equals(md5pwd)){
                return new ResponseEntity(Response.success(), HttpStatus.OK);
            }else{
                return new ResponseEntity(Response.error("password is error"), HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            return new ResponseEntity(Response.error("username or passwd is error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
