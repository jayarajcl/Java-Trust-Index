package com.vulnerablecode.JavaTI;

public class ImproperNutralisation {
	
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

}
