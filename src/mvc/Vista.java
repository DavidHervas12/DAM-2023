package mvc;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.TextArea;
import javax.swing.SwingConstants;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JRadioButton;
import java.awt.Choice;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import java.awt.SystemColor;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtRuta;
	private TextArea txtLlistaFitxers;
	private JButton btnLlista;
	private JButton btnFusionar;
	private JComboBox cmbOrdenarPer;
	private JRadioButton rdbtnAscendent;
	private JRadioButton rdbtnDescendent;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textRutaFusionar;
	private JButton btnBusca;
	private JButton btnAgregar;
	private JTextField txtTrobarCoincidencia;
	private JButton btnFileExplorer;

	/**
	 * Constructor de la classe Vista, dins d'aquesta classe es troba l'interfície de
	 * l'aplicació.
	 */
	public Vista() {
		setTitle("FileEditor");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 958, 699);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtLlistaFitxers = new TextArea();
		txtLlistaFitxers.setEditable(false);
		txtLlistaFitxers.setForeground(new Color(0, 255, 0));
		txtLlistaFitxers.setFont(new Font("Monospaced", Font.BOLD, 14));
		txtLlistaFitxers.setBackground(new Color(0, 0, 0));
		txtLlistaFitxers.setBounds(558, 45, 350, 591);
		contentPane.add(txtLlistaFitxers);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(SystemColor.activeCaption);
		panel.setBounds(28, 45, 487, 591);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblTitulo = new JLabel("Gestió de Fitxers");
		lblTitulo.setBounds(112, 30, 267, 36);
		panel.add(lblTitulo);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Liberation Sans", Font.BOLD, 30));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(216, 230, 254));
		panel_1.setBounds(44, 86, 406, 350);
		panel.add(panel_1);
		panel_1.setLayout(null);

		rdbtnAscendent = new JRadioButton("Ascendent");
		rdbtnAscendent.setSelected(true);
		rdbtnAscendent.setBackground(new Color(216, 230, 254));
		buttonGroup.add(rdbtnAscendent);
		rdbtnAscendent.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnAscendent.setBounds(40, 85, 131, 38);
		panel_1.add(rdbtnAscendent);

		btnLlista = new JButton("Llistar");
		btnLlista.setBounds(129, 183, 151, 36);
		panel_1.add(btnLlista);
		btnLlista.setFont(new Font("Liberation Mono", Font.BOLD, 12));

		rdbtnDescendent = new JRadioButton("Descendent");
		rdbtnDescendent.setBackground(new Color(216, 230, 254));
		buttonGroup.add(rdbtnDescendent);
		rdbtnDescendent.setFont(new Font("Tahoma", Font.BOLD, 14));
		rdbtnDescendent.setBounds(40, 125, 132, 38);
		panel_1.add(rdbtnDescendent);

		cmbOrdenarPer = new JComboBox();
		cmbOrdenarPer.setBounds(206, 92, 179, 27);
		String[] opcions = { "Nom", "Tamany", "Data" };
		for (String opcio : opcions) {
			cmbOrdenarPer.addItem(opcio);
		}
		panel_1.add(cmbOrdenarPer);

		txtRuta = new JTextField();
		txtRuta.setBounds(20, 32, 318, 36);
		panel_1.add(txtRuta);
		txtRuta.setColumns(10);

		btnFileExplorer = new JButton("...");
		btnFileExplorer.setBounds(348, 32, 37, 36);
		panel_1.add(btnFileExplorer);
		btnFileExplorer.setFont(new Font("Liberation Mono", Font.BOLD, 12));

		btnBusca = new JButton("Busca");
		btnBusca.setFont(new Font("Liberation Mono", Font.BOLD, 12));
		btnBusca.setBounds(230, 267, 142, 36);
		panel_1.add(btnBusca);

		txtTrobarCoincidencia = new JTextField();
		txtTrobarCoincidencia.setColumns(10);
		txtTrobarCoincidencia.setBounds(20, 278, 200, 25);
		panel_1.add(txtTrobarCoincidencia);

		JLabel lblNewLabel = new JLabel("Introdueix la paraula que vols trobar");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(20, 254, 200, 20);
		panel_1.add(lblNewLabel);

		textRutaFusionar = new JTextField();
		textRutaFusionar.setColumns(10);
		textRutaFusionar.setBounds(44, 472, 200, 25);
		panel.add(textRutaFusionar);

		btnFusionar = new JButton("Fusionar");
		btnFusionar.setBounds(48, 507, 402, 36);
		panel.add(btnFusionar);
		btnFusionar.setFont(new Font("Liberation Mono", Font.BOLD, 12));

		JLabel lblIntrodueixElFitxer = new JLabel("Introdueix un fitxer per a fusionar");
		lblIntrodueixElFitxer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIntrodueixElFitxer.setBounds(44, 452, 200, 20);
		panel.add(lblIntrodueixElFitxer);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setFont(new Font("Liberation Mono", Font.BOLD, 12));
		btnAgregar.setBounds(254, 461, 196, 36);
		panel.add(btnAgregar);

		setVisible(true);
	}

	public TextArea getTxtLlistaFitxers() {
		return txtLlistaFitxers;
	}

	public JButton getBtnLlista() {
		return btnLlista;
	}

	public JButton getBtnAgregar() {
		return btnAgregar;
	}

	public JButton getBtnBusca() {
		return btnBusca;
	}

	public JTextField getTextRutaFusionar() {
		return textRutaFusionar;
	}

	public JButton getBtnFusionar() {
		return btnFusionar;
	}

	public JTextField getTxtRuta() {
		return txtRuta;
	}

	public JComboBox getCmbOrdenarPer() {
		return cmbOrdenarPer;
	}

	public JRadioButton getRdbtnAscendent() {
		return rdbtnAscendent;
	}

	public JRadioButton getRdbtnDescendent() {
		return rdbtnDescendent;
	}

	public JTextField getTxtTrobarCoincidencia() {
		return txtTrobarCoincidencia;
	}

	public JButton getBtnFileExplorer() {
		return btnFileExplorer;
	}
}
