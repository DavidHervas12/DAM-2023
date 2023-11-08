package app;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

/**
 * Vista de l'aplicació.
 */
public class Order extends JFrame {

	private int limitPeces = 8;

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtQuantitat;
	private JComboBox cmbTipus;
	private JLabel lblTipus;
	private JCheckBox chckbxGuardarTxt;
	private JButton btnFabricar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Order frame = new Order();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the frame.
	 */
	public Order() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 296, 360);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		String[] elements = { "I", "O", "T", "J", "L", "S", "Z" };
		cmbTipus = new JComboBox(elements);
		cmbTipus.setFont(new Font("Liberation Sans", Font.PLAIN, 12));
		cmbTipus.setBounds(54, 113, 163, 29);
		contentPane.add(cmbTipus);

		txtQuantitat = new JTextField();
		txtQuantitat.setFont(new Font("Liberation Sans", Font.PLAIN, 12));
		txtQuantitat.setBounds(54, 62, 163, 29);
		contentPane.add(txtQuantitat);
		txtQuantitat.setColumns(10);

		lblTipus = new JLabel("Tipus:");
		lblTipus.setFont(new Font("Liberation Sans", Font.BOLD, 15));
		lblTipus.setBounds(54, 90, 100, 24);
		contentPane.add(lblTipus);

		JLabel lblQuantitat = new JLabel("Quantitat:");
		lblQuantitat.setFont(new Font("Liberation Sans", Font.BOLD, 15));
		lblQuantitat.setBounds(54, 42, 100, 18);
		contentPane.add(lblQuantitat);

		chckbxGuardarTxt = new JCheckBox("Guardar en txt");
		chckbxGuardarTxt.setFont(new Font("Liberation Sans", Font.BOLD, 12));
		chckbxGuardarTxt.setBounds(76, 163, 124, 23);
		contentPane.add(chckbxGuardarTxt);

		btnFabricar = new JButton("FABRICAR");
		btnFabricar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String clase = "app.Manufacture";
					String javaHome = System.getProperty("java.home");
					String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
					String classPath = System.getProperty("java.class.path");
					String className = clase;

					List<String> command = new ArrayList<>();
					command.add(javaBin);
					command.add("-cp");
					command.add(classPath);
					command.add(className);
					command.add(txtQuantitat.getText());
					command.add(cmbTipus.getSelectedItem().toString());

					System.out.println("Comando que se pasa a ProcessBuilder: " + command);
					System.out.println("Comando a ejecutar en cmd.exe: " + command.toString().replace(",", ""));

					ProcessBuilder builder = new ProcessBuilder(command);
					if (chckbxGuardarTxt.isSelected()) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
						String timestamp = sdf.format(new Date());
						String nomFitxer = "LOG_" + timestamp;
						builder.redirectOutput(new File(nomFitxer + ".txt")).start();
					} else {
						Process proceso = builder.inheritIO().start();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		btnFabricar.setFont(new Font("Liberation Sans", Font.BOLD, 15));
		btnFabricar.setBounds(63, 210, 137, 29);
		contentPane.add(btnFabricar);
	}
}
