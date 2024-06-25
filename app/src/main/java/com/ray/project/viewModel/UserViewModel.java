package com.ray.project.viewModel;

import androidx.lifecycle.MutableLiveData;

/**
 * @Description: 类说明
 * @Author: ray
 * @Date: 25/6/2024
 */
public class UserViewModel extends BaseViewModel {
    // Create a LiveData with a String
    private MutableLiveData<String> token;

    public MutableLiveData<String> getToken() {
        if (token == null) {
            token = new MutableLiveData<>();
        }
        return token;
    }

    public void setToken(String token) {
        this.token.setValue(token);
    }
}
