<%@page language="java"%>
<%@page import="java.sql.*,java.io.File"%>

<html>

<head>
	<title>Register</title>
</head>

<body>

	<!--Register Form-->
	<h3>Register</h3>
	<form action="noteServ" method="post">
		Name
		<input type="text" name="name"/><br/>
		Email
		<input type="text" name="email"/><br/>
		Password
		<input type="password" name="password"/><br/>
		<input type="submit" name="register" value="Register" />
	</form>

</body>

</html>