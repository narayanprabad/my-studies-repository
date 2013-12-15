<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Twitter Trends</title>
     <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
	<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	<link rel="stylesheet" href="/resources/demos/style.css">
	<style type="text/css">
	body
	{
	font-family: Arial, sans-serif;
    font-size: 14px;
    font-weight: normal;
    background-color:#95B9C7;
	}
	a{
	color:blue !important;
	
	}
	#screen
{
        position: absolute;
        left: 0;
        top: 0;
        background: #000;
}
	</style>
</head>
<body>
<div id='screen'>
</div>
	<div id="dialog" title="Trends" style="display: none;">
		<jsp:include page="twitter_locations_include.jsp" />
	</div>
	
	<input type="hidden" id="woeId" value="1">

	
	<div id="twitter-trends-div-main">
		<div id="twitter-header" style="display:none;">
			Trending now - <b><span id="woeName">Worldwide</span></b>. To select a Geo Location <a href="javascript:showLayer();">Click Here</a>.
		</div> 
		<div id="twitter-trends-div">
		</div>
	</div>

<script>
		function showLayer() {
			$('#screen').css({ opacity: 0.2, 'width':$(document).width(),'height':$(document).height()});
			$('#screen').show();
	        $('body').css({'overflow':'hidden'});
	        $( "#dialog" ).dialog(
	        		{
	        			close: function( event, ui ) {
	        				$('#screen').hide();
	        			}
	        		});
		}
		function loadTrends() {
			$.ajax({
				type: "GET",
				url: 'twitter_trends_include.jsp',
				cache: 'false',
				data:  { "woeid": $('#woeId').val() },
				dataType: "html",
				success: function(data) {
					if(data != null && data != "") {
						$("#twitter-header").show();
						$("#twitter-trends-div").html(data);
						
					}
				},
				error: function(xhr, error) {
					$("#twitter-header").hide();
					$("#twitter-trends-div").html("Error while connecting to Twitter API. Try refresh the Page after sometime. You may go to <a href='http://www.twitter.com'>Twitter</a> for latest updates.");
				},
				complete:function(){
					$('#dialog').dialog( "close" );
				}
			});
		}
		function refreshTrends(woeId, woeName) {
			  $('#woeId').val( woeId);
			  $("#woeName").html(woeName);
			  loadTrends();
			  
		}
		
		$(function() {
			// load recently viewed items
			loadTrends();
		});

</script>	    
</body>
</html>