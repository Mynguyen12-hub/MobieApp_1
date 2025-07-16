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

        // ✅ Bottom navigation
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

        // ✅ Ánh xạ view
        rvMessages = findViewById(R.id.rvMessages);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        // ✅ Khởi tạo danh sách tin nhắn và adapter
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);

        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(chatAdapter);
        rvMessages.setItemAnimator(new SlideInLeftAnimator());

        // ✅ Gửi tin nhắn khi click
        btnSend.setOnClickListener(v -> {
            String msg = edtMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(msg)) {
                sendMessage(msg);
                edtMessage.setText("");
            }
        });
    }

    private void sendMessage(String userMessage) {
        messageList.add(new ChatMessage(userMessage, true)); // Tin nhắn người dùng

        // ✅ Danh sách phản hồi ngẫu nhiên
        String[] autoReplies = {
                "🌸 Cảm ơn bạn đã nhắn tin!",
                "🌼 Chúng tôi sẽ phản hồi sớm nhất!",
                "💐 Bạn cần hỗ trợ gì thêm không?",
                "🌷 Cảm ơn vì đã ghé thăm shop hoa của chúng tôi!",
                "🌻 Mình có thể giúp gì cho bạn?",
                "💐 Xem tất cả sản phẩm trong danh sách nhá"
        };

        int randomIndex = (int) (Math.random() * autoReplies.length);
        messageList.add(new ChatMessage(autoReplies[randomIndex], false)); // Phản hồi

        // ✅ Cập nhật UI
        chatAdapter.notifyItemRangeInserted(messageList.size() - 2, 2);
        rvMessages.scrollToPosition(messageList.size() - 1);
    }
}
