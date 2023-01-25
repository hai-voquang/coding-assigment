package ca.ns.assignment.controller;

import ca.ns.assignment.model.entity.Todo;
import ca.ns.assignment.service.TodoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(value = TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Todo Backend API test cases")
class TodoControllerTest {
    private static final String PREFIX_URI = "/api/v1/todos";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    private Todo expected;

    @Test
    @DisplayName("Get all todos test case")
    void getAllTodosTest() throws Exception {
        //Given
        List<Todo> todoList = getTodoList();
        when(todoService.fetchAll()).thenReturn(todoList);

        //Act
        MvcResult result = mockMvc.perform(get(PREFIX_URI + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Assert

        var actuals = new ObjectMapper().readValue(result.getResponse().getContentAsString(), List.class);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(actuals.size(), 3);
    }

    @Test
    @DisplayName("Save new todo test case")
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
        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        assertEquals(expected, actualTodo);
    }

    @Test
    @DisplayName("Get existing todo detail test case")
    public void getTodoTest() throws Exception {
        // Arrange
        Long id = 1L;
        expected = Todo.builder()
                .name("old todo")
                .build();
        when(todoService.getTodo(id)).thenReturn(Optional.of(expected));

        // Act
        MvcResult result = mockMvc.perform(get(PREFIX_URI + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        // Assert
        var actualTodo = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Todo.class);
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(expected, actualTodo);
    }

    @Test
    @DisplayName("Get non-existing todo and return not found test case")
    public void getTodoNotFoundTest() throws Exception {
        // Arrange
        Long id = 1L;
        when(todoService.getTodo(id)).thenReturn(Optional.empty());

        // Act
        MvcResult result = mockMvc.perform(get(PREFIX_URI + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn();
        // Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus(), "HTTP 404 is not returned");
    }

    @Test
    @DisplayName("Update todo name test case")
    public void updateTodoNameTest() throws Exception {
        // Arrange
        Long id = 1L;
        String newTodoName = "new name";
        expected = Todo.builder()
                .name(newTodoName)
                .build();
        when(todoService.changeName(id, newTodoName)).thenReturn(Optional.of(expected));

        // Act
        MvcResult result = mockMvc.perform(put(PREFIX_URI + "/" + id + "/edit-name")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Todo.builder().name(newTodoName).build()))).andReturn();
        // Assert
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "HTTP 200 is not returned");
        var actualTodo = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Todo.class);
        assertEquals(actualTodo.getName(), newTodoName, "Update name is failed");

    }

    @Test
    @DisplayName("Mark todo as completed test case")
    public void markTodoCompletedTest() throws Exception {
        // Arrange
        Long id = 1L;
        String todoName = "todo name";
        expected = Todo.builder()
                .name(todoName)
                .completed(true)
                .build();
        when(todoService.changeStatus(id)).thenReturn(Optional.of(expected));

        // Act
        MvcResult result = mockMvc.perform(put(PREFIX_URI + "/" + id + "/mark-as-done")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Assert
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "HTTP 200 is not returned");
        var actualTodo = new ObjectMapper().readValue(result.getResponse().getContentAsString(), Todo.class);
        assertTrue(actualTodo.isCompleted(), "Mark status is failed");

    }

    @Test
    @DisplayName("Delete todo test case")
    public void deleteTodoTest() throws Exception {
        // Arrange
        Long id = 1L;
//        doNothing().when(todoService.delete(id));

        // Act
        MvcResult result = mockMvc.perform(delete(PREFIX_URI + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // Assert
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus(), "HTTP 200 is not returned");

    }

    private List<Todo> getTodoList() {
        List<Todo> todoList = new ArrayList<>();
        todoList.add(Todo.builder()
                .id(1L)
                .name("todo1")
                .completed(false)
                .build());
        todoList.add(Todo.builder()
                .id(2L)
                .name("todo2")
                .completed(false)
                .build());
        todoList.add(Todo.builder()
                .id(3L)
                .name("todo3")
                .completed(true)
                .build());
        return todoList;
    }
}