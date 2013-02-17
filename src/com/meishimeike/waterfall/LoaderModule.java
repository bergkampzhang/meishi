
package com.meishimeike.waterfall;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.meishimeike.R;
import com.meishimeike.Utils.Constants;
import com.meishimeike.waterfall.WaterfallView.ItemTag;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class LoaderModule {

    private Handler mWorkerHandler;
    private Handler mMainHandler;
    
    public LoaderModule() {

    }

    public LoaderModule(Handler workerHandler, Handler mainHandler) {
        mWorkerHandler = workerHandler;
        mMainHandler = mainHandler;
    }

    private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

    public List<Bitmap> loadBitmaps(String[] imageUrls) {

        int len = imageUrls.length;
        for (int i = 0; i < len; i++) {
            loadImage(imageUrls[i]);
        }

        return null;
    }

    public void cleanImageCache() {
        imageCache.clear();
    }

    public void reload(final View item) {
        System.out.println("reload");
        final ItemTag tag = (ItemTag) item.getTag();
        final ImageView icon_food = (ImageView) item.findViewById(R.id.imgFood);
        final ImageView icon_user = (ImageView) item.findViewById(R.id.imgUser);
        mWorkerHandler.post(new Runnable() {

            @Override
            public void run() {
                final Bitmap foodBitmap = loadImage(tag.foodUrl);
                final Bitmap userBitmap = loadImage(tag.userUrl);
                if (foodBitmap != null || userBitmap != null) {
                    mMainHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            synchronized (this) {
                                icon_food.setImageBitmap(foodBitmap);
                                icon_user.setImageBitmap(userBitmap);
                                tag.isFill = true;
                                item.setTag(tag);
                            }
                        }
                    });
                }
            }
        });
    }

    public void recycle(View preRecycleView) {
        System.out.println("recycle");
        ItemTag tag = (ItemTag) preRecycleView.getTag();
        String foodUrl = tag.foodUrl;
        String userUrl = tag.userUrl;
        String foodImageName = foodUrl.substring(foodUrl.lastIndexOf(File.separator) + 1);
        String userImageName = userUrl.substring(userUrl.lastIndexOf(File.separator) + 1);

        SoftReference<Bitmap> ref = imageCache.get(foodImageName);
        if (ref != null) {
            Bitmap bitmap = ref.get();
            if (bitmap != null &&!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            ref.clear();
            imageCache.remove(foodImageName);
        }

        ref = imageCache.get(userImageName);
        if (ref != null) {
            Bitmap bitmap = ref.get();
            if (bitmap != null&&!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
            ref.clear();
            imageCache.remove(userImageName);
        }
    }

    public Bitmap loadImage(String imageUrl) {
        String name = imageUrl
                .substring(imageUrl.lastIndexOf(File.separator) + 1);
        Bitmap cacheImage = loadImageFromCache(name);
        if (cacheImage != null) {
            return cacheImage;
        }

        Bitmap sdcardImage = loadImageFromSdcard(name);
        if (sdcardImage != null) {
            return sdcardImage;
        }

        Bitmap downloadImage = loadImageFromNetwork(imageUrl, name);
        if (downloadImage != null) {
            saveImageByName(name, downloadImage);
            return downloadImage;
        }

        return null;
    }

    private void saveImageByName(String name, Bitmap bitmap) {

        String state = Environment.getExternalStorageState();
        if (state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory();
            String dirPath = Constants.MSMK_CACHE_PATH;
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdir();
            }

            String filePath = Constants.MSMK_CACHE_PATH
                    + File.separator + name;
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                FileOutputStream fos = null;
                try {
                    boolean b = imageFile.createNewFile();
                    if (b) {
                        fos = new FileOutputStream(imageFile);
                        bitmap.compress(CompressFormat.JPEG, 50, fos);
                        fos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private Bitmap loadImageFromNetwork(String imageUrl, String name) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();

            System.out.println("loadImageFromNetwork" + imageUrl);
            if (responseCode != 200) {
                throw new Exception("图片文件不存在或路径错误，错误代码：" + responseCode);
            }

            InputStream in = conn.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(in);
            bitmap = BitmapFactory.decodeStream(bis);

            if (bitmap != null) {
                if (!imageCache.containsKey(name)) {
                    SoftReference<Bitmap> ref = new SoftReference<Bitmap>(
                            bitmap);
                    imageCache.put(name, ref);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        SoftReference<Bitmap> ref = imageCache.get(name);
        if (ref != null) {
            return ref.get();
        }

        return null;
    }

    private Bitmap loadImageFromCache(String name) {

        SoftReference<Bitmap> ref = imageCache.get(name);
        if (ref != null) {
            return ref.get();
        }
        return null;

    }

    private Bitmap loadImageFromSdcard(String name) {
        String state = Environment.getExternalStorageState();
        if (state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            File sdcard = Environment.getExternalStorageDirectory();

            String dirPath = Constants.MSMK_CACHE_PATH;
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdir();
            }

            String filePath = Constants.MSMK_CACHE_PATH
                    + File.separator + name;
            File image = new File(filePath);
            if (!image.exists()) {
                return null;
            }

            if (!imageCache.containsKey(name)) {
                SoftReference<Bitmap> ref = new SoftReference<Bitmap>(
                        BitmapFactory.decodeFile(image.getPath()));
                imageCache.put(name, ref);
            }

            SoftReference<Bitmap> ref = imageCache.get(name);
            if (ref != null) {
                return ref.get();
            }
        }
        return null;
    }
}
