package com.bridgelabz.fundoouserservice.service;

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

	@Override
	public UserServiceModel addUser(UserServiceDTO userServiceDTO) {
		UserServiceModel model = new UserServiceModel(userServiceDTO);
		userRepository.save(model);
		String body = "User is added succesfully with userId " + model.getId();
		String subject = "User Registration Successfull";
		mailService.send(model.getEmailId(), subject, body);		
		return model;
	}

	@Override
	public UserServiceModel updateUser(UserServiceDTO userServiceDTO, Long id, String token) {
		Long userId = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isUserPresent = userRepository.findById(id);
		if(isUserPresent.isPresent()) {
			isUserPresent.get().setName(userServiceDTO.getName());
			isUserPresent.get().setEmailId(userServiceDTO.getEmailId());
			isUserPresent.get().setPassword(userServiceDTO.getPassword());
			isUserPresent.get().setPhoneNumber(userServiceDTO.getPhoneNumber());
			isUserPresent.get().setDateOfbirth(userServiceDTO.getDateOfbirth());
			isUserPresent.get().setProfilePic(userServiceDTO.getProfilePic());
			isUserPresent.get().setCreatedAt(userServiceDTO.getCreatedAt().now());
			isUserPresent.get().setUpdatedAt(userServiceDTO.getUpdatedAt().now());
			userRepository.save(isUserPresent.get());
			String body = "User updated successfully with user Id" + isUserPresent.get().getId();
			String subject = "User updated Successfully";
			mailService.send(isUserPresent.get().getEmailId(), subject, body);
			return isUserPresent.get();
		}
		throw new UserNotFoundException(400, "User not present");
	}

	@Override
	public Optional<UserServiceModel> getUserById(Long id, String token) {		
		return userRepository.findById(id);
	}

	@Override
	public List<UserServiceModel> getAllUsers(String token) {
		Long userId = tokenUtil.decodeToken(token);
		List<UserServiceModel> getAllUsers = userRepository.findAll();
		if(getAllUsers.size()>0) {
			return getAllUsers;
		} else {
			throw new UserNotFoundException(400, "User not present");
		}	
	}

	@Override
	public Response deleteUser(Long id,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserServiceModel> isIdPresent = userRepository.findById(id);
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

	@Override
	public Response restoreUser(Long id,String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent()) {
			Optional<UserServiceModel> isIdPresent = userRepository.findById(id);
			if(isIdPresent.isPresent()) {
				isIdPresent.get().setActive(true);
				isIdPresent.get().setDeleted(false);
				userRepository.save(isIdPresent.get());
				return new Response(200, "success", isIdPresent.get());
			} else {
				throw new UserNotFoundException(400,"User not present");
			}
		}
		throw new UserNotFoundException(400, "Token not found");	
	}

	@Override
	public Response permanentDelete(Long id, String token) {
		Long userId = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isUserPresent = userRepository.findById(userId);
		if(isUserPresent.isPresent()) {
			Optional<UserServiceModel> isIdPresent = userRepository.findById(id);
			if(isIdPresent.isPresent()) {
				userRepository.delete(isIdPresent.get());
				return new Response(200, "Success", isIdPresent.get());
			} 
			throw new UserNotFoundException(400, "User not found");
		}		
		throw new UserNotFoundException(400, "Invalid token");
	}
	
	@Override
	public Response setProfilePic(Long id, MultipartFile profile, String token) {
		Long userId = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isUserPresent = userRepository.findById(userId);
		if(isUserPresent.isPresent()) {
			Optional<UserServiceModel> isIdPresent = userRepository.findById(id);
			if(isIdPresent.isPresent()) {
				isIdPresent.get().setProfilePic(profile);
				userRepository.delete(isIdPresent.get());
				return new Response(200, "Success", isIdPresent.get());
			} 
			throw new UserNotFoundException(400, "User not found");
		}		
		throw new UserNotFoundException(400, "Invalid token");
	}

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

	@Override
	public Response resetPassword(String emailId) {
		Optional<UserServiceModel> isMailPresent = userRepository.findByEmailId(emailId);
		if (isMailPresent.isPresent()){
			String token = tokenUtil.createToken(isMailPresent.get().getId());
			return new Response(400, "Reset password", token);
		}
		throw new UserNotFoundException(400, "Email not found");
	}

	@Override
	public Boolean validateUser(String token) {
		Long decode = tokenUtil.decodeToken(token);
		Optional<UserServiceModel> isTokenPresent = userRepository.findById(decode);
		if (isTokenPresent.isPresent())
			return true;
		throw new UserNotFoundException(400, "Token not found");
	}
}
