package com.seronis.todolist.Provider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import com.seronis.todolist.backend.todoListApi.TodoListApi;
import com.seronis.todolist.backend.todoListApi.model.TodoListItem;

import java.io.IOException;

class InsertAsyncTask extends AsyncTask<TodoListItem, Void, TodoListItem> {
    private static TodoListApi myApiService = null;
    private Context context;

    InsertAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected TodoListItem doInBackground(TodoListItem... params) {
        if(myApiService == null) { // Only do this once
            TodoListApi.Builder builder = new TodoListApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setRootUrl("https://todolist-1248.appspot.com/_ah/api")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        try {
            TodoListItem item = (TodoListItem) params[0];

            //call rest webservice
            return myApiService.insert(item).execute();
        } catch (IOException e) {
            Log.i("TAG", "error "+e.getMessage());
            return null;
        }
    }

    //@Override
    //protected void onPostExecute(TodoListItem result) {

        //for (TodoListItem q : result) {
        //    Toast.makeText(context, q.getBody() + " : " + q.getPriority(), Toast.LENGTH_LONG).show();
        //}
    //}
}