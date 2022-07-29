package com.kycassignment.model;


import com.kycassignment.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DocumentRequest {

    private Document document;
}
