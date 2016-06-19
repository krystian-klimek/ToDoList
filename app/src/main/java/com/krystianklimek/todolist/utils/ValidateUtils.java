package com.krystianklimek.todolist.utils;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.krystianklimek.todolist.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.krystianklimek.todolist.utils.ActivityUtils.showToast;

/**
 * Created by: Krystian Klimek
 * Date: 19.06.2016.
 */

public class ValidateUtils {
    public static int checkEditText(EditText editText){
        String username = editText.getText().toString();

        if(username.equals("")) return 0; // empty
        return 1; // ok
    }

    public static Boolean validateTaskData(View view, Context context) {
        Boolean validate = true;

        EditText title = (EditText) view.findViewById(R.id.titleEditText);
//        EditText description = (EditText) view.findViewById(R.id.descriptionEditText);
//        EditText urlToIcon = (EditText) view.findViewById(R.id.urlToIconEditText);

        List<EditText> toCheck = new ArrayList<>();

        toCheck.add(title);
//        toCheck.add(description);
//        toCheck.add(urlToIcon);

        for (EditText editText : toCheck) {
            switch (checkEditText(editText)) {
                case 0:
                    editText.setError(editText.getTag() + " " + context.getString(R.string.field_cannot_be_empty));
                    validate = false;
                    break;
                case 1:
                    break;
            }
        }

        TextView endTime = (TextView) view.findViewById(R.id.endTimeTextView);

        if (isDateValid(endTime.getText().toString())) {
            showToast(context, "End date must be in the future!");
            validate = false;
        }

        return validate;
    }

    public static Boolean isDateValid(String dateString1) {
        Calendar calendar = Calendar.getInstance();
        int year, month, day, hourOfDay, minute;

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        String dateString2 = year + "-" + twoDigits(month) + "-" + twoDigits(day) + " " + twoDigits(hourOfDay) + ":" + twoDigits(minute);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
            Date date1 = sdf.parse(dateString1);
            Date date2 = sdf.parse(dateString2);

            if(date1.compareTo(date2) < 0){
                // Date1 is after Date2
                return true;
            } else {
                return false;
            }

        } catch(ParseException ex){
            ex.printStackTrace();
            return false;
        }
    }

    private static String twoDigits (int number) {
        return String.format("%02d", number);
    }
}
