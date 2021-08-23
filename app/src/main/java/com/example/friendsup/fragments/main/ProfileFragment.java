package com.example.friendsup.fragments.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.friendsup.API.JSONPlaceHolderApi;
import com.example.friendsup.R;
import com.example.friendsup.models.RegisteredUser;
import com.example.friendsup.repository.NetworkAction;
import com.example.friendsup.utils.KeyboardUtils;
import com.google.gson.GsonBuilder;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private TextView nominationHeadingTextView;
    private TextView nominationAboutTextView;
    private EditText nominationHeadingEditText;
    private EditText nominationAboutEditText;

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

        this.nominationHeadingEditText.setText(this.nominationHeadingTextView.getText().toString());
        this.nominationAboutEditText.setText(this.nominationAboutTextView.getText().toString());

    }

    private void switchToDefaultMode() {
        this.isEditMode = false;
        this.defaultScrollView.setVisibility(View.VISIBLE);
        this.editScrollView.setVisibility(View.GONE);
        this.cameraButton.setVisibility(View.GONE);

        this.nominationHeadingEditText.setText(this.nominationHeadingEditText.getText().toString());
        this.nominationAboutTextView.setText(this.nominationAboutEditText.getText().toString());

        updateCurrentData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                this.selectedImage = result.getUri();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), this.selectedImage);
                    File file = new File(getContext().getCacheDir(), "image");
                    file.createNewFile();

                    //Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();

                    this.uploadProfileImage(file);

//                    this.saveImageToGallery(bitmap);
                    this.getProfileImageFromGallery();

                    System.out.println("file lenght is: " + file.length());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("error ->", String.valueOf(error));
            }
        }
    }

    private void getProfileImageFromGallery() {
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.userProfileImage), Context.MODE_PRIVATE);
        String uriString = sharedPref.getString(getString(R.string.userProfileImage), "");
        if (uriString != "") {
            this.profileImage.setImageURI(Uri.parse(uriString));
        }
    }

    private void saveImageToGallery(Bitmap bitmap) {
        String savedImageURL = MediaStore.Images.Media.insertImage(
                getActivity().getContentResolver(),
                bitmap,
                "Bird",
                "Image of bird"
        );

        Uri savedImageURI = Uri.parse(savedImageURL);

        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(getString(R.string.userProfileImage), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.userProfileImage), String.valueOf(savedImageURI));
        editor.commit();
    }

    private void uploadProfileImage(File file) {

        Retrofit retrofit = new NetworkAction().initializeRetrofit();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", file.getName(), requestBody);

        JSONPlaceHolderApi jsonPlaceHolderApi = new NetworkAction().initializeApi(retrofit);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);

        String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

        JSONPlaceHolderApi uploadApis = retrofit.create(JSONPlaceHolderApi.class);
        Call call = uploadApis.uploadImage(parts, "Bearer " + JWTToken);
        call.enqueue(new Callback() {
            private Context context = getActivity().getApplicationContext();

            @Override
            public void onResponse(Call call, Response response) {
                System.out.println("Ok");
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                System.out.println("error " + t.getMessage());
//                Toast.makeText(context, "Server connection error check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                updateCurrentData();
                getCurrentUserData();
            }
        });
    }

    private void updateCurrentData() {
        RegisteredUser registeredUser = new RegisteredUser(this.nominationHeadingEditText.getText().toString());
        this.nominationAboutTextView.setText(nominationAboutEditText.getText());
        this.nominationHeadingTextView.setText(nominationHeadingEditText.getText());
        registeredUser.setAbout(this.nominationAboutEditText.getText().toString());
        sendNewUserData(registeredUser);
    }

    private void sendNewUserData(RegisteredUser registeredUser) {
        Retrofit retrofit = new NetworkAction().initializeRetrofit();

        JSONPlaceHolderApi jsonPlaceHolderApi = new NetworkAction().initializeApi(retrofit);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);

        String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

        Call<RegisteredUser> call = jsonPlaceHolderApi.updateCurrentUserData("Bearer " + JWTToken, registeredUser);

        call.enqueue(new Callback<RegisteredUser>() {
            @Override
            public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                System.out.println("Ok");
                Log.d("Search response body is", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                System.out.println("error " + t.getMessage());
                makeRequestUserData(jsonPlaceHolderApi, JWTToken);
            }
        });
    }

    private void getCurrentUserData() {
        Retrofit retrofit = new NetworkAction().initializeRetrofit();

        JSONPlaceHolderApi jsonPlaceHolderApi = new NetworkAction().initializeApi(retrofit);

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);

        String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

        makeRequestUserData(jsonPlaceHolderApi, JWTToken);
    }

    private void makeRequestUserData(JSONPlaceHolderApi jsonPlaceHolderApi, String JWTToken) {
        Call<RegisteredUser> call = jsonPlaceHolderApi.getCurrentUserData("Bearer " + JWTToken);

        call.enqueue(new Callback<RegisteredUser>() {
            @Override
            public void onResponse(Call<RegisteredUser> call, Response<RegisteredUser> response) {
                System.out.println("Ok");
                Log.d("Search response body is", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                setCurrentUserData(response.body());
            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                System.out.println("error " + t.getMessage());
                makeRequestUserData(jsonPlaceHolderApi, JWTToken);
            }
        });
    }

    private void setCurrentUserData(RegisteredUser registeredUser) {
        this.nominationHeadingTextView.setText(registeredUser.getUsername());
        this.nominationAboutTextView.setText(registeredUser.getAbout());
        this.nominationHeadingEditText.setText(registeredUser.getUsername());
        this.nominationAboutEditText.setText(registeredUser.getAbout());

        try {
            Glide.with(getActivity().getApplicationContext()).load(registeredUser.getUserPhoto()).into(this.profileImage);
        } catch (Exception ex) {
            Log.e("Exception", ex.toString());
        }
    }

    private void startCrop() {
        System.out.println("ЩА кропить будем");
        Intent intent = CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setAspectRatio(16, 12)
                .getIntent(getContext());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }


//    public void keyBoardStateChangeListener() {
//        getActivity().getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        Rect r = new Rect();
//                        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                        int screenHeight = getActivity().getWindow().getDecorView().getRootView().getHeight();
//                        int keypadHeight = screenHeight - r.bottom;
//                        if (keypadHeight > screenHeight * 0.15) {
//                            Toast.makeText(getActivity().getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getActivity().getApplicationContext(), "Closed", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }


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
        this.nominationHeadingTextView = (TextView) v.findViewById(R.id.profile_heading);
        this.nominationAboutTextView = (TextView) v.findViewById(R.id.profile_about);
        this.nominationHeadingEditText = (EditText) v.findViewById(R.id.profile_heading_edit);
        this.nominationAboutEditText = (EditText) v.findViewById(R.id.profile_about_edit);

        this.getProfileImageFromGallery();
        this.getCurrentUserData();

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

        KeyboardUtils.addKeyboardToggleListener(getActivity(), new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isEditMode) {
                    ConstraintLayout constraintLayout = v.findViewById(R.id.main_constraint_nomination);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.userScrollViewEdit,ConstraintSet.TOP,R.id.main_constraint_nomination,ConstraintSet.TOP,0);
                    constraintSet.applyTo(constraintLayout);
                }
                if (!isVisible && isEditMode){
                    ConstraintLayout constraintLayout = v.findViewById(R.id.main_constraint_nomination);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(constraintLayout);
                    constraintSet.connect(R.id.userScrollViewEdit,ConstraintSet.TOP,R.id.userAvatar,ConstraintSet.BOTTOM,0);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        });

//        keyBoardStateChangeListener();

//        this.editButton

        return v;
    }
}