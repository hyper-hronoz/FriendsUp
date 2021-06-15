package com.example.friendsup;

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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendsup.models.BeginChat;
import com.example.friendsup.models.ImageMessage;
import com.example.friendsup.models.TextMessage;
import com.example.friendsup.ui.MessageAdapter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.WebSocket;


public class ChatActivity extends BaseActivity implements TextWatcher {

    private String chatterId;
    private WebSocket webSocket;
    private String SERVER_PATH = "ws://192.168.0.15:80";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.chatterId = getIntent().getStringExtra(getString(R.string.chatActivity));

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

    private void initiateSocketConnection() {
        try {
            socket = IO.socket("http://192.168.0.15:80");
            socket.connect();

            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.JWTTokenSharedPreferencesKey), Context.MODE_PRIVATE);
            String JWTToken = sharedPref.getString(getString(R.string.JWTToken), "");

            BeginChat beginChat = new BeginChat(JWTToken, this.chatterId);

            String json = new Gson().toJson(beginChat);

            socket.emit("begin-chat", json);

            socket.on("message", onNewMessage);

            this.initializeView();

        } catch (URISyntaxException e) {
            System.out.println();
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

//    private class SocketListener extends WebSocketListener {
//
//        @Override
//        public void onOpen(WebSocket webSocket, Response response) {
//            super.onOpen(webSocket, response);
//
//            System.out.println("Response is: " + response);
//
//            runOnUiThread(() -> {
//                Toast.makeText(ChatActivity.this,
//                        "Socket Connection Successful!",
//                        Toast.LENGTH_SHORT).show();
//
//                initializeView();
//            });
//
//        }
//
//        @Override
//        public void onMessage(WebSocket webSocket, String text) {
//            super.onMessage(webSocket, text);
//
//            runOnUiThread(() -> {
//
//
//
//            });
//
//        }
//    }

    private void initializeView() {

        System.out.println("Initilizing view");
        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        this.recyclerView = findViewById(R.id.recyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());
        this.recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageEdit.addTextChangedListener(this);

        sendBtn.setOnClickListener(v -> {

            try {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("userId", chatterId);
                jsonObject.put("message", messageEdit.getText().toString());

                socket.emit("message", jsonObject.toString());

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

            socket.emit("message", jsonObject.toString());

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}