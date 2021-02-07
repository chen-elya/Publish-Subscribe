package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import java.util.List;

public class SendAgentEvent<T> implements Event {
    int time;
    List<String> serials;

    public SendAgentEvent(List<String> serials, int time) {
        this.serials = serials;
        this.time = time;
    }

    public List<String> getSerials() {
        return serials;
    }

    public int getTime() {
        return time;
    }

}
