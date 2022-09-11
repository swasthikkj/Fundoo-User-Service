package com.bridgelabz.fundoouserservice.exception.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoouserservice.exception.UserNotFoundException;
import com.bridgelabz.fundoouserservice.util.Response;
@ControllerAdvice
public class UserExceptionHandler {
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Response> handleId(UserNotFoundException ab) {
		Response response = new Response();
		response.setErrorcode(400);
		response.setMessage(ab.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
