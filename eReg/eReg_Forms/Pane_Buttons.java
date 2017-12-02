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
public class Pane_Buttons extends JPanel
{
	public Pane_Buttons()
	{
		this.setBounds(10, 11, 330, 155);
		this.setLayout(null);
		
		// bash command down/reboot
		JButton 												btnShutdown 				= new JButton("Shutdown eReg");
		btnShutdown.setBounds(10, 21, 150, 20);
		btnShutdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_ShutDown);
			}
		});		
		add(btnShutdown);

		JButton 												btnReBoot 					= new JButton("ReBoot eReg");
		btnReBoot.setBounds(170, 22, 150, 20);
		btnReBoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Reboot);
			}
		});
		this.add(btnReBoot);
		
		// Stop app (and go to bash/gui) restart app
		JButton  												btnStop  					= new JButton("Stop App");
		btnStop.setBounds(10, 52, 150, 20);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Stop);
			}
		});
		this.add(btnStop);
		
		JButton  												btnRestart 					= new JButton("ReStart App");
		btnRestart.setBounds(170, 52, 150, 20);
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Restart);
			}
		});
		this.add(btnRestart);
		
		// Dubug alternatives
		JButton  												btnDebugWait  					= new JButton("Debug Wait");
		btnDebugWait.setBounds(10, 90, 150, 20);
		btnDebugWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Debug_Wait);
			}
		});
		this.add(btnDebugWait);
		
		JButton  												btnDebugNoWait  				= new JButton("Debug No Wait");
		btnDebugNoWait.setBounds(170, 90, 150, 20);
		btnDebugNoWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forceExit(Ctrl_Actions_Stop.ACTION_Debug_NoWait);
			}
		});		
		this.add(btnDebugNoWait);
	}
//	public void forceExit(int exitStatus, int dummy) 
//	{
//		Global.stopNow																	= true;
//		Global.exitStatus																= exitStatus;
//	}
	public void forceExit(int exitStatus) 
	{
		Global.stopNow																	= true;
		Global.exitStatus																= exitStatus;
	}
}
