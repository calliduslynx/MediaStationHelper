package de.mabe.mediastationhelper.model;

import java.io.File;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class CategoryFactory {
	public static final String SOURCE_FOLDER_CSV = "de.mabe.mediastationhelper.sourcefolder";
	public static final String GROUPS_CSV = "de.mabe.mediastationhelper.groups";
	public static final String TARGET_FOLDER = "de.mabe.mediastationhelper.target";

	public static Set<Category> loadCategories(Properties properties) {
		Set<Category> categories = createCategoryNames(properties);

		for (Category category : categories) {
			setTargetFolder(properties, category);
			setSourceFolders(properties, category);
		}
		for (Category category : categories) {
			System.out.println(category);
		}
		return categories;
	}

	private static Set<Category> createCategoryNames(Properties properties) {
		Set<Category> categories = new HashSet<Category>();

		String categoryNames = properties.getProperty(GROUPS_CSV);

		for (String categoryName : categoryNames.split(";")) {
			categories.add(new Category(categoryName));
		}

		return categories;
	}

	private static void setTargetFolder(Properties properties, Category category) {
		String targetFolderBase = properties.getProperty(TARGET_FOLDER);
		String targetFolder = targetFolderBase + File.separator + category.getName();
		category.setTargetFolder(new File(targetFolder));
	}

	private static void setSourceFolders(Properties properties, Category category) {
		String sourceFolders = properties.getProperty(SOURCE_FOLDER_CSV);

		for (String filename : sourceFolders.split(";")) {
			File file = new File(filename + File.separator + category.getName());
			if (file.exists()) {
				category.addSourceFolders(file);
			}
		}
	}
}
