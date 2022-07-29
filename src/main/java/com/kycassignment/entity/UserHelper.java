package com.kycassignment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserHelper {

    private String userName;
    private String email;
    private String password;
    private List<Document> documentList;
}
