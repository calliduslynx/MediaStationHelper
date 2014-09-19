package de.mabe.mediastationhelper.model;

import static de.mabe.mediastationhelper.util.ExifTool.Tag.AUDIO_CHANNELS;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.AUDIO_ENCODING;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.DURATION;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.FILE_SIZE;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.FILE_TYPE;
import static de.mabe.mediastationhelper.util.ExifTool.Tag.IMAGE_SIZE;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.mabe.mediastationhelper.util.ExifTool;
import de.mabe.mediastationhelper.util.ExifTool.Tag;
import de.mabe.mediastationhelper.util.FileUtil;

public class Category {
    private final List<File> sourceFolders = new ArrayList<File>();
    private final String name;
    private File targetFolder;

    // *****************************************************************
    // ***** plain getter/setter
    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setTargetFolder(File targetFolder) {
        this.targetFolder = targetFolder;
    }

    public void addSourceFolders(File file) {
        sourceFolders.add(file);
    }

    // *****************************************************************
    // ***** business logic
    public void lockSourceFolders() {
        for (File source : sourceFolders) {
            FileUtil.lock(source);
        }
    }

    public void prepareTargetFolder() {
        FileUtil.unlock(targetFolder);
        FileUtil.remove(targetFolder);
        FileUtil.createFolder(targetFolder);
    }

    public void unlockSourceFolders() {
        for (File source : sourceFolders) {
            FileUtil.unlock(source);
        }
    }

    public void createSymbolicLinks() {
        for (File source : sourceFolders) {
            for (File file : source.listFiles()) {
                FileUtil.createSymbolicLink(file, targetFolder);
            }
        }
    }

    public void lockTargetFolders() {
        FileUtil.lock(targetFolder);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" ===[" + name + "]===\n");
        buffer.append(" target: " + targetFolder + "\n");
        buffer.append(" source: ");
        for (File f : sourceFolders) {
            buffer.append(f.getAbsolutePath() + "\n         ");
        }
        return buffer.toString();
    }

    public void logMovieInformation() {
        for (File source : sourceFolders) {
            for (File file : source.listFiles()) {
                if (file.isFile()) {
                    Map<Tag, String> meta = ExifTool.getMeta(file);
                    System.out.println(
                            "[MOVIE] "
                                    + file.getName() + " ; "
                                    + meta.get(FILE_SIZE) + " ; "
                                    + meta.get(FILE_TYPE) + " ; "
                                    + meta.get(IMAGE_SIZE) + " ; "
                                    + meta.get(DURATION) + " ; "
                                    + meta.get(AUDIO_ENCODING) + " ; "
                                    + meta.get(AUDIO_CHANNELS)
                            );
                }
            }
        }
    }
}
