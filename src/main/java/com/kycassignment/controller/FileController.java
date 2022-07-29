package com.kycassignment.controller;

import com.kycassignment.dao.DocumentDao;
import com.kycassignment.dao.UserDao;
import com.kycassignment.entity.Document;
import com.kycassignment.entity.UploadDocumentHelper;
import com.kycassignment.entity.UploadedDocument;
import com.kycassignment.entity.UserEntity;
import com.kycassignment.model.UploadedDocumentRequest;
import com.kycassignment.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class FileController {

    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private DocumentDao documentDao;
    @Autowired
    private UserDao userDao;


    @RequestMapping("/download/{fileName}")
    public ResponseEntity downloadFile1(@PathVariable String fileName) throws IOException {
        String filePath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\uploadedfiles\\" + fileName;
        File file = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + file.getName())
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(file.length())
                .body(resource);
    }


    @PostMapping({"/uploadFile"})
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") int userId
    , @RequestParam("docId") int docId) throws IllegalStateException, IOException {

        UserEntity user = userDao.findById(userId).get();
        List<UploadedDocument> documentList = user.getUploadedDocuments();

//        System.out.println(userId+' '+docId);

//        return ResponseEntity.ok(userId+" "+docId);

        boolean isDocFound=false;
        String docStatus="pending";

        for(UploadedDocument ud : documentList){
            if(ud.getDocument().getId()==docId){
                isDocFound=true;
                docStatus = ud.getStatus();
            }
        }

        if(isDocFound && docStatus.equals("pending")){
                System.out.println("document duplicate ");
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("document already in pending status");
        } else if (isDocFound && docStatus.equals("accepted")) {
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("document already accepted");
        } else if(isDocFound && docStatus.equals("rejected")) {

            for(UploadedDocument ud : documentList){
                if(ud.getDocument().getId()==docId){
                    if(file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")){

                        String oldUrl = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\uploadedfiles\\" + ud.getUrl();
                        System.out.println("old url ----------------------------> "+oldUrl);

                        File fileToDelete = new File(oldUrl);
                        boolean success = fileToDelete.delete();

                        System.out.println("deltetion result --------------------------> "+success);

                        String url = fileUploadService.uploadFile(file,userId,docId);

                        System.out.println(docId + " " + userId + " " + "file reupload success--------------------");
                        ud.setUrl(url);
                        ud.setStatus("pending");
                        userDao.save(user);
                        return ResponseEntity.status(HttpStatus.ACCEPTED).body("reuploaded succefully");
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("file content type does not matches");
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("document not uploaded success");
        }else {
            if(file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg")){
                String url = fileUploadService.uploadFile(file,userId,docId);
                uploadDocument(new UploadedDocumentRequest(new UploadDocumentHelper(userId, url, "pending", docId)));
                System.out.println(docId + " " + userId + " " + "file upload success--------------------");
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("uploaded succefully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("file content type does not matches");
            }
        }
    }

    public UserEntity uploadDocument(@RequestBody UploadedDocumentRequest request){

        UploadDocumentHelper uploadDocumentHelper = request.getUploadDocumentHelper();
        int userId= uploadDocumentHelper.getUserId();
        int documentId= uploadDocumentHelper.getDocumentId();
        String url= uploadDocumentHelper.getUrl();
        String status= uploadDocumentHelper.getStatus();

        UserEntity userEntity = userDao.findById(userId).get();

        Document document = documentDao.findById(documentId).get();

        UploadedDocument uploadedDocument = new UploadedDocument();

        uploadedDocument.setUrl(url);

        uploadedDocument.setStatus(status);

        uploadedDocument.setDocument(document);

        System.out.println("docs list-->");
        System.out.println(userEntity.getUploadedDocuments());

        List<UploadedDocument> documentList= userEntity.getUploadedDocuments();

        documentList.forEach(docList->{
            if(docList.getDocument().getId()==documentId) {
                System.out.println("Document already uploaded : "+docList.getDocument().getId());
            }
        });

        documentList.add(uploadedDocument);

        userEntity.setUploadedDocuments(documentList);

        userDao.save(userEntity);

        return userEntity;
    }
}


