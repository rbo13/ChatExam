package com.example.bito.chatexam;

/**
 * Created by bito on 4/18/2017.
 */

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.Query;

import android.text.format.DateFormat;
import java.util.Date;

public class MessageAdapter extends FirebaseListAdapter<ChatMessage> {

    private ChatActivity chatActivity;

    /**
     * @param activity    The activity containing the ListView
     * @param modelClass  Firebase will marshall the data at a location into
     *                    an instance of a class that you provide
     * @param modelLayout This is the layout used to represent a single list item.
     *                    You will be responsible for populating an instance of the corresponding
     *                    view with the data from an instance of modelClass.
     * @param ref         The Firebase location to watch for data changes. Can also be a slice of a location,
     *                    using some combination of {@code limit()}, {@code startAt()}, and {@code endAt()}.
     */
    public MessageAdapter(ChatActivity activity, Class<ChatMessage> modelClass, int modelLayout, Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.chatActivity = activity;
    }

    @Override
    protected void populateView(View v, ChatMessage model, int position) {

        TextView messageText = (TextView) v.findViewById(R.id.message_text);
        TextView messageUser = (TextView) v.findViewById(R.id.message_user);
        TextView messageTime = (TextView) v.findViewById(R.id.message_time);

        messageText.setText(model.getMessageText());
        messageUser.setText(model.getMessageUserId().equals(chatActivity.getLoggedInUsername()) ? "You" : model.getMessageUser());

        // Format the date
        messageTime.setText(DateFormat.format("hh:mm a", model.getMessageTime()));

    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        ChatMessage chatMessage = getItem(position);

        if(chatMessage.getMessageUserId().equals(chatActivity.getLoggedInUsername()))
            view = chatActivity.getLayoutInflater().inflate(R.layout.item_out_message, viewGroup, false);
        else
            view = chatActivity.getLayoutInflater().inflate(R.layout.item_in_message, viewGroup, false);

        // Generate view
        populateView(view, chatMessage, position);
        return view;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }
}
