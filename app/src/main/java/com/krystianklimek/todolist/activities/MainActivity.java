package com.krystianklimek.todolist.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.krystianklimek.todolist.utils.DatabaseHelper;
import com.krystianklimek.todolist.R;
import com.krystianklimek.todolist.adapter.ToDoListViewAdapter;
import com.krystianklimek.todolist.model.TaskModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.krystianklimek.todolist.utils.ActivityUtils.replaceActivity;
import static com.krystianklimek.todolist.utils.ActivityUtils.showToast;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toDolistView)
    ListView toDoListView;

    private List<TaskModel> mTaskList = new ArrayList<>();
    private DatabaseHelper mDb;
    private ToDoListViewAdapter mAdapter;

    public static String KEY_ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        databaseInit();
        listViewInit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter = new ToDoListViewAdapter(this, getDataFromDatabase());
        toDoListView.setAdapter(mAdapter);
    }

    private void listViewInit() {
        mAdapter = new ToDoListViewAdapter(this, getDataFromDatabase());
        TextView empty = new TextView(this);
        empty.setHeight(240);
        toDoListView.addFooterView(empty);
        toDoListView.setAdapter(mAdapter);

//        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int currId = mTaskList.get(position).getId();
//                showToast(getApplicationContext(), String.valueOf(currId));
//                replaceActivity(MainActivity.this, SecondActivity.class, KEY_ID, currId);
//            }
//        });
    }

    private List<TaskModel> getDataFromDatabase() {
        mTaskList = mDb.getAllTasks();
        DatabaseHelper.sortTasks(mTaskList);
        return mTaskList;
    }

    private void databaseInit() {
        mDb = new DatabaseHelper(this);
    }

    @OnClick(R.id.addButton)
    public void onAddButtonClick() {
        replaceActivity(MainActivity.this, SecondActivity.class);
    }
}
