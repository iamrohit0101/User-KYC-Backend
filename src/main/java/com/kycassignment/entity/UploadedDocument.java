package com.kycassignment.entity;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class UploadedDocument {
    @Id
    @GeneratedValue
    private int id;
    private String url;
    private String status;


    @ManyToOne(fetch = FetchType.EAGER,targetEntity = Document.class, cascade = CascadeType.ALL)
    @JoinColumn(name="Doc_fk", referencedColumnName = "id",nullable = false)
    private Document document;
}
