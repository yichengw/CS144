CREATE TABLE Actors
(
Name varchar(40),
Movie varchar(80),
Year int,
Role varchar(40)
);
load data local infile '~/data/actors.csv' into table Actors
fields terminated by ',' optionally enclosed by '"';

SELECT Name FROM Actors
WHERE Movie = 'Die Another Day';
