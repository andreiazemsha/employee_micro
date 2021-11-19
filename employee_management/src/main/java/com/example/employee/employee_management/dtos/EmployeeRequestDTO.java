package com.example.employee.employee_management.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class EmployeeRequestDTO {

    private String name;
    private int age;
    @Email
    private String email;
}
