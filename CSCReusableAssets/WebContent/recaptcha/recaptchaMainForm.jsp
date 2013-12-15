<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%--Test Recaptcha Form Page --%>
<html>
<head>
<title>Recaptcha Main Form</title>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script type="text/javascript"
	src="http://www.google.com/recaptcha/api/js/recaptcha_ajax.js"></script>
	<script>
		<%-- Change your Main form Action appropriately --%>
		var formMainAction="recaptchaSuccess.jsp";
	</script>
</head>
<body>
<%-- Don't peform any action on Form Submit --%>
<form id="recaptcha-form" name="recaptcha-form" action="javascript:void(0);" method="POST">
	<%--Include this page in your Form and on submit call Reacptcha validate function --%>
	<jsp:include page="recaptchaInclude.jsp" />
	<input type="submit" value="Submit" onclick="javascript:recaptchaValidate();" />
</form>


</body>
</html>