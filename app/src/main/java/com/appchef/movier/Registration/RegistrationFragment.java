package com.appchef.movier.Registration;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.appchef.movier.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegistrationFragment extends Fragment {

    // Views
    private Button proceedBtn;
    private TextInputLayout fullNameLayout, usernameLayout, ageLayout, contactLayout, genderLayout;
    private ImageView profileIv;

    private Context context;

    // Firebase
    private FirebaseFirestore fireStore;
    private FirebaseAuth mAuth;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    private String[] cameraPermission;
    private String[] storagePermission;

    private Uri image_uri;

    // For Log Test
    String TAG = "NPE";

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        Init(view);

        context = getContext();

        profileIv.setOnClickListener(v -> showImagePickDialog());

        proceedBtn.setOnClickListener(view1 -> checkCredentials());

        return view;
    }

    private void showImagePickDialog() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            if (!checkCameraPermission()) {
                                requestCameraPermission();
                            } else {
                                pickFromCamera();
                            }
                        } else if (which == 1) {
                            if (!checkStoragePermission()) {
                                requestStoragePermission();
                            } else {
                                pickFromGallery();
                            }
                        }
                    }
                }).create().show();
    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private boolean checkStoragePermission() {

        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void pickFromGallery() {

        Intent gallleryIntent = new Intent(Intent.ACTION_PICK);
        gallleryIntent.setType("image/*");
        startActivityForResult(gallleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp_Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Log.d("Image Uri", image_uri + "");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseInit();
    }

    private void FirebaseInit() {
        //Fire base Init
        mAuth = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        collectionReference = fireStore.collection("users");
        documentReference = collectionReference.document("UUID");
    }

    private void checkCredentials() {
        if (!checkFullName() | !username() | !age() | !gender() | !checkContactNumber()) {
            return;
        }

        saveChangesToFireStore();
        Log.i(TAG, FirebaseAuth.getInstance().getUid() + ": Is the Unique Id of the User" +
                "Username:" + usernameLayout.getEditText().getText().toString());
    }

    private boolean checkContactNumber() {
        String contact = contactLayout.getEditText().getText().toString().trim();

        if (contact.isEmpty()) {
            contactLayout.setError("Field can't be empty");
            return false;
        } else if (contact.length() != 10) {
            contactLayout.setError("Not a valid Contact number");
            return false;
        } else {
            contactLayout.setErrorEnabled(false);
            contactLayout.setError(null);
            return true;
        }
    }

    private boolean gender() {
        String gender = genderLayout.getEditText().getText().toString();

        if (gender.isEmpty()) {
            genderLayout.setError("Field can't be empty");
            return false;
        } else {
            genderLayout.setError(null);
            return true;
        }
    }

    private boolean age() {
        String age = ageLayout.getEditText().getText().toString();

        if (age.isEmpty()) {
            ageLayout.setError("Field can't be empty");
            return false;
        } else {
            ageLayout.setError(null);
            return true;
        }
    }

    private boolean username() {
        String username = usernameLayout.getEditText().getText().toString();

        if (username.isEmpty()) {
            usernameLayout.setError("Field can't be empty");
            return false;
        } else {
            usernameLayout.setError(null);
            return true;
        }
    }

    private boolean checkFullName() {
        // To check the fields.
        String fullName = fullNameLayout.getEditText().getText().toString();

        if (fullName.isEmpty()) {
            fullNameLayout.setError("Field can't be empty");
            return false;
        } else {
            fullNameLayout.setError(null);
            return true;
        }
    }

    private void saveChangesToFireStore() {

        String fileAndPath = "profile_image/" + "" + mAuth.getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(fileAndPath);
        storageReference.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadImageUri = uriTask.getResult();

                        if (uriTask.isSuccessful()) {
                            Map<String, String> userDataMap = new HashMap<String, String>();

                            String username = usernameLayout.getEditText().getText().toString();

                            userDataMap.put("fullName", fullNameLayout.getEditText().getText().toString());
                            userDataMap.put("username", username);
                            userDataMap.put("age", ageLayout.getEditText().getText().toString());
                            userDataMap.put("gender", genderLayout.getEditText().getText().toString());
                            userDataMap.put("contactNumber", contactLayout.getEditText().getText().toString());
                            userDataMap.put("profileUrl", downloadImageUri+"");

                            String uniqueID = mAuth.getUid();
                            FirebaseFirestore.getInstance().collection("users").document(uniqueID).collection(username).document(uniqueID)
                                    .set(userDataMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Successfully Registered the Data", Toast.LENGTH_SHORT).show();
                                            changeFragment();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Failed to register the data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });


    }

    private void changeFragment() {
        try {
            getFragmentManager().beginTransaction()
                    .replace(R.id.registrationFrameContainer, new GenresSelectionFragment()).commit();

        } catch (NullPointerException e) {
            Log.i(TAG, Log.getStackTraceString(e));
        }
    }

    private void Init(View view) {

        proceedBtn = view.findViewById(R.id.proceedBtn);
        fullNameLayout = view.findViewById(R.id.fullNameLayout);
        usernameLayout = view.findViewById(R.id.usernameLayout);
        ageLayout = view.findViewById(R.id.ageLayout);
        contactLayout = view.findViewById(R.id.contactLayout);
        genderLayout = view.findViewById(R.id.genderLayout);
        profileIv = view.findViewById(R.id.profileIv);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(getActivity(), "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE: {

                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                image_uri = data.getData();
                profileIv.setImageURI(image_uri);
                Log.d("MAGE_PICK_CAMERA_CODE", image_uri + "");
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                profileIv.setImageURI(image_uri);
                Log.d("MAGE_PICK_CAMERA_CODE", image_uri + "");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}