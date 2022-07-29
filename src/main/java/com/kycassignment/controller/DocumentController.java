package com.kycassignment.controller;

import com.kycassignment.dao.DocumentDao;
import com.kycassignment.dao.UserDao;
import com.kycassignment.model.DocumentRequest;
import com.kycassignment.model.UploadedDocumentRequest;
import com.kycassignment.model.UserRequest;
import com.kycassignment.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class DocumentController {

    @Autowired
    UserDao userDao;
    @Autowired
    DocumentDao documentDao;


    @GetMapping({"/"})
    public Boolean get(){
        return true;
    }

    @PostMapping({"/addUser"})
    public UserHelper addUser(@RequestBody UserRequest request){
        UserEntity userEntity = new UserEntity();
        UserHelper userHelper=request.getUserHelper();
        userEntity.setUserName(userHelper.getUserName());
        userEntity.setEmail(userHelper.getEmail());
        userEntity.setPassword(userHelper.getPassword());
        userEntity.setIsKYC(false);
        userDao.save(userEntity);
        return userHelper;
    }

   @GetMapping({"/getAllDocuments"})
   public ResponseEntity<?> getAllDocuments(){

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(documentDao.findAll());
   }
    @GetMapping({"/getAllUploadedDocuments"})
    public ResponseEntity<?> getAllUploadedDocuments(){

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
    }
    @PostMapping({"/addNewDocument"})
    public Document addDoc(@RequestBody Document newDocument){
        return documentDao.save(newDocument);

    }


}
