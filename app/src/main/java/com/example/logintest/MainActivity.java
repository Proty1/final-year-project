package com.example.logintest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.logintest.adapter.MyFoodAdapter;
import com.example.logintest.listener.ICartLoadListener;
import com.example.logintest.listener.IFoodLoadListener;
import com.example.logintest.model.CartModel;
import com.example.logintest.model.FoodModel;
import com.example.logintest.utils.SpaceItemDecoration;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IFoodLoadListener, ICartLoadListener {
    @BindView(R.id.recycler_food)
    RecyclerView recyclerFood;
    @BindView(R.id.mainLayout)
    RelativeLayout mainLayout;
    @BindView(R.id.badge)
    NotificationBadge badge;
    @BindView(R.id.btnCart)
    FrameLayout btnCart;

    IFoodLoadListener foodLoadListener;
    ICartLoadListener cartLoadListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        loadFoodFromFirebase();
    }

    private void loadFoodFromFirebase() {
        List<FoodModel> foodModels = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Food")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            for(DataSnapshot foodSnapshot:snapshot.getChildren()){

                                FoodModel foodModel = foodSnapshot.getValue(FoodModel.class);
                                foodModel.setKey(foodSnapshot.getKey());
                                foodModels.add(foodModel);
                            }
                            foodLoadListener.onFoodLoadSuccess(foodModels);
                        }
                        else
                            foodLoadListener.onFoodLoadFailed("Can't find food.");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        foodLoadListener.onFoodLoadFailed(error.getMessage());
                    }
                });

    }

    private void init() {
        ButterKnife.bind(this);

        foodLoadListener = this;
        cartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerFood.setLayoutManager(gridLayoutManager);
        recyclerFood.addItemDecoration(new SpaceItemDecoration());
    }


    @Override
    public void onFoodLoadSuccess(List<FoodModel> foodModelList) {
        MyFoodAdapter adapter = new MyFoodAdapter(this,foodModelList);
        recyclerFood.setAdapter(adapter);
    }

    @Override
    public void onFoodLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
    }

    @Override
    public void onCartLoadFailed(String message) {

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}