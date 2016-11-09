LOAD DATA LOCAL INFILE './User.dat' INTO TABLE `User`
    FIELDS TERMINATED BY ' |*| '
    LINES TERMINATED BY "\n"
    (UserID, Rating, @location, @country) SET Location = nullif(@location, ''), Country = nullif(@country, '');
        
LOAD DATA LOCAL INFILE './Item.dat' INTO TABLE `Item`
    FIELDS TERMINATED BY ' |*| '
    LINES TERMINATED BY "\n"
    (ItemID, Name, Description, @started, @ends, Currently, 
     First_Bid, @buy_price, Number_of_Bids, Seller, @location, @country) SET Started = STR_TO_DATE(@started, "%Y-%m-%d %H:%i:%s"), 
	 Ends = STR_TO_DATE(@ends, "%Y-%m-%d %H:%i:%s"), Buy_Price = nullif(@buy_price, ''), Location = nullif(@location, ''), Country = nullif(@country, '');
                                            
LOAD DATA LOCAL INFILE './Item_Category.dat' INTO TABLE `ItemCategory`
    FIELDS TERMINATED BY ' |*| '
    LINES TERMINATED BY "\n";
        
LOAD DATA LOCAL INFILE './Bid.dat' INTO TABLE `Bid`
    FIELDS TERMINATED BY ' |*| '
    LINES TERMINATED BY "\n"
    (UserID, @time, ItemID, Amount) SET Time = STR_TO_DATE(@time, "%Y-%m-%d %H:%i:%s");
    
 LOAD DATA LOCAL INFILE './ItemLocation.dat' INTO TABLE `location`
    FIELDS TERMINATED BY ' |*| '
    LINES TERMINATED BY "\n";   


