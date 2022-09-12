package com.bridgelabz.fundoouserservice.model;

import java.io.File;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoouserservice.dto.UserServiceDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UserService")
@Data
@NoArgsConstructor
public class UserServiceModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
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
	
	public UserServiceModel(UserServiceDTO userServiceDTO) {		
		this.name = userServiceDTO.getName();
		this.emailId = userServiceDTO.getEmailId();
		this.password = userServiceDTO.getPassword();
		this.createdAt = userServiceDTO.getCreatedAt().now();
		this.updatedAt = userServiceDTO.getUpdatedAt().now();
		this.isActive = userServiceDTO.isActive();
		this.isDeleted = userServiceDTO.isDeleted();
		this.dateOfbirth = userServiceDTO.getDateOfbirth();
		this.phoneNumber = userServiceDTO.getPhoneNumber();
		this.profilePic = userServiceDTO.getProfilePic();
	}
}
