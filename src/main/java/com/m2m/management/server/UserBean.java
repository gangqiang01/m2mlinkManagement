package com.m2m.management.server;
import com.m2m.management.entity.User;
import com.m2m.management.repository.IUserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class UserBean {
    @Autowired
    IUserBean iuserBean;

    public List<User> findUsersByNameLike(String keywords, Pageable pageable){
        List<User> users = iuserBean.findByNameContaining(keywords, pageable);
        return users;
    }


}
