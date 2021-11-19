package com.example.employee.employee_management.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class EmployeeExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ EmployeeNotFoundException.class })
	public final ResponseEntity<Object> handleEmployeeException(EmployeeNotFoundException ex, WebRequest request) {
		ErrorMessage ErrorMessage = new ErrorMessage(ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(ErrorMessage, ex.getStatusCode());
	}

	@ExceptionHandler({ HttpClientErrorException.class })
	public final ResponseEntity<Object> handleEmployeeConflictException(HttpClientErrorException ex, WebRequest request) {
		ErrorMessage ErrorMessage = new ErrorMessage(ex.getLocalizedMessage(), request.getDescription(false));
		return new ResponseEntity<>(ErrorMessage, ex.getStatusCode());
	}

	@ExceptionHandler({ Exception.class })
	public final ResponseEntity<Object> handleEmployeeGeneralException(Exception ex, WebRequest request) {
		ErrorMessage ErrorMessage = new ErrorMessage(ex.getLocalizedMessage(), request.getDescription(true));
		return new ResponseEntity<>(ErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage ErrorMessage = new ErrorMessage(ex.getLocalizedMessage(), request.getDescription(false));
		return new ResponseEntity<>(ErrorMessage, status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		ErrorMessage ErrorMessage = new ErrorMessage(ex.getLocalizedMessage(), request.getDescription(false));
		return new ResponseEntity<>(ErrorMessage, status);
	}
}