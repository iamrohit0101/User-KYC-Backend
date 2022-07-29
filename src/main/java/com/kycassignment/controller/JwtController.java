package com.kycassignment.controller;


import com.kycassignment.dao.DocumentDao;
import com.kycassignment.dao.UserDao;
import com.kycassignment.entity.Document;
import com.kycassignment.entity.UploadedDocument;
import com.kycassignment.entity.UserEntity;
import com.kycassignment.entity.UserHelper;
import com.kycassignment.model.AdminRequest;
import com.kycassignment.model.JwtRequest;
import com.kycassignment.model.JwtResponse;
import com.kycassignment.model.UserRequest;
import com.kycassignment.service.UserDetailsServiceImplementation;
import com.kycassignment.service.UserService;
import com.kycassignment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class JwtController {
	@Autowired
	private AuthenticationManager authenticationManager;
   @Autowired
   private UserDetailsServiceImplementation userDetailsService;

 	

  	
   @Autowired
   private JwtUtil jwtUtil;

   @Autowired
   private UserDao userDao;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private DocumentDao documentDao;

	@Autowired
	private UserService userService;

	@PostConstruct
	public void initMethod(){
		userService.initMethod();
	}

//	public ResponseEntity<?> registerNewUser(@RequestBody UserRequest userRequest) throws Exception{
   	@PostMapping({"/registerNewUser"})
	public ResponseEntity<?> registerNewUser(@RequestBody UserHelper userHelper) throws Exception{

//		UserHelper userHelper = userRequest.getUserHelper();
		UserEntity userEntity = new UserEntity();
		userEntity.setUserName(userHelper.getUserName());
		userEntity.setEmail(userHelper.getEmail());
		userEntity.setPassword(passwordEncoder.encode(userHelper.getPassword()));
		userEntity.setIsKYC(false);
		userEntity.setUserRole("User");
		userDao.save(userEntity);


		UserDetails userDetails = this.userDetailsService.loadUserByUsername(userHelper.getUserName());
		String token = this.jwtUtil.generateToken(userDetails);

		System.out.println("Jwt---------------------> in register new user"+token);

		return ResponseEntity.status(HttpStatus.ACCEPTED).body((new JwtResponse(userEntity,token)));
	}

	@GetMapping({"/checkUserByUserName/{userName}"})
	public ResponseEntity<?> checkUserByUserName(@PathVariable String userName){

		boolean result;
		if(userDao.findByUserName(userName)==null){
			result=true;
		} else {
			result=false;
		}
		return ResponseEntity.ok(result);
	}

	@GetMapping({"/getUserByUserName/{userName}"})
	public ResponseEntity<?> getUserByUserName(@PathVariable String userName){

		UserEntity result = userDao.findByUserName(userName);

		return ResponseEntity.ok(result);
	}

	@PostMapping({"/userlogin"})
	public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception{
		System.out.println(jwtRequest);
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(),jwtRequest.getPassword()));
			
		}catch(UsernameNotFoundException e) {
			e.printStackTrace();
			throw new Exception("user not found");
		}catch(BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("Bad c Credential");
		}
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtRequest.getUserName());
		String token = this.jwtUtil.generateToken(userDetails);
		System.out.println("Jwt"+token);

		UserEntity userEntity = userDao.findByUserName(jwtRequest.getUserName());
		
		return ResponseEntity.ok((new JwtResponse(userEntity,token)));
	}

	@GetMapping({"/adminlogin"})
	public ResponseEntity<?> adminLogIn(HttpServletRequest request){

		boolean adminStatus = userService.isAdmin(request);
		if(adminStatus==true) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDao.findAll());
		}
		else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("only admin can access this api");
		}
	}

	@PostMapping({"/adminresponse"})
	public String adminResponse(@RequestBody AdminRequest adminRequest){

		UserEntity user = userDao.findById(adminRequest.getUserId()).get();
		List<UploadedDocument> documentList = user.getUploadedDocuments();

		for(UploadedDocument ud : documentList){
			if(ud.getId()==adminRequest.getUploadedDocId()){
				if(adminRequest.getVerification()==1) {
					ud.setStatus("accepted");
				} else {
					ud.setStatus("rejected");
				}
			}
		}

		userDao.save(user);

		System.out.println(user+"-----------------------------------user");

		List<Document> documents = documentDao.findAll();
		int docListSize = documents.size();

		int counter = 0;
		for (UploadedDocument doc:documentList) {
			if(doc.getStatus().equals("accepted")){
				counter+=1;
			}
		}
		if(counter==docListSize){
			user.setIsKYC(true);
		}


		userDao.save(user);
		System.out.println("it comes here---------------------------------------------------------------"+counter);

//		return ResponseEntity.status(HttpStatus.ACCEPTED).body(1);
//		return ResponseEntity.ok("success");
		return "success";
	}

	@GetMapping({"/getUserByToken"})
	public ResponseEntity<?> getUserByToken(HttpServletRequest request){
		UserEntity user = userService.getUserBYToken(request);

		return ResponseEntity.ok(user);
	}
}
