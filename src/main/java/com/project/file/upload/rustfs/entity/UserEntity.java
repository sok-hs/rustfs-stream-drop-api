package com.project.file.upload.rustfs.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = UserEntity.TABLE_NAME)
public class UserEntity {

    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String username;

    @Column(length = 100)
    private String key;
}
