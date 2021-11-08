package com.example.premssion;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static androidx.core.content.ContextCompat.getSystemService;


public class BasicFragment extends Fragment implements SensorEventListener {
    private ImageView basic_IMG_image;
    private TextView basic_TXT_info;
    private SensorManager mSensorManager;
    private Button basic_BTN_login;
    private float currentDegree = 0f;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic, container, false);
            findViews(view);
        return view;
    }

    private void findViews(View view) {
        basic_IMG_image = view.findViewById(R.id.basic_IMG_image);
        basic_TXT_info = view.findViewById(R.id.basic_TXT_info);
        basic_BTN_login = view.findViewById(R.id.basic_BTN_login);
    }
    public void onResume() {
        super.onResume();

        // for the system's orientation sensor registered listeners
        mSensorManager.registerListener((SensorEventListener) this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

    }



    public void onSensorChanged(SensorEvent event) {
        updateAngle(event.values[0]);
    }

    private void updateAngle(float value) {
        float degree = Math.round(value);

        basic_TXT_info.setText("Heading: " + Float.toString(degree) + " degrees");

        // create a rotation animation (reverse turn degree degrees)
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(240);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        basic_IMG_image.startAnimation(ra);
        currentDegree = -degree;
        if (currentDegree == 0 ) {
            basic_BTN_login.setVisibility(View.VISIBLE);
            basic_BTN_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToLogin();
                }
            });
        }
        else
        {
            basic_BTN_login.setVisibility(View.INVISIBLE);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public void goToLogin() {
        // Open your SignUp Activity if the user wants to signup
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}