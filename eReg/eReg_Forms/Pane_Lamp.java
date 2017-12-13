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
public class Pane_Lamp 											extends 					JPanel
{
	private JLabel 												label;
    private JLabel												lamp;

    private ImageIcon 											lampImageOn;
    private ImageIcon 											lampImageOff;
    
 
	public Pane_Lamp(String subject)
	{
		this.setLayout(null);
		this.setBounds(0, 0, 120, 35);

		lampImageOn																			= new ImageIcon(Pane_Lamp.class.getResource("/eReg_Forms/lamp_on.jpg"));		
		lampImageOff																		= new ImageIcon(Pane_Lamp.class.getResource("/eReg_Forms/lamp_off.jpg"));		
		
		label 																				= new JLabel(subject);
		label.setBounds(0, 0, 65, 35);
		this.add(label);
		
		lamp 																				= new JLabel();
		lamp.setIcon(lampImageOn);
		lamp.setBounds(70, 0, 35, 35);
		add(lamp);
	}
	public void switchLampOn()
	{
		lamp.setIcon(lampImageOn);
	}
	public void switchLampOff()
	{
		lamp.setIcon(lampImageOff);
	}
}
