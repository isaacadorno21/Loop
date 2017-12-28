package com.example.isaac.loop;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by isaac on 5/1/2017.
 */

public class ChatViewHolder extends RecyclerView.ViewHolder {
    private final TextView messageTextView;
    private final TextView authorTextView;
    private final RelativeLayout messageLayout;

    public ChatViewHolder(View itemView) {
        super(itemView);
        messageTextView = (TextView) itemView.findViewById(R.id.messageTextView);
        authorTextView = (TextView) itemView.findViewById(R.id.authorTextView);
        messageLayout = (RelativeLayout) itemView.findViewById(R.id.messageLayout);
    }

    public void setMessage(String message) {
        messageTextView.setText(message);
    }

    public void setAuthor(String author) {
        authorTextView.setText(author);
    }

    /**
     * Sets the color and layout depending on who sent the message.
     * @param isAuthor IF the user sent the message, than it's true. If they're the receiver, it's false.
     */
    public void setMessageColor(boolean isAuthor) {
        if (isAuthor) {
            messageTextView.setGravity(Gravity.END);
            authorTextView.setGravity(Gravity.END);
            messageLayout.setBackgroundColor(Color.parseColor("#3da5d8"));
        }
        else {
            messageTextView.setGravity(Gravity.START);
            authorTextView.setGravity(Gravity.START);
            messageLayout.setBackgroundColor(Color.parseColor("#231f20"));
        }
    }
}
