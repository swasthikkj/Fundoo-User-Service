package com.bridgelabz.fundoouserservice.dto;

import javax.validation.constraints.Pattern;
import lombok.Data;

/**
 * Purpose:DTO for user service
 * @version 4.15.1.RELEASE
 * @author Swasthik KJ
 */

@Data
public class UserServiceDTO {
	@Pattern(regexp = "^[A-Z]{1}[a-z\\s]{2,}$", message = "Name Invalid")
	private String name;
	@Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "email is invalid")
	private String emailId;
	@Pattern(regexp = "^[a-zA-Z0-9*&@]{8,20}$", message = "password is invalid")
	private String password;
	private boolean isActive;
	private boolean isDeleted;
	@Pattern(regexp = "^[1-31]+/[1-12]+/[1920-2020]$", message = "Date of birth is invalid")
	private String dateOfbirth;
	@Pattern(regexp = "^[9,8,7,6]{1}[0-9]{9}$", message = "mobile number is invalid")
	private long phoneNumber;
}
