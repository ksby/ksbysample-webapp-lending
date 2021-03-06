INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (3, 'kimura masao', '{bcrypt}$2a$10$yP1dLPIq9j7WQVH6ruSwkepf8jIkPxTtncbSnYM0/jAGQ4HCQO8R.', 'kimura.masao@test.co.jp', 0, 0, '2015-12-31 22:30:54.425000', '2015-10-15 22:31:03.316000');
INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (4, 'endo yoko', '{bcrypt}$2a$10$PVFe8Lh1Pkjc54DWS9mJL.q407x51ZK8MSXhwuTF9zxCnnt80LKwy', 'endo.yoko@sample.com', 1, 0, '2015-01-10 22:31:55.454000', '2015-12-31 22:32:11.886000');
INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (5, 'sato masahiko', '{bcrypt}$2a$10$qIU0kM/p1pa7KSIjF6YA4eORd2wL1Eo6TlvH./DmPs7D.xXQPEq7a', 'sato.masahiko@sample.com', 1, 0, '9999-12-31 23:59:00.000000', '2014-08-05 22:34:22.818000');
INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (6, 'takahasi naoko', '{bcrypt}$2a$10$iXp/d4wXmfaLKTjQKBvik.kETgx4nQ.FL1NjYt4ALJOGSyVOSchW6', 'takahasi.naoko@test.co.jp', 1, 0, '2015-12-01 22:39:48.475000', '2015-11-10 22:39:55.422000');
INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (7, 'kato hiroshi', '{bcrypt}$2a$10$g5dtFTtNBdJO30aHg50rluGNa2pEAzArcwYkYyCG91ElBZPs9sDi2', 'kato.hiroshi@sample.com', 0, 5, '2014-01-01 15:58:53.295000', '2013-12-31 15:59:07.668000');
INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (8, 'ito aoi', '{bcrypt}$2a$10$wD9VL.2cfiL0UY05N4PAs.l0Zd/X9CC0LouzGaHqp2W6PKLjQXJNq', 'ito.aoi@test.co.jp', 1, 0, '9999-12-31 23:59:00.000000', '9999-12-31 23:59:00.000000');
INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (1, 'tanaka taro', '{bcrypt}$2a$10$LKKepbcPCiT82NxSIdzJr.9ph.786Mxvr.VoXFl4hNcaaAn9u7jje', 'tanaka.taro@sample.com', 1, 0, '9999-12-31 23:59:00.000000', '9999-12-31 23:59:00.000000');
INSERT INTO public.user_info (user_id, username, password, mail_address, enabled, cnt_badcredentials, expired_account, expired_password) VALUES (2, 'suzuki hanako', '{bcrypt}$2a$10$.fiPEZ155Rl41/e.mdM3A.mG0iEQNPmhjFL/aIiV8dZnXsCd.oqji', 'suzuki.hanako@test.co.jp', 1, 0, '9999-12-31 23:59:00.000000', '9999-12-31 23:59:00.000000');

INSERT INTO public.user_role (role_id, user_id, role) VALUES (1, 1, 'ROLE_USER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (2, 1, 'ROLE_ADMIN');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (3, 1, 'ROLE_APPROVER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (4, 2, 'ROLE_USER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (100, 1, 'ROLE_USER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (5, 2, 'ROLE_APPROVER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (6, 3, 'ROLE_USER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (7, 4, 'ROLE_USER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (8, 5, 'ROLE_USER');
INSERT INTO public.user_role (role_id, user_id, role) VALUES (9, 8, 'ROLE_USER');

INSERT INTO public.library_forsearch (systemid, formal) VALUES ('Tokyo_NDL', '国立国会図書館東京本館');
