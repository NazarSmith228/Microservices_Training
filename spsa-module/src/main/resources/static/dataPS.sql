INSERT INTO article (author_name, author_surname, topic, description, picture_url, creation_date, content)
VALUES ('Author', 'Authorovich', 'Lorem ipsum dolor sit amet',
        'Partem iisque pericula vel et, duis dicunt aeterno no pro, ne solum exerci facilisis vis. Vero aeterno sententiae an qui.',
        null,
        NOW()::DATE,
        'Autem dicit legimus eam te. Sit in maluisset expetendis, inani quidam disputando eam ei. Mea eripuit indoctum intellegam ei. Ex cum detracto aliquando, facer alienum neglegentur ut quo, ne usu ullum dolor. Ne possit delectus mel, eum ne justo latine honestatis, ut est minim graece patrioque. Laudem noluisse perfecto eos ad, est ne inermis gubergren scribentur. Ea mel duis contentiones. Inermis incorrupte ad usu, ius fugit vitae ornatus ex. Vis atomorum adversarium te, eum alterum epicuri id. Omnis dicant cu duo, stet cetero eum no, facer quando nemore at vix. Ullum diceret est cu, vim no summo saperet, eos suavitate mediocritatem ei.'),
       ('Author', 'Authorovich', 'Nisl nostrum electram qui eu', 'Vero aeterno sententiae an qui.', null,
        NOW()::DATE,
        'Rebum tacimates conceptam no eos, esse splendide ut vis. Pri amet melius et. Eam ipsum verear et, ut sit salutandi tincidunt dissentiunt, ne nisl eripuit lobortis mel. Mei amet munere recusabo ea. Eu simul decore nostro eam, cum dicta voluptatibus ad. Eu duo iudico apeirian. Pro graeco ornatus luptatum id. Ad sea regione assentior voluptatum. Cum quando imperdiet at. Ex patrioque urbanitas accommodare mei. Cum meis principes accommodare ut. Ridens partiendo vix ei, diam luptatum antiopam eam cu, possim sensibus assentior cu mel.'),
       ('Author', 'Authorovich', 'Per ne adhuc delectus', 'Novum abhorreant no vel, ad veniam offendit vim.', null,
        NOW()::DATE,
        'An everti option inciderint vim, eius feugait singulis ut cum, ex dicat reprimique adversarium has. Vivendo scaevola cu sit, ne possit quodsi pro, sale exerci integre vis an. Id duo minim novum mollis. Ne assum oportere per. Eu tibique tincidunt definitionem sed, ut his aeque integre. Pro te possit appetere ocurreret. Vel prodesset dissentiet id, mea ut odio expetendis, has ne consul eleifend indoctum. Habemus forensibus vituperata te vel, ad saepe philosophia qui. An quas choro efficiendi usu, errem scripta explicari vim ad. Quo ut veritus omittam lucilius, at nam diceret percipit. Audire electram consequuntur in qui. In cum elitr disputando, id eos nulla commodo oportere.'),
       ('Author', 'Authorovich', 'Ad equidem voluptua eum',
        'No congue dignissim expetendis usu, ad pri essent definitionem, ne ferri labitur interesset eam.', null,
        NOW()::DATE,
        'Cu mea debet scripta, novum dolor erroribus has at. Nisl salutandi contentiones per ad, duis minim corpora ad per. Exerci definitiones sit eu, at fastidii suscipiantur vis, etiam utinam numquam duo an. Affert elaboraret cum no, te nonumy signiferumque vix, eam habeo verterem id. Eirmod omittantur no quo, vis nisl affert quando ex. Sint saepe euripidis ex nam. Harum nostrud vim id, his ne accusata prodesset persequeris. Laoreet molestiae per ut. Sint vidit nusquam at sit. Doming suscipiantur id cum. An pro fuisset intellegat voluptatum. Sit at errem blandit, ad mei wisi consetetur percipitur. Quo et nibh lorem timeam, no eum sumo vidit iriure.'),
       ('Author', 'Authorovich', 'Cu homero option fierent vim',
        'Soleat deleniti recteque qui et, nec novum causae mediocritatem ex.', null,
        NOW()::DATE,
        'Qualisque sententiae interpretaris mea an. Nec error aliquam id, cu omittam legendos adipiscing sit. At eum partem scaevola, quo eu mazim gloriatur posidonium, suas sonet dolorum duo ad. Nostrud labores delectus eam ad, cu eam facer aliquam. In quo atqui scribentur instructior, ea pro sale mazim. Inani accusata usu at, eos ea ipsum comprehensam. Vix elit omnis iudico cu. Eu ius legere alienum, ex eos affert civibus vivendum, decore veritus dignissim id duo. Modo perfecto referrentur has an, nec et facete fuisset, eros euismod ancillae cum et. His vide magna ad, eos voluptua adipiscing cu.');

INSERT INTO tag(name)
VALUES ('SWIMMING'),
       ('YOGA'),
       ('RUNNING');

INSERT INTO article_tag(article_id, tag_id)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 1);

INSERT INTO sport_type(name)
VALUES ('Running'),
       ('Swimming'),
       ('Football'),
       ('Yoga');

INSERT INTO address(latitude, longitude)
VALUES (49.843813, 24.035848),
       (49.825667, 23.984488),
       (49.838818, 24.035271),
       (49.830768, 23.969439),
       (49.863390, 24.018028),
       (49.789631, 24.015130);

INSERT INTO users (name, surname, password, email, phone, date_of_birth, address_id, gender, has_child, creation_date,
                   auth_provider,
                   enabled)
VALUES ('nazar', 'koval', '$2y$12$WWT/V2dAj8uL7FSPQ4G17e2wTtYlZ84661NL1sS0.X57dQXKxJh8.', 'user@gmail.com',
        '0983412832',
        '1960-01-17', 1, 'MALE', false, NOW(), 'LOCAL', true),
       ('yura', 'khanas', '$2y$12$WWT/V2dAj8uL7FSPQ4G17e2wTtYlZ84661NL1sS0.X57dQXKxJh8.', 'coach@gmail.com',
        '0983412833',
        '2000-05-30', 2, 'MALE', true, NOW(), 'LOCAL', true),
       ('yarek', 'onime', '$2y$12$WWT/V2dAj8uL7FSPQ4G17e2wTtYlZ84661NL1sS0.X57dQXKxJh8.', 'super_admin@gmail.com',
        '0983412834',
        '1963-01-01', 3, 'MALE', false, NOW(), 'LOCAL', true),
       ('max', 'onime', '$2y$12$WWT/V2dAj8uL7FSPQ4G17e2wTtYlZ84661NL1sS0.X57dQXKxJh8.', 'venue_admin@gmail.com',
        '0983412835',
        '2000-05-30', 4, 'FEMALE', false, NOW(), 'LOCAL', true),
       ('lucas', 'jordan', '$2y$12$WWT/V2dAj8uL7FSPQ4G17e2wTtYlZ84661NL1sS0.X57dQXKxJh8.', 'lucas@gmail.com',
        '0983412825',
        '2000-05-30', 5, 'MALE', false, NOW(), 'LOCAL', true),
       ('sacul', 'nadroj', '$2y$12$WWT/V2dAj8uL7FSPQ4G17e2wTtYlZ84661NL1sS0.X57dQXKxJh8.', 'sacul@gmail.com',
        '0983412805',
        '2000-05-30', 6, 'FEMALE', false, NOW(), 'LOCAL', true);

INSERT INTO criteria(user_id, sport_type_id, maturity, running_distance, gender, activity_time)
VALUES (1, 1, 'BEGINNER', 7, 'MALE', 'MORNING'),
       (1, 1, 'BEGINNER', 7, 'FEMALE', 'MORNING'),
       (1, 1, 'MIDDLE', 20, 'MALE', 'MORNING'),
       (1, 1, 'MIDDLE', 20, 'FEMALE', 'MORNING'),
       (1, 2, 'MIDDLE', 0, 'MALE', 'NOON'),
       (1, 2, 'MIDDLE', 0, 'FEMALE', 'NOON'),
       (1, 4, 'BEGINNER', 0, 'MALE', 'EVENING'),
       (1, 4, 'BEGINNER', 0, 'FEMALE', 'EVENING'),

       (2, 2, 'PRO', 0, 'FEMALE', 'EVENING'),
       (2, 2, 'PRO', 0, 'MALE', 'EVENING'),
       (2, 2, 'MIDDLE', 0, 'FEMALE', 'MORNING'),
       (2, 2, 'MIDDLE', 0, 'MALE', 'MORNING'),
       (2, 3, 'BEGINNER', 0, 'FEMALE', 'EVENING'),
       (2, 3, 'BEGINNER', 0, 'MALE', 'EVENING'),
       (2, 3, 'MIDDLE', 0, 'FEMALE', 'EVENING'),
       (2, 3, 'MIDDLE', 0, 'MALE', 'EVENING'),
       (2, 1, 'PRO', 38, 'MALE', 'EVENING'),
       (2, 1, 'PRO', 38, 'FEMALE', 'EVENING'),

       (3, 1, 'MIDDLE', 15, 'MALE', 'ALL'),
       (3, 1, 'MIDDLE', 15, 'FEMALE', 'ALL'),
       (3, 4, 'MIDDLE', 0, 'MALE', 'MORNING'),
       (3, 4, 'MIDDLE', 0, 'FEMALE', 'MORNING'),
       (3, 3, 'PRO', 0, 'MALE', 'NOON'),
       (3, 3, 'PRO', 0, 'FEMALE', 'NOON'),
       (3, 2, 'BEGINNER', 0, 'BOTH', 'EVENING'),

       (4, 1, 'PRO', 25, 'FEMALE', 'NOON'),
       (4, 1, 'PRO', 25, 'MALE', 'NOON'),
       (4, 3, 'MIDDLE', 0, 'FEMALE', 'NOON'),
       (4, 3, 'MIDDLE', 0, 'MALE', 'NOON'),
       (4, 3, 'MIDDLE', 0, 'FEMALE', 'EVENING'),
       (4, 3, 'MIDDLE', 0, 'MALE', 'EVENING'),
       (4, 2, 'BEGINNER', 0, 'FEMALE', 'MORNING'),
       (4, 2, 'BEGINNER', 0, 'MALE', 'MORNING'),

       (5, 4, 'PRO', 0, 'FEMALE', 'MORNING'),
       (5, 4, 'PRO', 0, 'MALE', 'MORNING'),
       (5, 4, 'BEGINNER', 0, 'FEMALE', 'NOON'),
       (5, 4, 'BEGINNER', 0, 'MALE', 'NOON'),
       (5, 3, 'MIDDLE', 0, 'FEMALE', 'EVENING'),
       (5, 3, 'MIDDLE', 0, 'MALE', 'EVENING'),
       (5, 2, 'MIDDLE', 0, 'FEMALE', 'ALL'),
       (5, 2, 'MIDDLE', 0, 'MALE', 'ALL'),
       (5, 1, 'BEGINNER', 5, 'FEMALE', 'NOON'),
       (5, 1, 'BEGINNER', 5, 'MALE', 'NOON'),

       (6, 3, 'PRO', 40, 'MALE', 'MORNING'),
       (6, 3, 'MIDDLE', 40, 'FEMALE', 'NOON'),
       (6, 3, 'MIDDLE', 40, 'MALE', 'NOON'),
       (6, 3, 'PRO', 40, 'FEMALE', 'MORNING'),
       (6, 4, 'MIDDLE', 0, 'MALE', 'ALL'),
       (6, 4, 'MIDDLE', 0, 'FEMALE', 'ALL'),
       (6, 2, 'BEGINNER', 0, 'MALE', 'ALL'),
       (6, 2, 'BEGINNER', 0, 'FEMALE', 'ALL'),
       (6, 1, 'PRO', 40, 'MALE', 'ALL'),
       (6, 1, 'PRO', 40, 'FEMALE', 'ALL');

INSERT INTO role(name)
VALUES ('USER'),
       ('COACH'),
       ('SUPER_ADMIN'),
       ('VENUE_ADMIN');

INSERT INTO user_role(user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4),
       (5, 1),
       (6, 1);

INSERT INTO user_stats(user_id, sport_type_id, result_km, result_hours, location_id, coach_id, insert_date)
VALUES (1, 1, 15, 7200000000000, 1, 1, TO_DATE('2020-04-04', 'yyyy-MM-dd')),
       (1, 2, 15, 7200000000000, 1, 1, TO_DATE('2020-04-05', 'yyyy-MM-dd')),
       (2, 2, 15, 7200000000000, 1, 1, TO_DATE('2020-04-04', 'yyyy-MM-dd')),
       (2, 1, 15, 7200000000000, 1, 1, TO_DATE('2020-04-05', 'yyyy-MM-dd')),
       (3, 3, 15, 7200000000000, 1, 1, TO_DATE('2020-04-04', 'yyyy-MM-dd')),
       (3, 4, 15, 7200000000000, 1, 1, TO_DATE('2020-04-05', 'yyyy-MM-dd')),
       (4, 4, 15, 7200000000000, 1, 1, TO_DATE('2020-04-04', 'yyyy-MM-dd')),
       (4, 1, 15, 7200000000000, 1, 1, TO_DATE('2020-04-05', 'yyyy-MM-dd')),
       (5, 1, 15, 7200000000000, 1, 1, TO_DATE('2020-04-04', 'yyyy-MM-dd')),
       (5, 2, 15, 7200000000000, 1, 1, TO_DATE('2020-04-05', 'yyyy-MM-dd')),
       (6, 2, 15, 7200000000000, 1, 1, TO_DATE('2020-04-04', 'yyyy-MM-dd')),
       (6, 2, 15, 7200000000000, 1, 1, TO_DATE('2020-04-05', 'yyyy-MM-dd'));

INSERT INTO event(date_meeting, time_meeting, description, location_id, owner_id)
VALUES ('2020-04-12', '09:00:00', 'Sport event', 1, 2),
       ('2020-04-25', '10:10:05', 'Go to gym', 3, 1);

INSERT INTO event_user(event_id, user_id)
VALUES (1, 1),
       (2, 2),
       (2, 3);
