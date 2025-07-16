package com.example.nguyenthimynguyen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentMethodActivity extends AppCompatActivity {

    RadioGroup rgMethod, rgBankOptions;
    RadioButton rbMomo, rbZalo, rbBankTransfer, rbCreditCard, rbPaypal, rbCOD;
    LinearLayout layoutBankOptions;
    Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        // Ánh xạ view
        rgMethod = findViewById(R.id.rgMethod);
        rgBankOptions = findViewById(R.id.rgBankOptions);
        layoutBankOptions = findViewById(R.id.layoutBankOptions);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        rbMomo = findViewById(R.id.rbMomo);
        rbZalo = findViewById(R.id.rbZalo);
        rbBankTransfer = findViewById(R.id.rbBankTransfer);
        rbCreditCard = findViewById(R.id.rbCreditCard);
        rbPaypal = findViewById(R.id.rbPaypal);
        rbCOD = findViewById(R.id.rbCOD);

        // Mặc định ẩn ngân hàng
        layoutBankOptions.setVisibility(View.GONE);

        // Khi chọn phương thức thanh toán
        rgMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBankTransfer) {
                layoutBankOptions.setVisibility(View.VISIBLE);
            } else {
                layoutBankOptions.setVisibility(View.GONE);
                rgBankOptions.clearCheck();
            }
        });

        // Xác nhận lựa chọn
        btnConfirmPayment.setOnClickListener(v -> {
            int selectedId = rgMethod.getCheckedRadioButtonId();

            if (selectedId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            String method = "";

            if (selectedId == R.id.rbMomo) {
                method = "MoMo";
            } else if (selectedId == R.id.rbZalo) {
                method = "ZaloPay";
            } else if (selectedId == R.id.rbCreditCard) {
                method = "Thẻ tín dụng / Ghi nợ";
            } else if (selectedId == R.id.rbPaypal) {
                method = "PayPal";
            } else if (selectedId == R.id.rbCOD) {
                method = "Thanh toán khi nhận hàng (COD)";
            } else if (selectedId == R.id.rbBankTransfer) {
                int selectedBankId = rgBankOptions.getCheckedRadioButtonId();
                if (selectedBankId == -1) {
                    Toast.makeText(this, "Vui lòng chọn ngân hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedBank = findViewById(selectedBankId);
                method = "Chuyển khoản: " + selectedBank.getText().toString();
            }

            // Trả kết quả về
            Intent resultIntent = new Intent();
            resultIntent.putExtra("selected_method", method);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
