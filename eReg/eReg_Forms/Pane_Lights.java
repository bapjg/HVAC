package eReg_Forms;

import java.awt.*;
import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Pane_Lights extends JPanel
{
	private JButton 											btnHotWater;
	private JButton 											btnFloor;
	private JButton 											btnRadiator;
	private JButton 											btnBurner;
	private JButton 											btnMixerUp;
	private JButton 											btnMixerDown;


	public Pane_Lights()
	{
		
		this.setLayout(null);
		this.setBounds(713, 11, 261, 166);
		
		btnHotWater 																		= new JButton("Hot Water");
		btnHotWater.setBounds(10, 31, 100, 23);
		this.add(btnHotWater);
		
		btnFloor 																			= new JButton("Floor");
		btnFloor.setBounds(10, 65, 100, 23);
		this.add(btnFloor);
		
		btnRadiator  																		= new JButton("Radiator");
		btnRadiator.setBounds(10, 99, 100, 23);
		this.add(btnRadiator);
		
		JLabel 													lblPumps  					= new JLabel("Pumps");
		lblPumps.setHorizontalAlignment(SwingConstants.CENTER);
		lblPumps.setBounds(10, 11, 100, 14);
		this.add(lblPumps);
		
		btnBurner  																			= new JButton("Burner");
		btnBurner.setBounds(138, 31, 100, 23);
		this.add(btnBurner);
		
		JLabel 													lblOther  					= new JLabel("Other");
		lblOther.setHorizontalAlignment(SwingConstants.CENTER);
		lblOther.setBounds(138, 11, 100, 14);
		this.add(lblOther);
		
		btnMixerUp  																		= new JButton("Mixer Up");
		btnMixerUp.setBounds(138, 65, 100, 23);
		this.add(btnMixerUp);
		
		btnMixerDown  																		= new JButton("Mixer Down");
		btnMixerDown.setBounds(138, 99, 100, 23);
		this.add(btnMixerDown);
	}
}
