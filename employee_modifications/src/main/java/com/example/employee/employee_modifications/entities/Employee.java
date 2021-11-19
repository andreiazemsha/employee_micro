package com.example.employee.employee_modifications.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;
    private String email;
    private EmployeeStates state;

    public Employee(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.state = EmployeeStates.ADDED;
    }
}
