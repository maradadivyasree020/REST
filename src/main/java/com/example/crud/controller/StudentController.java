package com.example.crud.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.crud.model.Student;
import com.example.crud.repository.StudentRepository;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentRepository repo;

    public StudentController(StudentRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/hello") //done -test
    public String hello() {
        System.out.println("Hello World endpoint hit");    
        return "Hello World!";
    }

    @GetMapping //done -test
    public List<Student> getAllStudents(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return repo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
        }
        return repo.findAll();
    }


    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
    }

    
    @PostMapping //done -test
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
