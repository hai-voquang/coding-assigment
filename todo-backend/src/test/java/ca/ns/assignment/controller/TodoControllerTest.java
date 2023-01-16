package ca.ns.assignment.controller;

import ca.ns.assignment.model.entity.Todo;
import ca.ns.assignment.repository.TodoRepository;
import ca.ns.assignment.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TodoController.class)
class TodoControllerTest {
    private static final String PREFIX_URI = "/api/v1/todos";
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoRepository todoRepository;

    private Todo expected;

    @Test
    void getAlladdTodosTest() throws Exception {
        //Given
        List<Todo> todoList = getTodoList();
        when(todoService.fetchAll()).thenReturn(todoList);

        //Act
        MvcResult result = mockMvc.perform(get(PREFIX_URI + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert

        var actuals = new ObjectMapper().readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertEquals(actuals.size(),3);
    }

    @Test
    public void saveTodoTest() throws Exception {
        // Arrange
        expected = Todo.builder()
                .name("new todo")
                .build();
        when(todoService.addTodo(expected)).thenReturn(expected);

        // Act
        MvcResult result = mockMvc.perform(post(PREFIX_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expected))).andReturn();

        // Assert
        var actualTodo = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Todo.class);
        assertEquals(result.getResponse().getStatus(), HttpStatus.CREATED.value());
        assertEquals(expected, actualTodo);
    }

    @Test
    public void getTodoTest() throws Exception {
        // Arrange
        Long id = 1l;
        expected = Todo.builder()
                .name("old todo")
                .build();
        when(todoService.getTodo(id)).thenReturn(Optional.of(expected));

        // Act
        MvcResult result = mockMvc.perform(get(PREFIX_URI + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Assert
        var actualTodo = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Todo.class);
        assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
        assertEquals(expected, actualTodo);
    }

    @Test
    public void getTodoNotFoundTest() throws Exception {
        // Arrange
        Long id = 1l;
        when(todoService.getTodo(id)).thenReturn(Optional.empty());

        // Act
        MvcResult result = mockMvc.perform(get(PREFIX_URI + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Assert
        assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
    }

    private List<Todo> getTodoList() {
        List<Todo> todoList = new ArrayList<>();
        todoList.add(Todo.builder()
                .id(1l)
                .name("todo1")
                .completed(false)
                .build());
        todoList.add(Todo.builder()
                .id(2l)
                .name("todo2")
                .completed(false)
                .build());
        todoList.add(Todo.builder()
                .id(3l)
                .name("todo3")
                .completed(true)
                .build());
        return todoList;
    }
}