package net.minecraftforge.fml.common.eventhandler;
import java.util.*;
public class EventBus {
    private final List<Object> listeners = new ArrayList<>();
    public void register(Object o) { listeners.add(o); }
    public void unregister(Object o) { listeners.remove(o); }
    public boolean post(Event e) { return false; }
}