-- test 1
SELECT COUNT(*) FROM User;

-- test 2
SELECT COUNT(*) FROM Item
WHERE BINARY Location = "New York";

-- test 3(?)
SELECT COUNT(*) FROM (
SELECT ItemID 
FROM ItemCategory 
GROUP BY ItemID 
HAVING COUNT(Category) = 4
) ItemID;

-- test 4		
SELECT Item.ItemID 
FROM Item
INNER JOIN Bid
ON Item.ItemID = Bid.ItemID
WHERE Started < "2001-12-20 00:00:00" AND Ends > "2001-12-20 00:00:01" AND Amount = (SELECT MAX(Amount) FROM Bid);

-- test 5
SELECT COUNT(*) FROM (
SELECT DISTINCT User.UserID
FROM User
INNER JOIN Item
ON User.UserID = Item.Seller
WHERE BINARY Rating > 1000
) UserID;


-- test 6
SELECT COUNT(*) FROM (
SELECT DISTINCT Item.Seller
FROM Item
INNER JOIN Bid
ON Item.Seller = Bid.UserID) SellerAndBidder;

-- test7
SELECT COUNT(*) FROM(
SELECT DISTINCT Category 
FROM ItemCategory
INNER JOIN Bid
ON ItemCategory.ItemID = Bid.ItemID
WHERE Amount > 100
AND Bid.ItemID
IN
(SELECT Bid.ItemID
FROM Item
INNER JOIN Bid
ON Bid.ItemID = Item.ItemID)
) A;