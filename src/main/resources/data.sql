-- Categories
INSERT INTO categories (name) VALUES
('Workshop'),
('Lecture'),
('Social'),
('Sports'),
('Entertainment');

-- Locations
INSERT INTO locations (name) VALUES
('Recitation Building'),
('Cordova Recreation Building'),
('Gym'),
('Stadium'),
('Library');

-- Events (linking to category_id and location_id)
INSERT INTO events (title, description, start_time, end_time, category_id, location_id)
VALUES
('AI Workshop', 'Intro to AI concepts', '2025-11-12T10:00:00', '2025-11-12T12:00:00', 1, 1),
('Quantum Lecture', 'Basics of quantum computing', '2025-11-13T14:00:00', '2025-11-13T15:30:00', 2, 5),
('Social Mixer', 'Meet fellow students', '2025-11-14T18:00:00', '2025-11-14T20:00:00', 3, 2),
('Basketball Game', 'Inter-college match', '2025-11-15T17:00:00', '2025-11-15T19:00:00', 4, 3),
('Tech Talk', 'Latest in AI hardware', '2025-11-16T11:00:00', '2025-11-16T12:30:00', 2, 1),
('Coding Bootcamp', 'Hands-on Python session', '2025-11-17T09:00:00', '2025-11-17T11:00:00', 1, 5),
('Music Night', 'Live student performances', '2025-11-18T19:00:00', '2025-11-18T21:00:00', 5, 2),
('Soccer Match', 'Friendly game between clubs', '2025-11-19T15:00:00', '2025-11-19T17:00:00', 4, 4),
('Research Symposium', 'Student research presentations', '2025-11-20T13:00:00', '2025-11-20T16:00:00', 2, 1),
('Board Games Night', 'Casual social gathering with games', '2025-11-21T18:00:00', '2025-11-21T22:00:00', 3, 2);
