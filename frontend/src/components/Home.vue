<template>
  <div class="home">
    <h1>🎵 Toy Music App</h1>

    <div class="song-list">
      <button
        v-for="(song, index) in songs"
        :key="song.id"
        class="song-button"
        @click="openSong(song.id)"
      >
        ▶ {{ song.title }} - {{ song.artist }}
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { apiSwitch } from "../stores/apiSwitch";

const router = useRouter();
const songs = ref([]);

/**
 * [FUNCTION]: onMounted() fetches song list on page load.
 */
onMounted(async () => {
  try {
    const response = await apiSwitch("queryMusic");

    if (response?.data?.data?.length > 0) {
      songs.value = response.data.data;
    }
  } catch (err) {
    console.error("Error fetching songs:", err);
  }
});

/**
 * [METHOD]: openSong() navigates to Song page with chosen songId
 * @param id
 */
const openSong = (id) => {
  router.push(`/song/${id}`);
};
</script>

<style scoped>
.song-button {
  display: block;
  margin: 10px 0;
  padding: 12px;
  font-size: 16px;
  cursor: pointer;
}
</style>
