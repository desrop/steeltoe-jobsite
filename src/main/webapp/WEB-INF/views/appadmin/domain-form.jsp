<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Steeltoe App Admin - Add/Edit Domain</title>
</head>
<body>
	<h1>Steeltoe App Admin</h1>

	<h2>Add/Edit Domain</h2>

	<form:form commandName="domain" name="f" method="post">
		<form:hidden path="id" />
		<form:errors path="*"/>
		<table>
			<tr>
				<td>Domain Name:</td>
				<td><form:input path="domainName" /></td>
			</tr>
			<tr>
				<td>Consumer Key:</td>
				<td><form:input path="consumerKey" /></td>
			</tr>
			<tr>
				<td>Consumer Secret:</td>
				<td><form:input path="consumerSecret" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Save"></td>
			</tr>
		</table>
	</form:form>

</body>
<html>