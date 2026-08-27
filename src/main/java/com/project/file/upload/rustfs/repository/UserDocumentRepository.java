package com.project.file.upload.rustfs.repository;

import com.project.file.upload.rustfs.entity.UserDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocumentRepository extends JpaRepository<UserDocument, Long> {

}
