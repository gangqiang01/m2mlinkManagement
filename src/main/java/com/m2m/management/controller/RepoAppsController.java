package com.m2m.management.controller;

import com.m2m.management.entity.Repo;
import com.m2m.management.entity.RepoApps;
import com.m2m.management.repository.IRepoAppsBean;

import com.m2m.management.repository.IRepoBean;
import com.m2m.management.utils.GetApkInfo;
import com.m2m.management.utils.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class RepoAppsController {
    private static final Logger logger = LoggerFactory.getLogger(RepoAppsController.class);
    @Autowired
    private IRepoAppsBean repoAppsService;

    @Autowired
    private IRepoBean repoService;

    @RequestMapping(value = "/repoapps", method = RequestMethod.GET)
    public ResponseEntity<List<RepoApps>> getRepoApps(
            @RequestParam(name="keywords") String keywords,
            @RequestParam(name="currentpage") int currentpage,
            @RequestParam(name="limit") int limit) {
        logger.info("repoapp:"+keywords);
        Pageable pageable = new PageRequest(currentpage, limit, Sort.Direction.DESC, "rfid");
        List<RepoApps> repoApps = repoAppsService.findByFilenameContaining(keywords, pageable);
        if (repoApps.isEmpty()) {
            return new ResponseEntity<List<RepoApps>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity(response.success(repoApps), HttpStatus.OK);
    }

    @RequestMapping(value = "/repoapps/{rfid}", method = RequestMethod.GET)
    public ResponseEntity<RepoApps> getRepoAppsById(@PathVariable("rfid") long rfid){
        try{
            File file = new File("/D");
            Optional<RepoApps> opRepoApps = repoAppsService.findById(rfid);
            RepoApps repoApps = opRepoApps.get();
            return new ResponseEntity(response.success(repoApps), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/repoapps", method = RequestMethod.POST)
    public ResponseEntity<Void> createRepoApps(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "rid") long rid,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "license", required = false) String license,
            @RequestParam(value = "summary", required = false) String summary,
            @RequestParam(value = "website", required = false) String website
            ){
        try{
            File convFile = new File( System.getProperty("java.io.tmpdir")+file.getOriginalFilename());
            file.transferTo(convFile);
            Map<String, Object> apkInfo =  GetApkInfo.readApk(convFile);
            String filename = apkInfo.get("filename").toString();
            String pkgname = apkInfo.get("pkgname").toString();
            logger.info(pkgname);
            String versioncode = apkInfo.get("versioncode").toString();
            String versionname = apkInfo.get("versionname").toString();
            RepoApps repoApps = new RepoApps(filename, pkgname, versioncode, versionname);
            repoApps.setDescription(description);
            repoApps.setLicense(license);
            repoApps.setSummary(summary);
            repoApps.setWebsit(website);
            Optional<Repo> oprepo = repoService.findById(rid);
            repoApps.setRepo(oprepo.get());
            repoAppsService.save(repoApps);
            return new ResponseEntity(response.success(), HttpStatus.OK);
        }catch (NoSuchElementException e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
    @RequestMapping(value = "/repoapps/{rfid}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoAppsById(@PathVariable("rfid") long rfid){
        try{
            repoAppsService.deleteById(rfid);
            return new ResponseEntity(response.success(), HttpStatus.OK);
        }catch(NullPointerException e){
            return new ResponseEntity(response.error("user not found"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value="/repoapps", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoAll(){
        try{
            repoAppsService.deleteAll();
            return new ResponseEntity(response.success(), HttpStatus.OK);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
