package com.example.headphones_ecommerce_store.ui.support;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.headphones_ecommerce_store.DAO.MessageDAO;
import com.example.headphones_ecommerce_store.R;
import com.example.headphones_ecommerce_store.adapters.MessageAdapter;
import com.example.headphones_ecommerce_store.model.Message;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SupportActivity extends AppCompatActivity {
    private static final String TAG = "SupportActivity";
    private EditText etMessage;
    private Button btnSend;
    private RecyclerView rvChat;
    private MessageDAO messageDAO;
    private String currentUserEmail;
    private String supportEmail = "support@example.com";
    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
//        Log.d(TAG, "SupportActivity onCreate called");

        // Initialize views and DAO
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        rvChat = findViewById(R.id.rvChat);
        messageDAO = new MessageDAO(this);

        // Lấy email người dùng từ SharedPreferences
        android.content.SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserEmail = prefs.getString("userEmail", "user@example.com");
//        Log.d(TAG, "Current user email: " + currentUserEmail);

        // Cấu hình RecyclerView
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        messageList = messageDAO.getMessages(currentUserEmail, supportEmail);
        Log.d(TAG, "Message list size: " + (messageList != null ? messageList.size() : "null"));
        if (messageList != null && !messageList.isEmpty()) {
            messageAdapter = new MessageAdapter(this, messageList, currentUserEmail);
            rvChat.setAdapter(messageAdapter);
        } else {
            Log.d(TAG, "No messages to display");
            Toast.makeText(this, "No chat history available", Toast.LENGTH_SHORT).show();
        }

        // Send button click listener
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = etMessage.getText().toString().trim();
                if (!messageContent.isEmpty()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timestamp = sdf.format(new Date());

                    // Thêm tin nhắn của người dùng (hiển thị bên phải)
                    long userMessageId = messageDAO.addMessage(currentUserEmail, supportEmail, messageContent, timestamp);
                    Log.d(TAG, "User message inserted with ID: " + userMessageId);

                    if (userMessageId > 0) {
                        etMessage.setText("");

                        // Tự động trả lời từ hệ thống (hiển thị bên trái)
                        String autoReply = generateAutoReply(messageContent);
                        long replyId = messageDAO.addMessage(supportEmail, currentUserEmail, autoReply, timestamp);
                        Log.d(TAG, "Auto reply inserted with ID: " + replyId);

                        // Cập nhật danh sách tin nhắn
                        messageList = messageDAO.getMessages(currentUserEmail, supportEmail);
                        messageAdapter = new MessageAdapter(SupportActivity.this, messageList, currentUserEmail);
                        rvChat.setAdapter(messageAdapter);
                        rvChat.scrollToPosition(messageList.size() - 1);

                        Toast.makeText(SupportActivity.this, "Message sent and replied", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SupportActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SupportActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm tạo phản hồi tự động dựa trên nội dung tin nhắn
    private String generateAutoReply(String userMessage) {
        if (userMessage.toLowerCase().contains("help")) {
            return "Cảm ơn bạn đã liên hệ! Chúng tôi đang xử lý yêu cầu của bạn. Vui lòng đợi trong giây lát.";
        } else if (userMessage.toLowerCase().contains("order")) {
            return "Vui lòng cung cấp số đơn hàng để chúng tôi hỗ trợ bạn!";
        } else {
            return "Cảm ơn bạn! Chúng tôi sẽ phản hồi sớm nhất có thể.";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageDAO.close();
        Log.d(TAG, "SupportActivity onDestroy called");
    }
}