package eReg_Forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import HVAC_Common.Ctrl_Actions_Stop;
import eRegulation.Global;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.Dimension;
import javax.swing.table.DefaultTableModel;

public class Form_Control extends JFrame
{

	private JPanel contentPane;
	private JTable tableLogIt;


	public Form_Control()
	{
		setMinimumSize(new Dimension(800, 800));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 136, 252);
		contentPane = new JPanel();
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
		
		JPanel paneTemperatures = new JPanel();
		paneTemperatures.setBounds(292, 11, 383, 155);
		contentPane.add(paneTemperatures);
		paneTemperatures.setLayout(null);
		
		JLabel lblUpdatedOn = new JLabel("Updated on");
		lblUpdatedOn.setBounds(10, 14, 111, 14);
		paneTemperatures.add(lblUpdatedOn);
		
		JLabel lblBoiler = new JLabel("Boiler");
		lblBoiler.setBounds(10, 60, 111, 14);
		paneTemperatures.add(lblBoiler);
		
		JLabel lblHotWater = new JLabel("Hot Water");
		lblHotWater.setBounds(10, 85, 111, 14);
		paneTemperatures.add(lblHotWater);
		
		JLabel lblFloorOut = new JLabel("Floor Out");
		lblFloorOut.setBounds(10, 110, 111, 14);
		paneTemperatures.add(lblFloorOut);
		
		JLabel lblFloorIn = new JLabel("Floor In");
		lblFloorIn.setBounds(10, 135, 111, 14);
		paneTemperatures.add(lblFloorIn);
		
		JFormattedTextField txtDate = new JFormattedTextField();
		txtDate.setEditable(false);
		txtDate.setBounds(131, 11, 73, 20);
		paneTemperatures.add(txtDate);
		
		JFormattedTextField txtTime = new JFormattedTextField();
		txtTime.setEditable(false);
		txtTime.setBounds(248, 11, 73, 20);
		paneTemperatures.add(txtTime);
		
		JFormattedTextField txtBoiler = new JFormattedTextField();
		txtBoiler.setEditable(false);
		txtBoiler.setBounds(193, 57, 73, 20);
		paneTemperatures.add(txtBoiler);
		
		JFormattedTextField txtHotWater = new JFormattedTextField();
		txtHotWater.setEditable(false);
		txtHotWater.setBounds(193, 82, 73, 20);
		paneTemperatures.add(txtHotWater);
		
		JFormattedTextField txtFloorOut = new JFormattedTextField();
		txtFloorOut.setEditable(false);
		txtFloorOut.setBounds(193, 107, 73, 20);
		paneTemperatures.add(txtFloorOut);
		
		JFormattedTextField txtFloorIn = new JFormattedTextField();
		txtFloorIn.setEditable(false);
		txtFloorIn.setBounds(193, 132, 73, 20);
		paneTemperatures.add(txtFloorIn);
		
		JScrollPane paneLogIt = new JScrollPane();
		paneLogIt.setPreferredSize(new Dimension(10, 10));
		paneLogIt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		paneLogIt.setBounds(10, 172, 665, 300);
		contentPane.add(paneLogIt);

		tableLogIt = new JTable();
		paneLogIt.setViewportView(tableLogIt);
		
		Form_Control_LogIt_Items items = new Form_Control_LogIt_Items(100);
		
		for (int i = 0; i < 110; i++)
		{
			items.add(new Form_Control_LogIt_Item("Warning", "Global", "Test " + i));
		}
		tableLogIt.setModel(items);
		
		
		
//		tableLogIt.setModel(new DefaultTableModel(
//				new Object[][] {
//					{"01/01/2017 11:54:00", "Warning", "Global", "Test 1"},
//					{"01/01/2017 11:54:00", "Warning", "Global", "Test 2"},
//					{"01/01/2017 11:54:00", "Warning", "Global", "Test 3"},
//				},
//				new String[] {
//					"Date Time", "Type", "Sevrity", "Message"
//				}
//			));
		tableLogIt.getColumnModel().getColumn(0).setPreferredWidth(114);
		tableLogIt.getColumnModel().getColumn(1).setPreferredWidth(56);
		tableLogIt.getColumnModel().getColumn(2).setPreferredWidth(148);
		tableLogIt.getColumnModel().getColumn(3).setPreferredWidth(299);
		tableLogIt.setVisible(true);
		
        this.pack();
        this.setVisible(true);
	}
	public void forceExit(int exitStatus) 
	{
		Global.stopNow																	= true;
		Global.exitStatus																= exitStatus;
	}
}
