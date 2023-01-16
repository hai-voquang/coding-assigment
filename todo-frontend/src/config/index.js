// export const API_BASE_URL = "/api/v1/todos";
export const API_BASE_URL = process.env.REACT_APP_BACKEND_SERVER_URL || "http://localhost:8080/api/v1/todos";
export const GET_ALL_TODOS_URL = API_BASE_URL + "/all";
export const UPDATE_NAME_TODO_URL = API_BASE_URL + "/{id}/edit-name";
export const MARK_DONE_TODO_URL = API_BASE_URL + "/{id}/mark-as-done";
export const CREATE_TODO_URL = API_BASE_URL;
export const DELETE_TODO_URL = API_BASE_URL + "/{id}";
