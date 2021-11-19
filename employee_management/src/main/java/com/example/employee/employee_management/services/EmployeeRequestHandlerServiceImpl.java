package com.example.employee.employee_management.services;

import com.example.employee.employee_management.dtos.EmployeeMessageDTO;
import com.example.employee.employee_management.entities.EmployeeEvents;
import com.example.employee.employee_management.exceptions.EmployeeNotFoundException;
import com.example.employee.employee_management.repositories.EmployeeRepository;
import com.example.employee.employee_management.entities.Employee;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class EmployeeRequestHandlerServiceImpl implements EmployeeRequestHandlerService {

    @Autowired
    ReplyingKafkaTemplate<String, EmployeeMessageDTO, Employee> kafkaTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Value("${spring.kafka.topic.employee.create}")
    private String createTopic;

    @Value("${spring.kafka.topic.employee.change-state}")
    private String changeTopic;

    @Value("${spring.kafka.topic.employee.reply}")
    private String replyTopic;


    @Override
    public Employee sendCreateEmployeeMessage(EmployeeMessageDTO employeeMessageDTO) throws ExecutionException, InterruptedException {
        return sendMessageAndGetResult(employeeMessageDTO, createTopic);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee sendChangeEmployeeMessage(Long id, EmployeeEvents event) throws ExecutionException, InterruptedException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new EmployeeNotFoundException("Employee with ID - " + id + " - was not found");
        }
        EmployeeMessageDTO employeeMessageDTO = modelMapper.map(optionalEmployee.get(), EmployeeMessageDTO.class);
        employeeMessageDTO.setEvent(event);
        Employee responseMessage = sendMessageAndGetResult(employeeMessageDTO, changeTopic);
        if (responseMessage == null) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Can't apply event " + event.name() + " to employee - " + optionalEmployee.get().getId() + " - with current state " + optionalEmployee.get().getState());
        }
        return responseMessage;
    }

    private Employee sendMessageAndGetResult(EmployeeMessageDTO employeeMessageDTO, String changeTopic) throws InterruptedException, ExecutionException {
        ProducerRecord<String, EmployeeMessageDTO> record = new ProducerRecord<>(changeTopic, employeeMessageDTO);
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes()));
        RequestReplyFuture<String, EmployeeMessageDTO, Employee> sendAndReceive = kafkaTemplate.sendAndReceive(record);
        ConsumerRecord<String, Employee> consumerRecord = sendAndReceive.get();
        return consumerRecord.value();
    }
}
