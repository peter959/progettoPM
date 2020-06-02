package com.example.instantreservation;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.view.ContextMenu;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {
    String textButton;

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    Animation fade_in;

    public ProgressButton(Context ct, View view, String text) {
        cardView = view.findViewById(R.id.card_btn);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textView);
        textButton = text;
        textView.setText(text);
    }

    public void buttonActivated() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Please wait...");
    }

    public void buttonFinished(String text) {
        textView.setText(text);
        progressBar.setVisibility(View.GONE);
    }

    void setTextButton(String text) {
        textButton = text;
    }

}
