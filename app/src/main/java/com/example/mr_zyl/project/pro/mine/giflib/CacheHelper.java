package com.example.mr_zyl.project.pro.mine.giflib;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache.Editor;

public class CacheHelper {

	public static File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so,
		// try and use external cache dir, otherwise use internal cache dir.
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
				|| !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context.getCacheDir()
				.getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static boolean isExternalStorageRemovable() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD || Environment.isExternalStorageRemovable();
	}

	private static String getExternDir(String dir) {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		if (dir != null) {
			path += dir;
		}

		return path;
	}

	public static String getSubOfRootDir(Context context) {
		String rootDir = "";
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			rootDir = getExternDir(rootDir) + "/huixin";
		} else {
			rootDir = context.getCacheDir().getPath() + "/huixin";
		}
		return rootDir;
	}

	public static File getExternalCacheDir(Context context) {
		File file = null;
		if (hasExternalCacheDir()) {
			file = context.getExternalCacheDir();
		}
		if (null != file) { return file; }
		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = getSubOfRootDir(context);
		System.out.println("cacheDir:" + cacheDir);
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	public static boolean hasExternalCacheDir() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * Writes a byte array into a DiskLruCache {@link Editor}.
	 * 
	 * @param source		The input byte array.
	 * @param editor		The {@link Editor} to write the byte array into.
	 * 
	 * @return				true if there were no errors, false otherwise.
	 * @throws IOException	If there was an error while writing the file.
	 */
	public static boolean writeByteArrayToEditor(byte[] source, Editor editor) throws IOException {
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(editor.newOutputStream(0), source.length);
			editor.newOutputStream(0).write(source);
			return true;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * Encodes URLs with the SHA-256 algorithm.
	 * @param uri	The URL to encode.
	 * 
	 * @return		The encoded URL.
	 * 
	 * @throws NoSuchAlgorithmException		If the SHA-256 algorithm is not found.
	 * @throws UnsupportedEncodingException	If the UTF-8 encoding is not supported.
	 */
	public static String UriToDiskLruCacheString(String uri) throws NoSuchAlgorithmException,
			UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] convBytes = digest.digest(uri.getBytes("UTF-8"));
		String result;
		StringBuilder sb = new StringBuilder();
		for (byte b : convBytes) {
			sb.append(String.format("%02X", b));
		}
		result = sb.toString();
		result = result.toLowerCase(Locale.US);
		return result;
	}
}
