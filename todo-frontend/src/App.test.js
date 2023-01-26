import { fireEvent, render, screen } from "@testing-library/react";
import App from "./App";
// import API mocking utilities from Mock Service Worker.
import { rest } from "msw";
import { setupServer } from "msw/node";
import { GET_ALL_TODOS_URL, CREATE_TODO_URL, DELETE_TODO_URL } from "./config";

const fakeTodosResponse = [];
const fakeNewTodoResponse = { id: 1, name: "todo 1", completed: false };
const server = setupServer(
  rest.get(GET_ALL_TODOS_URL, (req, res, ctx) => {
    return res(ctx.json(fakeTodosResponse));
  }),
  rest.post(CREATE_TODO_URL, (req, res, ctx) => {
    return res(ctx.json(fakeNewTodoResponse));
  })
);

beforeAll(() => server.listen());
afterEach(() => {
  server.resetHandlers();
});
afterAll(() => server.close());

test("renders home page with input and button", async () => {
  render(<App />);

  const toDoLabel = screen.getByLabelText(/What needs to be done/i);
  expect(toDoLabel).toBeInTheDocument();

  const input = screen.getByLabelText("new-todo-input");
  expect(input).toBeInTheDocument();

  const button = screen.getByText(/Add/);
  expect(button).toBeInTheDocument();

  const h2Text = await screen.findByLabelText(/todo-list-remaining/i);
  expect(h2Text).toBeInTheDocument();
});

test("Add new todo", async () => {
  render(<App />);

  const input = screen.getByLabelText("new-todo-input");
  fireEvent.change(input, {
    target: { value: "todo1" },
  });

  expect(input).toHaveDisplayValue("todo1");

  const button = screen.getByText(/Add/i);
  fireEvent.click(button);

  const deleteButton = await screen.findByText(/Delete/i);
  const h2Text = await screen.findByLabelText(/todo-list-remaining/i);
  expect(h2Text).toHaveTextContent(/1 task remaining/);
  expect(deleteButton).toBeInTheDocument();
});

test("Delete todo", async () => {
  server.use(
    rest.get(GET_ALL_TODOS_URL, (req, res, ctx) => {
      return res(ctx.json([{ id: 1, name: "todo 1", completed: false }]));
    }),
    rest.delete(DELETE_TODO_URL.replace("{id}", 1), (req, res, ctx) => {
      return res(ctx.status(200));
    })
  );
  render(<App />);

  const button = screen.getByText(/Add/i);
  fireEvent.click(button);

  const deleteButton = await screen.findByText(/Delete/i);
  const h2Text = await screen.findByLabelText(/todo-list-remaining/i);
  expect(h2Text).toHaveTextContent(/1 task remaining/);
  expect(deleteButton).toBeInTheDocument();

  fireEvent.click(deleteButton);

  const addButton = await screen.findByText(/Add/i);
  expect(addButton).toBeInTheDocument();
});
