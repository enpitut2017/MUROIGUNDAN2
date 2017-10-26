package com.muroigundan.task_app;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by local-user on 2017/10/20.
 */

public class SchedulerAdapter extends RealmBaseAdapter<Task> {
    private static class ViewHolder {
        TextView subject;
        TextView date;
    }
    public SchedulerAdapter(@Nullable OrderedRealmCollection<Task> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.subject= (TextView) convertView.findViewById(android.R.id.text1);
            viewHolder.date= (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Task task = adapterData.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String formatDate = sdf.format(task.getDate());
        viewHolder.subject.setText(task.getSubject());
        viewHolder.date.setText(formatDate);
        return convertView;
    }

}
