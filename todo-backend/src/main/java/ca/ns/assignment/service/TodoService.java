package ca.ns.assignment.service;

import ca.ns.assignment.model.entity.Todo;
import ca.ns.assignment.repository.TodoRepository;
import ca.ns.assignment.utils.InputUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }
    public Todo addTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public void delete(Long id) {
        todoRepository.deleteById(id);
    }

    public List<Todo> fetchAll() {
        return todoRepository.findAll();
    }

    public Optional<Todo> changeStatus(Long id) {
        Optional<Todo> opTodo = todoRepository.findById(id);
        opTodo.ifPresent(todo -> {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        });
        return opTodo;
    }

    public Optional<Todo> getTodo(Long id) {
        return todoRepository.findById(id);
    }

    public Optional<Todo> changeName(Long id, String name) {
        Optional<Todo> opTodo = todoRepository.findById(id);
        opTodo.ifPresent(todo -> {
            todo.setName(name);
            todoRepository.save(todo);
        });
        return opTodo;
    }
}
