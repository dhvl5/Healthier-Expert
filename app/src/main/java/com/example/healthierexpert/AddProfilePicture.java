package com.example.healthierexpert;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddProfilePicture extends AppCompatActivity
{
    ImageView imageviewback;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    File mPhotoFile;
    FileCompressor mCompressor;
    @BindView(R.id.img3)
    ImageView imageViewProfilePic;
    Button button;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile_picture);

        prefs = getSharedPreferences("image", MODE_PRIVATE);
        editor = prefs.edit();

        setResult(RESULT_OK);

        if(Build.VERSION.SDK_INT>=21)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }

        button =  findViewById(R.id.browse_gallery);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //editor.putString("status", editText.getText().toString());
                editor.apply();
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), AddProfilePicture.class);
                startActivity(intent);
            }
        });


        ButterKnife.bind(this);
        mCompressor = new FileCompressor(this);
    }
    /*private void selectImage()
    {
        final CharSequence[] items =
                {
                "Take Photo", "Choose from Library",
                "Cancel"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddProfilePicture.this);
        builder.setItems(items, (dialog, item) ->
        {
            if (items[item].equals("Take Photo"))
            {
                requestStoragePermission(true);
            }
            else if (items[item].equals("Choose from Library"))
            {
                requestStoragePermission(false);
            }
            else if (items[item].equals("Cancel"))
            {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            // Create the File where the photo should go
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        photoFile);
                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    private void dispatchGalleryIntent()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == REQUEST_TAKE_PHOTO)
            {
                try
                {
                    mPhotoFile = mCompressor.compressToFile(mPhotoFile);
                    editor.putString("image", mPhotoFile.toString());
                    editor.apply();
                    editor.commit();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                Glide.with(AddProfilePicture.this)
                        .load(mPhotoFile)
                        .apply(new RequestOptions().centerCrop()
                                .circleCrop()
                                .placeholder(R.drawable.addprofilepic))
                        .into(imageViewProfilePic);
            }
            else if (requestCode == REQUEST_GALLERY_PHOTO)
            {
                Uri selectedImage = data.getData();
                try
                {
                    mPhotoFile = mCompressor.compressToFile(new File(getRealPathFromUri(selectedImage)));

                    editor.putString("image", selectedImage.toString());
                    editor.apply();
                    editor.commit();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                Glide.with(AddProfilePicture.this)
                        .load(mPhotoFile)
                        .apply(new RequestOptions().centerCrop()
                                .circleCrop()
                                .placeholder(R.drawable.addprofilepic))
                        .into(imageViewProfilePic);
            }
        }
    }
    private void requestStoragePermission(boolean isCamera)
    {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new BaseMultiplePermissionsListener()
                {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted())
                        {
                            if (isCamera)
                            {
                                dispatchTakePictureIntent();
                            } else
                            {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied())
                        {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(
                        error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    @OnClick(R.id.img3)
    public void onViewClicked()
    {
        selectImage();
    }

    private void showSettingsDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Permissions");
        builder.setMessage(
                "This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
    private void openSettings()
    {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }*/

}
