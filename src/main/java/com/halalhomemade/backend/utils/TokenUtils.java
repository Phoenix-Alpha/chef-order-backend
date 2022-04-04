package com.halalhomemade.backend.utils;

import java.util.Random;

public class TokenUtils {

	/** The allowed characters */
	private static final char[] ALLOWED_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

	/** The random value generator instance */
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	private TokenUtils() {
		// Its a utility class. Instantiation is not allowed.
	}

	/**
	 * Generates a fresh token code with the specified length
	 *
	 * @param length The length
	 * @param withSplitter If the token would contain the splitter
	 * @return The token code
	 */
	public static String generateToken(int length, boolean withSplitter) {
		StringBuilder codeBuilder = new StringBuilder();
		for (int i = 0; i < length; i++) {
			codeBuilder.append(ALLOWED_CHARS[RANDOM.nextInt(32)]);
		}
		if (withSplitter) {
			codeBuilder.insert(length / 2, "-");
		}
		return codeBuilder.toString();
	}
}
