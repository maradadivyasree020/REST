package com.example.crud.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import java.util.List;
import java.util.Optional;

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

// import org.springframework.web.reactive.function.client.WebClient;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
    // @MockBean
    // private WebClient.Builder webClientBuilder;

    @Test //hello function
    public void hello() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/students/hello").accept("application/json");
        // MvcResult result = mockMvc.perform(request).andReturn();
        // System.out.println(result.getResponse().getContentAsString());
        // assertEquals("Hello World!", result.getResponse().getContentAsString());
       
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andExpect(content().string("Hello World!")).andReturn();
    }

    @Test //create
    public void createStudent() throws Exception {
        String studentJson = """
        {
        "name": "John Doe",
        "email": "john@example.com"
        }
        """;

        // Mocked student returned by repo.save()
        // Use proper constructor here: id, name, email
        Student savedStudent = new Student(100L, "John Doe", "john@example.com");

        // Mock the repository save method to return the savedStudent
        given(repo.save(any(Student.class))).willReturn(savedStudent);

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(studentJson))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(100L))
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.email").value("john@example.com"));
    }


    @Test //inavlid json
    public void testCreateStudent_invalidJson_shouldReturnBadRequest() throws Exception {
        String invalidJson = "{ \"name\": \"John Doe\", \"email\": }";  // malformed

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest());
    }

    // @Test //missing fields - present no backend validation
    // public void testCreateStudent_missingFields_shouldReturnBadRequest() throws Exception {
    //     String jsonMissingEmail = """
    //     {
    //         "name": "John Doe"
    //     }
    //     """;

    //     mockMvc.perform(post("/students")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(jsonMissingEmail))
    //         .andExpect(status().isBadRequest());
    // }

    @Test //without search
    public void testGetAllStudents_noSearchParam_shouldReturnAllStudents() throws Exception {
        List<Student> students = Arrays.asList(
            new Student(1L, "Alice", "alice@example.com"),
            new Student(2L,"Bob", "bob@example.com")
        );

        given(repo.findAll()).willReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("Alice"))
            .andExpect(jsonPath("$[1].email").value("bob@example.com"));

        verify(repo, times(1)).findAll();
        verify(repo, never()).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString());
    }

    @Test //empty search
    public void testGetAllStudents_emptySearchParam_shouldReturnAllStudents() throws Exception {
        String search = "";

        List<Student> students = Arrays.asList(
            new Student( 1L,"Charlie", "charlie@example.com")
        );

        given(repo.findAll()).willReturn(students);

        mockMvc.perform(MockMvcRequestBuilders.get("/students")
                .param("search", search))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Charlie"));

        verify(repo, times(1)).findAll();
        verify(repo, never()).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString());
    }

    @Test // search but no match
    public void testGetAllStudents_searchParam_noMatches() throws Exception {
        String search = "nomatch";

        given(repo.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search))
            .willReturn(Arrays.asList());

        mockMvc.perform(MockMvcRequestBuilders.get("/students")
                .param("search", search))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());

        verify(repo, times(1)).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search);
        verify(repo, never()).findAll();
    }

    @Test
    public void testUpdateStudent_success() throws Exception {
        Long studentId = 1L;

        Student existingStudent = new Student(1L,"Old Name", "old@example.com");
        Student updatedDetails = new Student(1L,"New Name", "new@example.com");
        Student savedStudent = new Student(1L, "New Name", "new@example.com");

        when(repo.findById(studentId)).thenReturn(Optional.of(existingStudent));
        when(repo.save(any(Student.class))).thenReturn(savedStudent);

        String updateJson = """
            {
                "name": "New Name",
                "email": "new@example.com"
            }
            """;

        mockMvc.perform(MockMvcRequestBuilders.put("/students/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
            .andExpect(status().isOk())
            // .andExpect(jsonPath("$.id").value(studentId))
            .andExpect(jsonPath("$.name").value("New Name"))
            .andExpect(jsonPath("$.email").value("new@example.com"));

        verify(repo, times(1)).findById(studentId);
        verify(repo, times(1)).save(any(Student.class));
    }
    @Test
public void testDeleteStudent_success() throws Exception {
    Long studentId = 1L;

    // Mock repo.deleteById to do nothing (success)
    doNothing().when(repo).deleteById(studentId);

    mockMvc.perform(MockMvcRequestBuilders.delete("/students/{id}", studentId))
        .andExpect(status().isOk());  // or .isNoContent() if you change controller to return ResponseEntity.noContent()

    verify(repo, times(1)).deleteById(studentId);
}




    

}
