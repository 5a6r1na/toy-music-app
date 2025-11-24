-- ===============================
-- DATABASE: musicapp
-- ===============================

-- Drop tables if exist
DROP TABLE IF EXISTS song_component;
DROP TABLE IF EXISTS song_attributes;
DROP TABLE IF EXISTS song_metadata;
DROP TABLE IF EXISTS component_attributes;
DROP TABLE IF EXISTS component_metadata;

-- ===============================
-- 1. Song tables
-- ===============================
CREATE TABLE song_metadata (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    artist VARCHAR(255),
    midi_file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Restart song_metadata.id from 100001
ALTER SEQUENCE song_metadata_id_seq RESTART WITH 100001;

CREATE TABLE song_attributes (
    id SERIAL PRIMARY KEY,
    song_id INT NOT NULL REFERENCES song_metadata(id) ON DELETE CASCADE,
    attribute_name VARCHAR(255) NOT NULL,
    attribute_value VARCHAR(255)
);

-- Restart song_attributes.id from 100001
ALTER SEQUENCE song_attributes_id_seq RESTART WITH 100001;

-- ===============================
-- 2. Component tables
-- ===============================
CREATE TABLE component_metadata (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    midi_file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT NOW()
);

-- Restart component_metadata.id from 100001
ALTER SEQUENCE component_metadata_id_seq RESTART WITH 100001;

CREATE TABLE component_attributes (
    id SERIAL PRIMARY KEY,
    component_id INT NOT NULL REFERENCES component_metadata(id) ON DELETE CASCADE,
    attribute_name VARCHAR(255) NOT NULL,
    attribute_value VARCHAR(255)
);

-- Restart component_attributes.id from 100001
ALTER SEQUENCE component_attributes_id_seq RESTART WITH 100001;

-- ===============================
-- 3. Relationship table: Song <-> Component (N:N)
-- ===============================
CREATE TABLE song_component (
    song_id INT NOT NULL REFERENCES song_metadata(id) ON DELETE CASCADE,
    component_id INT NOT NULL REFERENCES component_metadata(id) ON DELETE CASCADE,
    PRIMARY KEY (song_id, component_id)
);

-- ===============================
-- 4. Dummy Data
-- ===============================

-- Songs
INSERT INTO song_metadata (title, artist, midi_file_path) VALUES
('Pirates', 'Artist A', 'Pirates.mid'),
('Queen', 'Artist B', 'Queen.mid');

-- Song attributes
INSERT INTO song_attributes (song_id, attribute_name, attribute_value) VALUES
(100001, 'genre', 'Pop'),
(100001, 'bpm', '120'),
(100002, 'genre', 'Rock'),
(100002, 'bpm', '140');


-- Components
INSERT INTO component_metadata (name, midi_file_path) VALUES
('C1', 'Mermaid.mid'),
('C2', 'Dre.mid'),
('C3', 'Never.mid');

-- Component attributes
INSERT INTO component_attributes (component_id, attribute_name, attribute_value) VALUES
(100001, 'instrument', 'Piano'),
(100001, 'key', 'C'),
(100002, 'instrument', 'Violin'),
(100002, 'key', 'G'),
(100003, 'instrument', 'Drums'),
(100003, 'key', 'N/A');

-- Song <-> Component relationships
-- S1 -> M1, M2
INSERT INTO song_component (song_id, component_id) VALUES
(100001, 100001),
(100001, 100002);

-- S2 -> M1, M2, M3
INSERT INTO song_component (song_id, component_id) VALUES
(100002, 100001),
(100002, 100002),
(100002, 100003);
