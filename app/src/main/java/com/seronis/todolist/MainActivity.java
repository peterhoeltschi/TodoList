package com.seronis.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
    
    private Button insertButton;

    private Uri todoListURI = null;
    private TodoCursorAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*OAuthService oauth = OAuthServiceFactory.getOAuthService();
        String scope = "https://www.googleapis.com/auth/userinfo.email";
        Set<String> allowedClients = new HashSet<>();
        allowedClients.add("285844236946-92bk39tv6vigs2cj5396752dtj8sjfko.apps.googleusercontent.com"); // list your client ids here

        try {
            User user = oauth.getCurrentUser(scope);
            String tokenAudience = oauth.getClientId(scope);
            if (!allowedClients.contains(tokenAudience)) {
                throw new OAuthRequestException("audience of token '" + tokenAudience
                        + "' is not in allowed list " + allowedClients);
            }
            // proceed with authenticated user
            // ...
        } catch (OAuthRequestException ex) {
            // handle auth error
            // ...
        } catch (OAuthServiceFailureException ex) {
            // optionally, handle an oauth service failure
            // ...
        }
*/

        //declare rest endpoint
        todoListURI = Uri.parse(getApplicationContext().getString(R.string.uri));

        //fill up ListView
        Cursor todoCursor = getContentResolver().query(todoListURI, null, null, null, null);
        ListView lvItems = (ListView) findViewById(R.id.lvItems);
        todoAdapter = new TodoCursorAdapter(this, todoCursor, 0);
        lvItems.setAdapter(todoAdapter);

        //insert new item to the todolist
        insertButton = (Button) findViewById(R.id.insert_button);
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                EditText bodyEditText = (EditText) findViewById(R.id.body);
                String body = bodyEditText.getText().toString();
                values.put("body", body);
                EditText priorityEditText = (EditText) findViewById(R.id.priority);
                String priority = priorityEditText.getText().toString();
                values.put("priority", priority);
                getContentResolver().insert(todoListURI, values);
                bodyEditText.setText("");
                priorityEditText.setText("");
                bodyEditText.requestFocus();

                Cursor todoCursorUpdated = getContentResolver().query(todoListURI, null, null, null, null);
                todoAdapter.changeCursor(todoCursorUpdated);
            }
        });
    }

    //public void getTodos(View v) {
    //    new QueryAsyncTask(this).execute();
    //}
}