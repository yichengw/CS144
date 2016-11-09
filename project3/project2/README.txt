Part B
1.List your relations. Please specify all keys that hold on each relation. You need not specify attribute types at this stage.

Item(ItemID, Name, Currently, Buy_Price,First_Bid, Number_of_Bids, Location, Country, Started, Ends, Seller.UserID, Description) Keys: ItemID

Bid(ItemID, UserID, Time, Amount)
Keys: (ItemID, UserID, Time)

User(UserID, Rating, Location, Country) Keys: UserID  
//User is Seller adds Bidder

Item_Category(ItemID, Category) keys: (ItemID, Category)

2.List all completely nontrivial functional dependencies that hold on each relation, excluding those that effectively specify keys.

For the relation Item, the completely nontrivial functional dependencies are:
ItemID -> Name, Currently, Buy_Price,First_Bid, Number_of_Bids, Bids, Location, Country, Started, Ends, Seller.UserID, Description

For the relation Bid, the completely nontrivial functional dependencies are:
ItemID, UserID,Time-> Amount

For the relation User, the completely nontrivial functional dependencies are:
UserID->Rating, Location, Country


3.Are all of your relations in Boyce-Codd Normal Form (BCNF)? 
  If not, either redesign them and start over, or explain why you feel it is advantageous to use non-BCNF relations.
No. Relation Item_Category is not in BCNF. Since one key (for example ItemID) can have multiple categories.

4.Are all of your relations in Fourth Normal Form (4NF)? 
If not, either redesign them and start over, or explain why you feel it is advantageous to use non-4NF relations.
Yes.
