import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.Message;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class Main extends HttpServlet {
	
	public static final String ACCOUNT_SID = "PNd724ed33f48ad194e9ec4b67ce9ca55b";
	public static final String AUTH_TOKEN = "";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (req.getRequestURI().endsWith("/db")) {
			showDatabase(req, resp);
		} else {
			showHome(req, resp);
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String responseMsg;
		String textMessage = request.getParameter("Body");
		
		Wikipedia wiki = new Wikipedia();
		TwiMLResponse twiml = new TwiMLResponse();
		
		responseMsg = wiki.getSummary(textMessage,1);
		
		Message message = new Message(responseMsg);
		
		try {
			twiml.append(message);
		} catch (TwiMLException e) {
			e.printStackTrace();
		}

		response.setContentType("application/xml");
		response.getWriter().print(twiml.toXML());
	}

	private void showHome(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.getWriter().print("Hi DeltaHacks 2");
	}

	private void showDatabase(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Connection connection = null;
		try {
			connection = getConnection();

			Statement stmt = connection.createStatement();
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
			stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
			ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

			String out = "Hello!\n";
			while (rs.next()) {
				out += "Read from DB: " + rs.getTimestamp("tick") + "\n";
			}

			resp.getWriter().print(out);
		} catch (Exception e) {
			resp.getWriter().print("There was an error: " + e.getMessage());
		} finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
				}
		}
	}

	private Connection getConnection() throws URISyntaxException, SQLException {
		URI dbUri = new URI(System.getenv("DATABASE_URL"));

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		int port = dbUri.getPort();

		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port
				+ dbUri.getPath();

		return DriverManager.getConnection(dbUrl, username, password);
	}

	public static void main(String[] args) throws Exception {
		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
		ServletContextHandler context = new ServletContextHandler(
				ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new Main()), "/*");
		server.start();
		server.join();
	}
}
