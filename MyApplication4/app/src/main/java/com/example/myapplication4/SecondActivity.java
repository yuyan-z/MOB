package com.example.myapplication4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class SecondActivity extends AppCompatActivity {
    private TextView questionTextView;
    private EditText answerEditText ;
    private Button answerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        questionTextView = findViewById(R.id.textView2);
        answerBtn = findViewById(R.id.button2);
        answerEditText = findViewById(R.id.editText2);

        // 获得MainActivity传递的信息
        String question = getIntent().getStringExtra("QUESTION");
        if (question != null) {
            questionTextView.setText(question);
            Snackbar.make(answerBtn, question, Snackbar.LENGTH_LONG).show();
        }
    }

    public void answerQuestion(View v) {
        String answer = String.valueOf(answerEditText .getText());

        if (!answer.isEmpty()) {
            // 返回信息
            Intent intent = new Intent();
            intent.putExtra("ANSWER", answer);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}