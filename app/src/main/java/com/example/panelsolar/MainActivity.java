package com.example.panelsolar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    private EditText latitude;
    private EditText longitude;
    private EditText area;
    private SeekBar inclinationBar;
    private Button calcular;
    private TextView inclinationValue;
    private TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Obtener los parametros (findViewById)
        latitude= findViewById(R.id.latitude_edittext);
        longitude=findViewById(R.id.longitude_edittext);
        area= findViewById(R.id.area_edittext);
        inclinationBar= findViewById(R.id.inclination_sb);
        calcular= findViewById(R.id.calcular_button);
        inclinationValue= findViewById(R.id.inclination_text);
        result= findViewById(R.id.result_tv);

        inclinationBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                inclinationValue.setText("Inclinación de los paneles: "+progress+"°");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Configurar listener del boton calcular
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtener valores de los campos
                double latitudeValue= Double.parseDouble(latitude.getText().toString());
                double longitudeValue= Double.parseDouble(longitude.getText().toString());
                double areaValue= Double.parseDouble(area.getText().toString());
                int inclination= inclinationBar.getProgress();
                //Calcular producción de energia y mostrar resultado
                double energyGenerated= calculateEnergy(latitudeValue,longitudeValue,areaValue,inclination);
                result.setText("La energía generada es de: "+energyGenerated+" kWh");
            }
        });

    }

    private double calculateEnergy(double latitude, double longitude, double area, int inclination){
        //Convertir la lalitud, longitud e inclinación a radianes
        double latitudeRad= Math.toRadians(latitude);
        double longitudeRad= Math.toRadians(longitude);
        double inclinationRad= Math.toRadians(inclination);

        //obtener el día actual del año
        int dayOfYear= LocalDate.now().getDayOfYear();

        // Calcular ángulo de incidencia de la radiación solar
        double angle= Math.acos(Math.sin(latitudeRad)*Math.sin(inclinationRad)
                +Math.cos(latitudeRad)*Math.cos(inclinationRad));
        //Calcular radiación solar incidente
        double constantSolar= 0.1367; //kWh/m2
        double radiation= constantSolar*Math.cos(angle)*
                (1+0.033*Math.cos(Math.toRadians(360*dayOfYear/365)));

        //Calcular producción de energia
        double areaPanel= area/10000;
        double efficiencyPanel= 0.16;
        double lossFactor= 0.9;
        double energyGenerated= areaPanel*radiation*efficiencyPanel*lossFactor;

        return energyGenerated;

    }





}