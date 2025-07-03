package com.example.nguyenthimynguyen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup rgBankTransfer, rgOtherMethod;
    private RadioButton rbBCA, rbBNI, rbMandiri, rbBRI, rbCreditCard, rbPaypal;
    private TextView txtTotalPayment;
    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment); // Đảm bảo tên file XML là activity_payment.xml

        // Ánh xạ view
        rgBankTransfer = findViewById(R.id.rgBankTransfer);
        rgOtherMethod = findViewById(R.id.rgOtherMethod);

        rbBCA = findViewById(R.id.rbBCA);
        rbBNI = findViewById(R.id.rbBNI);
        rbMandiri = findViewById(R.id.rbMandiri);
        rbBRI = findViewById(R.id.rbBRI);
        rbCreditCard = findViewById(R.id.rbCreditCard);
        rbPaypal = findViewById(R.id.rbPaypal);

        txtTotalPayment = findViewById(R.id.txtTotalPayment);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Xử lý nút xác nhận thanh toán
        btnConfirmPayment.setOnClickListener(v -> {
            String selectedMethod = getSelectedPaymentMethod();
            if (selectedMethod == null) {
                Toast.makeText(PaymentActivity.this, "Please select a payment method.", Toast.LENGTH_SHORT).show();
            } else {
                String total = txtTotalPayment.getText().toString();
                Toast.makeText(PaymentActivity.this,
                        "Payment Confirmed via " + selectedMethod + "\n" + total,
                        Toast.LENGTH_LONG).show();

                // TODO: Xử lý logic tiếp theo ở đây (gửi dữ liệu lên server, lưu vào đơn hàng, v.v.)
            }
        });
    }

    private String getSelectedPaymentMethod() {
        int bankCheckedId = rgBankTransfer.getCheckedRadioButtonId();
        int otherCheckedId = rgOtherMethod.getCheckedRadioButtonId();

        if (bankCheckedId != -1) {
            RadioButton selected = findViewById(bankCheckedId);
            return selected.getText().toString();
        }

        if (otherCheckedId != -1) {
            RadioButton selected = findViewById(otherCheckedId);
            return selected.getText().toString();
        }

        return null; // Không chọn gì cả
    }
}
