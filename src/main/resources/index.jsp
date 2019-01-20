<%@ page language="java" contentType="application/json;charset=UTF-8" %>
<%@ page import="edu.uw.cldaws.WordCount" %>
<%
WordCount wc = new WordCount();
int count = 10;
try {
  count = request.getParameter("count") == null ? 10: Integer.parseInt(request.getParameter("count"));
} catch(NumberFormatException e) {
  System.err.println(e.getMessage());
}
String url = request.getParameter("url") == null ? "" : request.getParameter("url");
if (url.isEmpty()) {
  response.sendRedirect("input.html");
} else {
  out.print(wc.doReport(request.getParameter("url"), count));
}
%>
