package com.kycassignment.model;

import com.kycassignment.entity.UserHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class UserRequest {

    private UserHelper userHelper;
}
