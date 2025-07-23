package com.example.nguyenthimynguyen;

public class UserApiService {
    public static UserApi api = ApiClient.getClient().create(UserApi.class);
}
