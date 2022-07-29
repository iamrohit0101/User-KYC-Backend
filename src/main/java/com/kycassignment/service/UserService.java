package com.kycassignment.service;

import com.kycassignment.dao.DocumentDao;
import com.kycassignment.dao.UserDao;
import com.kycassignment.entity.Document;
import com.kycassignment.entity.UserEntity;
import com.kycassignment.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;
    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private UserDao userDao;
    @Autowired
    private DocumentDao documentDao;

    public void initMethod(){

        UserEntity tempAdmin = userDao.findByUserName("admin123");

        if(tempAdmin==null){

            UserEntity adminUser = new UserEntity();
            adminUser.setUserName("admin123");
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("admin@pass"));
            adminUser.setIsKYC(true);
            adminUser.setUserRole("Admin");
            userDao.save(adminUser);
            System.out.println("Admin admin123 added to table");
        }else{
            System.out.println("Admin admin123 already exits");
        }

        UserEntity tempUser = userDao.findByUserName("user123");
        if(tempUser==null){

            UserEntity user = new UserEntity();
            user.setUserName("user123");
            user.setEmail("user@gmail.com");
            user.setPassword(passwordEncoder.encode("user@pass"));
            user.setIsKYC(false);
            user.setUserRole("User");
            userDao.save(user);
            System.out.println("User user 123 added to table");
        }else {
            System.out.println("User user123 already exits");
        }

        documentDao.save(new Document(1,"pan"));
        documentDao.save(new Document(2,"voter"));
        documentDao.save(new Document(3,"adhar"));
        documentDao.save(new Document(4,"visa"));



    }

    public UserEntity getUserBYToken(HttpServletRequest request){

        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = this.jwtUtils.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            String userName = userDetails.getUsername();
            UserEntity user = userDao.findByUserName(userName);
            return user;
        }
        return null;
    }

    public boolean isAdmin(HttpServletRequest request){

        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = this.jwtUtils.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            String userName = userDetails.getUsername();
            UserEntity user = userDao.findByUserName(userName);

            if(user==null){
                System.out.println("user not found in isAdmin()");
                return false;
            }

            if (user.getUserRole().equals("Admin")) {
                return true;
            }
        }
        return false;
    }
}
