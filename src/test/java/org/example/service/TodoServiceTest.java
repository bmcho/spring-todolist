package org.example.service;

import org.example.Repository.TodoRepository;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void add() {
        Mockito.when(this.todoRepository.save(ArgumentMatchers.any(TodoEntity.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        TodoRequest ex = new TodoRequest();
        ex.setTitle("Test Title");

        TodoEntity ac = this.todoService.add(ex);

        Assertions.assertEquals(ex.getTitle(), ac.getTitle());
    }

    @Test
    void searchById() {
        TodoEntity entity = new TodoEntity();
        entity.setId(123L);
        entity.setTitle("test");
        entity.setOrder(0L);
        entity.setCompleted(false);
        Optional<TodoEntity> optional = Optional.of(entity);

        BDDMockito.given(this.todoRepository.findById(ArgumentMatchers.anyLong()))
                .willReturn(optional);

        TodoEntity ac = this.todoService.searchById(123L);
        TodoEntity ex = optional.get();

        Assertions.assertEquals(ex.getId(), ac.getId());
        Assertions.assertEquals(ex.getTitle(), ac.getTitle());
        Assertions.assertEquals(ex.getCompleted(), ac.getCompleted());
    }

    @Test
    public void searchByIdFailed() {
        BDDMockito.given(this.todoRepository.findById(ArgumentMatchers.anyLong()))
                .willReturn(Optional.empty());

        Assertions.assertThrows(ResponseStatusException.class, () -> {
            this.todoService.searchById(123L);
        });
    }
}