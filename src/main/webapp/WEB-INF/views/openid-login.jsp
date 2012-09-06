<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>Log in using OpenID</title>
</head>
<body>
	<h1>Welcome!</h1>
	<div>Please log in using your Google Apps for Business domain name (e.g.: "mycompany.com")</div>
	<form name="f" action="/j_google_openid_security_check" method="POST">
	<table>
		<tr>
			<td>Domain Name</td>
			<td><input type="text" name="hd"></td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit"></td>
		</tr>
	</table>
	</form>
</body>
</html>