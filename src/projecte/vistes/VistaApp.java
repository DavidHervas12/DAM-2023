package projecte.vistes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JTable;

/**
 * Vista principal de l'aplicació.
 */
public class VistaApp extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea txtConsultaSQL;
	private JLabel lblConsultaSQL;
	private JButton btnExecutar;
	private JButton btnEixir;
	private JLabel lblTipusUsuari;
	private JLabel lblRellotge;
	private JPanel pnlTable_1;
	private JButton btnTancarSessio;
	private JPanel pnlTable;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getContentPane() {
		return contentPane;
	}

	public JTextArea getTxtConsultaSQL() {
		return txtConsultaSQL;
	}

	public JLabel getLblConsultaSQL() {
		return lblConsultaSQL;
	}

	public JButton getBtnExecutar() {
		return btnExecutar;
	}

	public JButton getBtnEixir() {
		return btnEixir;
	}

	public JLabel getLblTipusUsuari() {
		return lblTipusUsuari;
	}

	public JLabel getLblRellotge() {
		return lblRellotge;
	}

	public JButton getBtnTancarSessio() {
		return btnTancarSessio;
	}

	public VistaApp() {
		setTitle("Gestió de Biblioteca");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1005, 613);
		setLocationRelativeTo(null);
		try {
			BufferedImage fondo = ImageIO.read(new File("assets/fondo.png"));
			contentPane = new JPanel() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
				}
			};

		} catch (Exception e) {
			e.printStackTrace();
		}

		contentPane.setBackground(SystemColor.activeCaption);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnEixir = new JButton("EIXIR");
		btnEixir.setFont(new Font("Liberation Sans", Font.BOLD, 14));
		btnEixir.setBounds(798, 507, 167, 38);
		contentPane.add(btnEixir);

		btnTancarSessio = new JButton("TANCAR SESSIÓ");
		btnTancarSessio.setFont(new Font("Liberation Sans", Font.BOLD, 14));
		btnTancarSessio.setBounds(798, 458, 167, 38);
		contentPane.add(btnTancarSessio);

		pnlTable = new JPanel();
		pnlTable.setBackground(new Color(139, 69, 19));
		pnlTable.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlTable.setBounds(83, 195, 700, 350);
		contentPane.add(pnlTable);

		pnlTable_1 = new JPanel();
		pnlTable_1.setBackground(new Color(139, 69, 19));
		pnlTable_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		pnlTable_1.setBounds(83, 33, 700, 151);
		contentPane.add(pnlTable_1);
		pnlTable_1.setLayout(null);

		lblConsultaSQL = new JLabel("Consulta SQL:");
		lblConsultaSQL.setForeground(new Color(255, 239, 213));
		lblConsultaSQL.setBounds(21, 11, 147, 22);
		pnlTable_1.add(lblConsultaSQL);
		lblConsultaSQL.setFont(new Font("Liberation Sans", Font.BOLD, 18));

		txtConsultaSQL = new JTextArea();
		txtConsultaSQL.setBounds(20, 41, 511, 88);
		pnlTable_1.add(txtConsultaSQL);
		txtConsultaSQL.setForeground(new Color(139, 69, 19));
		txtConsultaSQL.setBackground(new Color(255, 239, 213));
		txtConsultaSQL.setFont(new Font("Fira Code Retina", Font.PLAIN, 14));
		txtConsultaSQL.setLineWrap(true);
		txtConsultaSQL.setWrapStyleWord(true);

		btnExecutar = new JButton("EXECUTAR");
		btnExecutar.setBounds(542, 91, 133, 38);
		pnlTable_1.add(btnExecutar);
		btnExecutar.setFont(new Font("Liberation Sans", Font.BOLD, 12));

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(SystemColor.scrollbar);
		panel.setBounds(542, 11, 133, 69);
		pnlTable_1.add(panel);
		panel.setLayout(null);
		lblRellotge = new JLabel(sdf.format(new Date()));
		lblRellotge.setBounds(10, 11, 99, 25);
		panel.add(lblRellotge);
		lblRellotge.setBackground(SystemColor.activeCaptionBorder);
		lblRellotge.setFont(new Font("Calibri", Font.BOLD, 20));

		lblTipusUsuari = new JLabel("cuenta");
		lblTipusUsuari.setBounds(71, 42, 62, 27);
		panel.add(lblTipusUsuari);
		lblTipusUsuari.setForeground(new Color(0, 0, 0));
		lblTipusUsuari.setBackground(new Color(139, 69, 19));
		lblTipusUsuari.setFont(new Font("Calibri", Font.BOLD, 16));

		setVisible(true);
	}
}
