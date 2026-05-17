package com.example.moon_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moon_app.model.MoonApiResponse;
import com.example.moon_app.model.UpcomingPhase;
import com.example.moon_app.network.MoonApiService;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView tvDate, tvMoonPhase, btnPrev, btnNext;
    private TextView tvIllumination, tvDistance, tvMoonAge, tvZodiac, tvInterpretationBody, tvSpecialBody, tvForecastBody;
    private TextView tvTime1, tvTime2, tvTime3, tvTime4;
    private ImageView ivMoon;
    private MoonApiService moonApiService;
    private Calendar calendar = Calendar.getInstance();

    private static final String API_KEY = "77ad617ec997da6e753ad4b4264600e3d4465a1e81dfe0723fd6f55bba9f0f02";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRetrofit();
        updateDate();
        fetchMoonData();
        setupNavigation();
    }

    private void initViews() {
        tvDate = findViewById(R.id.tvDate);
        tvMoonPhase = findViewById(R.id.tvMoonPhase);
        tvIllumination = findViewById(R.id.tvIllumination);
        tvDistance = findViewById(R.id.tvDistance);
        tvMoonAge = findViewById(R.id.tvMoonAge);
        tvZodiac = findViewById(R.id.tvZodiac);
        tvInterpretationBody = findViewById(R.id.tvInterpretationBody);
        tvSpecialBody = findViewById(R.id.tvSpecialBody);
        tvForecastBody = findViewById(R.id.tvForecastBody);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        ivMoon = findViewById(R.id.ivMoon);
        tvTime1 = findViewById(R.id.tvTime1);
        tvTime2 = findViewById(R.id.tvTime2);
        tvTime3 = findViewById(R.id.tvTime3);
        tvTime4 = findViewById(R.id.tvTime4);
    }

    private void setupNavigation() {
        btnPrev.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
            updateDate();
            fetchMoonData();
        });

        btnNext.setOnClickListener(v -> {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            updateDate();
            fetchMoonData();
        });
    }

    private void setupRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.freeastroapi.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        moonApiService = retrofit.create(MoonApiService.class);
    }

    private void updateDate() {
        String displayDate = new SimpleDateFormat("EEEE, MMMM d yyyy", Locale.getDefault()).format(calendar.getTime());
        tvDate.setText(displayDate.toUpperCase());
    }

    private void fetchMoonData() {
        String apiDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
        
        moonApiService.getMoonData(API_KEY, apiDate, 48.8566, 2.3522, true, true, true, true).enqueue(new Callback<MoonApiResponse>() {
            @Override
            public void onResponse(Call<MoonApiResponse> call, Response<MoonApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    String errorMsg = "API Error: " + response.code();
                    if (response.code() == 429) {
                        errorMsg = "API Rate Limit Reached (80/day). Showing preview data.";
                    }
                    android.util.Log.e("MoonApp", errorMsg);
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    showMockData();
                }
            }

            @Override
            public void onFailure(Call<MoonApiResponse> call, Throwable t) {
                android.util.Log.e("MoonApp", "Network Failure", t);
                showMockData();
            }
        });
    }

    private void updateUI(MoonApiResponse response) {
        if (response == null || response.getPhase() == null) {
            android.util.Log.e("MoonApp", "updateUI: data is null");
            return;
        }
        
        MoonApiResponse.PhaseInfo phase = response.getPhase();
        MoonApiResponse.ZodiacInfo zodiac = response.getZodiac();
        java.util.Map<String, String> nextPhases = response.getNextPhases();
        
        android.util.Log.d("MoonApp", "Updating UI with: " + phase.getName());
        
        tvMoonPhase.setText(phase.getName());
        tvIllumination.setText(String.format(Locale.getDefault(), "%.1f%%", phase.getIllumination() * 100));
        tvDistance.setText(String.format(Locale.getDefault(), "%,.0f KM", phase.getDistanceKm()));
        tvMoonAge.setText(String.format(Locale.getDefault(), "%.1f days", phase.getAgeDays()));

        if (zodiac != null) {
            tvZodiac.setText(zodiac.getSign());
        }

        MoonApiResponse.InterpretationInfo interpretation = response.getInterpretation();
        if (interpretation != null) {
            tvInterpretationBody.setText(interpretation.getBody());
        } else {
            tvInterpretationBody.setText("");
        }

        MoonApiResponse.SpecialInfo special = response.getSpecial();
        if (special != null) {
            tvSpecialBody.setText(formatSpecialInfo(special));
        } else {
            tvSpecialBody.setText("");
        }

        MoonApiResponse.ForecastInfo forecast = response.getForecast();
        if (forecast != null) {
            tvForecastBody.setText(formatForecastInfo(forecast));
        } else {
            tvForecastBody.setText("");
        }

        if (nextPhases != null) {
            updateNextPhasesUI(nextPhases);
        }

        updateMoonImage(phase.getName());
    }

    private String formatSpecialInfo(MoonApiResponse.SpecialInfo special) {
        StringBuilder sb = new StringBuilder();
        java.util.List<String> types = new java.util.ArrayList<>();
        if (special.isSupermoon()) types.add("Supermoon");
        if (special.isMicromoon()) types.add("Micromoon");
        if (special.isBlueMoon()) types.add("Blue Moon");
        if (special.isBlackMoon()) types.add("Black Moon");

        if (!types.isEmpty()) {
            sb.append("This moon is a ");
            for (int i = 0; i < types.size(); i++) {
                sb.append(types.get(i));
                if (i < types.size() - 1) sb.append(", ");
            }
            sb.append(".\n\n");
        }

        if (special.getLabels() != null && !special.getLabels().isEmpty()) {
            sb.append("Labels: ");
            for (int i = 0; i < special.getLabels().size(); i++) {
                sb.append(special.getLabels().get(i));
                if (i < special.getLabels().size() - 1) sb.append(", ");
            }
        }

        if (sb.length() == 0) {
            return "No special celestial designations for this date.";
        }

        return sb.toString();
    }

    private String formatForecastInfo(MoonApiResponse.ForecastInfo forecast) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.getDefault(), "Full Moon in %.1f days.\n", forecast.getDaysUntilFullMoon()));
        sb.append(String.format(Locale.getDefault(), "New Moon in %.1f days.", forecast.getDaysUntilNewMoon()));

        if (forecast.getNextSpecialMoon() != null && forecast.getNextSpecialMoon().getType() != null) {
            sb.append(String.format(Locale.getDefault(), "\n\nNext Special Moon: %s in %.1f days",
                    forecast.getNextSpecialMoon().getType(), forecast.getNextSpecialMoon().getDaysUntil()));
        }

        if (forecast.getNextEclipse() != null && forecast.getNextEclipse().getType() != null) {
            sb.append(String.format(Locale.getDefault(), "\nNext Eclipse: %s in %.1f days",
                    forecast.getNextEclipse().getType(), forecast.getNextEclipse().getDaysUntil()));
        }

        return sb.toString();
    }

    private void updateNextPhasesUI(java.util.Map<String, String> nextPhases) {
        // Displaying dates of major phases in the small text slots
        // Full Moon, New Moon, 1st Quarter, Last Quarter
        String full = formatPhaseDate(nextPhases.get("full_moon"));
        String newM = formatPhaseDate(nextPhases.get("new_moon"));
        String first = formatPhaseDate(nextPhases.get("first_quarter"));
        String last = formatPhaseDate(nextPhases.get("last_quarter"));

        tvTime1.setText("🌕\n" + full + "\nFull");
        tvTime2.setText("🌑\n" + newM + "\nNew");
        tvTime3.setText("🌓\n" + first + "\n1st Q");
        tvTime4.setText("🌗\n" + last + "\n3rd Q");
    }

    private String formatPhaseDate(String isoDate) {
        if (isoDate == null) return "N/A";
        try {
            // "2023-10-28T20:00:43Z"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(isoDate);
            return new SimpleDateFormat("MMM d", Locale.US).format(date);
        } catch (Exception e) {
            return "N/A";
        }
    }

    private void updateMoonImage(String phase) {
        if (phase == null) return;
        
        String phaseLower = phase.toLowerCase();
        int resId = R.drawable.moon_placeholder;

        if (phaseLower.contains("new")) {
            resId = R.drawable.newmoon;
        } else if (phaseLower.contains("waxing crescent")) {
            resId = R.drawable.waxingcresent;
        } else if (phaseLower.contains("first quarter")) {
            resId = R.drawable.firstquarter;
        } else if (phaseLower.contains("waxing gibbous")) {
            resId = R.drawable.waxinggibous;
        } else if (phaseLower.contains("full")) {
            resId = R.drawable.fullmoon;
        } else if (phaseLower.contains("waning gibbous")) {
            resId = R.drawable.waninggibous;
        } else if (phaseLower.contains("last quarter")) {
            resId = R.drawable.thirdquarter;
        } else if (phaseLower.contains("waning crescent")) {
            resId = R.drawable.waningcrecent;
        }
        
        ivMoon.setImageResource(resId);
    }

    private void showMockData() {
        tvMoonPhase.setText("Full Moon");
        tvIllumination.setText("67%");
        tvDistance.setText("405,905 KM");
        tvMoonAge.setText("17.3 days");
        tvZodiac.setText("Sagittarius");
        tvInterpretationBody.setText("This Moon pattern emphasizes philosophical exploration, freedom, and seeking truth. In Sagittarius, the emotional style leans toward optimism and expansion. The Full Moon phase marks a period of culmination and heightened intuition. This is a time to release what no longer serves your growth and embrace new perspectives.");
        tvSpecialBody.setText("This moon is a Supermoon.\n\nLabels: Harvest Moon");
        tvForecastBody.setText("Full Moon in 0.0 days.\nNew Moon in 14.8 days.\n\nNext Special Moon: Hunter's Moon in 29.5 days\nNext Eclipse: Partial Lunar Eclipse in 45.2 days");
    }
}