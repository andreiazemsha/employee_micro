package com.example.employee.employee_management.services;

import com.example.employee.employee_management.dtos.EmployeeMessageDTO;
import com.example.employee.employee_management.entities.Employee;
import com.example.employee.employee_management.entities.EmployeeEvents;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface EmployeeRequestHandlerService {

    Employee sendCreateEmployeeMessage(EmployeeMessageDTO employeeMessageDTO) throws ExecutionException, InterruptedException;

    List<Employee> getAllEmployees();

    Employee sendChangeEmployeeMessage(Long id, EmployeeEvents event) throws ExecutionException, InterruptedException;

}
