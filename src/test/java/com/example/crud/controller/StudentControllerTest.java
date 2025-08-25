package com.example.crud.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.crud.model.Student;
import com.example.crud.repository.StudentRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(StudentController.class)  // <-- Specify the controller class here, NOT the test class
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock dependencies of StudentController
    @MockBean
    private StudentRepository repo;

    // Mock WebClient.Builder bean to avoid ApplicationContext loading errors
    @MockBean
    private WebClient.Builder webClientBuilder;

    @Test
    public void hello() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/students/hello").accept("application/json");
        // MvcResult result = mockMvc.perform(request).andReturn();
        // System.out.println(result.getResponse().getContentAsString());
        // assertEquals("Hello World!", result.getResponse().getContentAsString());
       
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string("Hello World!")).andReturn();
    }

    @Test
    public void createStudent() throws Exception {
        String studentJson = """
        {
          "name": "John Doe",
          "email": "john@example.com"
        }
        """;

        // Mocked student returned by repo.save()
        Student savedStudent = new Student(0, studentJson, studentJson);
        savedStudent.setId(100L);
        savedStudent.setName("John Doe");
        savedStudent.setEmail("jhon@example.com");

        given(repo.save(any(Student.class))).willReturn(savedStudent);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(studentJson))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("jhon@example.com"));
    }

    

}
