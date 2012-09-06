<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Steeltoe App Admin - Domain: ${domain.domainName}</title>
</head>
<body>
	<h1>Steeltoe App Admin</h1>

	<h2>${domain.domainName} Domain</h2>

	<table>
		<tr>
			<td>Consumer Key:</td>
			<td>${domain.consumerKey}</td>
		</tr>
		<tr>
			<td>Consumer Secret:</td>
			<td>${domain.consumerSecret}</td>
		</tr>
	</table>

	<h2>${domain.domainName} Users</h2>

	<table>
		<thead>
			<tr>
				<th>Login</th>
				<th>Is Admin</th>
				<th>Name</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="user" items="${userFeed}">
			<tr>
				<td>${user.login.userName}</td>
				<td>${user.login.admin}</td>
				<td>${user.name.familyName}, ${user.name.givenName}</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>

</body>
<html>