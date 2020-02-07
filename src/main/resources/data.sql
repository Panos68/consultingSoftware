INSERT INTO `users` (`id`, `password`, `username`) VALUES ('1', '123', 'Admin');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('2', '123', 'UserToEdit');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('3', '123', 'UserToDelete');
INSERT INTO `users` (`id`, `password`, `username`) VALUES ('4', '123', 'NonAdminUser');
insert into `user_roles` values ('1','admin');
insert into `user_roles` values ('4','user');