package videostabilizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPanel extends JPanel {


    public JTextField topText;
    public JTextField leftText;
    public JTextField rText;
    public JTextField zText;
    public JTextField focused;

    public String path;

    public ControlPanel(String[] vals, int indis, ControlFrame frame) {
        super();

        path = vals[1];
        JLabel number = new JLabel(vals[0]);
        setLayout(null);
        number.setBounds(10,5,50,15);
        add(number);

        number.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                frame.setSelected(indis);
                focused.requestFocus();
            }
        });



        topText = new JTextField(vals[2]);
        topText.setBounds(40,5,50,18);
        add(topText);

        leftText = new JTextField(vals[3]);
        leftText.setBounds(95,5,50,18);
        add(leftText);

        rText = new JTextField(vals[4]);
        rText.setBounds(150,5,50,18);
        add(rText);

        zText = new JTextField(vals[5]);
        zText.setBounds(205,5,50,18);
        add(zText);


        setBorder(BorderFactory.createLineBorder(Color.black));

        focused = new JTextField("");
        focused.setBounds(1,1,1,1);
        add(focused);
        focused.addKeyListener(new StabKeyListener(frame,this));

    }
}
