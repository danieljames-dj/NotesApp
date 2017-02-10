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
			String str = request.getParameter("newName");
			if (str != "") {
				File f = new File("../webapps/NotesApp/notes/"+str+".txt");
				f.getParentFile().mkdirs(); 
				boolean fvar = f.createNewFile();
				if (fvar) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
						PreparedStatement ps=cn.prepareStatement("insert into list values('"+str+"')");
						ps.executeUpdate();
						response.sendRedirect("./note.jsp?noteName="+str);
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
			response.sendRedirect("./index.jsp");

		} else if (request.getParameter("button3") != null) {
			
			//Delete File
			PrintWriter out = response.getWriter();
			String str = request.getParameter("newName");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection cn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/notes","dany","emmaus");
				PreparedStatement ps=cn.prepareStatement("delete from list where file_name='"+str+"';");
				ps.executeUpdate();
				File f = new File("../webapps/NotesApp/notes/"+str+".txt");
				f.delete();
				response.sendRedirect("./index.jsp");
			} catch (Exception ClassNotFoundException) { }

		}
	}
}