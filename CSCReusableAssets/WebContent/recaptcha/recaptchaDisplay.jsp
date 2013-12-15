<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setBundle basename="net.tanesha.recaptcha.Messages" />
<c:set var="accessTokenKey"><fmt:message key="recaptcha.public.key"/></c:set>
<div class="repatcha">
					<div id="recaptchaDiv"></div>
					 
                      <script type='text/javascript'>
                            Recaptcha.create('${accessTokenKey}',
	                        "recaptchaDiv", {
	                        theme: "red",
	                        callback: Recaptcha.focus_response_field
	                        });
	                  </script> 
					      
												        
					</div>