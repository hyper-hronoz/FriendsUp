package com.example.friendsup.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.example.friendsup.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ImageButton editButton;
    private ScrollView editScrollView;
    private ScrollView defaultScrollView;
    private boolean isEditMode = false;
    private ImageButton messageButton;
    private ImageButton cameraButton;
    private ImageView profileImage;
    private Uri selectedImage;
    private Uri uri;
    private String part_image;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void switchEditMode() {
        if (this.isEditMode) {
            switchToDefaultMode();
        } else {
            switchToEditMode();
        }
    }

    private void switchToEditMode() {
        this.isEditMode = true;
        this.defaultScrollView.setVisibility(View.GONE);
        this.editScrollView.setVisibility(View.VISIBLE);
        this.cameraButton.setVisibility(View.VISIBLE);
    }

    private void switchToDefaultMode() {
        this.isEditMode = false;
        this.defaultScrollView.setVisibility(View.VISIBLE);
        this.editScrollView.setVisibility(View.GONE);
        this.cameraButton.setVisibility(View.GONE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super method removed
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//            this.selectedImage = data.getData();
//            String[] imageprojection = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getActivity().getContentResolver().query(this.selectedImage,imageprojection,null,null,null);
//
//            if (cursor != null)
//            {
//                cursor.moveToFirst();
//                int indexImage = cursor.getColumnIndex(imageprojection[0]);
//                this.part_image = cursor.getString(indexImage);
//
//                if(this.part_image != null)
//                {
//                    File image = new File(part_image);
////                    this.uploadProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(image.getAbsolutePath()));
//                    System.out.println("File length: " + image.length());
//                    this.profileImage.setImageURI(this.selectedImage);
//                }
//            }
//        }
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
////            if (resultCode == RESULT_OK) {
//                Uri resultUri = result.getUri();
//            System.out.println("URI: " + resultUri);
////            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////                Exception error = result.getError();
////            }
//        }
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            Uri imageuri = CropImage.getPickImageResultUri(getActivity(), data);
//            System.out.println(imageuri);
//            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageuri)) {
//                uri = imageuri;
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//            } else {
//                System.out.println("Image uri " + uri);
//                startCrop(imageuri);
//            }
//        }
//
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
////            if (requestCode == RESULT_OK) {
////                .setImage
////            }
//            this.profileImage.setImageURI(result.getUri());
//
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                this.selectedImage = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), this.selectedImage);
                    File f = new File(getContext().getCacheDir(), "image");
                    f.createNewFile();

//Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                    this.profileImage.setImageBitmap(bitmap);

                    System.out.println("file lenght is: " + f.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                this.profileImage.setImageURI(this.selectedImage);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("error ->", String.valueOf(error));
            }
        }
    }

    private void startCrop() {
        System.out.println("ЩА кропить будем");
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(16, 9)
                .getIntent(getContext());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_nominations, container, false);

        this.editButton = (ImageButton) v.findViewById(R.id.edit_button);
        this.defaultScrollView = (ScrollView) v.findViewById(R.id.userScrollView);
        this.editScrollView = (ScrollView) v.findViewById(R.id.userScrollViewEdit);
        this.messageButton = (ImageButton) v.findViewById(R.id.message_button);
        this.cameraButton = (ImageButton) v.findViewById(R.id.camera_button);
        this.messageButton.setVisibility(View.GONE);
        this.editButton.setVisibility(View.VISIBLE);
        this.profileImage = (ImageView) v.findViewById(R.id.userAvatar);

        this.cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCrop();
            }
        });

        this.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchEditMode();
            }
        });
//        this.editButton


        return v;
    }
}