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
import android.os.Parcelable;
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
import android.widget.AdapterView;
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
import java.io.FileInputStream;
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
 * @author Hasan Ashraf 2020
 */
public class OrderTshirt extends Fragment {
    /**
     * Order T-Shirt Class, All Commands Conducted here
     */

    //class wide variables
    private String mPhotoPath;
    private Spinner mSpinner;
    private EditText mCustomerName;
    private EditText meditDelivery;
    private Button mSendButton;
    private ImageView mCameraImage;
    private EditText mcaptureImage;
    String currentPhotoPath;
    public int ordertype = 0;

    //static keys
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

    /**
     * Empty Public Constructor
     */
    public OrderTshirt() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Create the UI when the Order T-Shirt Interface is clicked by user
         *
         * @param mCameraImage, mSendButton, mSpinner, meditDelivery
         * @return The full correct user interface presented to the user
         */

        // Inflate the layout for this fragment get the root view.
        final View root = inflater.inflate(R.layout.fragment_order_tshirt, container, false);

        mCustomerName = root.findViewById(R.id.editCustomer);
        meditDelivery = root.findViewById(R.id.editDeliver);
        meditDelivery.setImeOptions(EditorInfo.IME_ACTION_DONE);
        meditDelivery.setRawInputType(InputType.TYPE_CLASS_TEXT);

        mCameraImage = root.findViewById(R.id.imageView);
        mcaptureImage = root.findViewById(R.id.image_capture);


        //set a listener on the the camera image
        mCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Camera Image View Clicked");
                askCameraPermissions();
            }
        });

        // send content to email
        mSendButton = root.findViewById(R.id.sendButton);

        //initialise spinner using the integer array
        mSpinner = root.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.ui_time_entries, R.layout.spinner_days);
        mSpinner.setAdapter(adapter);

        meditDelivery.addTextChangedListener(loginTextWatcher);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                String element = mSpinner.getSelectedItem().toString();
                Log.d(TAG, "test1 " + element );
                String defaultdays = "0";

                if (element.equals(defaultdays)) {
                    mSendButton.setEnabled(false);
                    Log.d(TAG, "test2 " + element + defaultdays);

                    //element = mSpinner.getSelectedItem().toString();
                }
                else {
                    mSendButton.setEnabled(true);
                    Log.d(TAG, "test3 " + element + defaultdays);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        //set a listener to start the email intent.
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendEmail(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        /**
         * TextWatcher user to Call out the delivery address field and check if its is populated or not
         *
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String deliveryadd = meditDelivery.getText().toString().trim();

            if (deliveryadd.isEmpty()){
                mSpinner.setEnabled(true);
                mSendButton.setEnabled(false);
                Log.d(TAG, "test11 ");
            }
            else{
                mSpinner.setEnabled(false);
                mSendButton.setEnabled(true);
                Log.d(TAG, "test12 ");
                ordertype = 1;
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void askCameraPermissions() {
        /**
         * Ask for the Permission to Access the Camera Function
         */
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
        /**
         * Camera is opened and the Image is taken and send to a file Prvider to be calle
         */
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d(TAG, "Camera Activity Opened");


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
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                }
            }
    }

    private File createImageFile() throws IOException {
        /**
         * Create a Image File to be Later used in Email Command
         */
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

    // handle permisison result. if permission granted or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        /**
         * Permission function for the Camera Command
         */
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
        /**
         * Activity that is called out when using the Camera functions and dusplay image to the Image View
         */
        Log.d(TAG, "Camera OnActivityResult");

        if(requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(currentPhotoPath);
                mCameraImage.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                getContext().sendBroadcast(mediaScanIntent);

            }
        }
    }

    private String createOrderSummary(View v) throws IOException {
        /**
         * Create the message that is sent to the email function
         */
        String orderMessage = "";
        String deliveryInstruction = meditDelivery.getText().toString();
        String customerName = getString(R.string.customer_name) + " " + mCustomerName.getText().toString();

        orderMessage += customerName + "\n" + "\n" + getString(R.string.order_message_1);


        if (ordertype == 1){
            orderMessage += "\n" + "Deliver my order to the following address: ";
            orderMessage += "\n" + deliveryInstruction;
        }
        else{
            orderMessage += "\n" + getString(R.string.order_message_collect) + mSpinner.getSelectedItem().toString() + "days";
            orderMessage += "\n" + getString(R.string.order_message_end) + "\n" + mCustomerName.getText().toString();
        }

        //Image File path
        orderMessage += "\n" + "\n" + createImageFile();

        return orderMessage;
    }

    //Update me to send an email
    private void sendEmail(View v) throws IOException {
        /**
         * By Clicking the button the content that is presnet in the App is sent to the email app to be proceeds and send onwards
         */
        //check that Name is not empty, and ask do they want to continue

        String customerName = mCustomerName.getText().toString();
        String recipientlist = "mytshirt@dcu.ie";

        if (mCustomerName == null || customerName.equals(""))
        {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();

        } else {
            Log.d(TAG, "sendEmail: should be sending an email with "+createOrderSummary(v));

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientlist});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Order Request");
            intent.putExtra(Intent.EXTRA_TEXT, createOrderSummary(v));



            Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(createImageFile().toString()));

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "choose an email client"));
        }
    }
}
