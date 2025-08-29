package com.example.crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.crud.model.Student;
import com.example.crud.repository.StudentRepository;

@Service
public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    public Student createStudent(Student student) {
        return repo.save(student);
    }

    public Optional<Student> getStudent(Long id){
        return repo.findById(id);
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student student = repo.findById(id).orElseThrow();
        student.setName(studentDetails.getName());
        student.setEmail(studentDetails.getEmail());
        return repo.save(student);
    }


    public void deleteStudent(Long id) {
        repo.deleteById(id);
    }

    public List<Student> getAllStudents(String search) {
        if (search != null && !search.isEmpty()) {
            return repo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
        }
        return repo.findAll();
    }
    
}
