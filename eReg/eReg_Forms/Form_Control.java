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
public class Form_Control extends JFrame
{

	private JPanel 										contentPane;
	private JTable 										tableLogIt;
	private Form_Control_LogIt_Items 					logItItems;
	private JTextField 									txtDate;
	private JTextField 									txtTime;
	private JTextField 									txtBoiler;
	private JTextField 									txtHotWater;
	private JTextField 									txtFloorOut;
	private JTextField 									txtFloorIn;
	private JTextField 									txtOutside;
	private JTextField 									txtRadiatorOut;
	private JTextField 									txtRadiatorIn;
	private JTextField 									txtBoilerOut;
	private JTextField 									txtBoilerIn;
	private JTextField 									txtLivingRoom;
	
	private JButton 									btnFreezeUnfreeze;
	
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
		
		this.setMinimumSize(new Dimension(1000, 800));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 877, 800);
		
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
		paneTemperatures.setBounds(292, 11, 346, 166);
		contentPane.add(paneTemperatures);
		paneTemperatures.setLayout(null);
		
		JLabel 													lblUpdatedOn 				= new JLabel("Updated on");
		lblUpdatedOn.setBounds(10, 14, 111, 14);
		paneTemperatures.add(lblUpdatedOn);
		
		JLabel 													lblBoiler 					= new JLabel("Boiler");
		lblBoiler.setBounds(10, 56, 50, 14);
		paneTemperatures.add(lblBoiler);
		
		JLabel 													lblHotWater 				= new JLabel("Hot Water");
		lblHotWater.setBounds(10, 84, 50, 14);
		paneTemperatures.add(lblHotWater);
		
		JLabel 													lblOutside 					= new JLabel("Outside");
		lblOutside.setBounds(10, 112, 50, 14);
		paneTemperatures.add(lblOutside);
		
		JLabel 													lblFloorOut 				= new JLabel("Floor");
		lblFloorOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblFloorOut.setBounds(208, 112, 70, 14);
		paneTemperatures.add(lblFloorOut);
		
		JLabel 													lblRadiatorOut 				= new JLabel("Radiator");
		lblRadiatorOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblRadiatorOut.setBounds(208, 56, 70, 14);
		paneTemperatures.add(lblRadiatorOut);
		
		JLabel 													lblRadiatorIn  				= new JLabel("In");
		lblRadiatorIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblRadiatorIn.setBounds(278, 39, 50, 14);
		paneTemperatures.add(lblRadiatorIn);
		
		JLabel 													lblBoilerOut  				= new JLabel("Boiler");
		lblBoilerOut.setHorizontalAlignment(SwingConstants.CENTER);
		lblBoilerOut.setBounds(208, 84, 70, 14);
		paneTemperatures.add(lblBoilerOut);
		
		JLabel 													lblBoilerIn  				= new JLabel("Out");
		lblBoilerIn.setHorizontalAlignment(SwingConstants.CENTER);
		lblBoilerIn.setBounds(158, 39, 50, 14);
		paneTemperatures.add(lblBoilerIn);
		
		txtDate 																			= new JTextField();
		txtDate.setBackground(new Color(240, 240, 240));
		txtDate.setHorizontalAlignment(SwingConstants.CENTER);
		txtDate.setForeground(Color.RED);
		txtDate.setBounds(86, 11, 50, 20);
		txtDate.setEditable(false);
		paneTemperatures.add(txtDate);
		
		txtTime 																			= new JTextField();
		txtTime.setBackground(new Color(240, 240, 240));
		txtTime.setHorizontalAlignment(SwingConstants.CENTER);
		txtTime.setForeground(Color.RED);
		txtTime.setBounds(146, 11, 56, 20);
		txtTime.setEditable(false);
		paneTemperatures.add(txtTime);

		txtBoiler  																			= new JTextField();
		txtBoiler.setHorizontalAlignment(SwingConstants.CENTER);
		txtBoiler.setEditable(false);
		txtBoiler.setBounds(86, 53, 50, 20);
		paneTemperatures.add(txtBoiler);
		
		txtHotWater  																		= new JTextField();
		txtHotWater.setHorizontalAlignment(SwingConstants.CENTER);
		txtHotWater.setEditable(false);
		txtHotWater.setBounds(86, 81, 50, 20);
		paneTemperatures.add(txtHotWater);
		
		txtOutside  				 				 				 						= new JTextField();
		txtOutside.setHorizontalAlignment(SwingConstants.CENTER);
		txtOutside.setEditable(false);
		txtOutside.setBounds(86, 109, 50, 20);
		paneTemperatures.add(txtOutside);
		
		txtFloorOut  																		= new JTextField();
		txtFloorOut.setHorizontalAlignment(SwingConstants.CENTER);
		txtFloorOut.setEditable(false);
		txtFloorOut.setBounds(158, 109, 50, 20);
		paneTemperatures.add(txtFloorOut);
		
		txtFloorIn  																		= new JTextField();
		txtFloorIn.setHorizontalAlignment(SwingConstants.CENTER);
		txtFloorIn.setEditable(false);
		txtFloorIn.setBounds(278, 109, 50, 20);
		paneTemperatures.add(txtFloorIn);
		
		txtRadiatorOut  				 				 				 					= new JTextField();
		txtRadiatorOut.setHorizontalAlignment(SwingConstants.CENTER);
		txtRadiatorOut.setEditable(false);
		txtRadiatorOut.setBounds(158, 53, 50, 20);
		paneTemperatures.add(txtRadiatorOut);
		
		txtRadiatorIn  				 				 				 				 		= new JTextField();
		txtRadiatorIn.setHorizontalAlignment(SwingConstants.CENTER);
		txtRadiatorIn.setEditable(false);
		txtRadiatorIn.setBounds(278, 53, 50, 20);
		paneTemperatures.add(txtRadiatorIn);
		
		txtBoilerOut  				 				 				 				 		= new JTextField();
		txtBoilerOut.setHorizontalAlignment(SwingConstants.CENTER);
		txtBoilerOut.setEditable(false);
		txtBoilerOut.setBounds(158, 81, 50, 20);
		paneTemperatures.add(txtBoilerOut);
		
		txtBoilerIn  				 				 				 				 		= new JTextField();
		txtBoilerIn.setHorizontalAlignment(SwingConstants.CENTER);
		txtBoilerIn.setEditable(false);
		txtBoilerIn.setBounds(278, 81, 50, 20);
		paneTemperatures.add(txtBoilerIn);
		
		JLabel 													lblLivingRoom 				= new JLabel("Living Room");
		lblLivingRoom.setBounds(10, 140, 70, 14);
		paneTemperatures.add(lblLivingRoom);
		
		txtLivingRoom 																		= new JTextField();
		txtLivingRoom.setHorizontalAlignment(SwingConstants.CENTER);
		txtLivingRoom.setEditable(false);
		txtLivingRoom.setBounds(86, 137, 50, 20);
		paneTemperatures.add(txtLivingRoom);
		
		JScrollPane 											paneLogIt 					= new JScrollPane();
		paneLogIt.setPreferredSize(new Dimension(10, 10));
		paneLogIt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		paneLogIt.setBounds(10, 212, 1910, 260);
		contentPane.add(paneLogIt);

		tableLogIt 																			= new JTable();
		paneLogIt.setViewportView(tableLogIt);
		tableLogIt.setBounds(0, 0, screenWidth - 10, 300);		

		tableLogIt.setModel(logItItems);
		
		btnFreezeUnfreeze 																	= new JButton("Freeze");
//		btnFreezeUnfreeze.addActionListener(new ActionListener() 
//		{
//			public void actionPerformed(ActionEvent arg0) 
//			{
//				if (btnFreezeUnfreeze.getText() == "Freeze") 	btnFreezeUnfreeze.setText("Unfreeze");
//				else 											btnFreezeUnfreeze.setText("Freeze");
//			}
//		});
		btnFreezeUnfreeze.setBounds(93, 178, 89, 23);
		contentPane.add(btnFreezeUnfreeze);

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
		this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
	}
	public void logMessage(String dateTimeStamp, String severity, String sender, String message)
	{
		logItItems.add(dateTimeStamp, severity, sender, message);
		logItItems.fireTableDataChanged();
	}
	public void showTemperatures()
	{
		Date 													now	    					= new Date();
		SimpleDateFormat    									dateFormat					= new SimpleDateFormat("dd.MM");
		SimpleDateFormat    									timeFormat					= new SimpleDateFormat("HH:mm:ss");
		
		txtDate			.setText(dateFormat					.format(now));
		txtTime			.setText(timeFormat					.format(now));
		
		txtBoiler		.setText(Global.thermoBoiler		.toDisplay());
		txtHotWater		.setText(Global.thermoHotWater		.toDisplay());
		txtOutside		.setText(Global.thermoOutside		.toDisplay());
		txtFloorOut		.setText(Global.thermoFloorOut		.toDisplay());
		txtFloorIn		.setText(Global.thermoFloorIn		.toDisplay());
		txtRadiatorOut	.setText(Global.thermoRadiatorOut	.toDisplay());
		txtRadiatorIn	.setText(Global.thermoRadiatorIn	.toDisplay());
		txtBoilerOut	.setText(Global.thermoBoilerOut		.toDisplay());
		txtBoilerIn		.setText(Global.thermoBoilerIn		.toDisplay());
		txtLivingRoom	.setText(Global.thermoLivingRoom	.toDisplay());
	}
	public void forceExit(int exitStatus) 
	{
		Global.stopNow																	= true;
		Global.exitStatus																= exitStatus;
	}
}
