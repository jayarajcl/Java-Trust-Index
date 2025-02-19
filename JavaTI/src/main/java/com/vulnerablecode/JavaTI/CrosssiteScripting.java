package com.vulnerablecode.JavaTI;

public class CrosssiteScripting {
	
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
has context menu

}
