CREATE TABLE ItemLocation
(
ItemID int not null,
g POINT not null) ENGINE = MYISAM;



INSERT INTO ItemLocation (ItemID, g)
  SELECT ItemID, POINT(Latitude, Longitude)
  FROM location
  WHERE Latitude != "" AND Longitude != "";
  
CREATE SPATIAL INDEX Coordinates_Index ON ItemLocation(g);