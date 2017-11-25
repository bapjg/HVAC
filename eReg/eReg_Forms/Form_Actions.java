package eReg_Forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import HVAC_Common.Ctrl_Actions_Stop;
import eRegulation.Global;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Form_Actions extends JFrame
{

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Form_Actions frame = new Form_Actions();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Form_Actions()
	{
		setTitle("Actions");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JButton btnReBoot = new JButton("ReBoot");
		btnReBoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.stopNow																	= true;
				Global.exitStatus																= Ctrl_Actions_Stop.ACTION_Reboot;
			}
		});
		contentPane.add(btnReBoot, BorderLayout.NORTH);
		
		JButton btnDebugWait = new JButton("Debug Wait");
		btnDebugWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Global.stopNow																	= true;
				Global.exitStatus																= Ctrl_Actions_Stop.ACTION_Debug_Wait;
			}
		});
		contentPane.add(btnDebugWait, BorderLayout.WEST);
		
		JButton btnBash = new JButton("Bash");
		btnBash.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.stopNow																	= true;
				Global.exitStatus																= Ctrl_Actions_Stop.ACTION_Stop;
			}
		});
		contentPane.add(btnBash, BorderLayout.CENTER);
		
		JButton btnDebugNoWait = new JButton("Debug No Wait");
		btnDebugNoWait.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.stopNow																	= true;
				Global.exitStatus																= Ctrl_Actions_Stop.ACTION_Debug_NoWait;
			}
		});
		contentPane.add(btnDebugNoWait, BorderLayout.EAST);
		
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Global.stopNow																	= true;
				Global.exitStatus																= Ctrl_Actions_Stop.ACTION_Stop;
			}
		});
		contentPane.add(btnQuit, BorderLayout.SOUTH);
	}

}
