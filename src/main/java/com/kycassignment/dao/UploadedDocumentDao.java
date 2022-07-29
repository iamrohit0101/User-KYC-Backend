package com.kycassignment.dao;

import com.kycassignment.entity.UploadedDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedDocumentDao extends JpaRepository<UploadedDocument, Integer> {
}
