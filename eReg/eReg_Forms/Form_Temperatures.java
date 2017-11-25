package eReg_Forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;

public class Form_Temperatures extends JFrame
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
					Form_Temperatures frame = new Form_Temperatures();
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
	public Form_Temperatures()
	{
		setTitle("Temperatures");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 332, 202);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUpdatedOn = new JLabel("Updated on");
		lblUpdatedOn.setBounds(5, 5, 111, 14);
		contentPane.add(lblUpdatedOn);
		
		JFormattedTextField txtDate = new JFormattedTextField();
		txtDate.setEditable(false);
		txtDate.setBounds(126, 2, 73, 20);
		contentPane.add(txtDate);
		
		JFormattedTextField txtTime = new JFormattedTextField();
		txtTime.setEditable(false);
		txtTime.setBounds(243, 2, 73, 20);
		contentPane.add(txtTime);
		
		JLabel lblBoiler = new JLabel("Boiler");
		lblBoiler.setBounds(5, 51, 111, 14);
		contentPane.add(lblBoiler);
		
		JLabel lblHotWater = new JLabel("Hot Water");
		lblHotWater.setBounds(5, 76, 111, 14);
		contentPane.add(lblHotWater);
		
		JLabel lblFloorOut = new JLabel("Floor Out");
		lblFloorOut.setBounds(5, 101, 111, 14);
		contentPane.add(lblFloorOut);
		
		JLabel lblFloorIn = new JLabel("Floor In");
		lblFloorIn.setBounds(5, 126, 111, 14);
		contentPane.add(lblFloorIn);
		
		JFormattedTextField txtBoiler = new JFormattedTextField();
		txtBoiler.setEditable(false);
		txtBoiler.setBounds(188, 48, 73, 20);
		contentPane.add(txtBoiler);
		
		JFormattedTextField txtHotWater = new JFormattedTextField();
		txtHotWater.setEditable(false);
		txtHotWater.setBounds(188, 73, 73, 20);
		contentPane.add(txtHotWater);
		
		JFormattedTextField txtFloorOut = new JFormattedTextField();
		txtFloorOut.setEditable(false);
		txtFloorOut.setBounds(188, 98, 73, 20);
		contentPane.add(txtFloorOut);
		
		JFormattedTextField txtFloorIn = new JFormattedTextField();
		txtFloorIn.setEditable(false);
		txtFloorIn.setBounds(188, 123, 73, 20);
		contentPane.add(txtFloorIn);
	}
}
