//package com.example.nguyenthimynguyen;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProductRepository {
//    private static final List<Product> productList = new ArrayList<>();
//
//    // Khởi tạo danh sách sản phẩm (chỉ thực hiện 1 lần)
//    public static void initProducts() {
//        if (!productList.isEmpty()) return;
//
//        productList.add(new Product("Hoa Hồng Đỏ", R.drawable.anh1, 150000, 135000, "Biểu tượng của tình yêu", 4.8f, 101, 1, "Mặt trời"));
//        productList.add(new Product("Hoa Hồng Trắng", R.drawable.anh2, 160000, 145000, "Thanh khiết và trong sáng", 4.6f, 102, 2, "Ánh trăng"));
//        productList.add(new Product("Hoa Hồng Vàng", R.drawable.anh3, 155000, 140000, "Tình bạn và niềm vui", 4.7f, 103, 3, "Ngôi sao"));
//        productList.add(new Product("Hoa Cẩm Tú Cầu", R.drawable.anh4, 180000, 165000, "Thành ý và biết ơn", 4.5f, 104, 4, "Mặt trắng"));
//        productList.add(new Product("Hoa Hướng Dương", R.drawable.anh5, 170000, 155000, "Vươn tới ánh sáng", 4.9f, 105, 5, "Đám mây"));
//        productList.add(new Product("Hoa Cẩm Chướng", R.drawable.anh6, 140000, 125000, "Sự ngọt ngào", 4.3f, 106, 6, "Tinh tú"));
//        productList.add(new Product("Hoa Lan Hồ Điệp", R.drawable.anh7, 250000, 230000, "Sang trọng và quý phái", 4.9f, 107, 7, "Mặt trời"));
//        productList.add(new Product("Hoa Ly", R.drawable.anh8, 200000, 180000, "Sự thanh cao", 4.6f, 108, 8, "Ánh trăng"));
//        productList.add(new Product("Hoa Tulip", R.drawable.anh9, 220000, 200000, "Lãng mạn và thanh lịch", 4.7f, 109, 9, "Ngôi sao"));
//        productList.add(new Product("Hoa Baby", R.drawable.anh10, 120000, 110000, "Nhẹ nhàng và thuần khiết", 4.2f, 110, 10, "Mặt trắng"));
//        productList.add(new Product("Hoa Cúc Mẫu Đơn", R.drawable.anh11, 160000, 145000, "May mắn và phúc lộc", 4.5f, 111, 11, "Đám mây"));
//        productList.add(new Product("Hoa Cúc Họa Mi", R.drawable.anh12, 130000, 120000, "Trong sáng tuổi trẻ", 4.4f, 112, 12, "Tinh tú"));
//        productList.add(new Product("Hoa Thược Dược", R.drawable.anh13, 145000, 130000, "Ngọt ngào và bền bỉ", 4.5f, 113, 13, "Mặt trời"));
//        productList.add(new Product("Hoa Oải Hương", R.drawable.anh14, 160000, 145000, "Hương thơm dịu nhẹ", 4.8f, 114, 14, "Ánh trăng"));
//        productList.add(new Product("Hoa Dạ Lan", R.drawable.anh15, 155000, 140000, "Quyến rũ trong đêm", 4.6f, 115, 15, "Ngôi sao"));
//        productList.add(new Product("Hoa Cát Tường", R.drawable.anh16, 150000, 135000, "May mắn và bình an", 4.5f, 116, 16, "Mặt trắng"));
//        productList.add(new Product("Hoa Bồ Công Anh", R.drawable.anh17, 125000, 115000, "Mong manh và nhẹ nhàng", 4.3f, 117, 17, "Đám mây"));
//        productList.add(new Product("Hoa Mai", R.drawable.anh18, 200000, 185000, "Tươi thắm ngày Tết", 4.7f, 118, 18, "Tinh tú"));
//        productList.add(new Product("Hoa Đào", R.drawable.anh19, 190000, 175000, "Sắc xuân miền Bắc", 4.8f, 119, 19, "Mặt trời"));
//        productList.add(new Product("Hoa Lưu Ly", R.drawable.anh20, 110000, 99000, "Nhớ mãi không quên", 4.4f, 120, 20, "Ánh trăng"));
//        productList.add(new Product("Hoa Đồng Tiền", R.drawable.anh21, 145000, 130000, "Tài lộc và thịnh vượng", 4.5f, 121, 21, "Ngôi sao"));
//        productList.add(new Product("Hoa Thiên Điểu", R.drawable.anh22, 210000, 195000, "Mạnh mẽ và độc đáo", 4.6f, 122, 22, "Mặt trắng"));
//        productList.add(new Product("Hoa Cúc Vạn Thọ", R.drawable.anh24, 135000, 120000, "Trường thọ và viên mãn", 4.2f, 124, 23, "Đám mây"));
//        productList.add(new Product("Hoa Dừa Cạn", R.drawable.anh25, 125000, 115000, "Giản dị và kiên cường", 4.4f, 125, 24, "Tinh tú"));
//    }
//
//    // Lấy toàn bộ danh sách sản phẩm
//    public static List<Product> getAllProducts() {
//        return productList;
//    }
//
//    // Lấy sản phẩm theo danh mục
//    public static List<Product> getProductsByCategory(String category) {
//        List<Product> related = new ArrayList<>();
//        for (Product p : productList) {
//            if (p.getCategory().equalsIgnoreCase(category)) {
//                related.add(p);
//            }
//        }
//        return related;
//    }
//
//    // Tìm sản phẩm theo ID
//    public static Product getProductById(int id) {
//        for (Product p : productList) {
//            if (p.getId() == id) {
//                return p;
//            }
//        }
//        return null;
//    }
//}
