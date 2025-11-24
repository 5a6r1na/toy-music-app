import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../components/Home.vue";
import SongView from "../components/Song.vue";

const routes = [
  { path: "/", name: "home", component: HomeView },
  { path: "/song/:id", name: "song", component: SongView, props: true },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
