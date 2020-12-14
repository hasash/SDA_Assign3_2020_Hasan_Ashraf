package com.example.sdaassign32020;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


/*
 * A simple {@link Fragment} subclass.
 * @author Chris Coughlan 2019
 */
public class OrderTshirt extends Fragment {


    //class wide variables
    private String mPhotoPath;
    private Spinner mSpinner;
    private EditText mCustomerName;
    private EditText meditDelivery;
    private Button mSendButton;
    private ImageView mCameraImage;
    private EditText mcaptureImage;
    String currentPhotoPath;

    //static keys
    private static final int REQUEST_TAKE_PHOTO = 2;
    private static final String TAG = "OrderTshirt";
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    //private String currentPhotoPath;
    private StorageReference storageReference;

    private static final int RESULT_OK = -1 ;
    Uri image_uri;

    private Context mContext;

    private static String root1 = null;
    private static String imageFolderPath = null;
    private String imageName = null;
    private static Uri fileUri = null;
    private static final int CAMERA_IMAGE_REQUEST=1;


    //private String[] array = getResources().getStringArray(R.array.ui_time_entries);

    public OrderTshirt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment get the root view.
        final View root = inflater.inflate(R.layout.fragment_order_tshirt, container, false);

        mCustomerName = root.findViewById(R.id.editCustomer);
        meditDelivery = root.findViewById(R.id.editDeliver);
        meditDelivery.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meditDelivery.setRawInputType(InputType.TYPE_CLASS_TEXT);


        mCameraImage = root.findViewById(R.id.imageView);
        mcaptureImage = root.findViewById(R.id.image_capture);


        storageReference = FirebaseStorage.getInstance().getReference();

        //set a listener on the the camera image
        mCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Camera Image View Clicked");
                askCameraPermissions();
                //Log.d(TAG, "Camera image Clicked");
            }
        });


        // send content to email
        mSendButton = root.findViewById(R.id.sendButton);

       // final String[] array = root.



        //initialise spinner using the integer array
        mSpinner = root.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);
        mSpinner.setEnabled(true);
        meditDelivery.addTextChangedListener(loginTextWatcher);



        //set a listener to start the email intent.
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(v);
            }
        });

        return root;
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String deliveryadd = meditDelivery.getText().toString().trim();


            //String entries = ui_time_entries(0);

            //String entries = array[0];

            if (deliveryadd.isEmpty()){
                //mSendButton.setEnabled(false);
                String[] array = getResources().getStringArray(R.array.ui_time_entries);
                String first_element = array[0];

                /// DOES THE APP KNOW THAT THE VARAILE HAS BEEN UPDATED USING THE SPINNER??
                if (first_element != "0"){
                    mSendButton.setEnabled(true);
                }
                else {
                    mSpinner.setEnabled(true);
                    mSendButton.setEnabled(false);
                }


            }
            else{
                mSpinner.setEnabled(true);
                mSendButton.setEnabled(true);
            }
            //mSendButton.setEnabled(!deliveryadd.isEmpty()); // || !entries.isEmpty());
        }



        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void askCameraPermissions() {
        //Toast.makeText(MainActivity, "Hello", Toast.LENGTH_SHORT).show();
        //int permissionCheckStorage = ContextCompat.checkSelfPermission(getActivity(),
        //      Manifest.permission.CAMERA);

        //operating system over marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
            }
            else {
                //Permisions already granted
                openCamera();
            }
        }
        else {
            //system < marsmallow
            openCamera();
        }
        Log.d(TAG, "Camera Permission Granted");

    }


    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        Log.d(TAG, "Camera Activity Opened");


        // Ensure that there's a camera activity to handle the intent
        //if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            Log.d(TAG, "Camera if loop resolve");
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(TAG, "Camera if photo file null");
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "dcu.sdaassign3.hasanashraf",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        //}


    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    // handle permisison result. if permission granted or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "Camera onrequest Permision");

        switch (requestCode){
            case CAMERA_PERM_CODE: {
                if (grantResults.length > 0 && grantResults [0] == PackageManager.PERMISSION_GRANTED) {
                    //permision granted fomr pop up
                    openCamera();
                }
                else {
                    //permision in popup denied
                    Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, "Camera OnActivityResult");

        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                mCameraImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                //this.send
                getContext().sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(),contentUri);



            }

        }

        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
                mCameraImage.setImageURI(contentUri);

                uploadImageToFirebase(imageFileName,contentUri);


            }

        }


    }



/*
        if(requestCode == GALLERY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp +"."+getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " +  imageFileName);
                mCameraImage.setImageURI(contentUri);

                uploadImageToFirebase(imageFileName,contentUri);




            }

        }

 */



    /*
    //Take a photo note the view is being passed so we can get context because it is a fragment.
    //update this to save the image so it can be sent via email
    private void dispatchTakePictureIntent(View v)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getContext().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }
    */

    /*
     * Returns the Email Body Message, update this to handle either collection or delivery
     */

    private void uploadImageToFirebase(String name, Uri contentUri) {
        Log.d(TAG, "Camera Upload Image to Firebase");
        final StorageReference image = storageReference.child("pictures/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                    }
                });

                Toast.makeText(getContext(), "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private String createOrderSummary(View v)
    {
        String orderMessage = "";
        String deliveryInstruction = meditDelivery.getText().toString();
        String customerName = getString(R.string.customer_name) + " " + mCustomerName.getText().toString();

        orderMessage += customerName + "\n" + "\n" + getString(R.string.order_message_1);
        orderMessage += "\n" + "Deliver my order to the following address: ";
        orderMessage += "\n" + deliveryInstruction;
        orderMessage += "\n" + getString(R.string.order_message_collect) + mSpinner.getSelectedItem().toString() + "days";
        orderMessage += "\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();

        return orderMessage;
    }

    //Update me to send an email
    private void sendEmail(View v)
    {
        //check that Name is not empty, and ask do they want to continue

        String customerName = mCustomerName.getText().toString();
        String recipientlist = "mytshirt@dcu.ie";

        if (mCustomerName == null || customerName.equals(""))
        {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();

            /* we can also use a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Notification!").setMessage("Customer Name not set.").setPositiveButton("OK", null).show();*/

        } else {
            Log.d(TAG, "sendEmail: should be sending an email with "+createOrderSummary(v));

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, recipientlist);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Order Request");
            intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(v));

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "choose an email client"));
        }
    }

}
