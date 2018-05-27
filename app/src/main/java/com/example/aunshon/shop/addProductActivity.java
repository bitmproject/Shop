package com.example.aunshon.shop;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static com.example.aunshon.shop.R.color.black;
import static com.example.aunshon.shop.R.color.red;

public class addProductActivity extends AppCompatActivity {
    private Spinner spinner;
    private Button upload,show,chooseImage;
    private TextView ptitle,pprice;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri mImageUir;
    private StorageTask mUploadTask;

    private StorageReference mStorageRef;
    private DatabaseReference mdatabaseRef;

    public static final int PICK_IMAGE_REQUEST=1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        spinner=findViewById(R.id.addCatagorySpinner);
        upload=findViewById(R.id.uploadBtn);
        show=findViewById(R.id.deshboardBtn);
        chooseImage=findViewById(R.id.chooseImageBtn);
        ptitle=findViewById(R.id.addtitleEt);
        pprice=findViewById(R.id.addPriceEt);
        imageView=findViewById(R.id.addImageView);
        progressBar=findViewById(R.id.addprogressbar);

        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mdatabaseRef=FirebaseDatabase.getInstance().getReference("uploads");


        ArrayAdapter<CharSequence> adapterSpinner=ArrayAdapter.createFromResource(addProductActivity.this,R.array.catagory,android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
    }

    public void chooseImageBtnClicked(View view) {
        openFileChooser();
    }

    @SuppressLint("ResourceAsColor")
    public void uploadBtnClicked(View view) {
        if(ptitle.getText().toString().isEmpty() || ptitle.getText().toString().equals(" ") || pprice.getText().toString().isEmpty() ||
                spinner.getSelectedItem().toString().equals("No seleted catagory") ||
                imageView.getResources().equals(0)) {

            if(ptitle.getText().toString().isEmpty() || ptitle.getText().toString().equals(" ")){
                ptitle.setHint("Title ?");
                ptitle.setError("Fill it up");
            }

            if(pprice.getText().toString().isEmpty()){
                pprice.setHint("Price ?");
                pprice.setError("Fill it up");
            }

            if(spinner.getSelectedItem().toString().equals("No seleted catagory")){
                Toast.makeText(this, "Select a catagory", Toast.LENGTH_SHORT).show();
            }
            if(imageView.getResources().equals(0)){
                Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            }
        }else {
            if (mUploadTask!=null && mUploadTask.isInProgress()){
                Toast.makeText(this, "Upload in process", Toast.LENGTH_SHORT).show();
            }
            else {
                uploadFile();
            }

        }

    }

    public void deshboardBtnClicked(View view) {
        Intent intent=new Intent(addProductActivity.this,Admin_Deshboard.class);
        startActivity(intent);
    }

    public void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            mImageUir=data.getData();
            Picasso.with(addProductActivity.this).load(mImageUir).into(imageView);
        }
    }

    private String getFileExtention(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public void uploadFile(){
        if(mImageUir!=null){

            StorageReference fileRefrence=mStorageRef.child(System.currentTimeMillis()+"."+ getFileExtention(mImageUir));

            mUploadTask=fileRefrence.putFile(mImageUir).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                            ptitle.setText("");
                            ptitle.setHint("Title ");
                            ptitle.setHintTextColor(black);

                            pprice.setText("");
                            pprice.setHint("Price");
                            pprice.setHintTextColor(black);

                            spinner.setSelection(0);
                            imageView.setImageResource(0);
                        }
                    },500);
                    Toast.makeText(addProductActivity.this, "Upload Successfull", Toast.LENGTH_SHORT).show();
                    porduct p=new porduct(ptitle.getText().toString().trim(),spinner.getSelectedItem().toString().trim(),pprice
                    .getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());
                    String uploadId=mdatabaseRef.push().getKey();
                    mdatabaseRef.child(uploadId).setValue(p);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress=(100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }else {
            Toast.makeText(this, "No File Selected", Toast.LENGTH_SHORT).show();
        }
    }
}
