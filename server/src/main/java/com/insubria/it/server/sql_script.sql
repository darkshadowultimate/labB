create table administrator
(
	uid varchar(30) not null
		constraint administrator_pkey
			primary key,
	password varchar(30) not null
);

create table users
(
	email varchar(60) not null,
	username varchar(20) not null,
	name varchar(20) not null,
	surname varchar(20) not null,
	password varchar(30) not null,
	is_confirmed boolean default false not null,
	code varchar(36),
	constraint users_pkey
		primary key (email, username)
);

create unique index users_email_uindex
	on users (email);

create unique index users_username_uindex
	on users (username);

create table game
(
	id serial not null
		constraint game_pkey
			primary key,
	name varchar(20) not null,
	max_players integer not null
		constraint players_constraint
			check ((max_players > 2) AND (max_players < 7)),
	status varchar(7) not null
		constraint status_constraint
			check ((status)::text = ANY ((ARRAY['open'::character varying, 'playing'::character varying, 'closed'::character varying])::text[])),
	date timestamp default CURRENT_TIMESTAMP not null,
	n_rounds integer default 1 not null
);

create table enter
(
	id_game integer not null
		constraint enter_id_game_fkey
			references game on delete cascade,
	email_user varchar(60) not null,
	username_user varchar(20) not null,
	session_number integer default 1 not null
		constraint session_number_constraint
			check (session_number > 0),
	characters varchar(16),
	constraint enter_pkey
		primary key (id_game, email_user, username_user, session_number),
	constraint enter_email_user_fkey
		foreign key (email_user, username_user) references users
);

create table discover
(
	word varchar(16) not null,
	id_game integer not null,
	email_user varchar(60) not null,
	username_user varchar(20) not null,
	session_number_enter integer not null,
	n_requests integer default 0 not null
		constraint n_requests_constrains
			check (n_requests >= 0),
	score integer not null
		constraint score_constraint
			check (score >= 0),
	is_valid boolean not null,
	reason varchar default ''::character varying,
	constraint discover_pkey
		primary key (id_game, email_user, username_user, session_number_enter, word),
	constraint discover_id_game_fkey
		foreign key (id_game, email_user, username_user, session_number_enter) references enter
);