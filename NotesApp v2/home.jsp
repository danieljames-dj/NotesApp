<%@page language="java"%>
<%@page import="java.sql.*,java.io.File"%>

<html>

<head>
	<title>Notes</title>
</head>

<body>

	<!--Heading-->
	<h1>List of Files</h1> <%
		String userID = request.getParameter("userID"); %>

	<!--New File Form-->
	<form action="noteServ" method="post">
		<input type="hidden" value=<%out.println(userID);%> name="userID"/>
		<input type="text" name="newName"/>
		<input type="submit" name="button1" value="New File" />
	</form>

	<!--List of Files-->
	<h3>Own Files</h3>
	<ul> <%
		Class.forName("com.mysql.jdbc.Driver");
		Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
		Statement stmt=cn.createStatement();
		ResultSet rs=stmt.executeQuery("select * from notes inner join access where userID="+userID+" and notes.noteID=access.noteID and owner=1");
		if (rs.next()) {
			do {
				String filename = rs.getString("noteName"); %>
				<li><a href="note.jsp?noteName=<%out.println(filename);%>&userID=<%out.println(userID);%>"><%out.println(filename);%></a></li> <%
			} while (rs.next());
		} else{
			%>No files to display<%
		}%>
	</ul>
	<h3>Shared Files</h3>
	<ul> <%
		rs=stmt.executeQuery("select * from notes inner join access where userID="+userID+" and notes.noteID=access.noteID and owner=0");
		if (rs.next()) {
			do {
				String filename = rs.getString("noteName"); %>
				<li><a href="note.jsp?noteName=<%out.println(filename);%>&userID=<%out.println(userID);%>"><%out.println(filename);%></a></li> <%
			} while (rs.next());
		} else{
			%>No files to display<%
		}%>
	</ul>

</body>

</html>