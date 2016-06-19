package com.krystianklimek.todolist.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.krystianklimek.todolist.R;
import com.krystianklimek.todolist.model.TaskModel;
import com.krystianklimek.todolist.utils.ActivityUtils;
import com.krystianklimek.todolist.utils.DatabaseHelper;
import com.krystianklimek.todolist.utils.ValidateUtils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.krystianklimek.todolist.utils.ActivityUtils.replaceActivity;
import static com.krystianklimek.todolist.utils.ActivityUtils.showToast;

/**
 * Created by: Krystian Klimek
 * Date: 19.06.2016.
 */

public class SecondActivity extends AppCompatActivity {
    @BindView(R.id.titleEditText)
    EditText titleEditText;
    @BindView(R.id.descriptionEditText)
    EditText descriptionEditText;
    @BindView(R.id.urlToIconEditText)
    EditText urlToIconEditText;
    @BindView(R.id.createdTextView)
    TextView createdTextView;
    @BindView(R.id.endTimeTextView)
    TextView endTimeTextView;
    @BindView(R.id.actionTaskButton)
    Button actionTaskButton;

    private TaskModel mTask;
    private DatabaseHelper mDb;

    private Calendar mCalendar;
    private int mId;
    private int mYear, mMonth, mDay, mHourOfDay, mMinute;
    private List<String> mMonths = Arrays.asList(new DateFormatSymbols().getMonths());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        mCalendar = Calendar.getInstance();

        databaseInit();

        Intent intent = getIntent();
        mId = -1;

        if (intent.getExtras() != null)
            mId = intent.getExtras().getInt(MainActivity.KEY_ID, -1);

        getSingleTask(mId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ActivityUtils.overridePendingTransition(this);
    }

    private void getSingleTask(int id) {
        if (id != -1) {
            actionTaskButton.setText(R.string.edit_task);
            mTask = mDb.getTask(id);
            displayTask();
        }
    }

    private void displayTask() {
        titleEditText.setText(mTask.getTitle());
        descriptionEditText.setText(mTask.getDescription());
        urlToIconEditText.setText(mTask.getUrl_to_icon());
        if (!mTask.getTime_end().equals(""))
            endTimeTextView.setText(mTask.getTime_end());
        createdTextView.setText(mTask.getCreated());
    }

    private void databaseInit() {
        mDb = new DatabaseHelper(this);
    }

    private void initDate() {
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHourOfDay = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        showDate();
    }

    @SuppressWarnings("deprecation")
    @OnClick(R.id.endTimeTextView)
    public void onDateTextViewClick(View view) {
        getDateFromTextView();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, mDateListener, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(mCalendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void getDateFromTextView() {
        if (!endTimeTextView.getText().toString().equals(getString(R.string.end_time))) {
            String date = endTimeTextView.getText().toString();

            mYear = Integer.parseInt(date.substring(0,4));
            mMonth = Integer.parseInt(date.substring(5,7)) - 1; // cause it must be downgrade to DatePickerDialog
            mDay = Integer.parseInt(date.substring(8,10));
            mHourOfDay = Integer.parseInt(date.substring(11,13));
            mMinute = Integer.parseInt(date.substring(14,16));
        }
    }

    private DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            mYear = arg1;
            mMonth = arg2 + 1; // cause it start count from 0
            mDay = arg3;

            TimePickerDialog timePickerDialog = new TimePickerDialog(SecondActivity.this, mTimeListener, mHourOfDay, mMinute, true);
            timePickerDialog.show();
        }
    };

    private TimePickerDialog.OnTimeSetListener mTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            mHourOfDay = hourOfDay;
            mMinute = minute;
            showDate();
        }
    };

    private void showDate() {
        String date = mYear + "-" + twoDigits(mMonth) + "-" + twoDigits(mDay) + " " + twoDigits(mHourOfDay) + ":" + twoDigits(mMinute);
        endTimeTextView.setText(date);
    }

    private String twoDigits (int number) {
        return String.format("%02d", number);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    @OnClick(R.id.actionTaskButton)
    public void onActionTaskButtonClick() {
        if(ValidateUtils.validateTaskData(this.getWindow().getDecorView().getRootView(), getApplicationContext())) {
            showToast(getApplicationContext(), "Validation task completed.");

            // -1 = add new task
            if (mId != -1) {
                mTask = getDataFromFields();
                mDb.updateTask(mTask);
            } else {
                mTask = getDataFromFields();
                mDb.addTask(mTask);
            }

            onBackPressed();
        }
    }

    private TaskModel getDataFromFields() {
        if (mId != -1) {
            return new TaskModel(mId, titleEditText.getText().toString(), descriptionEditText.getText().toString(), urlToIconEditText.getText().toString(), createdTextView.getText().toString(), endTimeTextView.getText().toString());
        } else {
            return new TaskModel(titleEditText.getText().toString(), descriptionEditText.getText().toString(), urlToIconEditText.getText().toString(), getCurrentTimeStamp(), (endTimeTextView.getText().toString().equals(getString(R.string.end_time)) ? "" : endTimeTextView.getText().toString()));
        }
    }
}
