package com.muroigundan.task_app;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by local-user on 2017/10/20.
 */

public class SchedulerAdapter extends RealmBaseAdapter<Task> {
    private static class ViewHolder {
        TextView subject;
        TextView deadline;
    }
    public SchedulerAdapter(@Nullable OrderedRealmCollection<Task> data) {
        super(data);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.subject= (TextView) convertView.findViewById(R.id.text1);

        }
        return null;
    }

}
