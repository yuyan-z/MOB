package com.example.myapplication4;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private EditText questionEditText;
    private Button questionBtn;
    private ActivityResultLauncher<Intent> activityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionEditText  = findViewById(R.id.editText);
        questionBtn = findViewById(R.id.button);

        // 创建activityLauncher，用于启动另一个活动并等待结果
        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        String answer = data.getStringExtra("ANSWER");
                        if (answer != null) {
                            Snackbar.make(questionBtn, answer, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void askQuestion(View v) {
        String question = String.valueOf(questionEditText .getText());

        if (!question.isEmpty()) {
            // 启动SecondActivity，传递信息等待结果
            Intent intent = new Intent(this, SecondActivity.class);
            intent.putExtra("QUESTION", question);
            activityLauncher.launch(intent);

        }
    }
}
