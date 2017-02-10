import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class HelloServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("button1") != null) {

			//New File
			PrintWriter out = response.getWriter();
			String userID = request.getParameter("userID"), str = request.getParameter("newName");
			if (str != "") {
				File f = new File("../webapps/NotesApp/notes/"+str+".txt");
				f.getParentFile().mkdirs(); 
				boolean fvar = f.createNewFile();
				if (fvar) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
						Statement stmt=cn.createStatement();
						ResultSet rs=stmt.executeQuery("select max(noteID) from notes");
						rs.next();
						PreparedStatement ps=cn.prepareStatement("insert into notes values ("+String.valueOf(rs.getInt("max(noteID)")+1) + ",'"+str+"')");
						ps.executeUpdate();
						ps=cn.prepareStatement("insert into access values ("+ userID + "," + String.valueOf(rs.getInt("max(noteID)")+1) + ",true)");
						ps.executeUpdate();
						response.sendRedirect("./note.jsp?noteName="+str+"&userID="+userID);
					} catch (Exception ClassNotFoundException) { }
				} else {
					out.println("Error: File already exists.");
				}
			} else {
				out.println("Error: Please enter a name for the file.");
			}

		} else if (request.getParameter("button2") != null) {

			//Update File
			PrintWriter out = response.getWriter();
			String str = request.getParameter("newName");
			FileOutputStream fout = new FileOutputStream("../webapps/NotesApp/notes/"+str+".txt");
			String content = request.getParameter("content");
			byte b[]=content.getBytes();
			fout.write(b);
			fout.close();
			response.sendRedirect("./home.jsp?userID="+request.getParameter("userID"));

		} else if (request.getParameter("button3") != null) {
			
			//Delete File
			PrintWriter out = response.getWriter();
			String str = request.getParameter("newName"), noteID;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("select noteID from notes where noteName = '"+str+"'");
				rs.next();
				noteID = String.valueOf(rs.getInt("noteID"));
				PreparedStatement ps=cn.prepareStatement("delete from access where noteID="+noteID+";");
				ps.executeUpdate();
				ps=cn.prepareStatement("delete from notes where noteID="+noteID+";");
				ps.executeUpdate();
				File f = new File("../webapps/NotesApp/notes/"+str+".txt");
				f.delete();
				response.sendRedirect("./home.jsp?userID="+request.getParameter("userID"));
			} catch (Exception ClassNotFoundException) { }

		} else if (request.getParameter("share") != null) {
			
			//Share File
			PrintWriter out = response.getWriter();
			String str = request.getParameter("newName"), noteID, email = request.getParameter("email");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("select noteID from notes where noteName = '"+str+"'");
				rs.next();
				noteID = String.valueOf(rs.getInt("noteID"));
				rs=stmt.executeQuery("select userID from users where email = '"+email+"'");
				if ( rs.next() ) {
					PreparedStatement ps = cn.prepareStatement("insert into access values ("+String.valueOf(rs.getInt("userID"))+","+noteID+",0)");
					ps.executeUpdate();
					out.println("Shared...");
				} else {
					out.println("User doesn't exist...");
				}
			} catch (Exception ClassNotFoundException) { }

		} else if (request.getParameter("login") != null) {

			//Login
			String email = request.getParameter("email"), password = request.getParameter("password");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("select userID,password from users where email='"+email+"';");
				if (rs.next() && rs.getString("password").equals(password)) {
					response.sendRedirect("./home.jsp?userID="+String.valueOf(rs.getInt("userID")));
				} else {
					PrintWriter out = response.getWriter();
					out.println("Wrong email/password");
				}
			} catch (Exception ClassNotFoundException) { }

		} else if (request.getParameter("register") != null) {

			//Register
			String name =request.getParameter("name"), email = request.getParameter("email"), password = request.getParameter("password");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("select max(userID) from users");
				rs.next();
				PreparedStatement ps=cn.prepareStatement("insert into users values ("+String.valueOf(rs.getInt("max(userID)")+1) + ",'"+name+"','"+email+"','"+password+"')");
				ps.executeUpdate();
				response.sendRedirect("./index.jsp");
			} catch (Exception ClassNotFoundException) { }

		}
	}
}