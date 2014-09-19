package de.mabe.mediastationhelper.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class ExifTool {
	public static final Boolean DEBUG = false;
	public static final String EXIF_TOOL_PATH = System.getProperty( "exiftool.path", "exiftool" );

	protected static final Pattern TAG_VALUE_PATTERN = Pattern.compile( ": " );

	public enum Tag {
		FILE_SIZE( "File Size" ),
		FILE_TYPE( "File Type" ),
		IMAGE_SIZE( "Image Size" ),
		DURATION( "Duration" ),
		AUDIO_ENCODING( "Audio Encoding", "Audio Format" ),
		AUDIO_CHANNELS( "Audio Channels" );

		private final String[] names;

		private Tag( String... names ) {
			this.names = names;
		}

		public static Tag forName( String name ) {
			for ( Tag tag : Tag.values() ) {
				for ( String tagName : tag.names ) {
					if ( tagName.equals( name ) ) { return tag; }
				}
			}
			return null;
		}
	}

	public static Map<Tag, String> getMeta( File image ) {
		// ***** validate
		if ( image == null ) throw new IllegalArgumentException( "image cannot be null and must be a valid stream of image data." );
		if ( !image.canRead() ) throw new SecurityException( "Unable to read the given image [" + image.getAbsolutePath() + "], ensure that the image exists at the given path and that the executing Java process has permissions to read it." );

		// ***** init values
		Map<Tag, String> resultMap = new HashMap<Tag, String>();
		List<String> args = new ArrayList<String>();
		IOStream streams = null;

		// ***** create command line
		args.clear();
		args.add( EXIF_TOOL_PATH ); // path to exiftool
		// args.add( "-n" ); // numeric output
		args.add( image.getAbsolutePath() ); // filename

		// ***** main process
		try {
			Process proc = new ProcessBuilder( args ).start();
			streams = new IOStream( new BufferedReader( new InputStreamReader( proc.getInputStream() ) ), new OutputStreamWriter( proc.getOutputStream() ) );

			String line = null;
			while ( (line = streams.reader.readLine()) != null ) {
				if ( DEBUG ) System.out.println( "> " + line );
				String[] pair = TAG_VALUE_PATTERN.split( line );

				if ( pair != null && pair.length == 2 ) {
					Tag tag = Tag.forName( pair[0].trim() );

					if ( tag != null ) {
						if ( resultMap.containsKey( tag ) ) {
							String value = resultMap.get( tag );
							resultMap.put( tag, value + ", " + pair[1] );
						} else {
							resultMap.put( tag, pair[1] );
						}
					}
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( streams != null ) {
				streams.close();
			}
		}

		return resultMap;
	}

	private static class IOStream {
		BufferedReader reader;
		OutputStreamWriter writer;

		public IOStream( BufferedReader reader, OutputStreamWriter writer ) {
			this.reader = reader;
			this.writer = writer;
		}

		public void close() {
			try {
				reader.close();
			} catch ( Exception e ) {
			}

			try {
				writer.close();
			} catch ( Exception e ) {
			}

			reader = null;
			writer = null;
		}
	}
}