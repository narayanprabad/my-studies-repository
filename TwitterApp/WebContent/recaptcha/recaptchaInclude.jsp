<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="ISO-8859-1"%>


<div id="message" style="color: #ff0000;"></div>
<div style="height: 148px;">
	<span class="js-recaptcha"></span>
</div>

<script type="text/javascript">
		function recaptchadisplay() {
			$.ajax({
				type : "POST",
				url : 'recaptchaDisplay.jsp',
				data : {
					time : (new Date().getTime())
				},
				dataType : "html",
				success : function(data) {
					if (data != null && data != "") {
						if (data != null && data != "") {
							$(".js-recaptcha").prepend(data);
						}
					}
				}
			});
		}

		$(function() {
			recaptchadisplay();

		});
		
		function recaptchaValidate() {
			var challenge = Recaptcha.get_challenge();
			var response = Recaptcha.get_response();
			var remoteip = "<%=request.getRemoteAddr()%>";

		$.ajax({
					type : "POST",
					url : "recaptchaValidate.jsp",
					data : {
						remoteip : remoteip,
						challenge : challenge,
						response : response
					},
					success : function(resp) {
						if (resp.trim() == "true") {
							$('#recaptcha-form').attr("action",
									formMainAction);
							$('#recaptcha-form').submit();
						} else {
							document.getElementById("message").innerHTML = "Incorrect Recaptcha! Please try again!";
							recaptchadisplay();
						}
					}
				});
		return false;
	}
</script>


