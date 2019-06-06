package gameInterface;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class LoginWindow extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel username;
    private JLabel password;
    private JTextField jcomp3;
    private JPasswordField jcomp4;
    private JButton register;
    private JButton login;
    private JButton exit;
    private JTextArea info;
    private JLabel title;

    public LoginWindow() {
        //construct components
        username = new JLabel ("Nume Utilizator");
        password = new JLabel ("Parola");
        jcomp3 = new JTextField (1);
        jcomp4 = new JPasswordField (1);
        register = new JButton ("Înregistrează-te");
        setLogin(new JButton ("Intră"));
        exit = new JButton ("Ieși");
        info = new JTextArea (1, 1);
        title = new JLabel ("Quizz Game");

        //adjust size and set layout
        setPreferredSize (new Dimension (504, 451));
        setLayout (null);

        //add components
        add (username);
        add (password);
        add (jcomp3);
        add (jcomp4);
        add (register);
        add (getLogin());
        add (exit);
        add (info);
        add (title);

        //set component bounds (only needed by Absolute Positioning)
        username.setBounds (35, 60, 429, 30);
        password.setBounds (35, 150, 429, 30);
        jcomp3.setBounds (35, 90, 429, 60);
        jcomp4.setBounds (35, 180, 429, 60);
        register.setBounds (35, 275, 129, 60);
        getLogin().setBounds (185, 275, 129, 60);
        exit.setBounds (335, 275, 129, 60);
        info.setBounds (35, 355, 429, 60);
        title.setBounds (200, 10, 100, 25);
    }

	public JButton getLogin() {
		return login;
	}

	public void setLogin(JButton login) {
		this.login = login;
	}

	public JLabel getUsername() {
		return username;
	}

	public void setUsername(JLabel username) {
		this.username = username;
	}

	public JLabel getPassword() {
		return password;
	}

	public void setPassword(JLabel password) {
		this.password = password;
	}

	public JTextField getJcomp3() {
		return jcomp3;
	}

	public void setJcomp3(JTextField jcomp3) {
		this.jcomp3 = jcomp3;
	}

	public JPasswordField getJcomp4() {
		return jcomp4;
	}

	public void setJcomp4(JPasswordField jcomp4) {
		this.jcomp4 = jcomp4;
	}

	public JButton getRegister() {
		return register;
	}

	public void setRegister(JButton register) {
		this.register = register;
	}

	public JButton getExit() {
		return exit;
	}

	public void setExit(JButton exit) {
		this.exit = exit;
	}

	public JTextArea getInfo() {
		return info;
	}

	public void setInfo(JTextArea info) {
		this.info = info;
	}

	public JLabel getTitle() {
		return title;
	}

	public void setTitle(JLabel title) {
		this.title = title;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
