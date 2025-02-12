package com.example.todoapp.unit.service;

import com.example.todoapp.entity.TodoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoItemTest {
    private TodoItem todoItem;

    @BeforeEach
    void setUp() {
        todoItem = new TodoItem("default");
    }

    @ParameterizedTest
    @ValueSource(strings = {"test title", "test", "title", "title123"})
    void constructorShouldSetCorrectTitle(String title) {

        //when
        TodoItem todoItem = new TodoItem(title);

        //then
        assertThat(todoItem.getTitle(), equalTo(title));
    }

    @Test
    void constructorShouldSetCorrectIsCompleteToFlase() {

        //then
        assertThat(todoItem.isCompleted(), equalTo(false));
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "testTitle"})
    void setTitleShouldChangeTitle(String newTitle) {

        //when
        todoItem.setTitle(newTitle);

        //then
        assertThat(todoItem.getTitle(), equalTo(newTitle));
    }

    @Test
    void setCompleteShouldChangeCompleteStatus() {

        //when
        todoItem.setCompleted(true);

        //then
        assertThat(todoItem.isCompleted(), equalTo(true));
    }
}
