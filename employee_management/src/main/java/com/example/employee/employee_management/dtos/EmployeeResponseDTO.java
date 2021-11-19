package com.example.employee.employee_management.dtos;

import com.example.employee.employee_management.entities.EmployeeStates;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class EmployeeResponseDTO {
    private Long id;

    private String name;
    private int age;
    private String email;

    @Enumerated(EnumType.STRING)
    private EmployeeStates state;

}
