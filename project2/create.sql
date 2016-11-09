CREATE TABLE User
(
UserID varchar(100) not null primary key,
Rating int not null,
Location varchar(100) default null,
Country varchar(100) default null
);

CREATE TABLE Item 
(
ItemID int not null primary key,
Name varchar(100),
Description varchar(4000),
Started timestamp not null,
Ends timestamp not null,
Currently decimal(8,2) not null,
First_Bid decimal(8,2) not null,
Buy_Price decimal(8,2) not null,
Number_of_Bids int,
Seller varchar(100),
Location varchar(100) default null,
Country varchar(100) default null,
foreign key (Seller) references User(UserID)
);

CREATE TABLE ItemCategory
(
ItemID int not null,
Category varchar(80) not null,
primary key (ItemID, Category),
foreign key (ItemID) references Item(ItemID)
);

CREATE TABLE Bid
(
ItemID int not null,
UserID varchar(80) not null,
Time timestamp not null,
Amount decimal(8,2) not null,
primary key (UserID, Time),
foreign key (ItemID) references Item(ItemID),
foreign key (UserID) references User(UserID)
);



