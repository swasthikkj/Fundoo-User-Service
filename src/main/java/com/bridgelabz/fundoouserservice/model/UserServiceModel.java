package com.bridgelabz.fundoouserservice.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.bridgelabz.fundoouserservice.dto.UserServiceDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Purpose:Modrl for user service
 * @version 4.15.1.RELEASE
 * @author Swasthik KJ
 */

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
	@Column(length = 10000)
	private String profilePic;
	
	public UserServiceModel(UserServiceDTO userServiceDTO) {		
		this.name = userServiceDTO.getName();
		this.emailId = userServiceDTO.getEmailId();
		this.password = userServiceDTO.getPassword();
		this.isActive = userServiceDTO.isActive();
		this.isDeleted = userServiceDTO.isDeleted();
		this.dateOfbirth = userServiceDTO.getDateOfbirth();
		this.phoneNumber = userServiceDTO.getPhoneNumber();
	}
}
