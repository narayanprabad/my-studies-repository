package com.twitter.tag;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import twitter4j.Trend;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TwitterTrendsTagHandler extends TagSupport {
	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 5032443941347089105L;

	private String woeid;

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
				int locationId = 0;
				try {
					locationId = Integer.parseInt(woeid);
				} catch (NumberFormatException ex) {
					// Do Nothing
				}
				out.println("<br>");
				Trends trends = twitter.getPlaceTrends(locationId);
				for (Trend t : trends.getTrends()) {
					out.println("<a href='https://twitter.com/search?q="
							+ urlEncode(t.getName(), "UTF-8") + "&src=tren'>");
					out.println(t.getName());
					out.println("</a>");
					out.println("<br>");
				}
				request.setAttribute("trends", trends.getTrends());
			} catch (TwitterException te) {
				throw new JspException("Service Unavailable. Please try refresh the Page after sometime.");
			}

		}  catch (Exception e) {
			throw new JspException("Internal Server error. Please try refresh the Page after sometime.");
		}
		return SKIP_BODY;
	}

	public static String urlEncode(String str, String encoding)
			throws UnsupportedEncodingException {
		if (str == null) {
			return "";
		}
		return java.net.URLEncoder.encode(str, encoding);
	}

	public String getWoeid() {
		return woeid;
	}

	public void setWoeid(String woeid) {
		this.woeid = woeid;
	}

}
