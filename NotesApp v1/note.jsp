<%@page language="java"%>
<%@page import="java.sql.*,java.io.*"%>

<html>

<head>
	<title>Notes</title>
</head>

<body> <%

	// Getting file name and content
	String str = request.getParameter("noteName"), content = "";
	FileInputStream in = new FileInputStream("../webapps/NotesApp/notes/"+str+".txt");
	int c;
	while ((c = in.read()) != -1) content += (char)c;
	if (in != null) in.close(); %>

	<!--Heading-->
	<h1><%out.println(str);%></h1>
	<div style="padding:10px">

	<!--Content and Save-->
	<form action="noteServ" method="post">
		<input type="hidden" value=<%out.println(str);%> name="newName"/>
		<textarea name="content" cols="50" rows="10"><%out.println(content);%></textarea>
		<br/><br/>
		<input type="submit" name="button2" value="Save" />
	</form>

	<!--Delete-->
	<form action="noteServ" method="post">
		<input type="hidden" value=<%out.println(str);%> name="newName"/>
		<input type="submit" name="button3" value="Delete" />
	</form>

</body>

</html>