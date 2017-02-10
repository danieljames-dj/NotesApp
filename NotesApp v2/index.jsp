<%@page language="java"%>
<%@page import="java.sql.*,java.io.File"%>

<html>

<head>
	<title>Login</title>
</head>

<body>

	<!--Login Form-->
	<h3>Login</h3>
	<form action="noteServ" method="post">
		Email
		<input type="text" name="email"/>
		Password
		<input type="password" name="password"/>
		<input type="submit" name="login" value="Login" />
	</form>

	<!--Register Link-->
	<form action="register.jsp" method="post">
		Click here to register as new user
		<input type="submit" name="register" value="Register" />
	</form>

</body>

</html>