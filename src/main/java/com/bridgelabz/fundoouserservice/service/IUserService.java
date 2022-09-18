package com.bridgelabz.fundoouserservice.service;

import com.bridgelabz.fundoouserservice.util.Response;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoouserservice.dto.UserServiceDTO;
import com.bridgelabz.fundoouserservice.model.UserServiceModel;

/**
 * Purpose:Interface for user service
 * @version 4.15.1.RELEASE
 * @author Swasthik KJ
 */

public interface IUserService {
	
	UserServiceModel addUser(UserServiceDTO userServiceDTO);
	
	UserServiceModel updateUser(UserServiceDTO userServiceDTO, Long userId, String token);
		
	Optional<UserServiceModel> getUserById(Long userId, String token);

	List<UserServiceModel> getAllUsers(String token);
	
	Response deleteUser(Long userId, String token);

	Response restoreUser(Long userId, String token);

	Response permanentDelete(Long userId, String token);

	Response setProfilePic(Long userId, MultipartFile profile) throws IOException;
	
	Response login(String emailId, String password);

	UserServiceModel changePassword(String token, String password);

	Response resetPassword(String emailId);

	Boolean validateUser(String token);

	Boolean validateEmail(String email);

}
