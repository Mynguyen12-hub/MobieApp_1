package com.example.nguyenthimynguyen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText edtMessage;
    private Button btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rvChat = findViewById(R.id.rvChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);

        btnSend.setOnClickListener(v -> {
            String content = edtMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                chatMessages.add(new ChatMessage(content, true)); // true = tin nhắn gửi đi
                chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                rvChat.scrollToPosition(chatMessages.size() - 1);
                edtMessage.setText("");

                // Giả lập phản hồi từ hệ thống hoặc người khác
                rvChat.postDelayed(() -> {
                    chatMessages.add(new ChatMessage("Đây là phản hồi tự động.", false));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    rvChat.scrollToPosition(chatMessages.size() - 1);
                }, 1000);
            }
        });
    }
}