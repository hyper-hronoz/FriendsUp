package com.example.friendsup.fragments.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendsup.BaseActivity;
import com.example.friendsup.R;
import com.example.friendsup.models.ImageMessage;
import com.example.friendsup.models.MessengerPagination;
import com.example.friendsup.models.TextMessage;
import com.example.friendsup.repository.NetworkConfig;
import com.example.friendsup.ui.MessageAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class ChatActivity extends BaseActivity implements TextWatcher {

    private String chatterId;
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;
    private int messagesWrote = 0;
    private int pagination = 1;
    private boolean isPreviousMessagesGet = false;
    private String JWTToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.chatterId = getIntent().getStringExtra(getString(R.string.chatActivity));

        this.recyclerView = findViewById(R.id.recyclerView);

        initiateSocketConnection();
    }

    private Socket socket;



    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("message recieved");
                        System.out.println(args[0]);

                        TextMessage textMessage = new Gson().fromJson(String.valueOf(args[0]), TextMessage.class);
                        ImageMessage imageMessage = new Gson().fromJson(String.valueOf(args[0]), ImageMessage.class);

                        JSONObject jsonObject = new JSONObject(String.valueOf(args[0]));
                        if (textMessage.getMessage() != null) {
                            jsonObject.put("name", textMessage.getUsername());
                            jsonObject.put("message", textMessage.getMessage());
                        } else {
                            jsonObject.put("name", imageMessage.getUsername());
                            jsonObject.put("image", imageMessage.getImage());
                        }

                        jsonObject.put("isSent", false);

                        updateMessagesCounter();

                        messageAdapter.addItem(jsonObject);

                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
//
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onGetMessages = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(args[0]);
                    Toast.makeText(getApplicationContext(), "JOb", Toast.LENGTH_LONG).show();

                    JsonElement jelement = new JsonParser().parse(String.valueOf(args[0]));
                    JsonArray output = jelement.getAsJsonArray();

                    for (int i = 0; i < output.size(); i++) {
                        JsonElement outputElement = output.get(i);
                        JsonObject iObjectOutput = outputElement.getAsJsonObject();

                        try {
                            JSONObject jsonObject = new JSONObject();
                            if (iObjectOutput.get("message") != null) {
                                TextMessage textMessage = new Gson().fromJson(outputElement.toString(), TextMessage.class);
                                jsonObject.put("username", textMessage.getUsername());
                                jsonObject.put("message", textMessage.getMessage());
                            }
                            else if (iObjectOutput.get("image") != null) {
                                ImageMessage imageMessage = new Gson().fromJson(outputElement.toString(), ImageMessage.class);
                                jsonObject.put("username", imageMessage.getUsername());
                                jsonObject.put("image", imageMessage.getImage());
                            }

                            jsonObject.put("isSent", Boolean.parseBoolean(String.valueOf(iObjectOutput.get("isUsers"))));

                            updateMessagesCounter();

                            messageAdapter.addItem(jsonObject);

                            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

    private void updateMessagesCounter() {
        this.messagesWrote++;
    }

    private void initiateSocketConnection() {
        System.out.println("initilizing socket connection");
        try {
            socket = IO.socket(NetworkConfig.BASE_URL);
            socket.connect();

            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);

            this.JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

            addPreviousMessages();

            socket.on("get-previous-messages", onGetMessages);

            socket.on("message", onNewMessage);

            this.initializeView();

        } catch (URISyntaxException e) {
            System.out.println(e);
        }

    }

    private void addPreviousMessages() {
        MessengerPagination messengerPagination = new MessengerPagination(this.pagination, this.messagesWrote, this.JWTToken, this.chatterId);
        String json = new Gson().toJson(messengerPagination);
        socket.emit("get-previous-messages", json);
        pagination++;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.socket.disconnect();
        this.socket.off("new message", onNewMessage);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {

            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }


    private void initializeView() {

        System.out.println("Initilizing view");
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);


        messageAdapter = new MessageAdapter(getLayoutInflater());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);


        this.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
//                    addPreviousMessages();
                }
            }
        });

        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(v -> {

            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("userId", chatterId);
                jsonObject.put("message", messageEdit.getText().toString());
                jsonObject.put("jwt", this.JWTToken);
                jsonObject.put("id", this.chatterId);

                socket.emit("message", jsonObject.toString());

                updateMessagesCounter();

                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                System.out.println("Image: " + image);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        System.out.println(base64String);


        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", chatterId);
            jsonObject.put("image", base64String);
            jsonObject.put("jwt", this.JWTToken);
            jsonObject.put("id", this.chatterId);

            socket.emit("message", jsonObject.toString());

            updateMessagesCounter();

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}