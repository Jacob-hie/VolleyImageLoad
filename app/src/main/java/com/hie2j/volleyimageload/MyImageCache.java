package com.hie2j.volleyimageload;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class MyImageCache implements ImageLoader.ImageCache {

    private LruCache<String, Bitmap> lruCache;

    public MyImageCache() {
        int maxSize = 10 * 1024 * 1024;
        lruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String s) {
        Bitmap bitmap = lruCache.get(s);
        if (bitmap == null) {
            Log.e("MyImageCache", "getBitmap failed bitmap == null");
        } else {
            Log.e("MyImageCache", "getBitmap failed bitmap != null");
        }
        return bitmap;
    }

    @Override
    public void putBitmap(String s, Bitmap bitmap) {
        if (bitmap == null) {
            Log.e("MyImageCache", "putBitmap failed bitmap == null");
        } else {
            Log.e("MyImageCache", "putBitmap success bitmap != null");
        }
        lruCache.put(s, bitmap);
    }
}
