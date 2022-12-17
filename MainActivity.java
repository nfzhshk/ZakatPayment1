package com.example.zakatpayment1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    EditText etWeight;
    Spinner spinner;
    EditText etPrice;
    Button btnCalculate;
    TextView tvAboutUs;

    int z_wear = 200;
    int z_keep = 85;

    String keep = "Keep";
    String wear= "Wear";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String getWeight = sharedPref.getString("weight", "");
        String getPrice = sharedPref.getString("price", "");

        etWeight = findViewById(R.id.etWeight);
        etPrice = findViewById(R.id.etPrice);

        etWeight.setText(getWeight);
        etPrice.setText(getPrice);

        spinner = findViewById(R.id.spinner);

        btnCalculate = findViewById(R.id.btnCalculate);

        tvAboutUs = findViewById(R.id.tvAboutUs);
        tvAboutUs.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        });

        String[] items = new String[]
        {
            keep, wear
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        btnCalculate.setOnClickListener(view ->
        {

            if (etWeight.getText().toString().length() == 0)
            {
                etWeight.setError("Please input weight in number.");
                Toast.makeText(this, "Missing input", Toast.LENGTH_SHORT).show();
                return;
            }

            if (etPrice.getText().toString().length() == 0)
            {
                etPrice.setError("Please input price in number");
                Toast.makeText(this, "Missing input", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight = Double.parseDouble(etWeight.getText().toString());
            double price = Double.parseDouble(etPrice.getText().toString());

            int zAmount;
            String spinValue = spinner.getSelectedItem().toString();

            if (spinValue.equals(keep))
            {
                zAmount = z_keep;
            }

            else if (spinValue.equals(wear))
            {
                zAmount = z_wear;
            }

            else
            {
                zAmount = 0;
            }

            double totalValueOfGold = weight * price;
            double uruf = weight - zAmount;
            double zakatPayable = uruf <= 0 ? 0 : price * uruf;
            double totalZakat = zakatPayable * 0.025;

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Result");
            builder.setMessage("" +
                    "Total Value of Gold :RM " + totalValueOfGold +
                    "\nZakat Payable :RM " + zakatPayable +
                    "\nUruf :RM " + uruf +
                    "\nTotal Zakat :RM " + totalZakat);

            builder.setPositiveButton("Continue", null);

            AlertDialog dialog = builder.create();
            dialog.show();

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("weight", String.valueOf(weight));
            editor.putString("price", String.valueOf(price));
            editor.apply();

        });
    }
}