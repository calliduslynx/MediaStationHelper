package de.mabe.mediastationhelper;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Set;

import de.mabe.mediastationhelper.model.Category;
import de.mabe.mediastationhelper.model.CategoryFactory;
import de.mabe.mediastationhelper.util.FileUtil;

public class MainController {
	private static final String LOG_MOVIE = "de.mabe.mediastationhelper.sourcefolder";
	private final File propertyFile;

	public MainController( String propertyFileName ) throws Exception {
		propertyFile = new File( propertyFileName );
		FileUtil.checkExist( propertyFile );
	}

	public void start() throws Exception {
		// ***** read property file
		Properties properties = new Properties();
		properties.load( new FileInputStream( propertyFile ) );

		// ***** load categories from properties
		Set<Category> categories = CategoryFactory.loadCategories( properties );

		// ***** handle each category
		for ( Category category : categories ) {
			System.out.println( "=== starting: " + category.getName() );
			category.prepareTargetFolder();

			// category.logMovieInformation();

			category.createSymbolicLinks();

			category.lockTargetFolders();
		}
	}

}
