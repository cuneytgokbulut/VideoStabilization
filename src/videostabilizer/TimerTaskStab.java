package videostabilizer;

import java.util.TimerTask;

/**
 * Created by asus on 23.06.2019.
 */
public class TimerTaskStab extends TimerTask {
    ControlFrame controlFrame;
    boolean onceki = true;

    public TimerTaskStab(ControlFrame controlFrame) {
        super();
        this.controlFrame = controlFrame;

    }
    @Override
    public void run() {
        onceki = !onceki;
        if (onceki) controlFrame.redraw();
        else controlFrame.redrawPrev();
    }
}
