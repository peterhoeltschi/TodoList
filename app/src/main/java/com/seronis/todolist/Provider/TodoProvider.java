package com.seronis.todolist.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.seronis.todolist.backend.todoListApi.model.TodoListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TodoProvider extends ContentProvider {
    public TodoProvider() {
    }

    private TodoDatabaseHandler todoDatabaseHandler;

    @Override
    public boolean onCreate() {
        todoDatabaseHandler = new TodoDatabaseHandler(getContext());
        return true;
    }

    public SQLiteDatabase getDB(){
        return todoDatabaseHandler.getWritableDatabase();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = getDB().rawQuery("SELECT  cloud_id FROM todo_items WHERE _id = "+ selection+";", null);
        cursor.moveToFirst();
        Long cloudID = cursor.getLong(0);

        new DeleteAsyncTask(getContext()).execute(cloudID);

        getDB().execSQL("DELETE FROM todo_items WHERE _id = " + selection + ";");
        //db.execSQL("DELETE FROM todo_items;");
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        TodoListItem item = new TodoListItem();
        String s1 = values.getAsString("body");
        String s2 = values.getAsString("priority");
        item.setBody(s1);
        item.setPriority(s2);

        TodoListItem resultItem = null;
        Long cloudID = null;
        try {
            resultItem = (TodoListItem) new InsertAsyncTask(getContext()).execute(item).get();
            cloudID = resultItem.getId();
            String resultBody = resultItem.getBody();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        getDB().execSQL("INSERT INTO todo_items (body, priority, cloud_id) VALUES ('"+values.get("body")+"', '"+values.get("priority")+"', '"+cloudID+"');");
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        QueryAsyncTask task = (QueryAsyncTask) new QueryAsyncTask(getContext()).execute();

        List<TodoListItem> itemsC = null;
        try{
            itemsC = (List<TodoListItem>)task.get();
            Log.i("TAG","ok");
        }catch(Exception e){
            Log.i("TAG", this.toString()+" Error: "+e.getMessage());
        }

        Cursor cursor = getDB().rawQuery("SELECT  * FROM todo_items", null);

        List<TodoListItem> itemsL = new ArrayList<TodoListItem>();
        if (itemsC != null) {
            for (TodoListItem itemC: itemsC){
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    String body = cursor.getString(0);
                    String priority = cursor.getString(1);

                    TodoListItem itemL = new TodoListItem();
                    itemL.setBody(body);
                    Log.i("TAG", "Item: " + body);
                    itemL.setPriority(priority);
                    itemsL.add(itemL);
                    cursor.moveToNext();
                }
            }
        }

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static class TodoDatabaseHandler extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "todoDB.db";
        private static int DATABASE_VERSION = 4;

        public TodoDatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db);
        }

        private void createTable(SQLiteDatabase sqLiteDatabase) {
            String qs = "CREATE TABLE todo_items (" +
                    BaseColumns._ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "body TEXT, " +
                    "priority TEXT, " +
                    "cloud_id LONG);";
            sqLiteDatabase.execSQL(qs);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + "todo_items;");
            createTable(db);
        }
    }
}
