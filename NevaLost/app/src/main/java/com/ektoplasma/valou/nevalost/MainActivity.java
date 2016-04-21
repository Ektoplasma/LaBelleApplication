package com.ektoplasma.valou.nevalost;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    // le beau commentaire
    // oui il est beau
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button ButtonPursuit = (Button)findViewById(R.id.ButtonCreatePursuit);
        Button ButtonJoin = (Button)findViewById(R.id.ButtonJoinPursuit);
        Button ButtonSettings = (Button)findViewById(R.id.ButtonSettings);

        ButtonPursuit.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, CreationActivity.class));
                    }
                }
        );

        ButtonJoin.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, JoinActivity.class));
                    }
                }
        );

        ButtonSettings.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                         startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                }
        );
    }
}
