package de.mabe.mediastationhelper.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtil {

	public static void lock(File file) {
		execute("chmod", "-R", "555", preparePath(file.getAbsolutePath()));
	}

	public static void unlock(File file) {
		execute("chmod", "-R", "777", preparePath(file.getAbsolutePath()));
	}

	public static void checkExist(File propertyFile) {
		if (!propertyFile.exists()) { throw new RuntimeException("property file " + propertyFile.getAbsolutePath() + " not found"); }
	}

	/***
	 * checks if given folder exists, if not create<br>
	 * throws exception when given folder is a file
	 */
	public static void createFolder(File folder) {
		if (!folder.exists()) {
			execute("mkdir", "-p", preparePath(folder.getAbsolutePath()));
		}
	}

	/***
	 * removes all files and folders from a given folder<br>
	 * throws exception when given folder is not a folder
	 */
	public static void clearFolder(File targetFolder) {
		remove(targetFolder + File.separator + "*");
	}

	public static void remove(File file) {
		remove(file.getAbsolutePath());
	}

	public static void remove(String filename) {
		execute("rm", "-Rf", preparePath(filename));
	}

	/***
	 * creates a symbolic link
	 */
	public static void createSymbolicLink(File sourceFile, File targetFolder) {
		String sourceFilePath = preparePath(sourceFile.getAbsolutePath());
		String targetFilePath = preparePath(targetFolder.getAbsolutePath() + File.separator + sourceFile.getName());

		if (new File(targetFilePath).exists()) {
			System.out.println("[WARN] file " + targetFilePath + " exists already!!");
			return;
		}

		execute("ln", "-s", sourceFilePath, targetFilePath);
	}

	private static String preparePath(String path) {
		return "" + path + "";
	}

	private static void execute(String... cmd) {
		try {
			System.out.println("TRACE cmd: " + createCommand(cmd));

			Process process = Runtime.getRuntime().exec(cmd);
			Reader r = new InputStreamReader(process.getErrorStream());
			BufferedReader in = new BufferedReader(r);
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String createCommand(String[] cmds) {
		String str = "";
		for (String cmd : cmds) {
			str += cmd + " ";
		}
		return str;
	}
}
