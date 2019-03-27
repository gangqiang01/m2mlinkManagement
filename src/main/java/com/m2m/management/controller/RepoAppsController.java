package com.m2m.management.controller;

import com.m2m.management.Resource.DeployResource;
import com.m2m.management.Resource.RepoResource;
import com.m2m.management.entity.Repo;
import com.m2m.management.entity.RepoApp;
import com.m2m.management.repository.IRepoAppsBean;

import com.m2m.management.repository.IRepoBean;
import com.m2m.management.utils.FileUtil;
import com.m2m.management.utils.GetApkInfo;
import com.m2m.management.utils.Response;

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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class RepoAppsController {
    private static final Logger logger = LoggerFactory.getLogger(RepoAppsController.class);
    private String baseRepoPath = DeployResource.BASEDEPLOYPATH+ RepoResource.TYPE;
    private String pathSeparate = File.separator;

    @Autowired
    private IRepoAppsBean repoAppsService;

    @Autowired
    private IRepoBean repoService;

    @RequestMapping(value = "/repoapps", method = RequestMethod.GET)
    public ResponseEntity<List<RepoApp>> getRepoApps(
            @RequestParam(name="keywords", required = false, defaultValue ="") String keywords,
            @RequestParam(name="currentpage", required = false, defaultValue ="0") int currentpage,
            @RequestParam(name="limit", required = false, defaultValue ="10") int limit) {
        logger.info("repoapp:"+keywords);
        Pageable pageable = new PageRequest(currentpage, limit, Sort.Direction.DESC, "raid");
        List<RepoApp> repoApps = repoAppsService.findByFilenameContaining(keywords, pageable);
        return new ResponseEntity(Response.success(repoApps), HttpStatus.OK);
    }

    @RequestMapping(value = "/repoapps/{raid}", method = RequestMethod.GET)
    public ResponseEntity<RepoApp> getRepoAppsById(@PathVariable("raid") long raid){
        try{
            File file = new File("/D");
            Optional<RepoApp> opRepoApps = repoAppsService.findById(raid);
            RepoApp repoApp = opRepoApps.get();
            return new ResponseEntity(Response.success(repoApp), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
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
            Boolean isSave = false;
            File convFile = new File( System.getProperty("java.io.tmpdir")+pathSeparate+file.getOriginalFilename());
            file.transferTo(convFile);
            Map<String, Object> apkInfo =  GetApkInfo.readApk(convFile);
            if(apkInfo == null){
                return new ResponseEntity(Response.error("file isnot apk "), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String filename = file.getOriginalFilename();
            String pkgname = apkInfo.get("pkgname").toString();
            String versioncode = apkInfo.get("versioncode").toString();
            String versionname = apkInfo.get("versionname").toString();
            Optional<Repo> oprepo = repoService.findById(rid);
            Repo repo = oprepo.get();
            String apkSavePath = baseRepoPath + pathSeparate+repo.getDarkname() + pathSeparate + pkgname + pathSeparate + versionname + pathSeparate + filename;
            logger.info("apkSavePath:"+apkSavePath);
            File baseDeplyFile = new File(DeployResource.BASEDEPLOYPATH);
            if(baseDeplyFile.exists()){
                File apkRepoFile = new File(baseRepoPath + pathSeparate + repo.getDarkname());
                if(apkRepoFile.exists()){
                    isSave = FileUtil.copyFile(convFile, apkSavePath);
                }else{
                    return new ResponseEntity(Response.error("apk repo is not found"), HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity(Response.error("server error"), HttpStatus.NOT_FOUND);
            }
            if(isSave){
                RepoApp repoApp = new RepoApp(filename, pkgname);
                repoApp.setVersionname(versionname);
                repoApp.setVersioncode(versioncode);
                repoApp.setDescription(description);
                repoApp.setLicense(license);
                repoApp.setSummary(summary);
                repoApp.setWebsit(website);
                repoApp.setRepo(repo);
                repoAppsService.save(repoApp);
                return new ResponseEntity(Response.success(), HttpStatus.OK);
            }else{
                return new ResponseEntity(Response.error("add apk error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (NoSuchElementException e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "repoapps/{raid}", method = RequestMethod.POST)
    public ResponseEntity<String> updateRepoApp(@PathVariable("raid") long raid, @RequestBody RepoApp repoApp){
        try{
            RepoApp crepoApp = repoAppsService.findById(raid).get();
            crepoApp.setWebsit(repoApp.getWebsit());
            crepoApp.setSummary(repoApp.getSummary());
            crepoApp.setLicense(repoApp.getLicense());
            crepoApp.setDescription(repoApp.getDescription());
            repoAppsService.save(crepoApp);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch (NoSuchElementException e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("raid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }



    }
    @RequestMapping(value = "/repoapps/{raid}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoAppsById(@PathVariable("raid") long raid){
        try{
            Boolean isDelete = false;
            RepoApp repoApp = repoAppsService.findById(raid).get();
            Repo repo = repoApp.getRepo();
            String darkname = repo.getDarkname();
            String filename = repoApp.getFilename();
            String pkgname = repoApp.getPkgname();
            String versionname = repoApp.getVersionname();
            String pkgPath = baseRepoPath + pathSeparate + darkname + pathSeparate + pkgname;
            if(FileUtil.isOnlyChildDir(pkgPath)){
                isDelete = FileUtil.delDir(pkgPath);
            }else{
                isDelete = FileUtil.delDir(pkgPath + pathSeparate + versionname);
            }
            if(isDelete){
                repoAppsService.deleteById(raid);
                return new ResponseEntity(Response.success(), HttpStatus.OK);
            }else{
                return new ResponseEntity(Response.error("delete ftp server app error"), HttpStatus.NOT_FOUND);
            }
        }catch(NullPointerException e){
            e.printStackTrace();
            return new ResponseEntity(Response.error("user not found"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
