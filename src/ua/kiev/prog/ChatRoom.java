package ua.kiev.prog;

import java.util.LinkedList;

public class ChatRoom {
    private String name;
    private LinkedList<String> usersList = new LinkedList<>();

    public ChatRoom(String name, String login) {
        this.name = name;
        usersList.add(login);
    }

    public void addUser(String login) {
        usersList.add(login);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(LinkedList<String> usersList) {
        this.usersList = usersList;
    }
}