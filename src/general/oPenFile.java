package general;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class oPenFile implements ActionListener {
	Frame f = new Frame("Open");
	JLabel l = new JLabel("File");
	JTextField tf = new JTextField(20);
	JButton b = new JButton("Open File");
	JFileChooser fd = new JFileChooser();
	private String FileName = "";
	
	public String getFileName(){
		return FileName;
	}
	
	public String xOpenFile() {
	   fd.setDialogTitle("Open");
	   fd.setCurrentDirectory(new File("D:\\"));
	   /*
	   fd.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    int state = fd.showOpenDialog(null);
	    if (state == 1) {
	     return null;
	    } else {
	     File f = fd.getSelectedFile();
	     return f.getAbsolutePath();
	    }
	   */
	   double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	   double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	   b.addActionListener(this);
	   f.setLayout(new FlowLayout());
	   f.add(l);
	   f.add(tf);
	   f.add(b);
	   f.setLocation((int) lx / 2 - 150, (int) ly / 2 - 150);
	   f.setSize(400, 400);
	   f.setVisible(true);
	   f.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent e) {
	     System.exit(0);
	    }
	   });
	   
	   return null;
	}

	public void actionPerformed(ActionEvent e) {
	   if (e.getSource().equals(b)) {
	    fd.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    int state = fd.showOpenDialog(null);
	    if (state == 1) {
	     return;
	    } else {
	     File f = fd.getSelectedFile();
	     tf.setText(f.getAbsolutePath());
	    }
	   }
	}
}
