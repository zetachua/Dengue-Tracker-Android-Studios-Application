package Boundary;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.searchlocationapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import Control.SymptomCheckerManager;

public class SymptomCheckerActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    private CheckBox feverCheck, headacheCheck, jointMuscleCheck, rashCheck, nauseaCheck, bleedingCheck;
    public static TextView resultTextView;
    public static ArrayList<String> result;
    public static Button nearbyClinicsBtn;
    public static final String EXTRA_INT = "com.example.searchlocationapp.Checkbox.EXTRA_INT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);
        nearbyClinicsBtn = findViewById(R.id.nearby_clinics);
        feverCheck=findViewById(R.id.check_fever);
        headacheCheck=findViewById(R.id.check_severe_headache);
        jointMuscleCheck=findViewById(R.id.check_joint_muscle_ache);
        rashCheck=findViewById(R.id.check_skin_rash);
        nauseaCheck=findViewById(R.id.check_nausea_vomit);
        bleedingCheck=findViewById(R.id.check_mild_bleeding);
        resultTextView=findViewById(R.id.result);
        result = new ArrayList<>();
        resultTextView.setEnabled(false);


        feverCheck.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (feverCheck.isChecked())
                    result.add("Fever");
                else
                    result.remove("Fever");
            }
        });

        headacheCheck.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (headacheCheck.isChecked())
                    result.add("Severe Headache");
                else
                    result.remove("Severe Headache");
            }
        });

        jointMuscleCheck.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (jointMuscleCheck.isChecked())
                    result.add("Joint/Muscle Ache");
                else
                    result.remove("Joint/Muscle Ache");
            }
        });

        rashCheck.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (rashCheck.isChecked())
                    result.add("Skin Rash");
                else
                    result.remove("Skin Rash");
            }
        });

        nauseaCheck.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (nauseaCheck.isChecked())
                    result.add("Nausea/Vomiting");
                else
                    result.remove("Nausea/Vomiting");
            }
        });

        bleedingCheck.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if (bleedingCheck.isChecked())
                    result.add("Mild Bleeding");
                else
                    result.remove("Mild Bleeding");
            }
        });


        CalculateResultButton calculateResultButton = new CalculateResultButton();
        Button calculateResult = findViewById(R.id.calculate_result);
        calculateResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                calculateResultButton.buttonClick();
            }
        });


        // Navigation Bar
        // Initialise and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Map selected
        bottomNavigationView.setSelectedItemId(R.id.nav_symptom);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_symptom:
                        return true;

                    case R.id.nav_map:
                        startActivity(new Intent(getApplicationContext()
                                ,MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        SearchActivity.searchInput = false;
        int number = Integer.parseInt(item.getTitle().toString());
        Intent intent = new Intent(SymptomCheckerActivity.this, MapsActivity.class);
        intent.putExtra(EXTRA_INT, number);
        this.startActivity(intent);
        return true;
    }

}