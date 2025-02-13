package com.example.todoapp.controller;

import com.example.todoapp.entity.TodoItem;
import com.example.todoapp.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TodoController {

    @Autowired
    private TodoItemRepository todoItemRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<TodoItem> todoItems = todoItemRepository.findAll();
        model.addAttribute("todoItems", todoItems);
        model.addAttribute("newTodo", new TodoItem());
        return "index";
    }

    @PostMapping("/add")
    public String addTodo(@ModelAttribute TodoItem newTodo) {
        todoItemRepository.save(newTodo);
        return "redirect:/";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id) {
        TodoItem todoItem = todoItemRepository.findById(id).orElse(null);
        if (todoItem != null) {
            todoItem.setCompleted(!todoItem.isCompleted());
            todoItemRepository.save(todoItem);
        }
        return "redirect:/";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoItemRepository.deleteById(id);
        return "redirect:/";
    }
}
