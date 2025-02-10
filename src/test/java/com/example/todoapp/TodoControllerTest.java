package com.example.todoapp;

import com.example.todoapp.controller.TodoController;
import com.example.todoapp.entity.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TodoControllerTest {
    @Mock
    private TodoItemRepository todoItemRepository;

    @InjectMocks
    private TodoController todoController;

    @Test
    void indexShouldReturnIndexView() {

        //given
        List<TodoItem> todoItems = Arrays.asList(new TodoItem("Task1"), new TodoItem("Task2"));
        when(todoItemRepository.findAll()).thenReturn(todoItems);
        Model model = new ConcurrentModel();

        //when
        String view = todoController.index(model);

        //then
        assertThat(view, equalTo("index"));
        assertThat(model.containsAttribute("todoItems"), is(true));
        assertThat(model.containsAttribute("newTodo"), is(true));
        assertThat(model.getAttribute("todoItems"), equalTo(todoItems));
        assertThat(model.getAttribute("newTodo"), instanceOf(TodoItem.class));
    }

    @Test
    void addShouldSaveNewTodo() {

        //given
        TodoItem newTodo = new TodoItem("New Task");

        //when
        String redirect = todoController.addTodo(newTodo);

        //then
        verify(todoItemRepository).save(newTodo);
        assertThat(redirect, equalTo("redirect:/"));
    }

    @Test
    void toggleShouldToggleStatusWhenTodoExists() {

        //given
        TodoItem existingTodo = new TodoItem("Task");
        existingTodo.setCompleted(false);
        Long id = 1L;
        when(todoItemRepository.findById(id)).thenReturn(Optional.of(existingTodo));

        //when
        String redirect = todoController.toggleTodo(id);

        //then
        assertThat(existingTodo.isCompleted(), is(true));
        verify(todoItemRepository).save(existingTodo);
        assertThat(redirect, equalTo("redirect:/"));
    }

    @Test
    void deleteShouldDeleteTask() {

        //given
        Long id = 1L;

        //when
        String redirect = todoController.deleteTodo(id);

        //then
        verify(todoItemRepository).deleteById(id);
        assertThat(redirect, equalTo("redirect:/"));
    }
}
