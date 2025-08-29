package com.example.crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import com.example.crud.model.Student;
import com.example.crud.repository.StudentRepository;
import com.example.crud.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @Autowired
    private StudentService service;

    @GetMapping("/hello") //done -test
    public String hello() {
        System.out.println("Hello World endpoint hit");    
        return "Hello World!";
    }

    @GetMapping //done -test
    public List<Student> getAllStudents(@RequestParam(value = "search", required = false) String search) {
        return service.getAllStudents(search);
    }


    @GetMapping("/{id}")
    public Optional<Student> getStudent(@PathVariable Long id) {
        System.out.println(id);
        return service.getStudent(id);
    }

    
    @PostMapping //done -test
    public Student createStudent(@RequestBody Student student) {
        return service.createStudent(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student studentDetails) {
       return service.updateStudent(id,studentDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        service.deleteStudent(id);
    }
}
