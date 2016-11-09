
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html> 
<html> 
<head> 
<title>Comfirm Order v1.1</title>
</head> 
<body> 

        <h2><b>Comfirm Your Order</b></h2>
        <hr>
        <h4>
        <a href = " http://${svn}:${svp}/eBay/index.html">ebay</a> 
        <a href = " http://${svn}:${svp}/eBay/keywordSearch.html">Keyword Search</a> 
        <a href = " http://${svn}:${svp}/eBay/getItem.html">Item Search</a>
        </h4>
        <br>
        <p>Item Name: ${name}</p>
        <p>Item ID: ${id}</p>
        <p>Item Price: ${price}</p>
        <p>Credit Card Number: ${cd}</p>
        
        <p>Order Time:</p>
        <p id="demo"></p>

        <script>
        document.getElementById("demo").innerHTML = Date();
        </script>
                
        

    </body>
</html>

