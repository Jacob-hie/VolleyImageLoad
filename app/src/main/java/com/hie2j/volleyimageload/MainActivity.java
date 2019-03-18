package com.hie2j.volleyimageload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.graphics.Bitmap.Config.ARGB_8888;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private MyImageCache myImageCache;

    private Button btn_httpUrlConnection;
    private Button btn_imageRequest;
    private Button btn_imageLoader;
    private Button btn_netWorkImageView;
    private ImageView imageView;

    private String imageUrl = "https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1386436458,4179574259&fm=58&bpow=640&bpoh=960";

    private static final int GET_IMG = 1001;
    private Handler handler;
    private RequestQueue requestQueue;
    private NetworkImageView net_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myImageCache = new MyImageCache();
        findViews();
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == GET_IMG){
                    imageView.setImageBitmap((Bitmap) msg.obj);
                    return true;
                }
                return false;
            }
        });
        btn_httpUrlConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        useHttpUrlConnection();
                    }
                }).start();
            }
        });
        btn_imageRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useImageRequest();
            }
        });
        btn_imageLoader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useImageLoader();
            }
        });
        btn_netWorkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageLoader imageLoader = new ImageLoader(requestQueue,myImageCache);
                net_img.setDefaultImageResId(R.drawable.timg);
                net_img.setErrorImageResId(R.drawable.error);
                net_img.setImageUrl(imageUrl,imageLoader);
                Log.e(TAG,"btn_netWorkImageView按钮加载图片");
            }
        });

    }

    private void useImageLoader() {
        ImageLoader imageLoader = new ImageLoader(requestQueue, myImageCache);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView,
                R.drawable.timg,R.drawable.error);
        imageLoader.get(imageUrl,imageListener);
        Log.e(TAG,"btn_imageLoader按钮加载图片");
    }

    private void useImageRequest() {
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                Log.e(TAG,"btn_imageRequest按钮加载图片");
                imageView.setImageBitmap(bitmap);
            }
        }, 0, 0, ARGB_8888, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG,volleyError.toString());
            }
        });
        requestQueue.add(imageRequest);
    }

    private void useHttpUrlConnection() {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = httpURLConnection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            Message msg = Message.obtain();
            msg.what = GET_IMG;
            msg.obj = bitmap;
            handler.sendMessage(msg);
            Log.e(TAG,"btn_httpUrlConnection按钮加载图片");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void findViews() {
        btn_httpUrlConnection = findViewById(R.id.btn_httpUrlConnection);
        btn_imageRequest = findViewById(R.id.btn_imageRequest);
        btn_imageLoader = findViewById(R.id.btn_imageLoader);
        btn_netWorkImageView = findViewById(R.id.btn_netWorkImageView);
        imageView = findViewById(R.id.img);
        net_img = findViewById(R.id.net_img);
    }
}
