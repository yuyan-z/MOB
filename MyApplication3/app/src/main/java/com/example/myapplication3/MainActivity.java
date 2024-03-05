package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private int clickCount = 0;
    private TextView count;
    private EditText date;
    private Spinner city;
    private LinearLayout phoneContainer;
    private int nPhone;
    private static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "onCreate");

        // 初始化变量
        date = findViewById(R.id.editTextText3);
        city = findViewById(R.id.spinner1);
        phoneContainer = findViewById(R.id.phoneContainer);
        count = findViewById(R.id.textView1);
        nPhone = 0;

        // 设置触摸监听器
        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    // 每次触摸按下，增加点击次数并更新TextView
                    clickCount++;
                    count.setText(String.valueOf(clickCount));
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume");

        // 恢复之前保存的数据
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        clickCount = prefs.getInt("clickCount", 0);
//        count.setText(String.valueOf(clickCount));

        date.setText(prefs.getString("date", ""));
        city.setSelection(prefs.getInt("city", 0));
        nPhone = prefs.getInt("nPhone", 0);

        LinearLayout phoneItem = (LinearLayout) phoneContainer.getChildAt(1);
        Button button = (Button) phoneItem.getChildAt(1);
        Resources res = getResources();

        count.setText(String.valueOf(nPhone));

        for (int i = nPhone - 1; i >= 0; i--) {
            if (i == 1) {
                EditText phoneNumber = (EditText) phoneItem.getChildAt(0);
                phoneNumber.setText(prefs.getString("phone" + i, ""));
            } else {
                LinearLayout newPhoneItem = new LinearLayout(this);
                newPhoneItem.setOrientation(LinearLayout.HORIZONTAL);
                newPhoneItem.setLayoutParams(phoneItem.getLayoutParams());
                EditText newPhoneNumber = new EditText(this);
                newPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, // width
                        LinearLayout.LayoutParams.MATCH_PARENT); // height
                newPhoneNumber.setLayoutParams(layoutParams);
                newPhoneNumber.setEms(10);
                newPhoneNumber.setText(prefs.getString("phone" + i, ""));
                Button newButton = new Button(this);
                newButton.setLayoutParams(button.getLayoutParams());
                newButton.setTextColor(button.getCurrentTextColor());
                newButton.setBackground(button.getBackground());
                newButton.setText(res.getString(R.string.add));
                newButton.setAllCaps(false);
                newButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        phoneAction(v);
                    }
                });
                button.setText(res.getString(R.string.delete));

                newPhoneItem.addView(newPhoneNumber);
                newPhoneItem.addView(newButton);
                phoneContainer.addView(newPhoneItem);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "onPause");

        // 保存数据
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();

        editor.clear();  // 清空之前的数据
        nPhone = 0;

//        editor.putInt("clickCount", clickCount);
        editor.putString("date", date.getText().toString());
        editor.putInt("city", city.getSelectedItemPosition());

        if (phoneContainer.getChildCount() > 1) {
            // 遍历phoneContainer中的每个LinearLayout
            for (int i = 1; i < phoneContainer.getChildCount(); i++) {
                // 获取当前LinearLayout
                LinearLayout phoneItem = (LinearLayout) phoneContainer.getChildAt(i);

                // 遍历phoneItem中的每个子项
                for (int j = 0; j < phoneItem.getChildCount(); j++) {
                    // 获取当前子项
                    View v = phoneItem.getChildAt(j);
                    // 检查当前子项是否是EditText
                    if (v instanceof EditText) {
                        // 获取EditText的文本内容
                        nPhone++;
                        editor.putString("phone" + nPhone, String.valueOf(((EditText) v).getText()));
                    }
                }
            }
        }
        editor.putInt("nPhone", nPhone);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity", "onRestart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MainActivity", "onSaveInstanceState");
        outState.putInt("clickCount", clickCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("MainActivity", "onRestoreInstanceState");
        // 恢复状态
        clickCount = savedInstanceState.getInt("clickCount", 0);
        count.setText(String.valueOf(clickCount));
    }

    public void resetAction (MenuItem item) {
//        name.setText("");
//        surname.setText("");
        date.setText("");
        city.setSelection(0);
        count.setText("0");

        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

        clearPhoneView();
        nPhone = 0;
    }

    public void clearPhoneView() {
        for (int i = phoneContainer.getChildCount() - 1; i > 0; i--) {
            LinearLayout phoneItem = (LinearLayout) phoneContainer.getChildAt(i);

            if (i == phoneContainer.getChildCount() - 1) {
                for (int j = 0; j < phoneItem.getChildCount(); j++) {
                    View view = phoneItem.getChildAt(j);
                    if (view instanceof EditText) {
                        ((EditText) view).setText("");
                    }
                }
            } else {
                phoneContainer.removeView(phoneItem);
            }
        }
    }

    public void validateAction (View view) {
        String str_res = date.getText() + ", "
                + city.getSelectedItem();

        if (phoneContainer.getChildCount() > 1) {
            // 遍历phoneContainer中的每个LinearLayout
            for (int i = 1; i < phoneContainer.getChildCount(); i++) {
                // 获取当前LinearLayout
                LinearLayout phoneItem = (LinearLayout) phoneContainer.getChildAt(i);

                // 遍历phoneItem中的每个子项
                for (int j = 0; j < phoneItem.getChildCount(); j++) {
                    // 获取当前子项
                    View v = phoneItem.getChildAt(j);
                    // 检查当前子项是否是EditText
                    if (v instanceof EditText) {
                        // 获取EditText的文本内容
                        str_res = str_res + ", " + ((EditText) v).getText();
                    }
                }
            }
        }
        str_res = str_res.substring(0, str_res.length() - 1);
        Snackbar.make(view, str_res, Snackbar.LENGTH_LONG).show();
    }

    public void showDatePickerDialog(View view) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // 在这里处理选择的日期
                String date_str = (month + 1) + "/" + dayOfMonth + "/" + year;
                date.setText(date_str);
            }
        }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    public void phoneAction(View view) {
        Button button = (Button) view;

        LinearLayout phoneItem = findParentLinearLayout(view);
        if (phoneItem == null) return;

        Resources res = getResources();

        if (button.getText().equals(res.getString(R.string.add))) {
            LinearLayout newPhoneItem = new LinearLayout(this);
            newPhoneItem.setOrientation(LinearLayout.HORIZONTAL);
            newPhoneItem.setLayoutParams(phoneItem.getLayoutParams());

            EditText newPhoneNumber = new EditText(this);
            newPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // width
                    LinearLayout.LayoutParams.MATCH_PARENT); // height
            newPhoneNumber.setLayoutParams(layoutParams);
            newPhoneNumber.setEms(10);

            Button newButton = new Button(this);
            newButton.setLayoutParams(button.getLayoutParams());
            newButton.setTextColor(button.getCurrentTextColor());
            newButton.setBackground(button.getBackground());
            newButton.setText(res.getString(R.string.add));
            newButton.setAllCaps(false);
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneAction(v);
                }
            });
            button.setText(res.getString(R.string.delete));

            newPhoneItem.addView(newPhoneNumber);
            newPhoneItem.addView(newButton);
            phoneContainer.addView(newPhoneItem);

        } else {
            phoneContainer.removeView(phoneItem);
        }

        int childCount = phoneContainer.getChildCount() - 2;
        String str = String.valueOf(childCount) + " phone number";
        Snackbar.make(phoneContainer, str, Snackbar.LENGTH_LONG).show();
    }

    private void addPhoneItem() {

    }

    private LinearLayout findParentLinearLayout(View view) {
        ViewParent parent = view.getParent();

        while (parent != null) {
            if (parent instanceof LinearLayout) {
                return (LinearLayout) parent;
            }
            parent = parent.getParent();
        }

        return null; // 如果未找到 LinearLayout，则返回 null
    }

}