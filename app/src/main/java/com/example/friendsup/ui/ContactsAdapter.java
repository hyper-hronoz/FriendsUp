package com.example.friendsup.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendsup.ChatActivity;
import com.example.friendsup.R;
import com.example.friendsup.models.Chat;
import com.example.friendsup.models.Contact;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private List<Chat> contacts;
    private LayoutInflater mInflater;
    private Activity activity;

    // Pass in the contact array into the constructor
    public ContactsAdapter(List<Chat> contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;
    }

    public ContactsAdapter(List<Chat> chats) {
        this.contacts = chats;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.contatic_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat contact = contacts.get(position);

        // Set item views based on your views and data model
        TextView nameTextView = holder.nameTextView;
        nameTextView.setText(contact.getUsername());


        TextView massageTextView = holder.lastMessage;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newChatActivity(contacts.get(position).getId());
            }
        });

    }

    private void newChatActivity(String id) {
        Intent intent = new Intent(this.activity.getApplicationContext(), ChatActivity.class);
        intent.putExtra(this.activity.getApplicationContext().getString(R.string.chatActivity), id);
        this.activity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        private TextView lastMessage;
        private ImageView contactAvatar;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            lastMessage = (TextView) itemView.findViewById(R.id.contact_last_message);
            contactAvatar = (ImageView) itemView.findViewById(R.id.contactProfile);

        }
    }
}