package eReg_Forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.FlowLayout;

//--------------------------------------------------------------|---------------------------|--------------------------------------------------------------------
public class Form_Control extends JFrame
{

	private JPanel 												contentPane;
//	private final JSplitPane 									splitPane 					= new JSplitPane();
//	private JSplitPane splitPane_1;


	public Form_Control()
	{
		setTitle("HVAC eRegulation");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 856, 545);

		contentPane 																		= new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setSize( new Dimension(200, 200));
//		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
//				FormSpecs.UNRELATED_GAP_COLSPEC,
//				ColumnSpec.decode("588px"),},
//			new RowSpec[] {
//				FormSpecs.UNRELATED_GAP_ROWSPEC,
//				RowSpec.decode("379px"),}));
		setContentPane(contentPane);
		
		JSplitPane 												splitTopBottom				= new JSplitPane();
		splitTopBottom.setMinimumSize(new Dimension(200, 200));
		splitTopBottom.setSize(new Dimension(200, 200));
		splitTopBottom.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitTopBottom, "2, 2, fill, fill");
		
		JSplitPane												splitLeftRight 				= new JSplitPane();
		splitLeftRight.setMinimumSize(new Dimension(203, 150));
		splitLeftRight.setResizeWeight(0.5);
		splitLeftRight.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitLeftRight.setContinuousLayout(false);
		splitTopBottom.setTopComponent(splitLeftRight);
		
		JPanel 													panelLeft					= new JPanel();
		JPanel 													panelRight					= new JPanel();
		JPanel 													panelBottom					= new JPanel();
		panelBottom.setMinimumSize(new Dimension(10, 100));
		splitLeftRight.setLeftComponent(panelLeft);
		panelLeft.setLayout(null);
		
		JButton 												btnReBoot 					= new JButton("ReBoot");
		btnReBoot.setBounds(10, 5, 90, 20);
		panelLeft.add(btnReBoot);
		JButton 												btnDebugWait 				= new JButton("Debug Wait");
		btnDebugWait.setBounds(10, 35, 90, 20);
		panelLeft.add(btnDebugWait);
		JButton 												btnBash 					= new JButton("Bash");
		btnBash.setBounds(10, 65, 90, 20);
		panelLeft.add(btnBash);
		JButton 												btnDebugNoWait 				= new JButton("Debug No Wait");
		btnDebugNoWait.setBounds(140, 5, 110, 20);
		panelLeft.add(btnDebugNoWait);
		JButton 												btnQuit 					= new JButton("Quit");
		btnQuit.setBounds(140, 35, 110, 20);
		panelLeft.add(btnQuit);
		
		splitLeftRight.setRightComponent(panelRight);
		panelRight.setLayout(null);
		
		JLabel lblUpdated = new JLabel("Updated on");
		lblUpdated.setBounds(20, 8, 56, 14);
		panelRight.add(lblUpdated);
		
		JFormattedTextField txtDate = new JFormattedTextField();
		txtDate.setBounds(86, 5, 69, 20);
		txtDate.setEditable(false);
		panelRight.add(txtDate);
		
		JFormattedTextField txtTime = new JFormattedTextField();
		txtTime.setBounds(165, 5, 80, 20);
		txtTime.setEditable(false);
		panelRight.add(txtTime);
		
		JLabel lblBoiler = new JLabel("Boiler");
		lblBoiler.setBounds(10, 36, 111, 14);
		panelRight.add(lblBoiler);
		
		JLabel lblHotWater = new JLabel("Hot Water");
		lblHotWater.setBounds(10, 61, 111, 14);
		panelRight.add(lblHotWater);
		
		JLabel lblFloorOut = new JLabel("Floor Out");
		lblFloorOut.setBounds(10, 86, 111, 14);
		panelRight.add(lblFloorOut);
		
		JLabel lblFloorIn = new JLabel("Floor In");
		lblFloorIn.setBounds(10, 111, 111, 14);
		panelRight.add(lblFloorIn);
		
		JFormattedTextField txtBoiler = new JFormattedTextField();
		txtBoiler.setEditable(false);
		txtBoiler.setBounds(193, 33, 73, 20);
		panelRight.add(txtBoiler);
		
		JFormattedTextField txtHotWater = new JFormattedTextField();
		txtHotWater.setEditable(false);
		txtHotWater.setBounds(193, 58, 73, 20);
		panelRight.add(txtHotWater);
		
		JFormattedTextField txtFloorOut = new JFormattedTextField();
		txtFloorOut.setEditable(false);
		txtFloorOut.setBounds(193, 83, 73, 20);
		panelRight.add(txtFloorOut);
		
		JFormattedTextField txtFloorIn = new JFormattedTextField();
		txtFloorIn.setEditable(false);
		txtFloorIn.setBounds(193, 108, 73, 20);
		panelRight.add(txtFloorIn);
		splitTopBottom.setBottomComponent(panelBottom);
	}
}
