package com.example.employee.employee_modifications.dtos;

import com.example.employee.employee_modifications.entities.EmployeeEvents;
import com.example.employee.employee_modifications.entities.EmployeeStates;
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
