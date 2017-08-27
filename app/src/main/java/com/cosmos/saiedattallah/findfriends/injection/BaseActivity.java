package com.cosmos.saiedattallah.findfriends.injection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphProvider graphProvider = GraphProvider.getInstance();
        graphProvider.getGraph().inject(this);
    }
}
