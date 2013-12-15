package com.twitter.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import twitter4j.Location;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterLocationTagHandler extends TagSupport {

	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = -3808203804717965379L;

	@Override
	public int doStartTag() throws JspException {

		try {
			// Get the writer object for output.
			JspWriter out = pageContext.getOut();
			HttpServletRequest request = (HttpServletRequest) pageContext
					.getRequest();
			try {
				Twitter twitter = new TwitterFactory().getInstance();
				String accessTokenKey = String.valueOf(pageContext
						.findAttribute("accessTokenKey"));
				String accessTokenSecret = String.valueOf(pageContext
						.findAttribute("accessTokenSecret"));
				String consumerKey = String.valueOf(pageContext
						.findAttribute("consumerKey"));
				String consumerSecret = String.valueOf(pageContext
						.findAttribute("consumerSecret"));
				AccessToken accessToken = new AccessToken(accessTokenKey,
						accessTokenSecret);
				twitter.setOAuthConsumer(consumerKey, consumerSecret);
				twitter.setOAuthAccessToken(accessToken);

				ResponseList<Location> locations;
				locations = twitter.getAvailableTrends();
				request.setAttribute("locations", locations);
				out.println("<table>");
				for (Location location : locations) {
					out.println("<tr>");
					out.println("<td>");
					// <a href="javascript:refreshTrends(123, 'Abc');">
					out.println("<a href=\"javascript:refreshTrends( "
							+ location.getWoeid() + " , '" + location.getName()
							+ "');\">");
					/*
					 * out.println("<a href=\"javascript:refreshTrends(" +
					 * location.getWoeid() + ", '" + location.getName() +
					 * "' );\">");
					 */
					out.println(location.getWoeid());
					out.println("</a>");
					out.println("</td>");
					out.println("<td>");
					out.println(location.getName());
					out.println("</td>");
					out.println("</tr>");
				}
				out.println("</table>");
			} catch (TwitterException te) {
				//throw new JspException("Service Unavailable. Please try refresh the Page after sometime.");
			}

		}  catch (Exception e) {
			//throw new JspException("Internal Server error. Please try refresh the Page after sometime.");
		}
		return SKIP_BODY;
	}

}
