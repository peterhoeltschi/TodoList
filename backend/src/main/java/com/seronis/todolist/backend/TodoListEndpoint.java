/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.seronis.todolist.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;
import com.googlecode.objectify.cmd.Saver;

import static com.seronis.todolist.backend.OfyService.ofy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "todoListApi",
        version = "v2",
        namespace = @ApiNamespace(
                ownerDomain = "backend.todolist.seronis.com",
                ownerName = "backend.todolist.seronis.com",
                packagePath = ""
        )
)
public class TodoListEndpoint {

    //public static List<TodoListItem> items = new ArrayList<TodoListItem>();

    @ApiMethod(name="insert")
    public TodoListItem insertItem(TodoListItem item) throws ConflictException {
        //Check for already exists
        //int index = items.indexOf(new TodoListItem(id));
        //if (index != -1) throw new NotFoundException("Item Record already exists");
        //TodoListItem i = new TodoListItem(id, body, priority);
        //items.add(i);
        //return i;

        if (item.getId() != null) {
            if (findRecord(item.getId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
        //Since our @Id field is a Long, Objectify will generate a unique value for us
        //when we use put

        ofy().save().entity(item).now();

       // Log.iitem.getId()
        //ofy().save().entities(recipe.getSteps()).now();  //superfluous?
        //ofy().save().entity(recipe).now();

        //logger.info("Created Recipe with ID: " + recipe.getId());


        //Long keyLong = Long.valueOf(key);
        //item.setId(keyLong);
        return item;
    }

    @ApiMethod(name="update")
    public TodoListItem updateItem(TodoListItem item) throws NotFoundException {
        //int index = items.indexOf(i);
        //if (index == -1)
        //    throw new NotFoundException("Item Record does not exist");
        //TodoListItem currentItem = items.get(index);
        //currentItem.setBody(i.getBody());
        //currentItem.setPriority(i.getPriority());
        //return i;
        if (findRecord(item.getId()) == null) {
            throw new NotFoundException("Quote Record does not exist");
        }
        ofy().save().entity(item).now();
        return item;

    }

    @ApiMethod(name="remove")
    public void removeItem(@Named("id") Long id) throws NotFoundException {
        //int index = items.indexOf(new TodoListItem(id));
        //if (index == -1)
        //    throw new NotFoundException("Item Record does not exist");
        //items.remove(index);
        TodoListItem record = findRecord(id);
        if(record == null) {
            throw new NotFoundException("Quote Record does not exist");
        }
        ofy().delete().entity(record).now();

    }

    //@ApiMethod(name="list")
    //public List<TodoListItem> getItems() {
    //    return items;
    //}

    @ApiMethod(name="list")
    public List<TodoListItem> listItems(@Nullable @Named("cursor") String cursorString,
                                               @Nullable @Named("count") Integer count) {
        Query<TodoListItem> query = ofy().load().type(TodoListItem.class);


       /* if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }*/

        List<TodoListItem> records = new ArrayList<TodoListItem>();
        QueryResultIterator<TodoListItem> iterator = query.iterator();

        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            if (count != null) {
                num++;
                if (num == count) break;
            }
        }

        /*
        //Find the next cursor
        if (cursorString != null && cursorString != "") {
            Cursor cursor = iterator.getCursor();
            if (cursor != null) {
                cursorString = cursor.toWebSafeString();
            }
        }
        return CollectionResponse.<TodoListItem>builder().setItems(records).setNextPageToken(cursorString).build();
    */

        return records;
    }

    //@ApiMethod(name="getItem")
    //public TodoListItem getItem(@Named("id") Long id) throws NotFoundException {
    //    int index = items.indexOf(new TodoListItem(id));
    //    if (index == -1)
    //        throw new NotFoundException("Item Record does not exist");
    //    return items.get(index);
    //}

    private TodoListItem findRecord(Long id) {
        return ofy().load().type(TodoListItem.class).id(id).now();
    //or return ofy().load().type(Quote.class).filter("id",id).first.now();
    }

}
