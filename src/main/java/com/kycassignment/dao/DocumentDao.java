package com.kycassignment.dao;

import com.kycassignment.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDao extends JpaRepository<Document, Integer> {
}
