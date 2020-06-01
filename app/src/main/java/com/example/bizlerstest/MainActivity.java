package com.example.bizlerstest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextInputEditText vehicleNo,
                      vehicleMake,
                      vehicleModel,
                      vehicleVarient,
                      vehicleFuelType;
    TextInputLayout inputVehicleNo,
            inputVehicleMake,
            inputVehicleModel,
            inputVehicleVarient,
            inputVehicleFuelType;
    ImageView vehiclePhoto;
    Button submit;
    int REQUEST_CAMERA = 0,
                SELECT_FILE = 1;
    String userChoosenTask;

    private DBManager dbManager;
    Bitmap thumbnail;
    TextView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        dbManager = new DBManager(this);
    }
    private void setView()
    {
        inputVehicleNo=findViewById(R.id.textInputLayoutvehicleNo);
        inputVehicleMake=findViewById(R.id.textInputLayoutvehicleMake);
        inputVehicleModel=findViewById(R.id.textInputLayoutvehicleModel);
        inputVehicleVarient=findViewById(R.id.textInputLayoutvehicleVariant);
        inputVehicleFuelType=findViewById(R.id.textInputLayoutvehicleFuelType);

         vehicleNo=findViewById(R.id.vehicleNo);
         vehicleMake=findViewById(R.id.vehicleMake);
         vehicleModel=findViewById(R.id.vehicleModel);
         vehicleVarient=findViewById(R.id.vehicleVariant);
         vehicleFuelType=findViewById(R.id.vehicleFuelType);
         vehiclePhoto=findViewById(R.id.vehiclePhoto);
         submit=findViewById(R.id.submit);
         vehiclePhoto.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 selectImage();
             }
         });
         submit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                  validateAndSave(
                             vehicleNo.getText().toString(),
                             vehicleMake.getText().toString(),
                             vehicleModel.getText().toString(),
                             vehicleVarient.getText().toString(),
                             vehicleFuelType.getText().toString(),
                            thumbnail
                     );
             }
         });
         listView=findViewById(R.id.footer_list);

         listView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(MainActivity.this,VehicleList.class);
                 startActivity(intent);
             }
         });

    }
    private void validateAndSave(String vNo,String vMake,String vModel,String vVarient,String vFuelType,Bitmap vPhoto)
    {
        if(vNo.isEmpty()) {
            inputVehicleNo.setError("Enter Vehicle Number");
        }else if(vMake.isEmpty()){
            inputVehicleMake.setError("Enter Vehicle Make");
        } else if(vModel.isEmpty()){
            inputVehicleModel.setError("Enter Vehicle Model");
        } else if(vVarient.isEmpty()){
            inputVehicleVarient.setError("Enter Vehicle Variant");
        } else if(vFuelType.isEmpty()) {
            inputVehicleFuelType.setError("Enter Vehicle Fuel Type");
        }else if(thumbnail==null) {
           Toast.makeText(this,"Add Photo",Toast.LENGTH_LONG).show();
        }
        else {
            ;
          byte[] vehiclePhoto=getBitmapAsByteArray(vPhoto);
          dbManager.open();
          long result=dbManager.insert(vNo,vMake,vModel,vVarient,vFuelType,vehiclePhoto);
          dbManager.close();
          Toast.makeText(this,"Details Saved",Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }


    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MainActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        vehiclePhoto.setImageBitmap(thumbnail);
    }
    private void onSelectFromGalleryResult(Intent data) {

         thumbnail=null;
        if (data != null) {
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        vehiclePhoto.setImageBitmap(thumbnail);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    private Bitmap getImageBitmap(ImageView view){
        Bitmap bitmap = ((BitmapDrawable)view.getDrawable()).getBitmap();
        return bitmap;
    }

}