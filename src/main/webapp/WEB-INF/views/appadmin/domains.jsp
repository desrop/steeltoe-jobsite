<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Steeltoe App Admin</title>
</head>
<body>
	<h1>Steeltoe App Admin</h1>
	<h2>Domains</h2>
	<ul>
		<c:forEach var="domain" items="${domains}">
			<li><a href="domains/${domain.id}">${domain.domainName}</a></li>
		</c:forEach>
	</ul>
	<a href="domains/add">Add a domain</a>
</body>
</html>