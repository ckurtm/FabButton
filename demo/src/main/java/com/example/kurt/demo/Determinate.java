package com.example.kurt.demo;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mbanje.kurt.fabbutton.FabButton;

/**
 * Created by kurt on 08 06 2015 .
 */
public class Determinate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.determinate);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final FabButton button = (FabButton) findViewById(R.id.determinate);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Determinate");

        final ProgressHelper helper = new ProgressHelper(button,this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.startDeterminate();
            }
        });
    }
}
