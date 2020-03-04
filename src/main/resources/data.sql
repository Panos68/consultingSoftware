INSERT INTO `users` (`id`, `password`, `username`) VALUES ('1', '$2a$10$ztXW2.Uw7qMAgDwP.5xvi.r6GYBSm.DVDDOsB3AyC5DJVf1fXQsSu', 'Admin');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('2', '$2a$10$ztXW2.Uw7qMAgDwP.5xvi.r6GYBSm.DVDDOsB3AyC5DJVf1fXQsSu', 'UserToEdit');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('3', '$2a$10$ztXW2.Uw7qMAgDwP.5xvi.r6GYBSm.DVDDOsB3AyC5DJVf1fXQsSu', 'UserToDelete');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('4', '$2a$10$ztXW2.Uw7qMAgDwP.5xvi.r6GYBSm.DVDDOsB3AyC5DJVf1fXQsSu', 'NonAdminUser');
insert into `user_roles` values ('1','admin');
insert into `user_roles` values ('4','user');

INSERT INTO `clients` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`) VALUES ('1', 'Joe', 'Michael', 'person@gmail.com', 'mainPerson', '123123123', 'MainClient');
INSERT INTO `clients` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`) VALUES ('2', 'Joe', 'Michael', 'person@gmail.com', 'mainPerson', '123123123', 'EditClient');
INSERT INTO `clients` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`) VALUES ('3', 'Joe', 'Michael', 'person@gmail.com', 'mainPerson', '123123123', 'DeleteClient');

INSERT INTO `client_teams` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`, `main_technologies`, `client_id`) VALUES ('1', 'Joe', 'Michael', 'test@gmail.com', 'MainPerson', '123123123', 'MainTeam', 'Java,Html', '1');
INSERT INTO `client_teams` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`, `main_technologies`, `client_id`) VALUES ('2', 'Joe', 'Michael', 'test@gmail.com', 'MainPerson', '123123123', 'EditTeam', 'Java,Html', '1');
INSERT INTO `client_teams` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`, `main_technologies`, `client_id`) VALUES ('3', 'Joe', 'Michael', 'test@gmail.com', 'MainPerson', '123123123', 'DeleteTeam', 'Java,Html', '1');
INSERT INTO `client_teams` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`, `main_technologies`, `client_id`) VALUES ('4', 'Joe', 'Michael', 'test@gmail.com', 'MainPerson', '123123123', 'DeleteTeam2', 'Java,Html', '3');

INSERT INTO `consultants` (`id`,`first_name`, `last_name`, `list_price`, `other`,`team_id`) VALUES ('1','MainConsultant', 'User', '200', 'Some other text','3');
INSERT INTO `consultants` (`id`,`first_name`, `last_name`, `list_price`, `other`,`team_id`) VALUES ('2','EditConsultant', 'User', '200', 'Some other text','1');
INSERT INTO `consultants` (`id`,`first_name`, `last_name`, `list_price`, `other`,`team_id`) VALUES ('3','DeleteConsultant', 'User', '200', 'Some other text','1');

INSERT INTO `contracts` (`id`, `active`, `client_name`, `discount`, `price`, `signed`, `consultant_id`) VALUES ('1', true, 'MainClient', '10', '100', true, '1');
INSERT INTO `contracts` (`id`, `active`, `client_name`, `consultant_id`) VALUES ('3', true, 'Mirado',2);
INSERT INTO `contracts` (`id`, `active`, `client_name`, `discount`,`price`, `signed`, `consultant_id`) VALUES ('2', true, 'EditClient', '10', '100', true, '3');

INSERT INTO `candidates` (`id`, `comment`, `company`, `consultant`, `diverse`, `linkedin_url`, `location`, `role`, `source`) VALUES ('1', 'comment', 'company', 'consultant', 'no', 'MainLinkedIn', 'Stockholm', 'backend', 'source');
INSERT INTO `candidates` (`id`, `comment`, `company`, `consultant`, `diverse`, `linkedin_url`, `location`, `role`, `source`) VALUES ('2', 'comment', 'company', 'consultant', 'no', 'EditLinkedIn', 'Stockholm', 'backend', 'source');
INSERT INTO `candidates` (`id`, `comment`, `company`, `consultant`, `diverse`, `linkedin_url`, `location`, `role`, `source`) VALUES ('3', 'comment', 'company', 'consultant', 'no', 'DeleteLinkedIn', 'Stockholm', 'backend', 'source');


INSERT INTO `vacations` (`id`, `description`, `user_id`) VALUES ('1', 'Brazil', '1');
INSERT INTO `vacations` (`id`, `description`, `user_id`) VALUES ('2', 'EditVac', '1');
INSERT INTO `vacations` (`id`, `description`, `user_id`) VALUES ('3', 'DeleteVac', '1');
INSERT INTO `vacations` (`id`, `description`, `user_id`) VALUES ('4', 'UserVac', '3');