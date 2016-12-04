package ua.kiev.prog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonMessages {
    private final List<Message> list;
    private final ChatRoomsList chrList = ChatRoomsList.getInstance();

    public JsonMessages(List<Message> sourceList, int fromIndex, String login) {
        this.list = new ArrayList<>();
        for (int i = fromIndex; i < sourceList.size(); i++) {
            if (!sourceList.get(i).getChatroom().equals("")) {
                for (ChatRoom cr: chrList) {
                    if(sourceList.get(i).getChatroom().equals(cr.getName())) {
                        if(cr.getUsersList().contains(login)) {
                            list.add(sourceList.get(i));
                        }
                    }
                }
            } else if (sourceList.get(i).getTo().equals("") || sourceList.get(i).getTo().equals(login) || sourceList.get(i).getFrom().equals(login))
                list.add(sourceList.get(i));
        }
    }

    public List<Message> getList() {
        return Collections.unmodifiableList(list);
    }
}
