package com.kensai.av.persist;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroturnaround.zip.ZipUtil;


public class DatasZipper {
	private static Logger log = LogManager.getLogger(DatasZipper.class);

	public static final Path DATAS_FOLDER = Paths.get("datas");
	public static final Path CURRENT_FOLDER = Paths.get("datas", "current");
	public static final Path OLD_FOLDER = Paths.get("datas", "old");

	private final Path fromFolder;
	private final Path destZip;

	public DatasZipper() {
		this(CURRENT_FOLDER, OLD_FOLDER, LocalDate.now());
	}

	public DatasZipper(Path fromFolder, Path toFolder) {
		this(fromFolder, toFolder, LocalDate.now());
	}

	public DatasZipper(Path fromFolder, Path toFolder, LocalDate date) {
		this.fromFolder = fromFolder;
		destZip = toFolder.resolve(date.toString() + ".zip");
	}

	public Path save() throws IOException {
		if (!shouldSave()) {
			log.warn("Can not save datas. Reason: already saved [" + destZip + "]");
			return destZip;
		}

		ZipUtil.pack(fromFolder.toFile(), destZip.toFile());
		return destZip;
	}

	private boolean shouldSave() {
		return !Files.exists(destZip);
	}

	private FileSystem createZipFileSystem(Path zipFolder, boolean create) throws IOException {
		// convert the filename to a URI
		URI uri = URI.create("jar:file:" + zipFolder.toUri().getPath());
		Map<String, String> env = new HashMap<>();
		if (create) {
			env.put("create", "true");
		}
		return FileSystems.newFileSystem(uri, env);
	}

	private void zip() throws IOException {
		try (FileSystem zipFileSystem = createZipFileSystem(destZip, true)) {
			final Path root = zipFileSystem.getPath("/");

			// iterate over the files we need to add
			for (Path src : fromFolder) {
				// add a file to the zip file system
				if (!Files.isDirectory(src)) {
					final Path dest = zipFileSystem.getPath(root.toString(), src.toString());
					final Path parent = dest.getParent();
					if (Files.notExists(parent)) {
						System.out.printf("Creating directory %s\n", parent);
						Files.createDirectories(parent);
					}
					Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
				} else {
					// for directories, walk the file tree
					Files.walkFileTree(src, new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
							final Path dest = zipFileSystem.getPath(root.toString(), file.toString());
							Files.copy(file, dest, StandardCopyOption.REPLACE_EXISTING);
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
							final Path dirToCreate = zipFileSystem.getPath(root.toString(), dir.toString());
							if (Files.notExists(dirToCreate)) {
								System.out.printf("Creating directory %s\n", dirToCreate);
								Files.createDirectories(dirToCreate);
							}
							return FileVisitResult.CONTINUE;
						}
					});
				}
			}
		}
	}
}
