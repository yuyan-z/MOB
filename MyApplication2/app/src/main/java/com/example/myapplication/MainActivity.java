package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private EditText name, surname, date;
    private Spinner city;
    private LinearLayout phoneContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.editTextText1);
        surname = findViewById(R.id.editTextText2);
        date = findViewById(R.id.editTextText3);
        city = findViewById(R.id.spinner1);
        phoneContainer = findViewById(R.id.phoneContainer);
    }

    public void validateAction (View view) {
        String str_res = name.getText() + ", "
                + surname.getText() + ", "
                + date.getText() + ", "
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void resetAction (MenuItem item) {
        name.setText("");
        surname.setText("");
        date.setText("");

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