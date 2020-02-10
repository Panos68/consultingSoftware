INSERT INTO `users` (`id`, `password`, `username`) VALUES ('1', '123', 'Admin');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('2', '123', 'UserToEdit');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('3', '123', 'UserToDelete');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('4', '123', 'NonAdminUser');
insert into `user_roles` values ('1','admin');
insert into `user_roles` values ('4','user');

INSERT INTO `clients` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`) VALUES ('1', 'Joe', 'Michael', 'person@gmail.com', 'mainPerson', '123123123', 'MainClient');
INSERT INTO `clients` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`) VALUES ('2', 'Joe', 'Michael', 'person@gmail.com', 'mainPerson', '123123123', 'DeleteClient');
INSERT INTO `clients` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`) VALUES ('3', 'Joe', 'Michael', 'person@gmail.com', 'mainPerson', '123123123', 'EditClient');

INSERT INTO `client_teams` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`, `main_technologies`, `client_id`) VALUES ('1', 'Joe', 'Michael', 'test@gmail.com', 'MainPerson', '123123123', 'MainTeam', 'Java,Html', '1');
INSERT INTO `client_teams` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`, `main_technologies`, `client_id`) VALUES ('2', 'Joe', 'Michael', 'test@gmail.com', 'MainPerson', '123123123', 'DeleteTeam', 'Java,Html', '1');
INSERT INTO `client_teams` (`id`, `last_interacted_by`, `last_interacted_with`, `main_person_email`, `main_person_name`, `main_person_phone`, `name`, `main_technologies`, `client_id`) VALUES ('3', 'Joe', 'Michael', 'test@gmail.com', 'MainPerson', '123123123', 'EditTeam', 'Java,Html', '1');

INSERT INTO `consultants` (`id`, `discount`, `first_name`, `last_name`, `list_price`, `other`, `price`, `signed`, `status`, `team_id`) VALUES ('1', '20', 'MainConsultant', 'User', '200', 'Some other text', '100', true, 'active', '1');
INSERT INTO `consultants` (`id`, `discount`, `first_name`, `last_name`, `list_price`, `other`, `price`, `signed`, `status`, `team_id`) VALUES ('2', '20', 'DeleteConsultant', 'User', '200', 'Some other text', '100', true, 'active', '1');
INSERT INTO `consultants` (`id`, `discount`, `first_name`, `last_name`, `list_price`, `other`, `price`, `signed`, `status`, `team_id`) VALUES ('3', '20', 'EditConsultant', 'User', '200', 'Some other text', '100', true, 'active', '1');
