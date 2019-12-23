package lan.client.game.base;

public class Button {
    public enum Type {
        UP,
        LEFT,
        DOWN,
        RIGHT
    }

    private long state;
    public Button() {

    }

    public long getState() {
        return state;
    }

    public void setState(long state) {
        this.state = state;
    }

    public void set(Type type, boolean pushed) {
       if(pushed) {
           state |= 1 << type.ordinal();
       } else {
           state &= -1 - 1 << type.ordinal();
       }
    }

    public boolean test(Type type) {
        return ((1 << type.ordinal()) & state) > 0;
    }
}
