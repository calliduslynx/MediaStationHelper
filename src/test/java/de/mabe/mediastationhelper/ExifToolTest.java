package de.mabe.mediastationhelper;

import static de.mabe.mediastationhelper.util.ExifTool.Tag.AUDIO_CHANNELS;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.AUDIO_ENCODING;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.DURATION;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.FILE_SIZE;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.FILE_TYPE;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.IMAGE_SIZE;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import de.mabe.mediastationhelper.util.ExifTool;
import de.mabe.mediastationhelper.util.ExifTool.Tag;

public class ExifToolTest {
    @Test
    public void testFiel() throws Exception {
        File f = new File("/home/calliduslynx/Downloads/1.flv");
        File f2 = new File("/home/calliduslynx/Downloads/2.mp4");

        assert f.exists();
        assert f2.exists();

        xx(f);
        xx(f2);

    }

    private void xx(File f) throws IOException {
        Map<Tag, String> imageMeta = ExifTool.getMeta(f);
        System.out.println("===== " + f.getName() + " =====");
        System.out.println("SIZE : " + imageMeta.size());
        System.out.println(imageMeta.get(FILE_SIZE));
        System.out.println(imageMeta.get(FILE_TYPE));
        System.out.println(imageMeta.get(IMAGE_SIZE));
        System.out.println(imageMeta.get(DURATION));
        System.out.println(imageMeta.get(AUDIO_ENCODING));
        System.out.println(imageMeta.get(AUDIO_CHANNELS));
        System.out.println("-----------------");
    }
}
