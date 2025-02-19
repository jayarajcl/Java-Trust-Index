package com.vulnerablecode.JavaTI;

public class SQLInjection {

	
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
}
