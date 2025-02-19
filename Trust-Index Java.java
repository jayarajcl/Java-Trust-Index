//vulnerable ---------78
private String ping(String host) {
    String output = "";
    Process proc;

    logger.info("Pinging: " + host);

    try {

        proc = Runtime.getRuntime().exec(new String[] { "bash", "-c", "ping -c1 " + host });


        proc.waitFor(5, TimeUnit.SECONDS);
        InputStreamReader isr = new InputStreamReader(proc.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        String line;

        while ((line = br.readLine()) != null) {
            output += line + "\n";
        }

        logger.info(proc.exitValue());
    } catch (IOException ex) {
        logger.error(ex);
    } catch (InterruptedException ex) {
        logger.error(ex);
    }

    return output;
}

//vulnerable ----798
private static User[] users = new User[] {
    User.create("admin", "admin", "Thats Mr Administrator to you."),
    User.create("john", "John", "John Smith"),
    User.create("paul", "Paul", "Paul Farrington"),
    User.create("chrisc", "Chris", "Chris Campbell"),
    User.create("laurie", "Laurie", "Laurie Mercer"),
    User.create("nabil", "Nabil", "Nabil Bousselham"),
    User.create("julian", "Julian", "Julian Totzek-Hallhuber"),
    User.create("joash", "Joash", "Joash Herbrink"),
    User.create("andrzej", "Andrzej", "Andrzej Szaryk"),
    User.create("april", "April", "April Sauer"),
    User.create("armando", "Armando", "Armando Bioc"),
    User.create("ben", "Ben", "Ben Stoll"),
    User.create("brian", "Brian", "Brian Pitta"),
    User.create("caitlin", "Caitlin", "Caitlin Johanson"),
    User.create("christraut", "Chris Trautwein", "Chris Trautwein"),
    User.create("christyson", "Chris Tyson", "Chris Tyson"),
    User.create("clint", "Clint", "Clint Pollock"),
    User.create("cody", "Cody", "Cody Bertram"),
    User.create("derek", "Derek", "Derek Chowaniec"),
    User.create("glenn", "Glenn", "Glenn Whittemore"),
    User.create("grant", "Grant", "Grant Robinson"),
    User.create("gregory", "Gregory", "Gregory Wolford"),
    User.create("jacob", "Jacob", "Jacob Martel"),
    User.create("jeremy", "Jeremy", "Jeremy Anderson"),
    User.create("johnny", "Johnny", "Johnny Wong"),
    User.create("kevin", "Kevin", "Kevin Rise"),
    User.create("scottrum", "Scott Rumrill", "Scott Rumrill"),
    User.create("scottsim", "Scott Simpson", "Scott Simpson") };


//vulnerable ----259
public class Constants {
	private final String JDBC_DRIVER = "mysql";
	private final String JDBC_HOSTNAME = "localhost";
	private final String JDBC_PORT = "3306";
	private final String JDBC_DATABASE = "blab";
	private final String JDBC_USER = "blab";
	/* START EXAMPLE VULNERABILITY */
	private final String JDBC_PASSWORD = "z2^E6J4$;u;d";
	/* END EXAMPLE VULNERABILITY */

	private String hostname;
	private String port;
	private String dbname;
	private String username;
	private String password;

	/**
	 * Pull info from the system as an override, otherwise fall back to hardcoded values.
	 * Environment variables are automatically set in AWS environments.
	 */
	public Constants() {
		String dbnameProp = System.getenv("RDS_DB_NAME");
		this.dbname = (dbnameProp == null) ? JDBC_DATABASE : dbnameProp;
		
		String hostnameProp = System.getenv("RDS_HOSTNAME");
		this.hostname = (hostnameProp == null) ? JDBC_HOSTNAME : hostnameProp;
		
		String portProp = System.getenv("RDS_PORT");
		this.port = (portProp == null) ? JDBC_PORT : portProp;
		
		String userProp = System.getenv("RDS_USERNAME");
		this.username = (userProp == null) ? JDBC_USER : userProp;
		
		String passwordProp = System.getenv("RDS_PASSWORD");
		this.password = (passwordProp == null) ? JDBC_PASSWORD : passwordProp;
	}

	public static final Constants create() {
		return new Constants();
	}

	public final String getJdbcConnectionString() {
		String connection = null;
		try {
			connection = String.format(
					"jdbc:%s://%s:%s/%s?user=%s&password=%s",
					JDBC_DRIVER,
					hostname,
					port,
					dbname,
					URLEncoder.encode(username, "UTF-8"),
					URLEncoder.encode(password, "UTF-8")
			);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return connection;
	}
}

//vulnerable ----80
public String showPasswordHint(String username) {
    logger.info("Entering password-hint with username: " + username);

    if (username == null || username.isEmpty()) {
        return "No username provided, please type in your username first";
    }

    try {
        Class.forName("com.mysql.jdbc.Driver");

        Connection connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

        String sql = "SELECT password_hint FROM users WHERE username = '" + username + "'";
        logger.info(sql);
        Statement statement = connect.createStatement();
        ResultSet result = statement.executeQuery(sql);
        if (result.first()) {
            String password = result.getString("password_hint");
            String formatString = "Username '" + username + "' has password: %.2s%s";
            logger.info(formatString);
            return String.format(
                    formatString,
                    password,
                    String.format("%0" + (password.length() - 2) + "d", 0).replace("0", "*"));
        } else {
            return "No password found for " + username;
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return "ERROR!";
}

//vulnerable ----327
private static String md5(String val) {
    MessageDigest md;
    String ret = null;
    try {
        md = MessageDigest.getInstance("MD5");
        md.update(val.getBytes());
        byte[] digest = md.digest();
        ret = DatatypeConverter.printHexBinary(digest);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    return ret;
}


//vulnerable ---- 73
public String downloadImage(
			@RequestParam(value = "image", required = true) String imageName,
			HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("Entering downloadImage");

		// Ensure user is logged in
		String sessionUsername = (String) request.getSession().getAttribute("username");
		if (sessionUsername == null) {
			logger.info("User is not Logged In - redirecting...");
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return Utils.redirect("login?target=profile");
		}

		logger.info("User is Logged In - continuing... UA=" + request.getHeader("User-Agent") + " U=" + sessionUsername);

		String path = context.getRealPath("/resources/images") + File.separator + imageName;

		logger.info("Fetching profile image: " + path);

		InputStream inputStream = null;
		OutputStream outStream = null;
		try {
			File downloadFile = new File(path);
			inputStream = new FileInputStream(downloadFile);

			// get MIME type of the file
			String mimeType = context.getMimeType(path);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			logger.info("MIME type: " + mimeType);

			// Set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());
			response.setHeader("Content-Disposition", "attachment; filename=" + imageName);

			// get output stream of the response
			outStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			outStream.flush();
		} catch (IllegalStateException | IOException ex) {
			logger.error(ex);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}
			try {
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}
		}

		return "profile";
	}
//vulnerable ---- 89
public String showProfile(
			@RequestParam(value = "type", required = false) String type,
			Model model,
			HttpServletRequest httpRequest) {
		logger.info("Entering showProfile");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=profile");
		}

		Connection connect = null;
		PreparedStatement myHecklers = null, myInfo = null;
		String sqlMyHecklers = "SELECT users.username, users.blab_name, users.created_at "
				+ "FROM users LEFT JOIN listeners ON users.username = listeners.listener "
				+ "WHERE listeners.blabber=? AND listeners.status='Active';";

		try {
			logger.info("Getting Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			// Find the Blabbers that this user listens to
			logger.info(sqlMyHecklers);
			myHecklers = connect.prepareStatement(sqlMyHecklers);
			myHecklers.setString(1, username);
			ResultSet myHecklersResults = myHecklers.executeQuery();

			List<Blabber> hecklers = new ArrayList<Blabber>();
			while (myHecklersResults.next()) {
				Blabber heckler = new Blabber();
				heckler.setUsername(myHecklersResults.getString(1));
				heckler.setBlabName(myHecklersResults.getString(2));
				heckler.setCreatedDate(myHecklersResults.getDate(3));
				hecklers.add(heckler);
			}

			// Get the audit trail for this user
			ArrayList<String> events = new ArrayList<String>();

			/* START EXAMPLE VULNERABILITY */
			String sqlMyEvents = "select event from users_history where blabber=\"" + username
					+ "\" ORDER BY eventid DESC; ";
			logger.info(sqlMyEvents);
			Statement sqlStatement = connect.createStatement();
			ResultSet userHistoryResult = sqlStatement.executeQuery(sqlMyEvents);
			/* END EXAMPLE VULNERABILITY */

			while (userHistoryResult.next()) {
				events.add(userHistoryResult.getString(1));
			}

			// Get the users information
			String sql = "SELECT username, real_name, blab_name FROM users WHERE username = '" + username + "'";
			logger.info(sql);
			myInfo = connect.prepareStatement(sql);
			ResultSet myInfoResults = myInfo.executeQuery();
			myInfoResults.next();

			// Send these values to our View
			model.addAttribute("hecklers", hecklers);
			model.addAttribute("events", events);
			model.addAttribute("username", myInfoResults.getString("username"));
			model.addAttribute("image", getProfileImageNameFromUsername(myInfoResults.getString("username")));
			model.addAttribute("realName", myInfoResults.getString("real_name"));
			model.addAttribute("blabName", myInfoResults.getString("blab_name"));
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (myHecklers != null) {
					myHecklers.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}

		return "profile";
	}

    //vulnerable ---- 209


    public String showFeed(
			@RequestParam(value = "type", required = false) String type,
			Model model,
			HttpServletRequest httpRequest) {
		logger.info("Entering showFeed");

		String username = (String) httpRequest.getSession().getAttribute("username");
		// Ensure user is logged in
		if (username == null) {
			logger.info("User is not Logged In - redirecting...");
			return Utils.redirect("login?target=profile");
		}

		logger.info("User is Logged In - continuing... UA=" + httpRequest.getHeader("User-Agent") + " U=" + username);

		Connection connect = null;
		PreparedStatement blabsByMe = null;
		PreparedStatement blabsForMe = null;

		try {
			logger.info("Getting Database connection");
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection(Constants.create().getJdbcConnectionString());

			// Find the Blabs that this user listens to
			logger.info("Preparing the BlabsForMe Prepared Statement");
			blabsForMe = connect.prepareStatement(String.format(sqlBlabsForMe, 10, 0));
			blabsForMe.setString(1, username);
			logger.info("Executing the BlabsForMe Prepared Statement");
			ResultSet blabsForMeResults = blabsForMe.executeQuery();

			// Store them in the Model
			List<Blab> feedBlabs = new ArrayList<Blab>();
			while (blabsForMeResults.next()) {
				Blabber author = new Blabber();
				author.setUsername(blabsForMeResults.getString(1));
				author.setBlabName(blabsForMeResults.getString(2));

				Blab post = new Blab();
				post.setId(blabsForMeResults.getInt(6));
				post.setContent(blabsForMeResults.getString(3));
				post.setPostDate(blabsForMeResults.getDate(4));
				post.setCommentCount(blabsForMeResults.getInt(5));
				post.setAuthor(author);

				feedBlabs.add(post);
			}
			model.addAttribute("blabsByOthers", feedBlabs);
			model.addAttribute("currentUser", username);

			// Find the Blabs by this user
			logger.info("Preparing the BlabsByMe Prepared Statement");
			blabsByMe = connect.prepareStatement(sqlBlabsByMe);
			blabsByMe.setString(1, username);
			logger.info("Executing the BlabsByMe Prepared Statement");
			ResultSet blabsByMeResults = blabsByMe.executeQuery();

			// Store them in the model
			List<Blab> myBlabs = new ArrayList<Blab>();
			while (blabsByMeResults.next()) {
				Blab post = new Blab();
				post.setId(blabsByMeResults.getInt(4));
				post.setContent(blabsByMeResults.getString(1));
				post.setPostDate(blabsByMeResults.getDate(2));
				post.setCommentCount(blabsByMeResults.getInt(3));

				myBlabs.add(post);
			}
			model.addAttribute("blabsByMe", myBlabs);
		} catch (SQLException | ClassNotFoundException ex) {
			logger.error(ex);
		} finally {
			try {
				if (blabsByMe != null) {
					blabsByMe.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (blabsForMe != null) {
					blabsForMe.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
			try {
				if (connect != null) {
					connect.close();
				}
			} catch (SQLException exceptSql) {
				logger.error(exceptSql);
			}
		}

		return "feed";
	}

    //vulnerable code -- 352

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get parameters from request
        String fromAccount = request.getParameter("fromAccount");
        String toAccount = request.getParameter("toAccount");
        double amount = Double.parseDouble(request.getParameter("amount"));
 
        // Transfer money
        Bank.transfer(fromAccount, toAccount, amount);
 
        response.sendRedirect("/success");
    }
}

//Vulnerable code -- 611

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import org.w3c.dom.NodeList;

import java.io.File;
 
public class VulnerableXMLProcessor {
 
    public static void main(String[] args) throws Exception {

        String uploadedFilePath = "/path/to/uploaded/file.xml"; // User-supplied path
 
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();
 
        // Assuming 'uploadedFilePath' is the path provided by the user

        File xmlFile = new File(uploadedFilePath);

        Document document = builder.parse(xmlFile);
 
        NodeList nodeList = document.getElementsByTagName("data");

        // Process the data...
 
        System.out.println("XML processing completed.");

    }

}
 
////Vulnerable code -- 601
@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLogin(
			@RequestParam(value = "target", required = false) String target,
			@RequestParam(value = "username", required = false) String username,
			Model model,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {
		// Check if user is already logged in
		if (httpRequest.getSession().getAttribute("username") != null) {
			logger.info("User is already logged in - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return Utils.redirect("feed");
			}
		}

		User user = UserFactory.createFromRequest(httpRequest);
		if (user != null) {
			Utils.setSessionUserName(httpRequest, httpResponse, user.getUserName());
			logger.info("User is remembered - redirecting...");
			if (target != null && !target.isEmpty() && !target.equals("null")) {
				return "redirect:" + target;
			} else {
				// default to user's feed
				return Utils.redirect("feed");
			}
		} else {
			logger.info("User is not remembered");
		}

		if (username == null) {
			username = "";
		}

		if (target == null) {
			target = "";
		}

		logger.info("Entering showLogin with username " + username + " and target " + target);

		model.addAttribute("username", username);
		model.addAttribute("target", target);
		return "login";
	}


