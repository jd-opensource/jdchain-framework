package com.jd.blockchain.utils;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jd.blockchain.utils.io.ByteArray;
import com.jd.blockchain.utils.io.RuntimeIOException;

public class ConsoleUtils {

	private static volatile ConsoleReader consoleReader;

	public static byte[] readPassword() {
		Console cs = getConsole();
		char[] pwdChars;
		do {
			pwdChars = cs.readPassword("\r\nInput password:");
		} while (pwdChars.length == 0);
		String pwd = new String(pwdChars);
		return ByteArray.fromString(pwd, "UTF-8");
	}

	public static void info(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	public static void error(String format, Object... args) {
		System.err.println(String.format(format, args));
	}

	public static String confirm(String fmt, Object... args) {
		Console cs = getConsole();
		return cs.readLine(fmt, args);
	}

	public static Console getConsole() {
		Console cs = System.console();
		if (cs == null) {
			throw new IllegalStateException("You are not running in console!");
		}
		return cs;
	}

	public static ConsoleReader getReader() {
		if (consoleReader == null) {
			synchronized (ConsoleUtils.class) {
				if (consoleReader == null) {
					Console cs = System.console();
					if (cs == null) {
						consoleReader = new SystemInputReader();
					} else {
						consoleReader = new SystemConsoleReader(cs);
					}
				}
			}
		}
		return consoleReader;
	}

	private static class SystemConsoleReader implements ConsoleReader {

		private Console cs;

		public SystemConsoleReader(Console cs) {
			this.cs = cs;
		}

		@Override
		public String readLine() {
			return cs.readLine();
		}

	}

	private static class SystemInputReader implements ConsoleReader {

		private InputStreamReader reader = new InputStreamReader(System.in);
		private BufferedReader bufReader = new BufferedReader(reader);

		@Override
		public String readLine() {
			try {
				return bufReader.readLine();
			} catch (IOException e) {
				throw new RuntimeIOException(e.getMessage(), e);
			}
		}

	}

}
