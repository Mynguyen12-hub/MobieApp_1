package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    LinearLayout layoutBankTransfer, layoutCreditCard, layoutPaypal;
    RadioGroup rgBankOptions;
    EditText edtCardNumber, edtCardHolder, edtExpiryDate;
    Button btnConfirm;
    TextView tvPaymentTitle;

    String selectedMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        // Ánh xạ view
        layoutBankTransfer = findViewById(R.id.layoutBankTransfer);
        layoutCreditCard = findViewById(R.id.layoutCreditCard);
        layoutPaypal = findViewById(R.id.layoutPaypal);
        rgBankOptions = findViewById(R.id.rgBankOptions);
        edtCardNumber = findViewById(R.id.edtCardNumber);
        edtCardHolder = findViewById(R.id.edtCardHolder);
        edtExpiryDate = findViewById(R.id.edtExpiryDate);
        btnConfirm = findViewById(R.id.btnConfirmPayment);
        tvPaymentTitle = findViewById(R.id.tvPaymentTitle);

        // Nhận phương thức thanh toán
        final String method = getIntent().getStringExtra("method");
        if (method == null) {
            Toast.makeText(this, "Không có phương thức thanh toán", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị layout phù hợp với phương thức
        switch (method) {
            case "bank":
                layoutBankTransfer.setVisibility(View.VISIBLE);
                tvPaymentTitle.setText("Chuyển khoản ngân hàng");
                break;
            case "credit":
                layoutCreditCard.setVisibility(View.VISIBLE);
                tvPaymentTitle.setText("Thanh toán bằng thẻ");
                break;
            case "paypal":
                layoutPaypal.setVisibility(View.VISIBLE);
                tvPaymentTitle.setText("Thanh toán bằng PayPal");
                break;
        }

        // Xử lý khi nhấn nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            if ("bank".equals(method)) {
                int checkedId = rgBankOptions.getCheckedRadioButtonId();
                if (checkedId == -1) {
                    Toast.makeText(this, "Vui lòng chọn ngân hàng", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedBank = findViewById(checkedId);
                selectedMethod = "Chuyển khoản qua " + selectedBank.getText().toString();

            } else if ("credit".equals(method)) {
                String cardNumber = edtCardNumber.getText().toString().trim();
                String cardHolder = edtCardHolder.getText().toString().trim();
                String expiry = edtExpiryDate.getText().toString().trim();

                if (cardNumber.isEmpty() || cardHolder.isEmpty() || expiry.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin thẻ", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!expiry.matches("\\d{2}/\\d{2}")) {
                    Toast.makeText(this, "Ngày hết hạn không hợp lệ (MM/YY)", Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedMethod = "Thẻ tín dụng: " + cardHolder;

            } else if ("paypal".equals(method)) {
                selectedMethod = "Thanh toán qua PayPal";
            }

            // Trả kết quả về CheckoutActivity
            Intent result = new Intent();
            result.putExtra("selectedMethod", selectedMethod);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
