package com.bridgelabz.fundoouserservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoouserservice.dto.UserServiceDTO;
import com.bridgelabz.fundoouserservice.model.UserServiceModel;
import com.bridgelabz.fundoouserservice.service.IUserService;
import com.bridgelabz.fundoouserservice.util.Response;


/**
 * Purpose:create user service controller
 * @version 4.15.1.RELEASE
 * @author Swasthik KJ
 */

@RestController
@RequestMapping("/fundooUserService")
public class UserServiceController {
	@Autowired
	IUserService userService;

	/**
	 * Purpose:add user
	 */

	@PostMapping("/addUser")
	public ResponseEntity<Response> addUser(@RequestBody UserServiceDTO userServiceDTO) {
		UserServiceModel userModel = userService.addUser(userServiceDTO);
		Response response = new Response(200, "user added successfully", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);		
	}

	/**
	 * Purpose:update user
	 */

	@PutMapping("updateUser")
	public ResponseEntity<Response> updateUser(@RequestBody UserServiceDTO userServiceDTO, @PathVariable Long id, @RequestHeader String token) {
		UserServiceModel userModel = userService.updateUser(userServiceDTO, id, token);
		Response response = new Response(200, "User updated successfully", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:get user by id
	 */

	@GetMapping("/getUserById/{id}")
	public ResponseEntity<Response> getUserById(@PathVariable Long id, @RequestHeader String token) {
		Optional<UserServiceModel> userModel = userService.getUserById(id, token);
		Response response = new Response(200, "USER fetched by id successfully", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:get all users
	 */

	@GetMapping("/getAllUsers")
	public ResponseEntity<Response> getAllUsers(@RequestHeader String token) {
		List<UserServiceModel> userModel = userService.getAllUsers(token);
		Response response = new Response(200, "All users fetched successfully", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);	
	}

	/**
	 * Purpose:delete user
	 */

	@DeleteMapping("deleteUser/{id}")
	public ResponseEntity<Response> deleteUser(@PathVariable Long id,  @RequestHeader String token) {
		Response userModel = userService.deleteUser(id, token);
		Response response = new Response(200, "User deleted successfully", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:restore user
	 */

	@GetMapping("restoreUser/{id}")
	public ResponseEntity<Response> restoreUser(@PathVariable Long id,@RequestHeader String token) {
		Response userModel = userService.restoreUser(id,token);
		Response response = new Response(200, "User restored successfully", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:delete permanent user
	 */

	@DeleteMapping("deletePermanent/{id}")
	public ResponseEntity<Response> permanentDelete(@PathVariable Long id,@RequestHeader String token) {
		Response userModel = userService.permanentDelete(id,token);
		Response response = new Response(200, "User deleted permanently ", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:To set profile pic
	 * @throws IOException 
	 */

	@PutMapping("profilePic/{id}")
	public ResponseEntity<Response> setProfilePic(@PathVariable Long id, @RequestParam MultipartFile profilePic) throws IOException {
		Response userModel = userService.setProfilePic(id, profilePic);
		Response response = new Response(200, "User profile pic set successfully", userModel);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Purpose:login to generate token
	 * @Param enter email and password
	 */

	@PostMapping("/login")
	public Response login(@RequestParam String emailId, @RequestParam String password) {
		return userService.login(emailId, password);
	}

	/**
	 * Purpose:reset user password
	 * @Param email
	 */

	@PostMapping("/resetPassword")
	public Response resetPassword(@RequestParam String emailId) {
		return userService.resetPassword(emailId);
	}

	/**
	 * Purpose:change user password
	 * @Param password
	 */

	@PutMapping("/changePassword/{token}")
	public UserServiceModel changePassword(@PathVariable("token") String token, @RequestParam String password) {
		return userService.changePassword(token, password);
	}

	/**
	 * Purpose:validate user
	 */

	@GetMapping("/validateUser/{token}")
	public Boolean validateUser(@PathVariable String token) {
		return userService.validateUser(token);
	}

	/**
	 * Purpose:validate email
	 */

	@GetMapping("/validateEmail/{email}")
	public Boolean validateEmail(@PathVariable String email) {
		return userService.validateEmail(email);
	}
}
