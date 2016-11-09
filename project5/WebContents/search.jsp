<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Search Results</title>
        <script type="text/javascript" src="Suggest.js"></script>
        <style>
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
    div.suggestions {
    -moz-box-sizing: border-box;
    box-sizing: border-box;
    position: absolute;   
    background-color: white;
    }

    div.suggestions div {
    cursor: default;
    padding: 0px 3px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    }

    div.suggestions div.current {
    background-color: gray;
    color: white;
    }
        </style>
    </head>
    <body>
        <h2><b>eBay Search Web Site</b></h2>
        <hr>
        <h4>
        <a href = " ./index.html">ebay</a> 
        <a href = " ./keywordSearch.html">Keyword Search</a> 
        <a href = " ./getItem.html">Item Search</a>
        </h4>
        <h3><b>Keyword Search</b><h3>
        <br>
        <form action="search" method="GET" id="searchForm">

            <input type="text" name="q" class="form-control"  placeholder="Keyword here" id="searchTextBox">
            <input type="hidden" name="numResultsToSkip" value="0">
            <input type="hidden" name="numResultsToReturn" value="30">
            <button class="btn btn-success" type="submit">Search</button>
            <hr>


        <c:url value="search" var="prevURL">
            <c:param name="q" value="${query}" />
            <c:param name="numResultsToSkip" value="${numResultsToSkip-numResultsToReturn}" />
            <c:param name="numResultsToReturn" value="${numResultsToReturn}" />
        </c:url>
        <c:url value="search" var="nextURL">
            <c:param name="q" value="${query}" />
            <c:param name="numResultsToSkip" value="${numResultsToSkip+numResultsToReturn}" />
            <c:param name="numResultsToReturn" value="${numResultsToReturn}" />
        </c:url>
                <c:choose>
                    <c:when test="${not empty search_result}">
                            <c:if test="${prev}">
                                <a class="btn btn-lg btn-primary" href="${prevURL}">Prev</a>
                            </c:if>
                            <c:if test="${next}">
                                <a class="btn btn-lg btn-primary" href="${nextURL}">Next</a>
                            </c:if>
                            <hr>
                        <c:forEach var="result" items="${search_result}" begin="0" end="${numResultsToReturn-1}">
                            <c:url value="item" var="itemURL">
                                <c:param name="id" value="${result.itemId}" />
                            </c:url>
                            <span class="col-xs-10"><p>${result.name}  <a href="${itemURL}">${result.itemId}</a></p></span>
                            <hr>
                        </c:forEach>

                    </c:when>
                    <c:otherwise>
                        <h3>Sorry, no matched result:(</h3>
                    </c:otherwise>
                </c:choose>
    </body>
</html>
