package com.leo.core.mycomponent.templateManager.model;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.leo.core.mybase.Constants;

/**
 * @author zhangzhen
 *
 */
public class FileTpl implements Tpl {
	private File file;

	public FileTpl(File file) {
		this.file = file;
	}

	public String getName() {
		String ap = file.getAbsolutePath();
		//ap = ap.replace(File.separatorChar, '/');
		return ap;
	}

	public String getPath() {
		String name = getName();
		return name.substring(0, name.lastIndexOf(File.separatorChar));
	}

	public String getFilename() {
		return file.getName();
	}

	public String getSource() {
		if (file.isDirectory()) {
			return null;
		}
		try {
			return FileUtils.readFileToString(this.file, Constants.UTF8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public long getLastModified() {
		return file.lastModified();
	}

	public Date getLastModifiedDate() {
		return new Timestamp(getLastModified());
	}

	public long getLength() {
		return file.length();
	}

	public int getSize() {
		return (int) (getLength() / 1024) + 1;
	}

	public boolean isDirectory() {
		return file.isDirectory();
	}

}
