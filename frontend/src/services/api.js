//api.js
import axios from "axios";

const instance = axios.create({
  baseURL: import.meta.env.VITE_APP_API,
});

export const queryMusic = (data) => {
  return instance.post("/music/queryMusic", data);
};

export const queryComponent = (data) => {
  return instance.post("/music/queryComponent", data);
};
