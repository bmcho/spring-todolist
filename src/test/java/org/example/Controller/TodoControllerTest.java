package org.example.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TodoService todoService;

    private TodoEntity ex;

    @BeforeEach
    void setUp() {
        this.ex = new TodoEntity();
        this.ex.setId(123L);
        this.ex.setTitle("test title");
        this.ex.setOrder(0L);
        this.ex.setCompleted(false);
    }

    @Test
    void create() throws Exception {
        Mockito.when(this.todoService.add(ArgumentMatchers.any(TodoRequest.class)))
                .then((i) -> {
                    TodoRequest request = i.getArgument(0, TodoRequest.class);
                    return new TodoEntity(
                            this.ex.getId(),
                            request.getTitle(),
                            this.ex.getOrder(),
                            this.ex.getCompleted());
                });

        TodoRequest request = new TodoRequest();
        request.setTitle("ANY TITLE");

        ObjectMapper mapper = new ObjectMapper();
        String content = mapper.writeValueAsString(request);

        this.mvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("ANY TITLE"));
    }

    @Test
    void readOne() {
    }
}