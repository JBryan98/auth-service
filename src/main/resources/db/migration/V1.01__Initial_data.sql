INSERT INTO users(first_name, last_name, email, username, password, enabled, created_by, last_modified_by, created_at, last_modified_at)
VALUES ('Bryan', 'Corrales', 'bcorrales@test.mail', 'bcorrales','$2a$10$.cw9tPmeH8b7V85Mmo5UCurhhSNpYafQEyYuX3YZObvFhQSfT4xSe', TRUE, 'admin', NULL,CURRENT_TIMESTAMP, NULL),
       ('John', 'Doe', 'jdoe@test.mail', 'jdoe', '$2a$10$C83zge98ffjHhw0uqCQAs.XWCMRVRevj6TM4f4YaRZZWvYmPBfv5a', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Janet', 'Weaver', 'jweaver@test.mail', 'jweaver', '$2a$10$u.SlA12fK8yyTphFEiqOeukaELv.xgAK1NPx9K/qMH0K94szzgoke', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('George', 'Bluth', 'gbluth@test.mail', 'gbluth', '$2a$10$24OedvAvz2XIiNPCCCvqb.kimPiqd3wkOrv2QVhug5980x8P6I6a6', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Emma', 'Wong', 'ewong@mail.com', 'ewong', '$2a$10$fZ8EaCZBtRh5NQMiqlIEVOuHrpiazAWMyPumMM2E6iSgVzQXbtd42', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Eve', 'Holt', 'eholt@test.mail', 'eholt', '$2a$10$lm3u43X6CszbyPu0UNfpyujCucPETFaj9Hn2VZIMTwGxccNfVLKhS', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Charles', 'Morris', 'cmorris@test.mail', 'cmorris', '$2a$10$Lt66IruiTZ6KXT0GyO4eneUIsPwVfETrHXRY3dnnenJ046x7C9qEy', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Lindsay', 'Ferguson', 'lferguson@gmail.com', 'lferguson', '$2a$10$6z/mbG9UIOUERD9SrVRvNO3lNGQ6U5UINGK7DFwM6yO4luLfWVqjq', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Tobias', 'Funker', 'tfunke@test.mail', 'tfunke', '$2a$10$BMEmGa4c6ugfB.mfm.13Oer4nKBm8HJ3joyqus3EZh.aNNiSzdz0q', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Byron', 'Fields', 'bfields@test.mail', 'bfields', '$2a$10$jZm/gpm0IEjrb3mlMXvFrOl1u20q.v5h2mK8HEfnXt9UdQyYhGxmW', TRUE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('George', 'Edwards', 'gedwards@test.mail', 'gedwards', '$2a$10$FQThoG0dvwnnA.NQQSXo.ugW.j43vxL9jfrfqJzhUHXrIYb0BZ7pi', FALSE, 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('Rachel', 'Howell', 'rhowell@test.mail', 'rhowell', '$2a$10$TgqMA.7hy24h3B1LTVT.iuQBAKlrGVs5rNFYRZJk94foe8th60Uru', FALSE, 'admin', NULL, CURRENT_TIMESTAMP, NULL);


INSERT INTO roles(name, created_by, last_modified_by, created_at, last_modified_at)
VALUES ('ROLE_ADMIN', 'admin', NULL, CURRENT_TIMESTAMP, NULL),
       ('ROLE_USER', 'admin', NULL, CURRENT_TIMESTAMP, NULL),
        ('ROLE_MANAGER', 'admin', NULL, CURRENT_TIMESTAMP, NULL);


INSERT INTO user_roles(user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (2, 2),
       (2, 3),
       (3, 2),
       (4, 2),
       (5, 2),
       (6, 2),
       (7, 2),
       (8, 2),
       (9, 2),
       (10, 2),
       (11, 2),
       (12, 2);