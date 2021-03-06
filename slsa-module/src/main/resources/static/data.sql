INSERT INTO location_type(name, place)
VALUES ('Park', 'OUTDOOR'),
       ('Studio', 'INDOOR'),
       ('Gym', 'INDOOR')
;

INSERT INTO sport_type(name)
VALUES ('Running'),
       ('Swimming'),
       ('Football'),
       ('Yoga')
;

INSERT INTO address(latitude, longitude)
values (49.7896, 24.01417),
       (52.74111, 63.4112),
       (100.214, 2.3021),
       (100.214, 7.0417),
--      next gyms from google places
       (49.7911844, 24.0728355),
       (49.83296, 24.038332),
       (49.8276435, 24.0444235),
       (49.857206, 24.0377137),
       (49.83226, 23.996549),
       (49.8209779, 24.0181732),
       (49.8376001, 24.0344086),
       (49.8360336, 24.0432153),
       (49.817287, 24.0718111),
       (49.8426821, 23.9995081),
       (49.8464715, 24.0226429),
       (49.8440123, 24.0241237),
       (49.790916, 24.0727249),
       (49.8348244, 24.0114655),
       (49.8266971, 24.0398602),
       (49.8359914, 24.0284142),
       (49.8242395, 24.051379),
       (49.8320849, 24.0261612),
       (49.8358572, 24.0389486),
       (49.80236, 24.0204342),
--        next parks from google places
       (49.8230219, 24.024981),
       (49.8384051, 24.0197281),
       (49.8244043, 24.0661119),
       (49.82731829, 24.0225077),
       (49.84457889, 24.0656912),
       (49.8474611, 24.0527286),
       (49.84860279, 24.037342),
       (49.830285, 24.0633387),
       (49.81303219, 24.0074236),
       (49.8210799, 24.0448562),
       (49.8200031, 24.0374174),
       (49.8126707, 24.097025),
       (49.8326664, 24.0311983),
       (49.8550846, 23.9950848),
       (49.92369919, 23.7225758),
       (49.8277795, 24.0284843),
--        next yoga-studios from google places
       (49.833904, 24.0179411),
       (49.8395214, 24.0011219),
       (49.835121, 24.0216673),
       (49.8365726, 24.0280866),
       (49.86103620000001, 24.0121054),
       (49.8441237, 24.0365687),
       (49.8242395, 24.051379),
       (49.8388889, 24.0250665),
       (49.833752, 24.0334452),
       (49.836458, 24.029476),
       (49.8508356, 24.0288748),
       (49.8698765, 23.9432805),
       (49.8122695, 24.0371493),
       (49.83925559999999, 24.0004706),
       (49.832838, 24.038404),
       (49.860077, 24.027276),
       (49.8423078, 24.0287056),
       (49.8285951, 24.0586638),
       (49.7923506, 24.0648597),
       (49.8612477, 24.0171497)
;

INSERT INTO location(name, address_id, location_type_id)
values ('Aquapark', 3, 2),
       ('World of football', 2, 3),
       ('YogaMaster', 1, 1),
       ('Struyskiy Park', 4, 1),
-- next gyms from google places
       ('Lviv Garage Gym', 5, 3),
       ('Fitness club "Forever"', 6, 3),
       ('Vasil Gym', 7, 3),
       ('Revat HardTotal', 8, 3),
       ('The Wall', 9, 3),
       ('Fitness club "Malibu"', 10, 3),
       ('Academy Aikibujutsu: Kikenta dojo', 11, 3),
       ('Sport-Kompleks Zoovet', 12, 3),
       ('Fitness club "Liga"', 13, 3),
       ('Fitnes-Klub Forever', 14, 3),
       ('???????????? ???????? Pride elite Gym', 15, 3),
       ('Fitness studio Antonik', 16, 3),
       ('Lviv Garage Gym', 17, 3),
       ('Kiwi Fitness', 18, 3),
       ('???????????? ?????????? ??????????????', 19, 3),
       ('Kivi Fitnes', 20, 3),
       ('Art Gym', 21, 3),
       ('Sportmaydanchyk - Tsytadel', 22, 3),
       ('Aysedora', 23, 3),
       ('Shulak GYM', 24, 3),
-- next parks from google places
       ('Stryiskyi Park', 25, 1),
       ('Ivan Franko Park', 26, 1),
       ('Pohulianka Park', 27, 1),
       ('Central Culture Park', 28, 1),
       ('Shevchenkivskyi Hai Park Museum', 29, 1),
       ('Znesinnia Landscape Park', 30, 1),
       ('Park High Castle', 31, 1),
       ('Lviv University Botanical Garden', 32, 1),
       ('Horikhovyi Hai Park', 33, 1),
       ('Snopkivskyi Park', 34, 1),
       ('Zalizna Voda Park', 35, 1),
       ('Vynnykivskyi park', 36, 1),
       ('Botanical Garden', 37, 1),
       ('Kortumova Hora', 38, 1),
       ('Yavoriv National Park', 39, 1),
       ('Ferris Wheel', 40, 1),
-- next yoga-studios from google places
       ('Ashtanga Yoga Centre of Lviv', 41, 2),
       ('Studio "Shakti Yoga Salah"', 42, 2),
       ('Birdie. Yoga Studio', 43, 2),
       ('Yoga art studio "Korali"', 44, 2),
       ('"Yoga Sadhana"', 45, 2),
       ('Ashtanga Yoga School First in Ukraine', 46, 2),
       ('Art Gym', 47, 2),
       ('Zanyattya Yohoyu U L??vovi Z Yeduardom Holyak', 48, 2),
       ('?????????? ???????? ???????????????? ??Green Tara??', 49, 2),
       ('Mantra House', 50, 2),
       ('???????????? ?????????????????? Luna Studio', 51, 2),
       ('Yoha Z Illeyu Kuz??moyu', 52, 2),
       ('???????????? ???????? ?? ???????????? ??????????????????', 53, 2),
       ('Shakti Yoga Shala', 54, 2),
       ('Yoha Studiya Zhanny Potapovoyi', 55, 2),
       ('LOFT Studio', 56, 2),
       ('Yoha Tsentr, Lviv', 57, 2),
       ('Health-exercise "Yoga"', 58, 2),
       ('L??vivs??ka Yoha-Studiya', 59, 2),
       ('Yoga23', 60, 2)
;

INSERT INTO coach(rating, work_with_children, location_id, user_id)
values (5, true, 1, 2),
       (5, false, 3, 6);


INSERT INTO link(url, type, coach_id)
values ('https://www.youtube.com/watch?v=Uzu9clzaLvg&t=1967s', 'YOUTUBE', 1);

INSERT INTO location_schedule(day, start_working_time, end_working_time, location_id)
values ('MONDAY', '10:00:00', '22:00:00', 1),
       ('TUESDAY', '10:00:00', '22:00:00', 1),
       ('WEDNESDAY', '10:00:00', '22:00:00', 1),
       ('THURSDAY', '10:00:00', '22:00:00', 1),
       ('FRIDAY', '10:00:00', '22:00:00', 1),
       ('SATURDAY', '10:00:00', '17:00:00', 1),
       ('SUNDAY', '10:00:00', '14:00:00', 1),

       ('MONDAY', '10:00:00', '22:30:00', 2),
       ('TUESDAY', '10:00:00', '22:30:00', 2),
       ('WEDNESDAY', '10:00:00', '22:30:00', 2),
       ('THURSDAY', '10:00:00', '22:30:00', 2),
       ('FRIDAY', '10:00:00', '22:30:00', 2),
       ('SATURDAY', '10:00:00', '17:30:00', 2),
       ('SUNDAY', '10:00:00', '14:30:00', 2),

       ('MONDAY', '10:00:00', '22:45:00', 3),
       ('TUESDAY', '10:00:00', '22:45:00', 3),
       ('WEDNESDAY', '10:00:00', '22:45:00', 3),
       ('THURSDAY', '10:00:00', '22:45:00', 3),
       ('FRIDAY', '10:00:00', '22:45:00', 3),
       ('SATURDAY', '10:00:00', '17:45:00', 3),
       ('SUNDAY', '10:00:00', '14:45:00', 3),

       ('MONDAY', '10:00:00', '22:55:00', 4),
       ('TUESDAY', '10:00:00', '22:55:00', 4),
       ('WEDNESDAY', '10:00:00', '22:55:00', 4),
       ('THURSDAY', '10:00:00', '22:55:00', 4),
       ('FRIDAY', '10:00:00', '22:55:00', 4),
       ('SATURDAY', '10:00:00', '17:55:00', 4),
       ('SUNDAY', '10:00:00', '14:55:00', 4);

INSERT INTO coach_sport_type(coach_id, sport_type_id)
values (1, 1),
       (2, 3);

INSERT INTO location_sport_type(location_id, sport_type_id)
values (1, 2),
       (2, 3),
       (3, 4),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 1),
       (10, 1),
       (11, 1),
       (12, 1),
       (13, 1),
       (14, 1),
       (15, 1),
       (15, 4),
       (16, 1),
       (17, 1),
       (18, 1),
       (19, 1),
       (20, 1),
       (21, 1),
       (21, 4),
       (22, 1),
       (23, 1),
       (23, 4),
       (24, 1),
       (25, 1),
       (26, 1),
       (27, 1),
       (28, 1),
       (29, 1),
       (30, 1),
       (30, 4),
       (31, 1),
       (32, 1),
       (33, 1),
       (34, 1),
       (35, 1),
       (35, 4),
       (36, 1),
       (37, 1),
       (39, 1),
       (40, 1),
       (41, 4),
       (42, 4),
       (43, 4),
       (44, 4),
       (45, 4),
       (46, 4),
       (47, 4),
       (48, 4),
       (49, 4),
       (50, 4),
       (51, 4),
       (52, 4),
       (53, 4),
       (54, 4),
       (55, 4),
       (56, 4),
       (57, 4),
       (58, 4),
       (59, 4),
       (60, 4)
;