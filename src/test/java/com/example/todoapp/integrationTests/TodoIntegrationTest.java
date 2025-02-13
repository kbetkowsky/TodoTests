package com.example.todoapp.integrationTests;

import com.example.todoapp.entity.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TodoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoItemRepository todoItemRepository;

    @BeforeEach
    void setup() {
        todoItemRepository.deleteAll();
    }

    @Test
    void shouldReturnIndexPage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("todoItems"))
                .andExpect(model().attributeExists("newTodo"));
    }

    @Test
    void shouldAddNewTodo() throws Exception {
        mockMvc.perform(post("/add").param("title", "zadanie testowe"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));

        List<TodoItem> todoItems = todoItemRepository.findAll();
        assertThat(todoItems).hasSize(1);
        assertThat(todoItems.get(0).getTitle()).isEqualTo("zadanie testowe");
        assertThat(todoItems.get(0).isCompleted()).isFalse();
    }

    @Test
    void shouldToggleSwtichToComplete() throws Exception {
        TodoItem todoItem = new TodoItem("new test task");
        todoItemRepository.save(todoItem);

        mockMvc.perform(post("/toggle/" + todoItem.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        TodoItem updatedTodo = todoItemRepository.findById(todoItem.getId()).orElseThrow();
        assertThat(updatedTodo.isCompleted()).isTrue();
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        TodoItem todoItem = new TodoItem("test task");
        todoItemRepository.save(todoItem);

        mockMvc.perform(delete("/delete/" + todoItem.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertThat(todoItemRepository.findAll()).isEmpty();
    }
}
