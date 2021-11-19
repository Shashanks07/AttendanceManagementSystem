package swing;

import java.awt.Color;
import javax.swing.JProgressBar;

public class circularProgressBar extends JProgressBar{

    
    public circularProgressBar() {
        //set Value to total attendance percentage
        setValue(50);
        setOpaque(false);
        setBackground(Color.yellow);
        setForeground(Color.ORANGE);
        setStringPainted(true);
        setUI(new ProgressCircleUI());
    }
    
}
