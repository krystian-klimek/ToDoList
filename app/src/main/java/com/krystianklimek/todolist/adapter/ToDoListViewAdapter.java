package com.krystianklimek.todolist.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.krystianklimek.todolist.R;
import com.krystianklimek.todolist.activities.MainActivity;
import com.krystianklimek.todolist.activities.SecondActivity;
import com.krystianklimek.todolist.model.TaskModel;
import com.krystianklimek.todolist.utils.DatabaseHelper;
import com.krystianklimek.todolist.utils.ValidateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.krystianklimek.todolist.utils.ActivityUtils.replaceActivity;
import static com.krystianklimek.todolist.utils.ActivityUtils.showToast;

/**
 * Created by: Krystian Klimek
 * Date: 19.06.2016.
 */

public class ToDoListViewAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<TaskModel> mTaskList;
    private DatabaseHelper mDb;

    public ToDoListViewAdapter(Activity activity, List<TaskModel> items) {
        mActivity = activity;
        mTaskList = items;
        mDb = new DatabaseHelper(activity);
    }

    @Override
    public int getCount() {
        return mTaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_view_single_item, parent, false);
        }

        holder = new ViewHolder(convertView);

        final TaskModel task = mTaskList.get(position);

        holder.title.setText(task.getTitle());

        if (!task.getDescription().equals("")) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(task.getDescription());
        } else {
            holder.description.setVisibility(View.GONE);
        }

        holder.created.setText(task.getCreated());

        if(!task.getTime_end().equals("")) {
            holder.endTime.setText(task.getTime_end());
            ColorStateList oldColor =  holder.endTime.getTextColors();
            if (ValidateUtils.isDateValid(task.getTime_end())) {
                holder.endTime.setTextColor(mActivity.getResources().getColor(R.color.colorError));
            } else {
                holder.endTime.setTextColor(oldColor);
            }
        } else {
            holder.endTime.setText("–");
        }

        if(!task.getUrl_to_icon().equals("") || task.getUrl_to_icon().contains(".jpg") || task.getUrl_to_icon().contains(".png")) {
            Glide
                    .with(mActivity)
                    .load(task.getUrl_to_icon())
                    .centerCrop()
                    .crossFade()
                    .into(holder.image);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setTitle("Deleting task \"" + task.getTitle() + "\"")
                        .setMessage("Accept to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDb.deleteTask(task.getId());
                                mTaskList.remove(position);
                                notifyDataSetChanged();
                                showToast(mActivity, "Task deleted.");
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();

                dialog.show();
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currId = task.getId();
                showToast(mActivity, String.valueOf(currId));
                replaceActivity(mActivity, SecondActivity.class, MainActivity.KEY_ID, currId);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.titleTextView)
        TextView title;
        @BindView(R.id.descriptionTextView)
        TextView description;
        @BindView(R.id.createdTextView)
        TextView created;
        @BindView(R.id.endTimeTextView)
        TextView endTime;
        @BindView(R.id.deleteButton)
        Button delete;
        @BindView(R.id.imageView)
        ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
