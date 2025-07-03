package com.example.nguyenthimynguyen;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    EditText edtName, edtPhone, edtAddress, edtNote;
    TextView tvTotalAmount, tvSelectedPayment;
    Button btnConfirm;
    RecyclerView rvCheckoutProducts;
    RadioGroup rgPaymentMethod, rgBankOptions;
    LinearLayout layoutBankOptions;

    CheckoutProductAdapter adapter;
    List<Product> productList;

    String selectedMethod = "";
    String selectedBank = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtNote = findViewById(R.id.edtNote);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvSelectedPayment = findViewById(R.id.tvSelectedPayment);
        btnConfirm = findViewById(R.id.btnConfirm);
        rvCheckoutProducts = findViewById(R.id.rvCheckoutProducts);

        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        rgBankOptions = findViewById(R.id.rgBankOptions);
        layoutBankOptions = findViewById(R.id.layoutBankOptions);

        // Hiển thị danh sách sản phẩm
        productList = CartManager.getCart();
        adapter = new CheckoutProductAdapter(productList);
        rvCheckoutProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCheckoutProducts.setAdapter(adapter);

        // Tổng tiền
        double total = CartManager.getTotalAmount();
        tvTotalAmount.setText(String.format("Tổng tiền: %,.0f đ", total));

        // Lắng nghe chọn phương thức thanh toán
        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbBankTransfer) {
                layoutBankOptions.setVisibility(View.VISIBLE);
                selectedMethod = "Chuyển khoản ngân hàng";
            } else {
                layoutBankOptions.setVisibility(View.GONE);
                if (checkedId == R.id.rbCreditCard) {
                    selectedMethod = "Thẻ tín dụng / Ghi nợ";
                } else if (checkedId == R.id.rbPaypal) {
                    selectedMethod = "PayPal";
                }
            }
            tvSelectedPayment.setText("Phương thức: " + selectedMethod);
        });

        // Xác nhận đặt hàng
        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(selectedMethod)) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedMethod.equals("Chuyển khoản ngân hàng")) {
                int selectedBankId = rgBankOptions.getCheckedRadioButtonId();
                if (selectedBankId == -1) {
                    Toast.makeText(this, "Vui lòng chọn ngân hàng", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton selectedBankRb = findViewById(selectedBankId);
                selectedBank = selectedBankRb.getText().toString();
                selectedMethod += " - " + selectedBank;
            }

            tvSelectedPayment.setText("Phương thức: " + selectedMethod);

            Toast.makeText(this, "🎉 Đặt hàng thành công!", Toast.LENGTH_LONG).show();
            CartManager.clearCart();
            finish();
        });
    }
}
