package com.kycassignment.model;

import com.kycassignment.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class JwtResponse {
    private UserEntity userEntity;
    private String jwtToken;
}
