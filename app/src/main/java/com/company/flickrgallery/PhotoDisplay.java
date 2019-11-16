package com.company.flickrgallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.flickrgallery.adapters.PhotoAdapter;
import com.company.flickrgallery.models.Photo;
import com.company.flickrgallery.utils.MyUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class PhotoDisplay extends AppCompatActivity {

    private AppCompatImageView imageView;
    private FloatingActionButton fabLeft, fabRight;
    private AppCompatButton btnShare, btnDelete, btnSaveToPhone;
    private Intent mIntent;
    Photo photo;
    private List<Photo> photoList;
    private int position = 0;
    private boolean permission = false;
    private View a,b;
    private PhotoAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_display);
        initUI();
    }

    private void initUI(){


        mIntent = getIntent();
        photo = (Photo) mIntent.getSerializableExtra(MyUtils.photos);
        photoList = (List<Photo>) mIntent.getSerializableExtra(MyUtils.photosList);
        photoList = new MainActivity().stuff(photoList);
        position = mIntent.getIntExtra(MyUtils.photoID,0);

        imageView = findViewById(R.id.img_photo_display);
        fabLeft = findViewById(R.id.fab_previous);
        fabRight = findViewById(R.id.fab_next);
        btnDelete = findViewById(R.id.btn_delete);
        btnShare = findViewById(R.id.btn_share);
        btnSaveToPhone = findViewById(R.id.btn_save);

        a = findViewById(R.id.display_layout);
        b = findViewById(R.id.display_layout_r);

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhotoDisplay.this,MainActivity.class));
                finish();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PhotoDisplay.this,MainActivity.class));
                finish();
            }
        });

        btnSaveToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAlertDialog();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePhoto();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("XU",MODE_PRIVATE);
                String x = preferences.getString("XU","");
                SharedPreferences.Editor editor = preferences.edit();
                if(position <= 0){
                    editor.putString("XU",x+position+",");
                    editor.apply();
                    DeletePhoto(position);
                    position += 1;
                    Picasso.get().load(photoList.get(position).getImage_url()).into(imageView);
                }else{
                    editor.putString("XU",x+position+",");
                    editor.apply();
                    DeletePhoto(position);
                    position -= 1;
                    Picasso.get().load(photoList.get(position).getImage_url()).into(imageView);
                }


            }
        });
        prepareUI();
        changeSelection();
    }

    private void prepareUI(){
        Picasso.get().load(photo.getImage_url()).into(imageView);
    }

    private void changeSelection(){
        fabLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position <= 0){
                    Picasso.get().load(photoList.get(position).getImage_url()).into(imageView);
                    Toast.makeText(PhotoDisplay.this, "End of List", Toast.LENGTH_SHORT).show();
                }else{
                    position -= 1;
                    Picasso.get().load(photoList.get(position).getImage_url()).into(imageView);
                }
            }
        });

        fabRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position >= photoList.size() - 1 ){
                    Picasso.get().load(photoList.get(position).getImage_url()).into(imageView);
                    Toast.makeText(PhotoDisplay.this, "End of List", Toast.LENGTH_SHORT).show();
                }else{
                    position += 1;
                    Picasso.get().load(photoList.get(position).getImage_url()).into(imageView);
                }
            }
        });
    }

    private void createAlertDialog(){
        final Dialog dialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.save_image_dialog,null,false);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        final TextInputEditText edt = view.findViewById(R.id.edt_file_name);
        AppCompatButton btn = view.findViewById(R.id.btn_save_stuff);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                saveImageFile(bitmap,edt.getText().toString());
                edt.setText("");
                dialog.dismiss();
            }
        });
        dialog.setTitle("SAVE FILE");
        dialog.show();
        Window window = dialog.getWindow();
        assert window!=null;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    public String saveImageFile(Bitmap bitmap, String name) {
        FileOutputStream out = null;
        String filename = getFilename(name);
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(this, "Image saved as: "+filename, Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
    }

    private String getFilename(String name) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "FlickrGallery");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + name + ".jpg");
        return uriSting;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!permission){
            checkForPermission();
        }

        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private void checkForPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},150);
        }else{
            permission = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 150 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
          permission = true;
        }else{
            Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            permission = false;
        }
    }

    private void sharePhoto(){
        View content = findViewById(R.id.img_photo_display);
        content.setDrawingCacheEnabled(true);

        Bitmap bitmap = content.getDrawingCache();
        File root = Environment.getExternalStorageDirectory();
        File cachePath = new File(root.getAbsolutePath() + "/DCIM/Camera/image.jpg");
        try {
            cachePath.createNewFile();
            FileOutputStream ostream = new FileOutputStream(cachePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(cachePath));
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share,"Share via"));
    }

    private void DeletePhoto(int pos){
        photoList.remove(pos);
    }
}
