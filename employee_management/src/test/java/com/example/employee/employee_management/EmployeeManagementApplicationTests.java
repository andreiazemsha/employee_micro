package com.example.employee.employee_management;

import com.example.employee.employee_management.dtos.EmployeeRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = EmployeeManagementApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeManagementApplicationTests {

    @Value("${local.server.port}")
    private int ports;

    @BeforeAll
    public void setUp() {
        port = ports;
        baseURI = "http://localhost/";
    }

    @Test
    public void post_newEmployee_returnsNewEmployee_201() {
        EmployeeRequestDTO employeeRequestDTO = new EmployeeRequestDTO("Glen", 66, "blabla@bla.bla");

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(employeeRequestDTO)
                .when().post("/employee")
                .then();

        response.assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo("Glen"))
                .body("age", equalTo(66))
                .body("email", equalTo("blabla@bla.bla"))
                .body("state", equalTo("ADDED"));
    }

    @Test
    public void get_AllEmployees_returnsAllEmployees_200() {

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/employees")
                .then();

        response.assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("content.size()", greaterThanOrEqualTo(4))
                .body("find {it.id == 3}.email", equalTo("third_email@mail.com"))
                .body("find {it.id == 2}.age", equalTo(16))
                .body("name", hasItems("Andrew", "Brian", "Creed"))
                .body("state", not(hasItem("ACTIVE")));
    }

    @Test
    public void put_registerEmployee_returnsRegisteredEmployee_202() {

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/employee/{employeeId}/REGISTER", 2)
                .then();

        response.assertThat()
                .statusCode(HttpStatus.ACCEPTED.value())
                .body("id", equalTo(2))
                .body("name", equalTo("Brian"))
                .body("age", equalTo(16))
                .body("email", equalTo("second_email@mail.com"))
                .body("state", equalTo("IN_CHECK"));
    }

    @Test
    public void put_activateEmployee_returnsConflict_409() {

        ValidatableResponse response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/employee/{employeeId}/ACTIVATE", 3)
                .then();

        response.assertThat()
                .statusCode(HttpStatus.CONFLICT.value())
                .body("errorMessage", containsString("409 Can't apply event ACTIVATE to employee - 3 - with current state ADDED"));
    }
}
