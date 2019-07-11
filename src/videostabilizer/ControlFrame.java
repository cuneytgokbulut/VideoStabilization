package videostabilizer;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ColorProcessor;
import ij.process.ImageProcessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class ControlFrame extends JFrame {
    ArrayList<ControlPanel> panelList = new ArrayList<>();
    ImagePlus imagePlus =null;
    ImagePlus imagePlus2 =null;
    ImagePlus imagePlusDifference =null;
    ControlPanel currentPanel = null;
    Timer timer = null;
    TimerTaskStab tts = null;
    boolean started = false;

    Image currentImage = null;
    Image previousImage = null;

    int yaricap = 1;
    int threshold = 100;

    boolean acik = true;


    public ControlFrame() {

        imagePlus = IJ.openImage("C:/png/Untitled00090255.png");
        imagePlus.show();

        imagePlus2 = IJ.openImage("C:/png/Untitled00090255.png");
        imagePlusDifference = IJ.openImage("C:/png/Untitled00090255.png");
        imagePlusDifference.show();

        JButton bRefresh = new JButton("Refresh");
        bRefresh.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {

                acik = !acik;

            }
        });
        bRefresh.setBounds(10,5,100,22);
        add(bRefresh);


        JPanel panel = new JPanel();
        panel.setLayout(null);
        JLabel topLabel = new JLabel("Top");
        topLabel.setBounds(50,30,50,18);
        add(topLabel);

        JLabel leftLabel = new JLabel("Left");
        leftLabel.setBounds(105,30,50,18);
        add(leftLabel);

        JLabel rotateLabel = new JLabel("Rotate");
        rotateLabel.setBounds(155,30,50,18);
        add(rotateLabel);

        JLabel zoomLabel = new JLabel("Zoom");
        zoomLabel.setBounds(210,30,50,18);
        add(zoomLabel);
        try {
            File f = new File("D:/config.txt");
            BufferedReader b = new BufferedReader(new FileReader(f));
            String readLine = "";
            int i=2;
            while ((readLine = b.readLine()) != null) {
                String[] vals = readLine.split("&&&");
                ControlPanel cp = new ControlPanel(vals,i-2,this);
                cp.setBounds(0,i*30,400,30);
                panel.add(cp);
                panelList.add(cp);
                i++;
                System.out.println(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane);
        setSize(300,800);
        panel.setPreferredSize(new Dimension(300,2000));
        setLocation(15, 15);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void main(String[] args) throws Exception{
        new ControlFrame();
    }


    public void setSelected(int  indis) {
        for (int i = 0; i < panelList.size(); i++) {
            panelList.get(i).setBackground(i == indis ? Color.lightGray : Color.white);
        }



        currentPanel = panelList.get(indis);
        currentImage = IJ.openImage(panelList.get(indis).path).getImage();
        previousImage = IJ.openImage(panelList.get(panelList.indexOf(currentPanel) - 1).path).getImage();
        imagePlus.setImage(currentImage);
        redraw();
        farkCikar();
    }
    public void redraw(ControlPanel cp, boolean isCurrent) {
        imagePlus2.setImage(isCurrent?currentImage:previousImage);
        ImageProcessor proc = imagePlus2.getProcessor();
        proc.applyParameters(Double.valueOf(cp.rText.getText()),Integer.valueOf(cp.topText.getText()),Integer.valueOf(cp.leftText.getText()),Double.valueOf(cp.zText.getText()));
        imagePlus.setImage(imagePlus2);
        imagePlus.unlock();
    }

    public void redraw() {
        redraw(currentPanel,true);
    }
    public void redrawPrev() {
        redraw(panelList.get(panelList.indexOf(currentPanel) - 1),false);
    }

    public void terminate() {

    }

    public void blink() {
        if (started) {
            timer.cancel();
            tts.cancel();
            started = false;
            redraw();
        }
        else {
            timer = new Timer();
            tts = new TimerTaskStab(this);
            started = true;
            timer.schedule(tts, 0, 10);
        }
    }

    public void farkCikar() {
        int[] a = ((ColorProcessor)imagePlus.getProcessor()).pixels;
        ControlPanel cp =panelList.get(panelList.indexOf(currentPanel) - 1);
        imagePlus2.setImage(previousImage);
        ImageProcessor proc = imagePlus2.getProcessor();
        proc.applyParameters(Double.valueOf(cp.rText.getText()),Integer.valueOf(cp.topText.getText()),Integer.valueOf(cp.leftText.getText()),Double.valueOf(cp.zText.getText()));

        int[] b = ((ColorProcessor)imagePlus2.getProcessor()).pixels;

        imagePlusDifference.setImage(imagePlus.getImage());

        imagePlusDifference.getCanvas().cf = this;

        double hit = 0.0;
        double difference = 0.0;
        double count = 0.0;
        for (int i=0;i<a.length;i++) {
            int mod = i%imagePlusDifference.getWidth();
            if (imagePlusDifference.getRoi()!=null && !imagePlusDifference.getRoi().contains(mod,(i-mod)/imagePlusDifference.getWidth())) {
                ((ColorProcessor)imagePlusDifference.getProcessor()).pixels[i] = b[i];
                continue;
            }
            count++;
            int enKucuk = Integer.MAX_VALUE;
            for (int x=-yaricap;x<=yaricap;x++) {
                for (int y=-yaricap;y<=yaricap;y++) {
                    try {
                        int cons = Math.abs(a[i + x * imagePlusDifference.getWidth() + y] - b[i + x * imagePlusDifference.getWidth() + y]);
                        if (cons < enKucuk) enKucuk = cons;
                    }
                    catch (Exception ex) {}
                }
            }
            if (i==0) difference = ((double)Math.abs(a[i]-b[i]));
            else difference = (difference*((double)(i-1))+((double)Math.abs(a[i]-b[i])))/((double)i);

            if (enKucuk>threshold) {
                ((ColorProcessor)imagePlusDifference.getProcessor()).pixels[i] = new Color(255, 255-(enKucuk/55000), 255-(enKucuk/55000)).getRGB();

            }
            else {
                ((ColorProcessor)imagePlusDifference.getProcessor()).pixels[i] = b[i];
                hit++;
            }
        }
        imagePlusDifference.updateAndDraw();
        imagePlusDifference.setTitle("Hit: % "+(hit*100.0/count)+"\nDifference: "+(difference/count));
       }
}
