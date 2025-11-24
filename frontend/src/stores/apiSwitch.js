//apiSwitch.js
import { queryMusic, queryComponent } from "../services/api";

export const apiSwitch = async (apiKey, req) => {
  let res = {};
  try {
    switch (apiKey) {
      case "queryMusic":
        res = await queryMusic(req);
        break;
      case "queryComponent":
        res = await queryComponent(req);
        break;
      default:
    }
  } catch (error) {
    throw error;
  } finally {
  }
  return res;
};
