import React, { useState } from "react";

function Form(props) {
  const [name, setName] = useState("");
  const [valid, setValid] = useState(true);

  function handleSubmit(e) {
    e.preventDefault();
    if (!name.trim()) {
      setValid(false);
      return;
    }
    props.addTask(name);
    setValid(true);
    setName("");
  }

  function handleChange(e) {
    setName(e.target.value);
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>
        <label htmlFor="new-todo-input">What needs to be done?</label>
      </h2>
      {!valid && (
        <span
          id="changed-name-error"
          className="ns-error-message field-validation-error"
        >
          <i
            className="fa fa-exclamation-circle error-indicator errorAlert"
            aria-hidden="true"
          ></i>
          <span className="ns-visually-hidden">Error:</span> Enter a todo name
        </span>
      )}
      <input
        type="text"
        id="new-todo-input"
        className="input input__lg"
        name="text"
        autoComplete="off"
        value={name}
        onChange={handleChange}
        aria-label="new-todo-input"
      />
      <button type="submit" className="btn btn-success btn-lg">
        Add
      </button>
    </form>
  );
}

export default Form;
