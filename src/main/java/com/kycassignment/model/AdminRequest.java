package com.kycassignment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminRequest {

    private int userId;
    private int uploadedDocId;
    private int verification;
}
