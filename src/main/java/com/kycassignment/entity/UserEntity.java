package com.kycassignment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UserEntity {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String email;
    private String password;
    private String userRole;
    private Boolean isKYC;

    @OneToMany(fetch = FetchType.EAGER,targetEntity = UploadedDocument.class, cascade = CascadeType.ALL)
    @JoinColumn(name="user_fk", referencedColumnName = "id",nullable = false)
    private List<UploadedDocument> uploadedDocuments;

}
