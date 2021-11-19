package com.example.employee.employee_management.controllers;

import com.example.employee.employee_management.dtos.EmployeeMessageDTO;
import com.example.employee.employee_management.dtos.EmployeeRequestDTO;
import com.example.employee.employee_management.dtos.EmployeeResponseDTO;
import com.example.employee.employee_management.entities.EmployeeEvents;
import com.example.employee.employee_management.entities.Employee;
import com.example.employee.employee_management.exceptions.EmployeeNotFoundException;
import com.example.employee.employee_management.services.EmployeeRequestHandlerService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class EmployeeController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmployeeRequestHandlerService employeeRequestHandlerService;

    @PostMapping("/employee")
    @ApiOperation("Creates a new employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO) throws ExecutionException, InterruptedException {
        EmployeeMessageDTO employeeMessageDTO = modelMapper.map(employeeRequestDTO, EmployeeMessageDTO.class);
        Employee createdEmployee = employeeRequestHandlerService.sendCreateEmployeeMessage(employeeMessageDTO);
        EmployeeResponseDTO employeeResponseDTO = modelMapper.map(createdEmployee, EmployeeResponseDTO.class);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(employeeResponseDTO.getId()).toUri();
        return ResponseEntity.created(location).body(employeeResponseDTO);
    }

    @GetMapping("/employees")
    @ApiOperation("Retrieves full list of employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployees() {
        List<Employee> employees = employeeRequestHandlerService.getAllEmployees();
        if (employees.isEmpty()) {
            throw new EmployeeNotFoundException("Employees weren't found");
        }
        List<EmployeeResponseDTO> employeeResponseDTOS = Arrays.asList(modelMapper.map(employees, EmployeeResponseDTO[].class));
        return ResponseEntity.ok(employeeResponseDTOS);
    }

    @PutMapping("/employee/{employeeId}/{event}")
    @ApiOperation("Sends provided event for employee with provided ID which changes employee state")
    public ResponseEntity<EmployeeResponseDTO> processEmployeeEvent(@PathVariable Long employeeId, @PathVariable EmployeeEvents event) throws ExecutionException, InterruptedException {
        Employee createdEmployee = employeeRequestHandlerService.sendChangeEmployeeMessage(employeeId, event);
        EmployeeResponseDTO employeeResponseDTO = modelMapper.map(createdEmployee, EmployeeResponseDTO.class);
        return ResponseEntity.accepted().body(employeeResponseDTO);
    }

}
