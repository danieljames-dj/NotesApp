<%@page language="java"%>
<%@page import="java.sql.*,java.io.File"%>

<html>

<head>
	<title>Notes</title>
</head>

<body>

	<!--Heading-->
	<h1>List of Files</h1>

	<!--New File Form-->
	<form action="noteServ" method="post">
		<input type="text" name="newName"/>
		<input type="submit" name="button1" value="New File" />
	</form>

	<!--List of Files-->
	<ul> <%
		Class.forName("com.mysql.jdbc.Driver");
		Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
		Statement stmt=cn.createStatement();
		ResultSet rs=stmt.executeQuery("select * from list");
		while (rs.next()) {
			String filename = rs.getString("file_name"); %>
			<li><a href="note.jsp?noteName=<%out.println(filename);%>"><%out.println(filename);%></a></li> <%
		} %>
	</ul>

</body>

</html>