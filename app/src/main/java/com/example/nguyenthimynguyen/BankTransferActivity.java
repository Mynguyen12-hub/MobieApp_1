package com.example.nguyenthimynguyen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class BankTransferActivity extends AppCompatActivity {

    private ImageView imgQrCode;
    private Button btnConfirmPayment;
    private TextView txtNote;

    private String tenKhachHang, soDienThoai, diaChi, tenSanPham;
    private int soLuong;

    private final String SHEET_URL = "https://script.google.com/macros/s/AKfycbzQybOZ8Of4lR_oan2QEi-vm1Z9l_YadibdmBK62S2xZsCNuClZLMgaDtjkx__6ITi0hg/exec";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);

        imgQrCode = findViewById(R.id.imgQrCode);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        txtNote = findViewById(R.id.txtNote);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        tenKhachHang = intent.getStringExtra("ten");
        soDienThoai = intent.getStringExtra("sdt");
        diaChi = intent.getStringExtra("diachi");
        tenSanPham = intent.getStringExtra("tenSanPham");
        soLuong = intent.getIntExtra("soLuong", 1);

        // QR hiển thị
        imgQrCode.setImageResource(R.drawable.qrcode_vietcombank);

        txtNote.setText("💳 Ngân hàng: Vietcombank\n"
                + "🏦 Số tài khoản: 9352306655\n"
                + "👩‍💼 Chủ tài khoản: NGUYEN THI MY NGUYEN\n"
                + "📝 Nội dung: CK mua hoa shop Flower\n\n"
                + "Vui lòng chuyển khoản đúng nội dung và số tiền.\nSau đó nhấn xác nhận.");

        btnConfirmPayment.setOnClickListener(v -> sendOrderToGoogleSheet());
    }

    private String safeEncode(String input) {
        try {
            return URLEncoder.encode(input != null ? input : "", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void sendOrderToGoogleSheet() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("🔄 Đang kiểm tra thanh toán...");
        dialog.setCancelable(false);
        dialog.show();

        new Thread(() -> {
            try {
                String postData =
                        "ten=" + safeEncode(tenKhachHang) +
                                "&sdt=" + safeEncode(soDienThoai) +
                                "&diachi=" + safeEncode(diaChi) +
                                "&sanpham=" + safeEncode(tenSanPham) +
                                "&soluong=" + safeEncode(String.valueOf(soLuong)) +
                                "&pttt=" + safeEncode("Chuyển khoản - Vietcombank");

                URL url = new URL(SHEET_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                OutputStream os = conn.getOutputStream();
                os.write(postData.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                Log.d("HTTP_RESPONSE", "Response Code: " + responseCode);

                runOnUiThread(() -> {
                    dialog.dismiss();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Toast.makeText(BankTransferActivity.this, "✅ Thanh toán thành công ,Đơn hàng đã được gửi!", Toast.LENGTH_LONG).show();

                        // 👉 Trả về HomeActivity ngay sau thanh toán
                        Intent intent = new Intent(BankTransferActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(BankTransferActivity.this, "❌ Gửi thất bại! Mã: " + responseCode, Toast.LENGTH_LONG).show();
                    }
                });

            } catch (Exception e) {
                Log.e("SEND_ERROR", "Exception: ", e);
                runOnUiThread(() -> {
                    dialog.dismiss();
                    Toast.makeText(BankTransferActivity.this, "❌ Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}
