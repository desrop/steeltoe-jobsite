<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>Welcome Page</title>
</head>
<body>
	<h1>Welcome!</h1>
	<hr>	
	<ul>
		<li><a href="/hello/form">Form Login Example</a></li>
		<li><a href="/hello/openid">OpenID Form Login Example</a></li>
		<li><a href="/hello/openid?hd=desropolis.com">OpenID Login Example</a></li>
		<li><a href="/hello/openid/extreme?hd=desropolis.com">Extreme OpenID Login Example</a></li>
	</ul>
</body>
</html>