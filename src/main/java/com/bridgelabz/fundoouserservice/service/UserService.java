package com.bridgelabz.fundoouserservice.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoouserservice.util.TokenUtil;
import com.bridgelabz.fundoouserservice.dto.UserServiceDTO;
import com.bridgelabz.fundoouserservice.exception.UserNotFoundException;
import com.bridgelabz.fundoouserservice.model.UserServiceModel;
import com.bridgelabz.fundoouserservice.repository.UserServiceRepository;
import com.bridgelabz.fundoouserservice.util.Response;

/**
 * Purpose:User service class to perform crud operations
 * @version 4.15.1.RELEASE
 * @author Swasthik KJ
 */

@Service
public class UserService implements IUserService {
	@Autowired
	UserServiceRepository userRepository;

	@Autowired
	TokenUtil tokenUtil;

	@Autowired
	MailService mailService;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * Purpose:add user and notify through email
	 */

	@Override
	public UserServiceModel addUser(UserServiceDTO userServiceDTO) {
		UserServiceModel model = new UserServiceModel(userServiceDTO);
		model.setCreatedAt(LocalDateTime.now());
		userRepository.save(model);
		String body = "User is added succesfully with userId " + model.getId();
		String subject = "User Registration Successfull";
		mailService.send(model.getEmailId(), subject, body);		
		return model;
	}

	/**
	 * Purpose:update user 
	 */

	@Override
	public UserServiceModel updateUser(UserServiceDTO userServiceDTO, Long userId, String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isUserPresent = userRepository.findById(userId);
		if(isUserPresent.isPresent()) {
			isUserPresent.get().setName(userServiceDTO.getName());
			isUserPresent.get().setEmailId(userServiceDTO.getEmailId());
			isUserPresent.get().setPassword(userServiceDTO.getPassword());
			isUserPresent.get().setPhoneNumber(userServiceDTO.getPhoneNumber());
			isUserPresent.get().setDateOfbirth(userServiceDTO.getDateOfbirth());
			isUserPresent.get().setUpdatedAt(LocalDateTime.now());
			userRepository.save(isUserPresent.get());
			String body = "User updated successfully with user Id" + isUserPresent.get().getId();
			String subject = "User updated Successfully";
			mailService.send(isUserPresent.get().getEmailId(), subject, body);
			return isUserPresent.get();
		}
		throw new UserNotFoundException(400, "User not present");
	}

	/**
	 * Purpose:fetch user by id
	 */

	@Override
	public Optional<UserServiceModel> getUserById(Long userId, String token) {		
		return userRepository.findById(userId);
	}

	/**
	 * Purpose:fetch all users
	 */

	@Override
	public List<UserServiceModel> getAllUsers(String token) {
		Long decode = tokenUtil.decodeToken(token);
		List<UserServiceModel> getAllUsers = userRepository.findAll();
		if(getAllUsers.size()>0) {
			return getAllUsers;
		} else {
			throw new UserNotFoundException(400, "User not present");
		}	
	}

	/**
	 * Purpose:delete user
	 */

	@Override
	public Response deleteUser(Long userId, String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserServiceModel> isIdPresent = userRepository.findById(userId);
			if(isIdPresent.isPresent()) {
				isIdPresent.get().setActive(false);
				isIdPresent.get().setDeleted(true);
				userRepository.save(isIdPresent.get());
				return new Response(200, "success", isIdPresent.get());
			} else {
				throw new UserNotFoundException(400,"User not present");
			}
		}
		throw new UserNotFoundException(400, "Token not found");	
	}

	/**
	 * Purpose:restore user
	 */

	@Override
	public Response restoreUser(Long userId, String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserServiceModel> isIdPresent = userRepository.findById(userId);
			if(isIdPresent.isPresent()) {
				isIdPresent.get().setActive(true);
				isIdPresent.get().setDeleted(false);
				userRepository.save(isIdPresent.get());
				return new Response(200, "success", isIdPresent.get());
			}
			throw new UserNotFoundException(400,"User not present");
		}
		throw new UserNotFoundException(400, "Token not found");	
	}

	/**
	 * Purpose:delete user permanently
	 */

	@Override
	public Response permanentDelete(Long userId, String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isUserPresent = userRepository.findById(decode);
		if(isUserPresent.isPresent()) {
			Optional<UserServiceModel> isIdPresent = userRepository.findById(userId);
			if(isIdPresent.isPresent()) {
				userRepository.delete(isIdPresent.get());
				return new Response(200, "Success", isIdPresent.get());
			} 
			throw new UserNotFoundException(400, "User not found");
		}		
		throw new UserNotFoundException(400, "Invalid token");
	}

	/**
	 * Purpose:setting profile pic of user
	 */

	@Override
	public Response setProfilePic(Long userId, MultipartFile profile) throws IOException {
		Optional<UserServiceModel> isIdPresent = userRepository.findById(userId);
		if(isIdPresent.isPresent()) {
			isIdPresent.get().setProfilePic(String.valueOf(profile.getBytes()));
			userRepository.save(isIdPresent.get());
			return new Response(200, "Success", isIdPresent.get());
		}
		throw new UserNotFoundException(400, "User not found");
	}

	/**
	 * Purpose:login user and generate token
	 */

	@Override
	public Response login(String emailId, String password) {
		Optional<UserServiceModel> isEmailPresent = userRepository.findByEmailId(emailId);
		if(isEmailPresent.isPresent()) {
			if(isEmailPresent.get().getPassword().equals(password)){
				String token = tokenUtil.createToken(isEmailPresent.get().getId());
				return new Response(400, "login succesfull", token);
			}
			throw new UserNotFoundException(400, "Invalid credentials");
		}
		throw new UserNotFoundException(400, "User not found");
	}

	/**
	 * Purpose:change user password
	 */

	@Override
	public UserServiceModel changePassword(String token, String password) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			isTokenPresent.get().setPassword(passwordEncoder.encode(password));
			userRepository.save(isTokenPresent.get());
			String body = "User password changed successfully with Id" + isTokenPresent.get().getId();
			String subject = "User password changed Successfully";
			mailService.send(isTokenPresent.get().getEmailId(), subject, body);
			return isTokenPresent.get();
		}
		throw new UserNotFoundException(400, "Token not found");
	}

	/**
	 * Purpose:reset user password
	 */

	@Override
	public Response resetPassword(String emailId) {
		Optional<UserServiceModel> isMailPresent = userRepository.findByEmailId(emailId);
		if (isMailPresent.isPresent()){
			String token = tokenUtil.createToken(isMailPresent.get().getId());
			return new Response(400, "Reset password", token);
		}
		throw new UserNotFoundException(400, "Email not found");
	}

	/**
	 * Purpose:validate user
	 */

	@Override
	public Boolean validateUser(String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent())
			return true;
		throw new UserNotFoundException(400, "Token not found");
	}

	/**
	 * Purpose:validate email
	 */

	@Override
	public Boolean validateEmail(String email) {
		Optional<UserServiceModel> isEmailPresent = userRepository.findByEmailId(email);
		if (isEmailPresent.isPresent()) {
			return true;
		} 
		throw new UserNotFoundException(400, "Token not found");
	}
}
