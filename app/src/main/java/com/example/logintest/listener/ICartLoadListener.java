package com.example.logintest.listener;

import com.example.logintest.model.CartModel;
import com.example.logintest.model.FoodModel;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);
    void onCartLoadFailed(String message);
}
