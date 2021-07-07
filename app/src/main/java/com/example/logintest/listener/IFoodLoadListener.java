package com.example.logintest.listener;

import java.util.List;

import com.example.logintest.model.FoodModel;

public interface IFoodLoadListener {
    void onFoodLoadSuccess(List<FoodModel> foodModelList);
    void onFoodLoadFailed(String message);

}
