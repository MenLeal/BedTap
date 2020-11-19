package com.example.bedtab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class ImagenCrop extends AppCompatActivity {
    private  Button add;
    private ImageButton img;
    private EditText product;
    private EditText prize;
    private EditText descrip;
    private ImageView foto;
    private Bitmap thumb_bitmap;
    private DatabaseReference imgref;
    private StorageReference storageReference;
    private String name;
    private String producto;
    private String precio;
    private String descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_item_offer);
         add=findViewById(R.id.btnsubir);
         img=findViewById(R.id.btnimg);
         product=findViewById(R.id.product);
         prize=findViewById(R.id.precio);
         foto=findViewById(R.id.foto);
         descrip=findViewById(R.id.descrip);
         imgref= FirebaseDatabase.getInstance().getReference().child("Productos");
         storageReference= FirebaseStorage.getInstance().getReference().child("FotosProductos");


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(ImagenCrop.this);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode== RESULT_OK){
            Uri imageuri=CropImage.getPickImageResultUri(this,data);

            //Recortar imagen
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(300,400)
                    .setAspectRatio(1,1).start(ImagenCrop.this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK){
                Uri resultUri=result.getUri();

                File url=new File(resultUri.getPath());
                Picasso.with(this).load(url).into(foto);
                //Comprimiendo imagen

                try{
                    thumb_bitmap=new Compressor(this)
                            .setMaxHeight(400)
                            .setMaxWidth(300)
                            .setQuality(90)
                            .compressToBitmap(url);

                }catch (IOException e){
                    e.printStackTrace();
                }
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                final byte[] thumb_byte=byteArrayOutputStream.toByteArray();

                Long timestamp=System.currentTimeMillis()/1000;
                name=timestamp.toString();

                //fin del compresor
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        producto=product.getText().toString().trim();
                        precio=prize.getText().toString().trim();
                        descripcion=descrip.getText().toString().trim();
                        if(producto.isEmpty() || precio.isEmpty() || descripcion.isEmpty()){
                            Toast.makeText(ImagenCrop.this, "Debe llenar todos los campos", Toast.LENGTH_LONG).show();
                        }
                        else{
                        final StorageReference ref=storageReference.child(name);
                        UploadTask uploadTask=ref.putBytes(thumb_byte);

                        Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri downloaduri=task.getResult();
                                Map<String, Object> offer=new HashMap<>();
                                offer.put("product", producto);
                                offer.put("prize","$"+precio);
                                offer.put("description",descripcion);
                                offer.put("imageURL", downloaduri.toString());
                                offer.put("id",imgref.push().getKey());
                                imgref.push().setValue(offer);
                                Toast.makeText(ImagenCrop.this, "Producto subido con éxito", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                    }
                });
            }
        }
    }
}