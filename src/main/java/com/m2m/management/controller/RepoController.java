package com.m2m.management.controller;

import com.m2m.management.Resource.DeployResource;
import com.m2m.management.Resource.RepoResource;
import com.m2m.management.entity.Repo;
import com.m2m.management.entity.User;
import com.m2m.management.repository.IRepoBean;
import com.m2m.management.repository.IUserBean;
import com.m2m.management.utils.FileUtil;
import com.m2m.management.utils.Response;
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

import java.io.File;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class RepoController {
    private static final Logger logger = LoggerFactory.getLogger(RepoController.class);
    private String baseRepoPath = DeployResource.BASEDEPLOYPATH+ RepoResource.TYPE;
    private String pathSeparate = File.separator;


    @Autowired
    private IRepoBean repoService;

    @Autowired
    private IUserBean userService;

    @RequestMapping(value = "/repo", method = RequestMethod.GET)
    public ResponseEntity<List<Repo>> getRepo(
            @RequestParam(name="keywords", required = false, defaultValue ="") String keywords,
            @RequestParam(name="currentpage", required = false, defaultValue ="0") int currentpage,
            @RequestParam(name="limit", required = false, defaultValue ="10") int limit) {
        logger.info(keywords);
        Pageable pageable = new PageRequest(currentpage, limit, Sort.Direction.DESC,"rid");
        List<Repo> repos = repoService.findByReponameContaining(keywords, pageable);
        return new ResponseEntity(Response.success(repos), HttpStatus.OK);
    }

    @RequestMapping(value = "/repo/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Repo> getRepoById(@PathVariable("id") long id){
        try{
            Optional<Repo> oprepo = repoService.findById(id);
            Repo repo = oprepo.get();
            return new ResponseEntity(Response.success(repo), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/repo/{reponame}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Repo> getRepoByName(@PathVariable("reponame") String reponame){
        try{
            List<Repo> repos = repoService.findByReponame(reponame);
            Repo repo = repos.get(0);
            return new ResponseEntity(Response.success(repo), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/repo", method = RequestMethod.POST)
    public ResponseEntity<Void> createRepo(@RequestBody Repo repo){
        try{
            Boolean isCreatApkRepo = false;
            String darkname = DigestUtils.md5DigestAsHex(repo.getReponame().getBytes());
            File baseDeplyFile = new File(DeployResource.BASEDEPLOYPATH);
            logger.info("baseRepoPath:"+baseRepoPath+"/baseIsExist:"+baseDeplyFile.exists());
            if(baseDeplyFile.exists()){
                isCreatApkRepo = FileUtil.createDir(baseRepoPath + pathSeparate + darkname);
            }else{
                isCreatApkRepo = false;
            }
            if(isCreatApkRepo){
                long uid = repo.getUid();
                Optional<User> opu = userService.findById(uid);
                User u = opu.get();
                repo.setUser(u);
                repo.setDarkname(darkname);
                repoService.save(repo);
                return new ResponseEntity(Response.success(), HttpStatus.OK);
            }else{
                return new ResponseEntity(Response.error("create repo error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }catch(NoSuchElementException e){
            return new ResponseEntity(Response.error("uid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @RequestMapping(value = "/repo/{rid}", method = RequestMethod.POST)
    public ResponseEntity<Void> updateRepo(@PathVariable("rid") long rid, @RequestBody Repo repo){
        try{
            Optional<Repo> oprp = repoService.findById(rid);
            Repo rp = oprp.get();
            rp.setDescription(repo.getDescription());
            rp.setRepoType(repo.getRepoType());
            repoService.save(rp);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/repo/{rid}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoById(@PathVariable("rid") long rid){
         try{
             repoService.deleteById(rid);
             Boolean isDelete = false;
             Repo repo = repoService.findById(rid).get();
             String darkname = repo.getDarkname();
             logger.info(darkname);
             File baseRepoFile = new File(baseRepoPath);
             if(baseRepoFile.exists()){
                 File apkRepoFile = new File(baseRepoFile + File.separator + darkname);
                 if(apkRepoFile.exists()){
                      isDelete = FileUtil.delDir(baseRepoPath + File.separator + darkname);
                 }else{
                     isDelete = false;
                 }
             }
             if(isDelete){
                 return new ResponseEntity(Response.success(), HttpStatus.OK);
             }else{
                 return new ResponseEntity(Response.error("delete repo dir error"), HttpStatus.NOT_FOUND);
             }

         }catch(NullPointerException e){
             return new ResponseEntity(Response.error("rid not found"), HttpStatus.NOT_FOUND);
         }catch(Exception e){
             logger.error(e.getMessage());
             return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
         }

    }

    @RequestMapping(value="/repo", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoAll(){
        try{
            repoService.deleteAll();
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
