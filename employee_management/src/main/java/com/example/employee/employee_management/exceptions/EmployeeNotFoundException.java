package com.example.employee.employee_management.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

public class EmployeeNotFoundException extends HttpStatusCodeException {

    public EmployeeNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }

}
