<%@ page language="java" contentType="application/json;charset=UTF-8" %>
<%@ page import="edu.uw.cldaws.WordCount" %>
<%
WordCount wc = new WordCount();
int count = request.getParameter("count") == null ? 10: Integer.parseInt(request.getParameter("count"));
%>
<%=wc.doReport(request.getParameter("url"), count)%>