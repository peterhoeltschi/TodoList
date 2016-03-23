package com.seronis.todolist.backend;

/**
 * The object model for the data we are sending through endpoints
 */

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Entity
public class TodoListItem {

    @Id
    private Long id;

    private String body;

    private String priority;

    public TodoListItem() {
    }

    public TodoListItem(Long id) {
        super();
        this.id = id;
    }

    public TodoListItem(Long id, String body, String priority) {
        super();
        this.id = id;
        this.body = body;
        this.priority = priority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public String getPriority() {
        return priority;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TodoListItem other = (TodoListItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Quote [id=" + id + ", body=" + body + ", priority="
                + priority + "]";
    }
}