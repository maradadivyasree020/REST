package com.example.crud.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.crud.model.Student;
import com.example.crud.repository.StudentRepository;
import reactor.core.publisher.Mono;
import com.example.crud.service.StudentClientService;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository repo;
    private final StudentClientService clientService; 

    public StudentController(StudentRepository repo,StudentClientService clientService) {
        this.repo = repo;
        this.clientService = clientService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping
    public List<Student> getAllStudents() {
        System.out.println(repo.findAll());
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }

    @GetMapping("/external-data")
    public Mono<String> getExternalData() {
        return clientService.getExampleData();
    }

    
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        System.out.println(student);
        return repo.save(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
        Student student = repo.findById(id).orElseThrow();
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        return repo.save(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
