package com.fronttcapital.imageloader.utils.disk_lru_cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.common.utils.IOUtils;
import com.blankj.utilcode.util.Utils;
import com.fronttcapital.imageloader.Constants;
import com.fronttcapital.imageloader.utils.Md5Utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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

    /**
     * this function will create bitmap ,don't call concurrently!
     */
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

    /**
     * this function will create bitmap ,don't call concurrently!
     */
    public void put(String ossImgPath, Bitmap bitmap) {

        if (Looper.getMainLooper() == Looper.myLooper()) {
            Log.e(TAG, "不能在主线程操作DiskLruCache");
            return;
        }

        String key = Md5Utils.encode(ossImgPath);

        Bitmap ossBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        OutputStream outputStream = null;
        ByteArrayOutputStream baos = null;
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit != null) {
                outputStream = edit.newOutputStream(0);
                baos = new ByteArrayOutputStream();
                ossBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
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

    /**
     * 直接将oss的输入流获取bitmap,写到diskLru
     * this function will create bitmap ,don't call concurrently!
     */
    public void put(String ossImgPath, InputStream inputStream) {

        if (Looper.getMainLooper() == Looper.myLooper()) {
            Log.e(TAG, "不能在主线程操作DiskLruCache");
            return;
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        if (bitmap == null || bitmap.isRecycled()) return;

        String key = Md5Utils.encode(ossImgPath);

        OutputStream outputStream = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

            DiskLruCache.Editor edit = mDiskLruCache.edit(key);
            if (edit != null) {
                outputStream = edit.newOutputStream(0);
                outputStream.write(baos.toByteArray());
                baos.flush();
                outputStream.flush();
                edit.commit();
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            IOUtils.safeClose(outputStream);
            IOUtils.safeClose(inputStream);
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

    public String getDrawPathJson(String ossImgPath) {

        if (Looper.getMainLooper() == Looper.myLooper()) {
            Log.e(TAG, "不能在主线程操作DiskLruCache");
            return null;
        }

        String key = Md5Utils.encode(ossImgPath);

        String drawPathJson = null;
        FileInputStream inputStream = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
            if (snapshot != null) {
                inputStream = (FileInputStream) snapshot.getInputStream(0);
                FileDescriptor fd = inputStream.getFD();
                FileReader fileReader = new FileReader(fd);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                drawPathJson = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.safeClose(inputStream);
        }

        return drawPathJson;
    }

    public void put(String key, String drawPathJson) {

        if (Looper.getMainLooper() == Looper.myLooper()) {
            Log.e(TAG, "不能在主线程操作DiskLruCache");
            return;
        }

        String ossImgPath = Md5Utils.encode(key);

        OutputStream outputStream = null;
        try {
            DiskLruCache.Editor edit = mDiskLruCache.edit(ossImgPath);
            if (edit != null) {
                outputStream = edit.newOutputStream(0);
                outputStream.write(drawPathJson.getBytes());
                outputStream.flush();
                edit.commit();
                mDiskLruCache.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.safeClose(outputStream);
        }
    }

    public void remove(String ossImgPath) {

        String key = Md5Utils.encode(ossImgPath);

        if (TextUtils.isEmpty(key)) return;
        try {
            mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
