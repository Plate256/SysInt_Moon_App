package com.example.moon_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moon_app.model.MoonApiResponse;
import com.example.moon_app.model.MoonData;
import com.example.moon_app.model.UpcomingPhase;
import com.example.moon_app.network.MoonApiService;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tvCurrentDate, tvPhaseName, tvLunarDay, tvIlluminationPercent;
    private LinearProgressIndicator progressIllumination;
    private RecyclerView rvUpcomingPhases;
    private MoonApiService moonApiService;
    
    // NOTE: Replace with your actual API key from freeastroapi.com
    private static final String API_KEY = "73052baf90a3c7c9645d0ba2b6f2e97ee981cbfca9be3566644e871af62e80fa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRetrofit();
        updateDate();
        fetchMoonData();
    }

    private void initViews() {
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tvPhaseName = findViewById(R.id.tv_phase_name);
        tvLunarDay = findViewById(R.id.tv_lunar_day);
        tvIlluminationPercent = findViewById(R.id.tv_illumination_percent);
        progressIllumination = findViewById(R.id.progress_illumination);
        rvUpcomingPhases = findViewById(R.id.rv_upcoming_phases);
        rvUpcomingPhases.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.freeastroapi.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        moonApiService = retrofit.create(MoonApiService.class);
    }

    private void updateDate() {
        String currentDate = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(new Date());
        tvCurrentDate.setText(currentDate);
    }

    private void fetchMoonData() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        
        moonApiService.getMoonPhase(API_KEY, today).enqueue(new Callback<MoonApiResponse>() {
            @Override
            public void onResponse(Call<MoonApiResponse> call, Response<MoonApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body().getData());
                } else {
                    // Using mock data for demonstration if API fails or key is missing
                    showMockData();
                }
            }

            @Override
            public void onFailure(Call<MoonApiResponse> call, Throwable t) {
                showMockData();
            }
        });
    }

    private void updateUI(MoonData data) {
        if (data == null) return;
        
        tvPhaseName.setText(data.getPhase());
        tvLunarDay.setText(String.format(Locale.getDefault(), "Day %.0f of lunar cycle", data.getAgeDays()));
        
        int illumination = (int) data.getIllumination();
        tvIlluminationPercent.setText(illumination + "%");
        progressIllumination.setProgress(illumination);
    }

    private void showMockData() {
        // Sample data matching the image provided
        tvPhaseName.setText("Waning Gibbous");
        tvLunarDay.setText("Day 17 of lunar cycle");
        tvIlluminationPercent.setText("93%");
        progressIllumination.setProgress(93);

        List<UpcomingPhase> upcomingPhases = new ArrayList<>();
        upcomingPhases.add(new UpcomingPhase("Last Quarter", "May 9", "5 days"));
        upcomingPhases.add(new UpcomingPhase("New Moon", "May 17", "12 days"));
        upcomingPhases.add(new UpcomingPhase("First Quarter", "May 24", "20 days"));
        upcomingPhases.add(new UpcomingPhase("Full Moon", "May 31", "27 days"));

        rvUpcomingPhases.setAdapter(new UpcomingPhaseAdapter(upcomingPhases));
        
        Toast.makeText(this, "Showing preview data (API Key required for real data)", Toast.LENGTH_LONG).show();
    }
}