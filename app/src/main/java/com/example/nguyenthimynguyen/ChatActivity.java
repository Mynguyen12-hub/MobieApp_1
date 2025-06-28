package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView rvMessages;
    private EditText edtMessage;
    private ImageView btnSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_chat);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (id == R.id.nav_products) {
                startActivity(new Intent(this, ProductListActivity.class));
                return true;
            } else if (id == R.id.nav_admin) {
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            }
            return false;
        });

        // Ánh xạ
        rvMessages = findViewById(R.id.rvMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        // Khởi tạo danh sách tin nhắn
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        // Thiết lập RecyclerView
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(chatAdapter);

        // ✅ Thêm hiệu ứng trượt
        rvMessages.setItemAnimator(new SlideInLeftAnimator());

        // Gửi tin nhắn
        btnSend.setOnClickListener(v -> {
            String msg = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(msg)) {
                sendMessage(msg);
                edtMessage.setText("");
            }
        });
    }

    private void sendMessage(String userMessage) {
        messageList.add(new ChatMessage(userMessage, true)); // Tin người dùng
        messageList.add(new ChatMessage("🌼 Cảm ơn bạn đã nhắn tin!", false)); // Phản hồi đơn giản

        // Cập nhật giao diện
        chatAdapter.notifyItemRangeInserted(messageList.size() - 2, 2);
        rvMessages.scrollToPosition(messageList.size() - 1);
    }
}
