<template>
  <div class="song-page">
    <!-- HEADER -->
    <div class="header-row">
      <button @click="goHome" class="back-btn">⬅ Back to Home</button>
    </div>

    <!-- SONG INFO -->
    <div class="song-info">
      <div><strong>Name:</strong> {{ currentSong?.title }}</div>
      <div><strong>Artist:</strong> {{ currentSong?.artist }}</div>
    </div>

    <!-- SOURCE SELECTOR -->
    <div class="source-row">
      <span class="source-label">Song:</span>
      <button class="full-song-btn" @click="selectSong(currentSong)">▶</button>

      <span class="source-label">Components:</span>
      <button
        class="component-btn"
        v-for="comp in components"
        :key="comp.id"
        @click="playComponent(comp.fileUrl)"
      >
        ▶ {{ comp.name }}
      </button>
    </div>

    <!-- CONTROLS -->
    <div class="controls">
      <span class="source-label">Controls:</span>
      <button @click="playMidi">Play</button>
      <button @click="pauseMidi">Pause</button>
      <button @click="restartMidi">Restart</button>
    </div>

    <!-- PIANO ROLL -->
    <canvas ref="canvasRef" width="1200" height="400"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { Midi } from "@tonejs/midi";
import * as Tone from "tone";
import { apiSwitch } from "../stores/apiSwitch";
import { useRouter, useRoute } from "vue-router";

const router = useRouter();
const route = useRoute();

const canvasRef = ref(null);
const isPlaying = ref(false);
const songs = ref([]);
const components = ref([]);
const currentSong = ref(null);
const noteHeight = 6;
const pixelsPerTimeStep = 30;
let synth;
let midi;
let animationFrameId;

/**
 * [FUNCTION]: goHome() navigates to homepage at click
 */
const goHome = () => {
  router.push("/");
};

/**
 * [METHOD]: selectSong() loads a selected song into the piano roll and fetches its components.
 * @param song
 */
const selectSong = async (song) => {
  // [STEP]: Load song midi to piano roll
  currentSong.value = song;
  await loadMidi(song.fileUrl);

  // [STEP]: Fetches and load components for this song
  const compRes = await apiSwitch("queryComponent", { songId: song.id });
  components.value = compRes?.data?.data || [];
};

/**
 * [METHOD]: playComponent(fileUrl): plays midi component
 * @param fileUrl
 */
const playComponent = async (fileUrl) => {
  // [CASE]: Empty file
  if (!fileUrl) {
    console.warn("Component has no MIDI file URL");
    return;
  }
  // [STEP]: Load component midi to piano roll
  await loadMidi(fileUrl);
};

/**
 * [FEATURE]: loadMidi() loads and parses a MIDI file, then initializes the piano roll display.
 * @param fileUrl
 */
const loadMidi = async (fileUrl) => {
  // [STEP]: Build public URL
  const backendBase = import.meta.env.VITE_APP_API.replace("/api", "");
  const publicUrl = `${backendBase}${fileUrl}`;

  try {
    // [STEP]: Fetch MIDI file as ArrayBuffer
    const buffer = await fetch(publicUrl).then((res) => {
      if (!res.ok) throw new Error("Failed to fetch MIDI file");
      return res.arrayBuffer();
    });
    // [STEP]: Parse MIDI data
    midi = await new Midi(buffer);

    // [STEP]: Reset scheduled events and create new synth instance
    Tone.getTransport().cancel();
    synth = new Tone.PolySynth(Tone.Synth).toDestination();

    // [STEP]: Schedule MIDI note onto Transport
    midi.tracks.forEach((track) => {
      track.notes.forEach((note) => {
        Tone.getTransport().schedule((time) => {
          synth.triggerAttackRelease(note.name, note.duration, time);
        }, note.time);
      });
    });
    // [STEP]: Render the piano roll
    drawInitialPianoRoll();
  } catch (err) {
    console.error("Error loading MIDI:", err);
  }
};

/**
 * [FEATURE]: playMidi() starts MIDI playback inside the Piano Roll feature.
 */
const playMidi = async () => {
  if (!isPlaying.value) {
    // [STEP]: Unlock audio
    await Tone.start();

    // [STEP]: Start audio with slight delay
    Tone.getTransport().start("+0.1");

    // [STEP]: Update playback state
    isPlaying.value = true;

    // [STEP]: Trigger animation
    animate();
  }
};

/**
 * [FEATURE]: pauseMidi() pauses MIDI playback and stops animation loop.
 */
const pauseMidi = () => {
  // [STEP]: Pause audio
  Tone.getTransport().pause();
  // [STEP]: Update playback state
  isPlaying.value = false;
  // [STEP]: Stop animation
  cancelAnimationFrame(animationFrameId);
};

/**
 * [FEATURE]: restartMidi() stops playback, resets transport, and redraws the initial piano roll.
 */
const restartMidi = () => {
  // [STEP]: Stop audio
  Tone.getTransport().stop();

  // [STEP]: Reset transport to begining
  Tone.getTransport().seconds = 0;

  // [STEP]: Update playback state
  isPlaying.value = false;

  // [STEP]: Draw initianl piano roll
  drawInitialPianoRoll();
};

/**
 * [FEATURE]: drawInitialPianoRoll() draws the initial state of the piano roll.
 */
const drawInitialPianoRoll = () => {
  if (!midi) return;

  // [STEP]: Get canvas context
  const canvas = canvasRef.value;
  const ctx = canvas.getContext("2d");

  // [STEP]: Flatten all tracks into single notes list
  const allNotes = midi.tracks.flatMap((t) => t.notes);

  // [STEP]: Calculate min and max pitches to scale the piano roll
  const minPitch = Math.min(...allNotes.map((n) => n.midi));
  const maxPitch = Math.max(...allNotes.map((n) => n.midi));

  // [STEP]: Draw notes on canvas at initial time 0
  draw(ctx, allNotes, minPitch, maxPitch, canvas.width, canvas.height, 0);
};

/**
 * [FEATURE]: draw() draws the piano roll notes and current playback line.
 * @param ctx
 * @param notes
 * @param minPitch
 * @param maxPitch
 * @param width
 * @param height
 * @param currentTime
 */
const draw = (ctx, notes, minPitch, maxPitch, width, height, currentTime) => {
  // [STEP]: Clear canvas
  ctx.clearRect(0, 0, width, height);

  // [STEP]: Draw each note as rectangle
  notes.forEach((note) => {
    const x = (note.time - currentTime) * pixelsPerTimeStep + width / 4;
    const y = height - (note.midi - minPitch) * noteHeight;
    const w = note.duration * pixelsPerTimeStep;
    const h = noteHeight;
    ctx.fillStyle = "blue";
    ctx.fillRect(x, y, w, h);
  });

  // [STEP]: Draw current playback line
  ctx.strokeStyle = "black";
  ctx.beginPath();
  ctx.moveTo(width / 4, 0);
  ctx.lineTo(width / 4, height);
  ctx.stroke();
};

/**
 * [FEATURE]: animate() updates piano roll during playback.
 */
const animate = () => {
  if (!midi) return;

  // [STEP]: Get canvas context
  const canvas = canvasRef.value;
  const ctx = canvas.getContext("2d");

  // [STEP]: Flatten all tracks into single notes list
  const notes = midi.tracks.flatMap((t) => t.notes);

  // [STEP]: Calculate min and max pitches to scale the piano roll
  const minPitch = Math.min(...notes.map((n) => n.midi));
  const maxPitch = Math.max(...notes.map((n) => n.midi));

  // [STEP]: Recursive animation loop
  const loop = () => {
    draw(
      ctx,
      notes,
      minPitch,
      maxPitch,
      canvas.width,
      canvas.height,
      Tone.Transport.seconds
    );
    animationFrameId = requestAnimationFrame(loop);
  };

  // [STEP]: Start animation loop
  loop();
};

/**
 * [FUNCTION]: onMounted() fetches song list on page load.
 */
onMounted(async () => {
  const res = await apiSwitch("queryMusic");
  songs.value = res?.data?.data || [];

  const songId = route.params.id;
  const foundSong = songs.value.find((s) => String(s.id) === String(songId));

  if (foundSong) {
    await selectSong(foundSong);
  }
});
</script>

<style scoped>
.song-page {
  padding: 20px;
}

.back-btn {
  background: #1d1b1b;
  color: white;
}

.song-info {
  margin: 15px 0;
}

.source-row {
  margin: 10px 0;
}

.full-song-btn {
  margin-right: 10px;
}

.component-btn {
  margin-right: 8px;
}

.source-label {
  margin-right: 8px;
  font-weight: bold;
}

.controls button {
  margin-right: 8px;
}

canvas {
  border: 1px solid #ccc;
  margin-top: 20px;
}
</style>
