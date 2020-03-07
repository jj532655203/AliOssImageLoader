package com.fronttcapital.imageloader.utils.disk_lru_cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;

import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.blankj.utilcode.util.Utils;
import com.fronttcapital.imageloader.Constants;
import com.fronttcapital.imageloader.utils.Md5Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

;

/**
 * Jay
 * lru本地缓存
 */
public class DiskLruCacheUtils {

    private static final String TAG = "DiskLruCacheUtils";
    private static DiskLruCacheUtils instance;
    private DiskLruCache mDiskLruCache;

    private DiskLruCacheUtils() {
        try {
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(), 1, 1, Constants.DISK_LRU_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCacheDir() {
        String diskLruDirPath = Utils.getApp().getFilesDir().getAbsolutePath() + File.separator + Constants.DISK_LRU_CACHE_DIR;
        File diskDir = new File(diskLruDirPath);
        if (!diskDir.exists()) {
            diskDir.mkdirs();
        }
        return diskDir;
    }

    public static DiskLruCacheUtils getInstance() {
        if (instance == null) {
            synchronized (DiskLruCacheUtils.class) {
                if (instance == null) {
                    instance = new DiskLruCacheUtils();
                }
            }
        }
        return instance;
    }

    public Bitmap getBitmap(String ossImgPath) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            Log.e(TAG, "不能在主线程操作DiskLruCache");
            return null;
        }

        String key = Md5Utils.encode(ossImgPath);

        Bitmap bitmap = null;
        FileInputStream inputStream = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                inputStream = (FileInputStream) snapshot.getInputStream(0);
                FileDescriptor fd = inputStream.getFD();
                bitmap = BitmapFactory.decodeFileDescriptor(fd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.safeClose(inputStream);
        }

        return bitmap;
    }

    public void put(String ossImgPath, Bitmap bitmap) {

        if (Looper.getMainLooper() == Looper.myLooper()) {
            Log.e(TAG, "不能在主线程操作DiskLruCache");
            return;
        }

        String key = Md5Utils.encode(ossImgPath);

        OutputStream outputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit != null) {
                outputStream = edit.newOutputStream(0);
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                outputStream.write(baos.toByteArray());
                outputStream.flush();
                edit.commit();
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.safeClose(outputStream);
            IOUtils.safeClose(baos);
        }
    }

    public boolean contains(String ossUrl) {
        Log.d(TAG, "contains ossUrl=" + ossUrl);

        String key = Md5Utils.encode(ossUrl);

        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = mDiskLruCache.get(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean contain = snapshot != null;
        if (snapshot != null) {
            snapshot.close();
        }
        return contain;
    }

    public void close() {
        try {
            mDiskLruCache.close();
            instance = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
