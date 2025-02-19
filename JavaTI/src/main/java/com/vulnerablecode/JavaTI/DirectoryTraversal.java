package com.vulnerablecode.JavaTI;

import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DirectoryTraversal {
	
	
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

}
