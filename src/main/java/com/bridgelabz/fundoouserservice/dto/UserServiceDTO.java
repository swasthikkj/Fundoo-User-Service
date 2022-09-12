package com.bridgelabz.fundoouserservice.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class UserServiceDTO {
	private String name;
	private String emailId;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private boolean isActive;
	private boolean isDeleted;
	private String dateOfbirth;
	private long phoneNumber;
	private MultipartFile profilePic;
}
