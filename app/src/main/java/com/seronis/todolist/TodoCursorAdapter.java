package com.seronis.todolist;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 *
 * https://github.com/codepath/android_guides/wiki/Populating-a-ListView-with-a-CursorAdapter
 */
public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
    }
/**
* bind
*
*/
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final Uri todoListURI = Uri.parse(context.getString(R.string.uri));

        TextView tvBody = (TextView) view.findViewById(R.id.tvBody);
        TextView tvPriority = (TextView) view.findViewById(R.id.tvPriority);
        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
        int priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority"));
        tvBody.setText(body);
        tvPriority.setText(String.valueOf(priority));
        final int rowID = cursor.getInt(cursor.getColumnIndex("_id"));
        final CheckBox lvCheckbox = (CheckBox) view.findViewById(R.id.checkBox);
        lvCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String rowIDString = String.valueOf(rowID);
                context.getContentResolver().delete(todoListURI,rowIDString,null);
                Cursor todoCursorUpdated = context.getContentResolver().query(todoListURI, null, null, null, null);
                changeCursor(todoCursorUpdated);
            }
        });
    }
}