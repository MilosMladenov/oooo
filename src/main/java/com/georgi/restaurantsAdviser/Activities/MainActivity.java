package com.georgi.restaurantsAdviser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.georgi.restaurantsAdviser.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public AlertDialog.Builder builder;
    public AlertDialog dialog;

    public ImageView foodImageView;
    public Button checkRestaurants;
    public Button closeRestaurants;

    public  EditText editRestaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        foodImageView = findViewById(R.id.food_image_view);
        foodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupDialogRestaurants();
            }
        });
    }

    private void createPopupDialogRestaurants() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup_food, null);

        checkRestaurants = view.findViewById(R.id.popup_food_button);
        closeRestaurants = view.findViewById(R.id.closeButtonFood);
        editRestaurants = view.findViewById(R.id.popup_food_editText);

        checkRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRestaurants.getText().toString().trim().length() != 0) {

                    Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
                    intent.putExtra("restCityName", editRestaurants.getText().toString());
                    startActivity(intent);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Enter city name to check nearby restaurants.", Toast.LENGTH_LONG).show();
                }
            }
        });
        closeRestaurants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create(); //creating dialog object
        dialog.show(); // important step
    }
}