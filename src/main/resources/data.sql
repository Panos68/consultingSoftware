INSERT INTO `users` (`id`, `password`, `username`) VALUES ('1', '123', 'Admin');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('2', '123', 'UserToEdit');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('3', '123', 'UserToDelete');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('4', '123', 'NonAdminUser');
insert into `user_roles` values ('1','admin');
insert into `user_roles` values ('4','user');

INSERT INTO `consultants` (`id`, `contract_ending`, `contract_started`, `discount`, `first_name`, `last_name`, `list_price`, `other`, `price`, `signed`, `status`, `updated_contract_ending`) VALUES ('1', null, null, '20', 'Main', 'User', '200', 'Some other text', '100', true, 'active', null);
INSERT INTO `consultants` (`id`, `contract_ending`, `contract_started`, `discount`, `first_name`, `last_name`, `list_price`, `other`, `price`, `signed`, `status`, `updated_contract_ending`) VALUES ('2', null, null, '20', 'Delete', 'User', '200', 'Some other text', '100', true, 'active', null);
INSERT INTO `consultants` (`id`, `contract_ending`, `contract_started`, `discount`, `first_name`, `last_name`, `list_price`, `other`, `price`, `signed`, `status`, `updated_contract_ending`) VALUES ('3', null, null, '20', 'Edit', 'User', '200', 'Some other text', '100', true, 'active', null);
