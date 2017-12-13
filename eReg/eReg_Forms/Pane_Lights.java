package eReg_Forms;

import java.awt.*;
import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import HVAC_Common.Ctrl_Actions_Stop;
import eRegulation.Global;
import eRegulation.LogIt;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Pane_Lights 										extends 					JPanel
{
	private JLabel 												lblHotWater;
	private JLabel 												lblFloor;
	private JLabel 												lblRadiator;
	private JLabel 												lblBurner;
	private JLabel 												lblMixerUp;
	private JLabel 												lblMixerDown;

    private ImageIcon 											lampOn;
    private ImageIcon 											lampOff;
    
    private JLabel												lampHotWater;
	private JLabel 												lampFloor;
 
	public Pane_Lights()
	{
		this.setLayout(null);
		this.setBounds(713, 11, 291, 174);

		lampOn																				= new ImageIcon(Pane_Lights.class.getResource("/eReg_Forms/lamp_on.jpg"));		
		lampOff																				= new ImageIcon(Pane_Lights.class.getResource("/eReg_Forms/lamp_off.jpg"));		
		
		
		lblHotWater 																		= new JLabel("Hot Water");
		lblHotWater.setBounds(10, 31, 65, 23);
		this.add(lblHotWater);
		
		lblFloor 																			= new JLabel("Floor");
		lblFloor.setBounds(10, 65, 65, 23);
		this.add(lblFloor);
		
		lblRadiator  																		= new JLabel("Radiator");
		lblRadiator.setBounds(10, 99, 65, 23);
		this.add(lblRadiator);
		
		JLabel 													lblPumps  					= new JLabel("Pumps");
		lblPumps.setHorizontalAlignment(SwingConstants.CENTER);
		lblPumps.setBounds(10, 11, 130, 14);
		this.add(lblPumps);
		
		lblBurner  																			= new JLabel("Burner");
		lblBurner.setBounds(150, 31, 65, 23);
		this.add(lblBurner);
		
		JLabel 													lblOther  					= new JLabel("Other");
		lblOther.setHorizontalAlignment(SwingConstants.CENTER);
		lblOther.setBounds(150, 11, 65, 14);
		this.add(lblOther);
		
		lblMixerUp  																		= new JLabel("Mixer Up");
		lblMixerUp.setBounds(150, 65, 65, 23);
		this.add(lblMixerUp);
		
		lblMixerDown  																		= new JLabel("Mixer Down");
		lblMixerDown.setBounds(150, 99, 65, 23);
		this.add(lblMixerDown);
		
		lampHotWater 																		= new JLabel("Hot Water");
		lampHotWater.setIcon(lampOn);
		lampHotWater.setBounds(41, 133, 35, 35);
		add(lampHotWater);

		lampFloor 																			= new JLabel("Floor");
		lampFloor.setIcon(lampOff);
		lampFloor.setBounds(96, 133, 35, 35);
		add(lampFloor);
		
	}
//	@Override
//    protected void paintComponent(Graphics g) 
//	{
//        super.paintComponent(g);
//        g.drawImage(lampOn, 0, 0, this); // see javadoc for more info on the parameters            
//        g.drawImage(lampOff, 0, 0, this); // see javadoc for more info on the parameters            
//	}
}
