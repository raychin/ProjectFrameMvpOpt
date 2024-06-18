package com.ray.project.web;

import com.ray.project.commons.Logger;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				Logger.e("IOUtils", e.getMessage());
			}
		}
		return true;
	}
}
