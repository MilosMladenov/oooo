package com.georgi.restaurantsAdviser.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.georgi.restaurantsAdviser.Constants;
import com.georgi.restaurantsAdviser.Model.Restaurants;
import com.georgi.restaurantsAdviser.R;
import com.georgi.restaurantsAdviser.RecyclerViewAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantListActivity extends AppCompatActivity  {

	private RecyclerView recyclerView;
	private RecyclerViewAdapter recyclerViewAdapter;
	private List<Restaurants> restaurantsList;

	public ArrayList<String> arrayList;

	public String restCityName;

	TextView restCityNameTxt;

	public AlertDialog.Builder builder;
	public AlertDialog dialog;

	public Button checkRestAgain;
	public Button closeRestaurants;

	public  EditText editRestaurants;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_list);

		Objects.requireNonNull(getSupportActionBar()).setElevation(0);

		Intent intent = getIntent();
		restCityName = intent.getStringExtra("restCityName");
		new RetrieveFeedTask(restCityName).execute();

		restaurantsList = new ArrayList<>();

		recyclerView = findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		recyclerViewAdapter = new RecyclerViewAdapter(RestaurantListActivity.this, restaurantsList);
		recyclerView.setAdapter(recyclerViewAdapter);
		recyclerViewAdapter.notifyDataSetChanged();

		arrayList = new ArrayList<>();

		restCityNameTxt = findViewById(R.id.restCityNameTxt);
	}

	@SuppressLint("StaticFieldLeak")
	class RetrieveFeedTask extends AsyncTask<String, Void, List<Restaurants>> {

		private URL url;

		public RetrieveFeedTask(String city) {
			try {
				this.url = new URL(String.format(Constants.RESTAURANT_URL, city));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		@SuppressLint({"WrongThread", "SetTextI18n", "DefaultLocale"})
		@Override
		protected List<Restaurants> doInBackground(String... strings) {
			try {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				final StringBuilder json = new StringBuilder(1024);
				String temp;

				while ((temp = bufferedReader.readLine()) != null) {
					json.append(temp).append("\n");
				}
				bufferedReader.close();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						try {
							JSONObject jsonObject = new JSONObject(json.toString());

							JSONArray jsonArray = jsonObject.getJSONArray("results");

							for (int i = 0; i < jsonArray.length(); i++) {
								Restaurants restaurants = new Restaurants();

								JSONObject restaurant = jsonArray.getJSONObject(i);

								restaurants.setmName(restaurant.getString("name"));
								restaurants.setmAddress(restaurant.getString("formatted_address"));
								restaurants.setmRating(restaurant.getDouble("rating"));

								restCityNameTxt.setText(restCityName);

								restaurantsList.add(restaurants);
							}
							recyclerViewAdapter = new RecyclerViewAdapter(RestaurantListActivity.this, restaurantsList);
							recyclerView.setAdapter(recyclerViewAdapter);
							recyclerViewAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (Exception e) {
				Log.d("Error", e.toString());
			}
			return restaurantsList;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_food, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		switch (item.getItemId()) {

			case R.id.action_search_another_rest:

				builder = new AlertDialog.Builder(RestaurantListActivity.this);
				View view = getLayoutInflater().inflate(R.layout.popup_food, null);

				checkRestAgain = view.findViewById(R.id.popup_food_button);
				closeRestaurants = view.findViewById(R.id.closeButtonFood);
				editRestaurants = view.findViewById(R.id.popup_food_editText);

				checkRestAgain.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (editRestaurants.getText().toString().trim().length() != 0) {
							Intent intent = new Intent(RestaurantListActivity.this, RestaurantListActivity.class);
							intent.putExtra("restCityName", editRestaurants.getText().toString());
							startActivity(intent);
							dialog.dismiss();

							finish();
						} else {
							Toast.makeText(RestaurantListActivity.this,
									"Enter city name to check the restaurants.", Toast.LENGTH_LONG).show();
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
				break;

			case R.id.home_button:

				Intent intent = new Intent(RestaurantListActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
