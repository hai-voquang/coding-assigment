import React, { useState, useRef, useEffect } from "react";
import Form from "./components/Form";
import Todo from "./components/Todo";
import request from "./util/APIUtils";
import {
  GET_ALL_TODOS_URL,
  CREATE_TODO_URL,
  MARK_DONE_TODO_URL,
  UPDATE_NAME_TODO_URL,
  DELETE_TODO_URL,
} from "./config";
function usePrevious(value) {
  const ref = useRef();
  useEffect(() => {
    ref.current = value;
  });
  return ref.current;
}
function App() {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(false);
  function toggleTaskCompleted(id) {
    const updatedTasks = tasks.map((task) => {
      if (id === task.id) {
        return { ...task, completed: !task.completed };
      }
      return task;
    });
    request({
      url: MARK_DONE_TODO_URL.replace("{id}", id),
      method: "PUT",
    }).then((response) => {
      setTasks(updatedTasks);
    });
  }

  function deleteTask(id) {
    const remainingTasks = tasks.filter((task) => id !== task.id);
    request({
      url: DELETE_TODO_URL.replace("{id}", id),
      method: "DELETE",
    }).then((response) => {
      setTasks(remainingTasks);
    });
  }

  function editTask(id, newName) {
    const editedTaskList = tasks.map((task) => {
      if (id === task.id) {
        return { ...task, name: newName };
      }
      return task;
    });
    request({
      url: UPDATE_NAME_TODO_URL.replace("{id}", id),
      method: "PUT",
      body: JSON.stringify({ id: id, name: newName }),
    }).then((response) => {
      setTasks(editedTaskList);
    });
  }

  const taskList = tasks.map((task) => (
    <Todo
      id={task.id}
      name={task.name}
      completed={task.completed}
      key={task.id}
      toggleTaskCompleted={toggleTaskCompleted}
      deleteTask={deleteTask}
      editTask={editTask}
    />
  ));

  function addTask(name) {
    const newTask = { name: name, completed: false };
    request({
      url: CREATE_TODO_URL,
      method: "POST",
      body: JSON.stringify(newTask),
    }).then((response) => {
      newTask.id = response.id;
      setTasks([...tasks, newTask]);
    });
  }
  const tasksCompleted = tasks.filter((task) => !task.completed).length;
  const tasksNoun = tasksCompleted !== 1 ? "tasks" : "task";
  const headingText = `${tasksCompleted} ${tasksNoun} remaining`;

  const listHeadingRef = useRef(null);
  const prevTaskLength = usePrevious(tasks.length);

  useEffect(() => {
    if (!loading) {
      request({
        url: GET_ALL_TODOS_URL,
        method: "GET",
      }).then((tasks) => {
        setTasks(tasks);
        setLoading(true);
      });
    }
  }, [tasks, loading]);

  useEffect(() => {
    if (tasks.length - prevTaskLength === -1) {
      listHeadingRef.current.focus();
    }
  }, [tasks.length, prevTaskLength]);

  return (
    <div className="todoapp stack-large">
      <Form addTask={addTask} />
      <h2
        aria-label="todo-list-remaining"
        id="list-heading"
        tabIndex="-1"
        ref={listHeadingRef}
      >
        {headingText}
      </h2>
      <ul
        className="todo-list stack-large stack-exception"
        aria-labelledby="list-heading"
      >
        {taskList}
      </ul>
    </div>
  );
}

export default App;
