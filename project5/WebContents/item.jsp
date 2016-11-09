<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html> 
<html> 
<head> 
<title>Search Result v1.1</title>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
<style type="text/css"> 

  html { height: 100% } 
  body { height: 100%; margin: 5px; padding: 5px }

    h2{
        font-family: verdana;
        text-align: :center;
    }
    h3{
        font-family: verdana;
    }
    p{
        font-size: 50%
    }
</style> 
<script type="text/javascript" 
    src="http://maps.google.com/maps/api/js?sensor=false"> 
</script> 
<script type="text/javascript"> 
function initialize() {
    if(${item.location.latitude}!=null && ${item.location.longitude} != null)
    {
            var latlng = new google.maps.LatLng(${item.location.latitude},${item.location.longitude}); 
            var myOptions = { 
                zoom: 14, // default is 8  
                center: latlng, 
                mapTypeId: google.maps.MapTypeId.ROADMAP 
              }; 
            var map = new google.maps.Map(document.getElementById("map_canvas"), 
            myOptions); 
            var marker = new google.maps.Marker({
            map: map,
            position: latlng
    });    }
    else
    {
    var latlng = new google.maps.LatLng(0,70); 
    var myOptions = { 
      zoom: 14, // default is 8  
      center: latlng, 
      mapTypeId: google.maps.MapTypeId.ROADMAP 
    }; 
    var map = new google.maps.Map(document.getElementById("map_canvas"), 
        myOptions);  
    }

}

</script> 

</head> 
<body onload="initialize()"> 

        <h2><b>eBay Search Web Site</b></h2>
        <hr>
        <h4>
        <a href = " ./index.html">ebay</a> 
        <a href = " ./keywordSearch.html">Keyword Search</a> 
        <a href = " ./getItem.html">Item Search</a>
        </h4>
        <h3><b>Item Search</b><h3>
        <br>
                    <form action="item" method="GET" id="itemForm">
                            <input type="text" class="form-control" name="id" placeholder="ItemID here">
                            <button class="btn btn-success" type="submit">Search</button>
                    </form>
            <hr>
                                    
                <c:choose>
                    <c:when test="${not empty item}">
                                    <h3>Seller Information</h3>
                                    Seller: ${item.seller.name} (${item.seller.rating})
                                    <hr>
                                    <div id="map_canvas" style="width:600px; height:400px"></div>
                                    <hr>
                                    <h3>Item Information</h3> 
                                    <p>Item: ${item.name}</p> 
                                    <p>ID: ${item.id}</p> 
                                    <p>Started: ${item.started}</p>
                                    <p>Ends: ${item.ends}</p>
                                    <p>Current: ${item.currently}</p> 
                                    <c:if test="${not empty item.buyPrice}">
                                        <p>Buy Price: ${item.buyPrice}</p> 
                                        <form action=" /eBay/makeOrder">
                                            <input type="submit" value="Pay Now">
                                        </form>
                                    </c:if>
                                    <hr>
                                    <h3>Categories</h3>
                                    <c:forEach var="category" items="${item.categories}">
                                    <button type = "button"> ${category}</button>
                                    </c:forEach>
                                    <h3>${item.numberOfBids} Bidding</h3>
                                    <hr>
                                    <c:if test="${item.numberOfBids > 0}">
                                        <c:forEach var="bid" items="${item.bids}">
                                            <p>${bid.time}  ${bid.bidder.name} (${bid.bidder.rating})  Amount: ${bid.amount}</p>
                                        </c:forEach>
                                    </c:if>                            
                                    <hr>
                                    <h3>Description</h3>
                                    <hr>
                                    <p>${item.description}</p>
                                    
                   </c:when>
                   <c:otherwise>
                       <h3>Sorry, no matched result :(</h3>
                   </c:otherwise>
               </c:choose>
    </body>
</html>