package com.example.employee.employee_management.dtos;

import com.example.employee.employee_management.entities.EmployeeEvents;
import com.example.employee.employee_management.entities.EmployeeStates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMessageDTO {

    private Long id;
    private String name;
    private int age;
    private String email;
    private EmployeeStates state;
    private EmployeeEvents event;

}
