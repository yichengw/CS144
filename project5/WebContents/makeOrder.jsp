<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html> 
<html> 
<head> 
<title>Make Order v1.1</title>
</head> 
<body> 

        <h2><b>Make Your Order</b></h2>
        <hr>
        <h4>
        <a href = " ./index.html">ebay</a> 
        <a href = " ./keywordSearch.html">Keyword Search</a> 
        <a href = " ./getItem.html">Item Search</a>
        </h4>
        <br>
        <p>Item Name: ${name}</p>
        <p>Item ID: ${id}</p>
        <p>Item Price: ${price}</p>
        
        <form action=" https://${svn}:8443/eBay/comfirmOrder" >
          Credit Card:
          <input type="text" name="cardNum">
          <br>
          <input type="submit" value="Submit">
        </form> 

    </body>
</html>