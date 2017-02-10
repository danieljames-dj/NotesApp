import java.util.regex.*;

public class javaTest {
	public static void main(String args[]) {
		String email = "sd";
		boolean bool = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$").matcher(email).matches();
		if (bool) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}
}
