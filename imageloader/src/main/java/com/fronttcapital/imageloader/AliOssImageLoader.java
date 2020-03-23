package com.fronttcapital.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.Utils;
import com.jj.fst_disk_lru.utils.disk_lru_cache.DiskLruCacheUtils;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;

/**
 * Jay
 * 适用于加载oss上的图片(优化版)
 */
public class AliOssImageLoader {

    private static final String TAG = "AliOssImageLoader";
    private static final int HANDLER_WHAT_LOAD_IMAGE_VIEW = 101;
    private static OSSCredentialProvider sCredentialProvider;
    private static String sBucketName;

    /**
     * @param credentialProvider oss验证token过期时将自动回调本实例的getFederationToken()方法,方法中请正确按照oss要求操作
     * @param BUCKET_NAME        阿里云oss的BUCKET_NAME
     */
    public static void init(OSSCredentialProvider credentialProvider, String BUCKET_NAME) {
        sCredentialProvider = credentialProvider;
        sBucketName = BUCKET_NAME;
    }

    protected static String getBucketName() {
        return sBucketName;
    }

    protected static OSSCredentialProvider getCredentialProvider() {
        return sCredentialProvider;
    }

    private static UiHandler uiHandler = new UiHandler(Looper.getMainLooper());

    private static class UiHandler extends Handler {
        UiHandler(Looper mainLooper) {
            super(mainLooper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_WHAT_LOAD_IMAGE_VIEW: {

                    ImageView imageView;
                    Bitmap bitmap;
                    String ossImgPath;
                    try {
                        Object[] objects = (Object[]) msg.obj;
                        WeakReference<ImageView> imageViewRef = (WeakReference<ImageView>) objects[0];
                        imageView = imageViewRef.get();
                        bitmap = (Bitmap) objects[2];
                        ossImgPath = (String) objects[1];
                    } catch (Exception e) {
                        Log.e(TAG, "handleMessage HANDLER_WHAT_LOAD_IMAGE_VIEW-->传递参数不规范");
                        return;
                    }

                    if (TextUtils.isEmpty(ossImgPath) || bitmap == null || bitmap.isRecycled() || imageView == null) {
                        Log.e(TAG, "handleMessage HANDLER_WHAT_LOAD_IMAGE_VIEW-->有参数为空");
                        return;
                    }

                    Object tag = imageView.getTag();
                    if (tag != null && TextUtils.equals((String) tag, ossImgPath)) {
                        DecimalFormat decimalFormat = new DecimalFormat("0.0000");
                        String ratio = decimalFormat.format(((float) bitmap.getWidth()) / bitmap.getHeight());
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) imageView.getLayoutParams();
                        params.dimensionRatio = ratio;
                        imageView.setImageBitmap(bitmap);
                        Log.d(TAG, "handleMessage HANDLER_WHAT_LOAD_IMAGE_VIEW 为imageView加载了一张新图ossImgPath=" + ossImgPath);
                    } else {
                        Log.d(TAG, "handleMessage HANDLER_WHAT_LOAD_IMAGE_VIEW 该imageView的index已变更,绑定了新图");
                        if (!bitmap.isRecycled()) bitmap.recycle();
                    }
                }
                break;
            }
        }
    }

    /**
     * 给imageView 加载oss上的图片,为了保证图片展示不变形,此处按原图宽高比等比缩放,所以无需传入高度
     * 注意!:为支持imageView根据原图比例调整宽高比,本方法默认imageView在ConstraintLayout中,本方法将通过layoutParams.ratio属性改变imageView的宽高比
     *
     * @param ossImgPath 图url
     * @param imageView  要加载图片的控件
     * @param width      图片宽度
     */
    public static void loadImageView(final String ossImgPath, ImageView imageView, final int width) {
        Log.d(TAG, "loadImageView ossImgPath=" + ossImgPath);

        if (imageView == null || TextUtils.isEmpty(ossImgPath) || width <= 0) {
            Log.e(TAG, "loadImageView 传参异常 return");
            return;
        }

        recycleBitmap(imageView);
        imageView.setTag(ossImgPath);
        final WeakReference<ImageView> imageViewRef = new WeakReference<>(imageView);

        ImgLoaderExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "loadImageView 开启一个加载图片任务 ossImgPath=" + ossImgPath);

                //致读者:不建议用内存缓存bitmap,建议使用前后条目预加载来加快图片显示

                //从本地获取
                Bitmap bitmap = DiskLruCacheUtils.getInstance().getBitmap(ossImgPath);
                if (bitmap == null || bitmap.isRecycled()) {
                    //从网络获取
                    AliOssResponse ossResponse = null;
                    try {
                        ossResponse = AliOssUtils.getObjectRequest(Utils.getApp(), ossImgPath);
                    } catch (Exception e) {
                        Log.e(TAG, "loadImageView 下载失败 e=" + Log.getStackTraceString(e));
                    }

                    if (ossResponse == null || ossResponse.getStatusCode() != 200) {
                        Log.e(TAG, "loadImageView 从网络下载失败 为空或!=200");
                        return;
                    }

                    bitmap = BitmapFactory.decodeStream(ossResponse.getInputStream());
                    if (bitmap == null || bitmap.isRecycled()) {
                        Log.e(TAG, "loadImageView decodeStream失败");
                        return;
                    }

                    //采样率压缩
                    float height = ((float) bitmap.getHeight() * width) / bitmap.getWidth();
                    bitmap = ImageUtils.compressBySampleSize(bitmap, width, (int) height, true);

                    //本地缓存
                    DiskLruCacheUtils.getInstance().put(ossImgPath, bitmap);

                }

                Message message = Message.obtain();
                message.what = HANDLER_WHAT_LOAD_IMAGE_VIEW;
                message.obj = new Object[]{imageViewRef, ossImgPath, bitmap};
                uiHandler.sendMessage(message);

                Log.d(TAG, "loadImageView 下载成功 path=" + ossImgPath);
            }
        });

    }


    private static void recycleBitmap(ImageView iv) {
        if (iv == null || iv.getDrawable() == null) return;

        BitmapDrawable bitmapDrawable = (BitmapDrawable) iv.getDrawable();
        iv.setImageDrawable(null);
        if (bitmapDrawable != null) {
            recycle(bitmapDrawable.getBitmap());
        }
    }

    private static void recycle(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) return;
        bitmap.recycle();
    }

}
