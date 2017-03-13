package com.example.mr_zyl.project.pro.mine.giflib;

import android.app.Activity;
import android.content.Context;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.example.mr_zyl.project.R;
import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * gif加载类（双缓存）
 * 
 * @description：
 * @author wangxuhao(379394493@qq.com)
 * @date 2014年12月19日 下午4:39:40
 */
public class GifLoader {
	public static Map<ImageView, String> imageViews = Collections.synchronizedMap(new HashMap<ImageView, String>());
	private ExecutorService executorService;

	private static int mMemCacheSize = 10 * 1024 * 1024; // 10MiB
	private static LruCache<String, byte[]> mMemCache;
	private static int mAppVersion = 1;
	private static int mDiskCacheSize = 50 * 1024 * 1024; // 50MiB
	private static DiskLruCache mDiskCache;
	private static boolean mCacheInit = false;
	private static final int DISK_CACHE_VALUE_COUNT = 1;
	private static GifLoader loader;
	// 默认图片id
	final int default_image_id = R.mipmap.default_error;

	private GifLoader(Context context) {
		executorService = Executors.newFixedThreadPool(2);
		initCaches(context);
	}

	public synchronized static GifLoader getInstance(Context context) {
		if (loader == null) {
			loader = new GifLoader(context);
		}
		return loader;
	}

	public void displayImage(String url, GifImageView imageView, boolean isGif) {
		try {
			if (new File(url).exists()) {
				imageView.setImageDrawable(new GifDrawable(url));
				return;
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}

		imageViews.put(imageView, url);
		byte[] data = mMemCache.get(url);
		if (data != null) {
			try {
				imageView.setImageDrawable(new GifDrawable(data));
			}
			catch (Exception e) {
				e.printStackTrace();
				imageView.setImageResource(default_image_id);
			}
		}
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(default_image_id);
		}

	}

	private void queuePhoto(String url, GifImageView imageView) {
		PhotoToLoad photoToLoad = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(photoToLoad));
	}

	/**
	 * 此方法待优化以防止内存溢出 先从文件里面读取，没有的话再到网上下载
	 * 
	 * @param url
	 * @return
	 */
	private byte[] getBitmap(String url) {
		Snapshot cacheEntry = null;
		try {
			cacheEntry = mDiskCache.get(CacheHelper.UriToDiskLruCacheString(url));
		}
		catch (Exception e) {
			Log.i("wang", "DISK CACHE: Error while getting value for URL " + url);
		}

		byte[] image = null;

		// If the object is not null, convert it
		if (cacheEntry != null) {
			// Convert the InputStream
			image = inputStreamToByteArray(cacheEntry.getInputStream(0), (int) cacheEntry.getLength(0));
			// Saves the image in the in-memory cache
			mMemCache.put(url, image);
		}
		try {
			if (image != null) {

				return image;

			}
			else {
				URL imageUrl = new URL(url);
				HttpURLConnection con = (HttpURLConnection) imageUrl.openConnection();
				con.setConnectTimeout(30000);
				con.setReadTimeout(30000);
				con.setInstanceFollowRedirects(true);
				InputStream is = con.getInputStream();
				image = inputStreamToByteArray(is, 8096);
				if (image != null) {

					try {
						Editor editor = mDiskCache.edit(CacheHelper.UriToDiskLruCacheString(url));
						if (editor != null) {
							if (CacheHelper.writeByteArrayToEditor(image, editor)) {
								mDiskCache.flush();
								editor.commit();
							}
							else {
								editor.abort();
							}
						}
					}
					catch (Exception e) {
						Log.e("wang", "Storage of image into the disk cache failed!");
					}

					mMemCache.put(url, image);
				}
			}

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}

	private class PhotosLoader implements Runnable {
		private PhotoToLoad photoToLoad;

		public PhotosLoader(PhotoToLoad photoToLoad) {
			super();
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			// 下载前检查imageview是否被复用
			if (imageViewReused(photoToLoad)) {
				return;
			}
			byte[] bm = getBitmap(photoToLoad.url);

			// 下载完毕后再次检查imageview是否被复用
			if (imageViewReused(photoToLoad)) {
				return;
			}
			DisplayImageRunnable displayImageRunnable = new DisplayImageRunnable(bm, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(displayImageRunnable);

		}

	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		// 代表imageviews map中存放的imageview对应的value值已经被覆盖掉，也就是重用了
		if (tag == null || !tag.equals(photoToLoad.url)) {
			return true;
		}
		else {
			return false;
		}

	}

	private class DisplayImageRunnable implements Runnable {
		private byte[] data;
		private PhotoToLoad photoToLoad;

		public DisplayImageRunnable(byte[] data, PhotoToLoad photoToLoad) {
			super();
			this.data = data;
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (imageViewReused(photoToLoad)) {
				return;
			}
			if (data != null) {
				try {
					photoToLoad.imageView.setImageDrawable(new GifDrawable(data));
				}
				catch (Exception e) {
					e.printStackTrace();
					photoToLoad.imageView.setImageResource(default_image_id);
				}
			}
			else {
				photoToLoad.imageView.setImageResource(default_image_id);
			}

		}
	}

	private class PhotoToLoad {
		public String url;
		public GifImageView imageView;

		public PhotoToLoad(String url, GifImageView imageView) {
			super();
			this.url = url;
			this.imageView = imageView;
		}

	}

	private void initCaches(Context context) {
		if (!mCacheInit) {
			mMemCache = new LruCache<String, byte[]>(mMemCacheSize) {
				protected int sizeOf(String key, byte[] value) {
					return value.length;
				}
			};
			File diskCacheDir = CacheHelper.getDiskCacheDir(context, "imagecache");
			try {
				mDiskCache = DiskLruCache.open(diskCacheDir, mAppVersion, DISK_CACHE_VALUE_COUNT, mDiskCacheSize);
			}
			catch (IOException ignored) {
			}
			mCacheInit = true;
		}
	}

	private byte[] inputStreamToByteArray(InputStream is, int size) {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		byte[] buffer = new byte[size];

		int len = 0;
		try {
			while ((len = is.read(buffer)) != -1) {
				byteBuffer.write(buffer, 0, len);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		buffer = byteBuffer.toByteArray();
		return buffer;
	}
}
