package com.kycassignment.model;

import com.kycassignment.entity.UploadDocumentHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadedDocumentRequest {

    private UploadDocumentHelper uploadDocumentHelper;
}
