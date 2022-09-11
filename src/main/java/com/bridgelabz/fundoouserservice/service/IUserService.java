package com.bridgelabz.fundoouserservice.service;

import com.bridgelabz.fundoouserservice.util.Response;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.fundoouserservice.dto.UserServiceDTO;
import com.bridgelabz.fundoouserservice.model.UserServiceModel;

public interface IUserService {
	
	UserServiceModel addUser(UserServiceDTO userServiceDTO);
	
	UserServiceModel updateUser(UserServiceDTO userServiceDTO, Long id, String token);
		
	Optional<UserServiceModel> getUserById(Long id, String token);

	List<UserServiceModel> getAllUsers(String token);
	
	Response deleteUser(Long id, String token);

	Response restoreUser(Long id, String token);

	Response permanentDelete(Long id, String token);
	
	Response login(String emailId, String password);

	UserServiceModel changePassword(String token, String password);

	Response resetPassword(String emailId);

	Boolean validateUser(String token);

}
