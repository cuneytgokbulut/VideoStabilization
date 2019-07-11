package videostabilizer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by asus on 22.06.2019.
 */
public class StabKeyListener extends KeyAdapter {
    ControlFrame frame;
    ControlPanel controlPanel;

    public StabKeyListener(ControlFrame frame, ControlPanel controlPanel) {
        super();
        this.frame = frame;
        this.controlPanel = controlPanel;
    }

    public void keyPressed(KeyEvent evt) {
        System.out.println(evt.getKeyCode());
        if (evt.getKeyCode()==65) {
            controlPanel.leftText.setText((Integer.valueOf(controlPanel.leftText.getText()) - 1) + "");
        }
        else if (evt.getKeyCode()==68) {
            controlPanel.leftText.setText((Integer.valueOf(controlPanel.leftText.getText())+1)+"");
        }
        else if (evt.getKeyCode()==83) {
            controlPanel.topText.setText((Integer.valueOf(controlPanel.topText.getText()) - 1) + "");
        }
        else if (evt.getKeyCode()==87) {
            controlPanel.topText.setText((Integer.valueOf(controlPanel.topText.getText()) + 1) + "");
        }
        else if (evt.getKeyCode()==32) {
            frame.blink();
        }
        else if (evt.getKeyCode()==74) {
            controlPanel.rText.setText((Double.valueOf(controlPanel.rText.getText()) + 0.001) + "");
        }
        else if (evt.getKeyCode()==72) {
            controlPanel.rText.setText((Double.valueOf(controlPanel.rText.getText()) - 0.001) + "");
        }
        else if (evt.getKeyCode()==79) {
            controlPanel.zText.setText((Double.valueOf(controlPanel.zText.getText()) + 0.001) + "");
        }
        else if (evt.getKeyCode()==80) {
            controlPanel.zText.setText((Double.valueOf(controlPanel.zText.getText()) - 0.001) + "");
        }
        else if (evt.getKeyCode()==90) {
                frame.terminate();
        }
        frame.redraw();
        frame.farkCikar();
    }


}