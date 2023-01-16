package ca.ns.assignment.controller;


import ca.ns.assignment.exception.TodoNotFoundException;
import ca.ns.assignment.model.entity.Todo;
import ca.ns.assignment.service.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
@Slf4j
public class TodoController {
    private final TodoService todoService;

    @Autowired

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public ResponseEntity<Todo> addTodo(@RequestBody Todo todo) {
        log.info("Processing addTodo request");
        Todo newTodo = todoService.addTodo(todo);
        return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable("id") Long id) {
        log.info("Processing getTodo request with id: {}", id);
        return todoService.getTodo(id)
                .map(todo -> new ResponseEntity<>(todo, HttpStatus.OK))
                .orElseThrow(() -> new TodoNotFoundException(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Todo>> getAllTodos() {
        log.info("Processing getAllTodos request");
        return new ResponseEntity<>(todoService.fetchAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<List<Todo>> delete(@PathVariable("id") Long id) {
        log.info("Processing deleteTodo request with id: {}", id);
        todoService.delete(id);
        return new ResponseEntity<>(todoService.fetchAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}/mark-as-done")
    public ResponseEntity<Todo> changeStatus(@PathVariable("id") Long id) {
        log.info("Processing changeStatus request with id: {}", id);
        return todoService.changeStatus(id)
                .map(updatedTodo -> new ResponseEntity<>(updatedTodo, HttpStatus.OK))
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    @PutMapping("/{id}/edit-name")
    public ResponseEntity<Todo> changeName(@PathVariable("id") Long id, @RequestBody Todo todo) {
        log.info("Processing changeName request with id: {}", id);
        return todoService.changeName(id, todo.getName())
                .map(updatedTodo -> new ResponseEntity<>(updatedTodo, HttpStatus.OK))
                .orElseThrow(() -> new TodoNotFoundException(id));
    }
}
