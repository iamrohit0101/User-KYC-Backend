package com.kycassignment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class UploadDocumentHelper {

    private int userId;
    private String url;
    private String status;
    private int documentId;

}
