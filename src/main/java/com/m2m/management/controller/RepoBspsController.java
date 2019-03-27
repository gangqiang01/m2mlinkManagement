package com.m2m.management.controller;

import com.auth0.jwt.internal.org.apache.commons.codec.digest.DigestUtils;
import com.m2m.management.Resource.DeployResource;
import com.m2m.management.Resource.RepoResource;
import com.m2m.management.entity.Repo;
import com.m2m.management.entity.RepoApp;
import com.m2m.management.entity.RepoBsp;

import com.m2m.management.repository.IRepoBean;
import com.m2m.management.repository.IRepoBspsBean;
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
import java.io.FileInputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class RepoBspsController {
    private static final Logger logger = LoggerFactory.getLogger(RepoBspsController.class);
    private String baseRepoPath = DeployResource.BASEDEPLOYPATH+ RepoResource.TYPE;
    private String pathSeparate = File.separator;

    @Autowired
    private IRepoBspsBean repoBspsService;

    @Autowired
    private IRepoBean repoService;

    @RequestMapping(value = "/repobsps", method = RequestMethod.GET)
    public ResponseEntity<List<RepoApp>> getRepoBsps(
            @RequestParam(name="keywords", required = false, defaultValue ="") String keywords,
            @RequestParam(name="currentpage", required = false, defaultValue ="0") int currentpage,
            @RequestParam(name="limit", required = false, defaultValue ="10") int limit) {
        Pageable pageable = new PageRequest(currentpage, limit, Sort.Direction.DESC, "rbid");
        List<RepoBsp> repoBsps = repoBspsService.findByBoardnameContaining(keywords, pageable);
        return new ResponseEntity(Response.success(repoBsps), HttpStatus.OK);
    }

    @RequestMapping(value = "/repobsps/{rbid}", method = RequestMethod.GET)
    public ResponseEntity<RepoApp> getRepoAppsById(@PathVariable("rbid") long rbid){
        try{
            File file = new File("/D");
            Optional<RepoBsp> opRepoBsps = repoBspsService.findById(rbid);
            RepoBsp repoBsp = opRepoBsps.get();
            return new ResponseEntity(Response.success(repoBsp), HttpStatus.OK);
        }catch(NoSuchElementException e){
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @RequestMapping(value = "/repobsps", method = RequestMethod.POST)
    public ResponseEntity<Void> createRepoBsps(@RequestBody RepoBsp repobsps){
        try {
            Optional<Repo> oprepo = repoService.findById(repobsps.getRid());
            Repo repo = oprepo.get();
            repobsps.setRepo(repo);
            repoBspsService.save(repobsps);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/addBspFile", method = RequestMethod.POST)
    public ResponseEntity<Void> createRepoApps(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "rid") long rid,
            @RequestParam(value = "description") String description,
            @RequestParam(value = "boardname") String boardname,
            @RequestParam(value = "versionname") String versionname,
            @RequestParam(value = "file_md5") String file_md5
    ){
        try{
            Boolean isSave = false;
            File convFile = new File( System.getProperty("java.io.tmpdir")+pathSeparate+file.getOriginalFilename());
            file.transferTo(convFile);
            String fileMd5 = DigestUtils.md5Hex(new FileInputStream(convFile));
            logger.info("reqMd5:"+ file_md5+"/repMd5:"+ fileMd5);
            if(!file_md5.equals(fileMd5)){
                return new ResponseEntity(Response.error("file upload error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String filename = file.getOriginalFilename();
            Optional<Repo> oprepo = repoService.findById(rid);
            Repo repo = oprepo.get();
            String bspSavePath = baseRepoPath + pathSeparate+repo.getDarkname() + pathSeparate + boardname + pathSeparate + versionname + pathSeparate + filename;
            File baseDeplyFile = new File(DeployResource.BASEDEPLOYPATH);
            if(baseDeplyFile.exists()){
                File bspRepoFile = new File(baseRepoPath + pathSeparate + repo.getDarkname());
                if(bspRepoFile.exists()){
                    isSave = FileUtil.copyFile(convFile, bspSavePath);
                }else{
                    return new ResponseEntity(Response.error("bsp repo is not found"), HttpStatus.NOT_FOUND);
                }
            }else{
                return new ResponseEntity(Response.error("server error"), HttpStatus.NOT_FOUND);
            }
            if(isSave){
                InetAddress address = InetAddress.getLocalHost();
                RepoBsp repoBsp = new RepoBsp(versionname, boardname);
                repoBsp.setDescription(description);
                String downloadAddress = "http://"+ address.getHostAddress()+ bspSavePath;
                repoBsp.setAddress(downloadAddress);
                repoBsp.setRepo(repo);
                repoBspsService.save(repoBsp);
                return new ResponseEntity(Response.success(), HttpStatus.OK);
            }else{
                return new ResponseEntity(Response.error("add bsp error"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }catch (NoSuchElementException e){
            e.printStackTrace();
            return new ResponseEntity(Response.error("rid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "repobsps/{rbid}", method = RequestMethod.POST)
    public ResponseEntity<String> updateRepoApp(@PathVariable("rbid") long rbid, @RequestBody RepoBsp repoBsp){
        try{
            RepoBsp crepoBsp = repoBspsService.findById(rbid).get();
            crepoBsp.setDescription(repoBsp.getDescription());
            repoBspsService.save(crepoBsp);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch (NoSuchElementException e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("rbid is error"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @RequestMapping(value = "/repobsps/{rbid}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteRepoBspsById(@PathVariable("rbid") long rbid){
        try{
            Boolean isDelete = false;
            RepoBsp repoBsp = repoBspsService.findById(rbid).get();
            Repo repo = repoBsp.getRepo();
            String darkname = repo.getDarkname();
            String boardname = repoBsp.getBoardname();
            String versionname = repoBsp.getVersionname();
            String boardPath = baseRepoPath + pathSeparate+repo.getDarkname() + pathSeparate + boardname;
            if(FileUtil.isOnlyChildDir(boardPath)){
                isDelete = FileUtil.delDir(boardPath);
            }else{
                isDelete = FileUtil.delDir(boardPath + pathSeparate + versionname);
            }
            repoBspsService.deleteById(rbid);
            return new ResponseEntity(Response.success(), HttpStatus.OK);
        }catch(NullPointerException e){
            e.printStackTrace();
            return new ResponseEntity(Response.error("user not found"), HttpStatus.NOT_FOUND);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity(Response.error("server error"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
