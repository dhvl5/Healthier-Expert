package com.example.healthierexpert;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditMedicineDetails extends AppCompatActivity
{
    EditText editTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_medicine_details);

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container),true);
        fade.excludeTarget(android.R.id.statusBarBackground,true);
        fade.excludeTarget(android.R.id.navigationBarBackground,true);

        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        editTitle = findViewById(R.id.edit_title);

        editTitle.setText(getIntent().getStringExtra("edit_note"));
        editTitle.setTransitionName("noteCardAnime");

        editTitle.setText(getIntent().getStringExtra("edit_title"));
    }
}
