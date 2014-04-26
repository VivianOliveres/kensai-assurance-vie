package com.kensai.av.persist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

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

}
