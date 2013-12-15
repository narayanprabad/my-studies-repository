<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaImpl,net.tanesha.recaptcha.ReCaptchaResponse" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="net.tanesha.recaptcha.Messages" />
<c:set var="accessTokenKey"><fmt:message key="recaptcha.private.key"/></c:set>
<%
ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
reCaptcha.setPrivateKey((String) pageContext.findAttribute("accessTokenKey"));

String remoteip = request.getParameter("remoteip");
String challenge = request.getParameter("challenge");
String res = request.getParameter("response");
ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteip, challenge, res);

if (reCaptchaResponse.isValid()) {
  out.print("true");
} else {
  out.print("false");
}
%>