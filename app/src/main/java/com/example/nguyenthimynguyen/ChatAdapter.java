package com.example.nguyenthimynguyen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatMessage> messages;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isSent() ? VIEW_TYPE_SENT : VIEW_TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).txtSentMessage.setText(message.getContent());
        } else if (holder instanceof ReceivedViewHolder) {
            ((ReceivedViewHolder) holder).txtReceivedMessage.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView txtSentMessage;

        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtSentMessage = itemView.findViewById(R.id.txtSentMessage);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView txtReceivedMessage;

        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtReceivedMessage = itemView.findViewById(R.id.txtReceivedMessage);
        }
    }
}