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
public class Pane_Lights extends JPanel
{
	private JButton 											btnHotWater;
	private JButton 											btnFloor;
	private JButton 											btnRadiator;
	private JButton 											btnBurner;
	private JButton 											btnMixerUp;
	private JButton 											btnMixerDown;

    private ImageIcon 											lampOn;
    private ImageIcon 											lampOff;
    
    private JLabel												lblHotWater;
	private JLabel 												lblFloor;
 
	public Pane_Lights()
	{
		this.setLayout(null);
		this.setBounds(713, 11, 291, 174);

		lampOn																				= new ImageIcon(Pane_Lights.class.getResource("/eReg_Forms/lamp_on.jpg"));		
		lampOff																				= new ImageIcon(Pane_Lights.class.getResource("/eReg_Forms/lamp_off.jpg"));		
		
		
		btnHotWater 																		= new JButton("Hot Water");
		btnHotWater.setBounds(10, 31, 130, 23);
//		btnHotWater.setIcon(new ImageIcon("D:\\HVAC_Repository\\git\\HVAC\\eReg\\eReg_Forms\\lamp_on.jpg"));
		this.add(btnHotWater);
		
		btnFloor 																			= new JButton("Floor");
		btnFloor.setBounds(10, 65, 130, 23);
		this.add(btnFloor);
		
		btnRadiator  																		= new JButton("Radiator");
		btnRadiator.setBounds(10, 99, 130, 23);
		this.add(btnRadiator);
		
		JLabel 													lblPumps  					= new JLabel("Pumps");
		lblPumps.setHorizontalAlignment(SwingConstants.CENTER);
		lblPumps.setBounds(10, 11, 130, 14);
		this.add(lblPumps);
		
		btnBurner  																			= new JButton("Burner");
		btnBurner.setBounds(150, 31, 130, 23);
		this.add(btnBurner);
		
		JLabel 													lblOther  					= new JLabel("Other");
		lblOther.setHorizontalAlignment(SwingConstants.CENTER);
		lblOther.setBounds(150, 11, 130, 14);
		this.add(lblOther);
		
		btnMixerUp  																		= new JButton("Mixer Up");
		btnMixerUp.setBounds(150, 65, 130, 23);
		this.add(btnMixerUp);
		
		btnMixerDown  																		= new JButton("Mixer Down");
		btnMixerDown.setBounds(150, 99, 130, 23);
		this.add(btnMixerDown);
		
		lblHotWater 																		= new JLabel("Hot Water");
		lblHotWater.setIcon(lampOn);
		lblHotWater.setBounds(41, 133, 35, 35);
		add(lblHotWater);

		lblFloor 																			= new JLabel("Floor");
		lblFloor.setIcon(lampOff);
		lblFloor.setBounds(96, 133, 35, 35);
		add(lblFloor);
		
	}
//	@Override
//    protected void paintComponent(Graphics g) 
//	{
//        super.paintComponent(g);
//        g.drawImage(lampOn, 0, 0, this); // see javadoc for more info on the parameters            
//        g.drawImage(lampOff, 0, 0, this); // see javadoc for more info on the parameters            
//	}
}
