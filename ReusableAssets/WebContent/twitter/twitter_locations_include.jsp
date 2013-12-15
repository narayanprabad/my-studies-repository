<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="twitter" uri="/WEB-INF/twitterDescriptor.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="com.twitter.tag.Messages" />
<c:set var="accessTokenKey"><fmt:message key="accessTokenKey"/></c:set>
<c:set var="accessTokenSecret"><fmt:message key="accessTokenSecret"/></c:set>
<c:set var="consumerKey"><fmt:message key="consumerKey"/></c:set>
<c:set var="consumerSecret"><fmt:message key="consumerSecret"/></c:set>

<div id="locations" style="overflow-y:scroll;overflow-x:hidden;height: 400px;" >
<twitter:locations />
</div>


