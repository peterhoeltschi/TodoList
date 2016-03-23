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
import java.util.Collections;
import java.util.List;


class QueryAsyncTask extends AsyncTask<Void, Void, List<TodoListItem>> {
    private static TodoListApi myApiService = null;
    private Context context;

    QueryAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<TodoListItem> doInBackground(Void... params) {
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
                            abstractGoogleClientRequest.setDisableGZipContent(false);
                        }
                    });

            myApiService = builder.build();
        }

        try {

            //call rest webservice
            return myApiService.list().execute().getItems();
        } catch (IOException e) {
            Log.i("TAG", "in "+e.getMessage());
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    protected void onPostExecute(List<TodoListItem> result) {
        //for (TodoListItem q : result) {
        //    Toast.makeText(context, q.getBody() + " : " + q.getPriority(), Toast.LENGTH_SHORT).show();
        //}
    }
}