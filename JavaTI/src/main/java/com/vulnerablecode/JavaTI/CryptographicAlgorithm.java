package com.vulnerablecode.JavaTI;

public class CryptographicAlgorithm {
	
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
}

}
