package com.m2m.management.controller;

import com.m2m.management.entity.Repo;
import com.m2m.management.entity.User;
import com.m2m.management.repository.IRepoBean;
import com.m2m.management.repository.IUserBean;
import com.m2m.management.utils.response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class RepoController {
    private static final Logger logger = LoggerFactory.getLogger(RepoController.class);
    @Autowired
    private IRepoBean repoService;

    @Autowired
    private IUserBean userService;

    @RequestMapping(value = "/repo", method = RequestMethod.GET)
    public ResponseEntity<List<Repo>> getRepo(
            @RequestParam(name="keywords") String keywords,
            @RequestParam(name="currentpage") int currentpage,
            @RequestParam(name="limit") int limit) {
        logger.info(keywords);
        Pageable pageable = new PageRequest(currentpage, limit, Sort.Direction.DESC,"rid");
        List<Repo> repos = repoService.findByReponameContaining(keywords, pageable);
        if (repos.isEmpty()) {
            return new ResponseEntity<List<Repo>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity(response.success(repos), HttpStatus.OK);
    }

    @RequestMapping(value = "/repo/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Repo> getRepoById(@PathVariable("id") long id){
        try{
            Optional<Repo> oprepo = repoService.findById(id);
            Repo repo = oprepo.get();
            return new ResponseEntity(response.success(repo), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/repo", method = RequestMethod.POST)
    public ResponseEntity<Void> createRepo(@RequestBody Repo repo){
        try{
            User u = null;
            long uid = repo.getUid();
            Optional<User> opu = userService.findById(uid);
            u = opu.get();
            repo.setUser(u);
            repo.setDarkname(DigestUtils.md5DigestAsHex(repo.getReponame().getBytes()));
            repoService.save(repo);
            return new ResponseEntity(response.success(), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(response.error("uid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @RequestMapping(value = "/repo/{id}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateRepo(@PathVariable("rid") long rid, @RequestBody Repo repo){
        try{
            Optional<Repo> oprp = repoService.findById(rid);
            Repo rp = oprp.get();
            rp.setDarkname(repo.getDarkname());
            rp.setDescription(repo.getDescription());
            rp.setReponame(repo.getReponame());
            repoService.save(rp);
            return new ResponseEntity(response.success(), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/repo/{rid}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoById(@PathVariable("rid") long rid){
         try{
             repoService.deleteByRid(rid);
             return new ResponseEntity(response.success(), HttpStatus.OK);
         }catch(NullPointerException e){
             return new ResponseEntity(response.error("rid not found"), HttpStatus.NOT_FOUND);
         }catch(Exception e){
             logger.error(e.getMessage());
             return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
         }

    }

    @RequestMapping(value="/repo", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoAll(){
        try{
            repoService.deleteAll();
            return new ResponseEntity(response.success(), HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
