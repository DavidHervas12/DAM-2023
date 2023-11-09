package projecte.vistes;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;

/**
 * Vista d'inici de sessió per a entrar a l'aplicació.
 */
public class VistaLogIn extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNomUsuari;
	private JLabel lblNomUsuari;
	private JTextField txtContrasenya;
	private JLabel lblContrasenya;
	private JButton btnIniciarSesio;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTextField getTxtNomUsuari() {
		return txtNomUsuari;
	}

	public JLabel getLblNomUsuari() {
		return lblNomUsuari;
	}

	public JTextField getTxtContrasenya() {
		return txtContrasenya;
	}

	public JLabel getLblContrasenya() {
		return lblContrasenya;
	}

	public JButton getBtnIniciarSesio() {
		return btnIniciarSesio;
	}

	public VistaLogIn() {
		setTitle("Inici de Sessió");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 315, 341);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtNomUsuari = new JTextField();
		txtNomUsuari.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtNomUsuari.setBounds(39, 50, 221, 31);
		contentPane.add(txtNomUsuari);
		txtNomUsuari.setColumns(10);
		
		lblNomUsuari = new JLabel("Usuari:");
		lblNomUsuari.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNomUsuari.setBounds(42, 27, 121, 25);
		contentPane.add(lblNomUsuari);
		
		txtContrasenya = new JTextField();
		txtContrasenya.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtContrasenya.setColumns(10);
		txtContrasenya.setBounds(39, 136, 221, 31);
		contentPane.add(txtContrasenya);
		
		lblContrasenya = new JLabel("Contrasenya:");
		lblContrasenya.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblContrasenya.setBounds(42, 113, 121, 25);
		contentPane.add(lblContrasenya);
		
		btnIniciarSesio = new JButton("Iniciar Sessió");

		btnIniciarSesio.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnIniciarSesio.setBounds(53, 217, 169, 39);
		contentPane.add(btnIniciarSesio);
		setVisible(true);}

}
