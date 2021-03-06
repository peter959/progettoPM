package com.example.instantreservation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ProgressButton {
    private String textButton;

    private CardView cardView;
    private ConstraintLayout layout;
    private ProgressBar progressBar;
    private TextView textView;

    public ProgressButton(Context ct, View view, String text) {
        cardView = view.findViewById(R.id.card_btn);
        layout = view.findViewById(R.id.constraint_layout);
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textView);
        textButton = text;
        textView.setText(text);
        layout.setBackground(cardView.getResources().getDrawable(R.drawable.background_gradient));
    }


    public void buttonActivated() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Please wait...");
    }

    public void buttonFinished(String text) {
        textView.setText(text);
        progressBar.setVisibility(View.GONE);
    }

    public void buttonRemove(String text) {
        layout.setBackground(cardView.getResources().getDrawable(R.drawable.background_gradient_button_unsuccess));
        textView.setText(text);
    }

    public void buttonFinishedUnsuccessully(String text) {
        layout.setBackground(cardView.getResources().getDrawable(R.drawable.background_gradient_button_unsuccess));
        textView.setText(text);
        progressBar.setVisibility(View.GONE);
    }

    void setTextButton(String text) {
        textView.setText(text);
    }

}
