package de.mabe.mediastationhelper;

public class Main {
	public static void main(String[] args) throws Exception {
		String propertyFileName = args.length == 0 ? "mediastationhelper.properties" : args[0];
		new MainController(propertyFileName).start();
	}
}
