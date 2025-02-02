package com.practice.user_management.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailExistsException(EmailAlreadyExistsException ex) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    
    
   
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String,String> handleInvalidArugument(MethodArgumentNotValidException ex){
		
		Map<String,String> errorMap=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error-> {
			errorMap.put(error.getField(),error.getDefaultMessage());	
		});
		return errorMap;
	}
	

    
    
    
	
	  
}
