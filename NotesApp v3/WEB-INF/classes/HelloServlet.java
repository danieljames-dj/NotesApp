import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.regex.*;
import java.nio.file.*;

public class HelloServlet extends HttpServlet {

	String checkPassword(String email, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
			Statement stmt=cn.createStatement();
			ResultSet rs=stmt.executeQuery("select userID,password from users where email='"+email+"';");
			if (rs.next() && rs.getString("password").equals(password)) {
				return ""+String.valueOf(rs.getInt("userID"));
			} else {
				return "false";
			}
		} catch (Exception ClassNotFoundException) { }
		return "false";
	}

	boolean emailExists(String email) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
			Statement stmt=cn.createStatement();
			ResultSet rs=stmt.executeQuery("select email from users where email='"+email+"';");
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ClassNotFoundException) { }
		return true;
	}

	void createUser(String name, String email, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
			Statement stmt=cn.createStatement();
			ResultSet rs=stmt.executeQuery("select max(userID) from users");
			rs.next();
			PreparedStatement ps=cn.prepareStatement("insert into users values ("+String.valueOf(rs.getInt("max(userID)")+1) + ",'"+name+"','"+email+"','"+password+"')");
			ps.executeUpdate();
		} catch (Exception ClassNotFoundException) { }
	}

	String getNotes(String userID) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
			Statement stmt=cn.createStatement();
			ResultSet rs=stmt.executeQuery("select * from notes where ownerID="+userID);
			String result = "[ ";
			while (rs.next()) {
				result += "{ " + "\"noteID\":\"" + String.valueOf(rs.getInt("noteID")) + "\", \"noteName\":\"" + rs.getString("noteName") + "\", \"mode\":\"4\"},";
			}
			rs=stmt.executeQuery("select sharing.noteID,notes.noteName,sharing.permission from notes inner join sharing where sharing.noteID=notes.noteID and sharing.userID = "+userID);
			while (rs.next()) {
				result += "{ " + "\"noteID\":\"" + String.valueOf(rs.getInt("noteID")) + "\", \"noteName\":\"" + rs.getString("noteName") + "\", \"mode\":\""+ String.valueOf(rs.getInt("permission")) +"\"},";
			}
			result = result.substring(0,result.length()-1) + "];";
			return result;
		} catch (Exception ClassNotFoundException) { }
		return "";
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		if (request.getParameter("checkLogin") != null) {

			//CHECK LOGIN
			String email = request.getParameter("email"), password = request.getParameter("password");
			response.getWriter().write(""+checkPassword(email, password));

		} else if (request.getParameter("checkReg") != null) {

			//CHECK REGISTER
			String name = request.getParameter("name"), email = request.getParameter("email"),
			password = request.getParameter("password"), password1 = request.getParameter("password1");
			if (emailExists(email)) {
				response.getWriter().write("3");
			} else {
				createUser(name, email, password);
				response.getWriter().write("6");
			}

		} else if (request.getParameter("getNotes") != null) {

			//GET NOTES
			String userID = request.getParameter("userID");
			response.getWriter().write(getNotes(userID));

		} else if (request.getParameter("createNote") != null) {

			//CREATE NOTE
			String userID = request.getParameter("userID");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("select max(noteID) from notes");
				rs.next();
				int noteID = rs.getInt("max(noteID)")+1;
				response.getWriter().write(String.valueOf(noteID));
				File f = new File("../webapps/NotesApp/notes/"+String.valueOf(noteID)+".txt");
				FileOutputStream fop = null;
				f.getParentFile().mkdirs(); 
				f.createNewFile();
				fop = new FileOutputStream(f);
				fop.write(content.getBytes());
				fop.flush();
				fop.close();
				PreparedStatement ps=cn.prepareStatement("insert into notes values (" + String.valueOf(noteID) + ",'" +title + "'," + String.valueOf(userID) + ")");
				ps.executeUpdate();
			} catch (Exception ClassNotFoundException) { }

		} else if (request.getParameter("readNote") != null) {

			//READ NOTE
			String noteID = request.getParameter("noteID");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("select noteName from notes where noteID="+noteID);
				rs.next();
				response.getWriter().write(rs.getString("noteName")+"\n");
				byte[] encoded = new byte[0];
				encoded = Files.readAllBytes(Paths.get("../webapps/NotesApp/notes/"+noteID+".txt"));
				response.getWriter().write(new String(encoded));
			} catch (Exception ClassNotFoundException) { }

		} else if (request.getParameter("updateNote") != null) {

			//UPDATE NOTE
			String noteID = request.getParameter("noteID");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			FileOutputStream fout = new FileOutputStream("../webapps/NotesApp/notes/"+noteID+".txt");
			byte b[]=content.getBytes();
			fout.write(b);
			fout.close();

		} else if (request.getParameter("deleteNote") != null) {

			//DELETE NOTE
			String noteID = request.getParameter("noteID");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				PreparedStatement ps=cn.prepareStatement("delete from sharing where noteID="+noteID+";");
				ps.executeUpdate();
				ps=cn.prepareStatement("delete from notes where noteID="+noteID+";");
				ps.executeUpdate();
				File f = new File("../webapps/NotesApp/notes/"+noteID+".txt");
				f.delete();
			} catch (Exception ClassNotFoundException) { }

		} else if (request.getParameter("share") != null) {

			//SHARE NOTE
			String noteID = request.getParameter("noteID"), email = request.getParameter("email"), mode = request.getParameter("mode");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				Statement stmt=cn.createStatement();
				ResultSet rs=stmt.executeQuery("select userID from users where email = '" + email + "'");
				if (rs.next()) {
					int userID = rs.getInt("userID");
					rs=stmt.executeQuery("select * from sharing where userID = " + String.valueOf(userID));
					if (rs.next()) {
						PreparedStatement ps=cn.prepareStatement("delete from sharing where userID="+String.valueOf(userID)+" AND noteID="+noteID);
						ps.executeUpdate();
					}
					PreparedStatement ps=cn.prepareStatement("insert into sharing values ("+noteID+","+String.valueOf(userID)+","+mode+");");
					ps.executeUpdate();
					response.getWriter().write("Success");
				} else {
					response.getWriter().write("Invalid email ID");
				}
			} catch (Exception ClassNotFoundException) { }

		}
	}
}