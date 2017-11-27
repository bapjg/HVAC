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

//------------------------------------------------------------65|-------------------------93|--------------------------------------------------------------------
public class Form_Control extends JFrame
{

	private JPanel 										contentPane;
	private JTable 										tableLogIt;
	private Form_Control_LogIt_Items 					logItItems;
	private JFormattedTextField 						txtDate;
	private JFormattedTextField 						txtTime;
	private JFormattedTextField 						txtBoiler;
	private JFormattedTextField 						txtHotWater;
	private JFormattedTextField 						txtFloorOut;
	private JFormattedTextField 						txtFloorIn;
	private int 										screenHeight;
	private int 										screenWidth;

	public Form_Control()
	{
		GraphicsDevice 											gd 							= GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0]; // Only works if NOT headLess
		GraphicsConfiguration 									gc 							= gd.getConfigurations()[0];
        Rectangle 												bounds 						= gc.getBounds();
        screenHeight																		= bounds.height;
        screenWidth																			= bounds.width;
		
		logItItems	 																		= new Form_Control_LogIt_Items(100);
		
		this.setMinimumSize(new Dimension(800, 800));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 136, 252);
		this.setVisible(true);
		this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
		
		contentPane 																		= new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel paneButtons = new JPanel();
		paneButtons.setBounds(10, 11, 284, 155);
		paneButtons.setLayout(null);
		contentPane.add(paneButtons);
		
		JButton btnReBoot = new JButton("ReBoot");
		btnReBoot.setBounds(34, 21, 90, 20);
		btnReBoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Reboot);
			}
		});
		paneButtons.add(btnReBoot);
		
		JButton btnBash = new JButton("Bash");
		btnBash.setBounds(34, 81, 90, 20);
		btnBash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Stop);
			}
		});
		paneButtons.add(btnBash);
		
		JButton btnRestart= new JButton("ReStart");
		btnRestart.setBounds(164, 51, 110, 20);
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Restart);
			}
		});
		paneButtons.add(btnRestart);
		
		JButton btnDebugWait = new JButton("Debug Wait");
		btnDebugWait.setBounds(34, 51, 90, 20);
		btnDebugWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Debug_Wait);
			}
		});
		paneButtons.add(btnDebugWait);
		
		JButton btnDebugNoWait = new JButton("Debug No Wait");
		btnDebugNoWait.setBounds(164, 21, 110, 20);
		btnDebugNoWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Debug_NoWait);
			}
		});
		paneButtons.add(btnDebugNoWait);
		
		JPanel 													paneTemperatures 			= new JPanel();
		paneTemperatures.setBounds(292, 11, 383, 155);
		contentPane.add(paneTemperatures);
		paneTemperatures.setLayout(null);
		
		JLabel 													lblUpdatedOn 				= new JLabel("Updated on");
		lblUpdatedOn.setBounds(10, 14, 111, 14);
		paneTemperatures.add(lblUpdatedOn);
		
		JLabel 													lblBoiler 					= new JLabel("Boiler");
		lblBoiler.setBounds(10, 60, 111, 14);
		paneTemperatures.add(lblBoiler);
		
		JLabel 													lblHotWater 				= new JLabel("Hot Water");
		lblHotWater.setBounds(10, 85, 111, 14);
		paneTemperatures.add(lblHotWater);
		
		JLabel 													lblFloorOut 				= new JLabel("Floor Out");
		lblFloorOut.setBounds(10, 110, 111, 14);
		paneTemperatures.add(lblFloorOut);
		
		JLabel 													lblFloorIn 					= new JLabel("Floor In");
		lblFloorIn.setBounds(10, 135, 111, 14);
		paneTemperatures.add(lblFloorIn);

		
		
		
		txtDate 																			= new JFormattedTextField();
		txtDate.setEditable(false);
		txtDate.setBounds(131, 11, 73, 20);
		paneTemperatures.add(txtDate);
		
		txtTime  																			= new JFormattedTextField();
		txtTime.setEditable(false);
		txtTime.setBounds(248, 11, 73, 20);
		paneTemperatures.add(txtTime);
		
		txtBoiler  																			= new JFormattedTextField();
		txtBoiler.setEditable(false);
		txtBoiler.setBounds(193, 57, 73, 20);
		paneTemperatures.add(txtBoiler);
		
		txtHotWater  																		= new JFormattedTextField();
		txtHotWater.setEditable(false);
		txtHotWater.setBounds(193, 82, 73, 20);
		paneTemperatures.add(txtHotWater);
		
		txtFloorOut  																		= new JFormattedTextField();
		txtFloorOut.setEditable(false);
		txtFloorOut.setBounds(193, 107, 73, 20);
		paneTemperatures.add(txtFloorOut);
		
		txtFloorIn  																		= new JFormattedTextField();
		txtFloorIn.setEditable(false);
		txtFloorIn.setBounds(193, 132, 73, 20);
		paneTemperatures.add(txtFloorIn);
		
		JScrollPane 											paneLogIt 					= new JScrollPane();
		paneLogIt.setPreferredSize(new Dimension(10, 10));
		paneLogIt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		paneLogIt.setBounds(10, 172, screenWidth - 10, 300);
		contentPane.add(paneLogIt);

		tableLogIt 																			= new JTable();
		paneLogIt.setViewportView(tableLogIt);
		tableLogIt.setBounds(0, 0, screenWidth - 10, 300);		
//		for (int i = 0; i < 110; i++)
//		{
//			logItItems.add(new Form_Control_LogIt_Item("Warning", "Global", "Test " + i));
//		}
		tableLogIt.setModel(logItItems);
//		Form_Control_LogIt_Item 								lastItem 					= new Form_Control_LogIt_Item("Error", "Circuit_Abstract/Constructor", "HotWater" + " invalid pump " + "3");
//		logItItems.add(lastItem);
		

		tableLogIt.getColumnModel().getColumn(0).setMinWidth(150);
		tableLogIt.getColumnModel().getColumn(0).setMaxWidth(150);
		tableLogIt.getColumnModel().getColumn(1).setMinWidth(90);
		tableLogIt.getColumnModel().getColumn(1).setMaxWidth(90);
		tableLogIt.getColumnModel().getColumn(2).setMinWidth(200);
		tableLogIt.getColumnModel().getColumn(2).setMaxWidth(200);
		tableLogIt.getColumnModel().getColumn(3).setMinWidth(500);
		tableLogIt.getColumnModel().getColumn(3).setMinWidth(500);
		tableLogIt.setVisible(true);
		
        this.pack();
        this.setVisible(true);
	}
	public void logMessage(String dateTimeStamp, String severity, String sender, String message)
	{
		logItItems.add(dateTimeStamp, severity, sender, message);
		logItItems.fireTableDataChanged();
	}
	public void showTemperatures()
	{
		Date 													date    					= new Date();
		SimpleDateFormat    									dateFormat					= new SimpleDateFormat("dd.MM");
		SimpleDateFormat    									timeFormat					= new SimpleDateFormat("HH:mm:ss");
		
		txtDate			.setValue(dateFormat);
		txtTime			.setValue(timeFormat);

		txtBoiler		.setValue(Global.thermoBoiler		.toDisplay());
		txtHotWater		.setValue(Global.thermoHotWater		.toDisplay());
		txtFloorOut		.setValue(Global.thermoBoilerOut	.toDisplay());
		txtFloorIn		.setValue(Global.thermoFloorIn		.toDisplay());

		// ToDo : Add other temperatures
		
//		Global.display.writeAtPosition(2, 5,  Global.thermoBoilerOut.toDisplay() + "  ");
//		Global.display.writeAtPosition(3, 16, Global.thermoLivingRoom.toDisplay());
	}
	public void forceExit(int exitStatus) 
	{
		Global.stopNow																	= true;
		Global.exitStatus																= exitStatus;
	}
}
