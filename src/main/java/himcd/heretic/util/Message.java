package himcd.heretic.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Message {
    public static final MiniMessage msg = MiniMessage.miniMessage();

    public static LinkedList<Component> convertMsg(List<String> sl) {
        return sl.stream().map(msg::deserialize).collect(Collectors.toCollection(LinkedList::new));
    }
}