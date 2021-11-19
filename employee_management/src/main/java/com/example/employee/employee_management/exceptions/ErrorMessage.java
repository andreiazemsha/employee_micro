package com.example.employee.employee_management.exceptions;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

	private String errorMessage;
	private String requestingURI;
}
