package ua.kiev.prog;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChatRoomsList implements Iterable<ChatRoom>{

    private static final ChatRoomsList chrList = new ChatRoomsList();

    private final List<ChatRoom> list = new LinkedList<>();

    private ChatRoomsList() {

    }

    public static ChatRoomsList getInstance() {
        return chrList;
    }

    public synchronized void addChatRoom(ChatRoom chatroom) {
        list.add(chatroom);
    }

    public ChatRoom getChatRoom(int n) {
        return list.get(n);
    }

    public ChatRoom getChatRoom(String name) {
        for (ChatRoom cr : list) {
            if(cr.getName().equals(name))
                return cr;
        }
        return null;
    }

    public Iterator<ChatRoom> iterator() {
        return list.iterator();
    }

    public boolean isChatRoomExist (String name) {
        for (ChatRoom cr : list) {
            if(cr.getName().equals(name))
                return true;
        }
        return false;
    }
}
