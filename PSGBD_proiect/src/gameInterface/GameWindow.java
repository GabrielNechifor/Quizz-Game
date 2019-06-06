package gameInterface;

import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameWindow extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea jcomp1;
    private JLabel jcomp2;
    private JComboBox<String> jcomp3;
    private JButton rank;
    private JButton room;
    private JButton start;
    private JButton join;
    private JTextField jcomp8;
    private JLabel jcomp9;
    private JButton exit;
    private JButton submit;
    private JCheckBox a;
    private JCheckBox c;
    private JCheckBox b;
    private JCheckBox d;
    private JTextArea jcomp16;
    private DefaultListModel<String> listModel;
    private JList<String> listOfQuestions ;

    public GameWindow() {
        //construct preComponents
        String[] jcomp3Items = {"Informatica", "Capitale"};

        //construct components
        jcomp1 = new JTextArea (1, 1);
        jcomp2 = new JLabel ("Alege Domeniul");
        jcomp3 = new JComboBox<String> (jcomp3Items);
        rank = new JButton ("Rangul");
        room = new JButton ("Cameră nouă");
        start = new JButton ("Start");
        join = new JButton ("Joacă în");
        jcomp8 = new JTextField (5);
        jcomp9 = new JLabel ("Introdu numărul camerei");
        exit = new JButton ("Ieși");
        submit = new JButton ("Trimite răspunsul");
        a = new JCheckBox ("a");
        c = new JCheckBox ("c");
        b = new JCheckBox ("b");
        d = new JCheckBox ("d");
        jcomp16 = new JTextArea (1, 1);
        listModel = new DefaultListModel<>();
    	listOfQuestions=new JList<>(listModel);


        //adjust size and set layout
        setPreferredSize (new Dimension (944, 563));
        setLayout (null);

        //add components
        add (listOfQuestions);
        add (jcomp2);
        add (jcomp3);
        add (rank);
        add (room);
        add (start);
        add (join);
        add (jcomp8);
        add (jcomp9);
        add (exit);
        add (submit);
        add (a);
        add (c);
        add (b);
        add (d);
        add (jcomp16);

        //set component bounds (only needed by Absolute Positioning)
        listOfQuestions.setBounds (65, 135, 810, 310);
        jcomp2.setBounds (170, 50, 175, 25);
        jcomp3.setBounds (170, 75, 175, 25);
        rank.setBounds (65, 50, 100, 60);
        room.setBounds (350, 50, 113, 60);
        start.setBounds (470, 50, 130, 60);
        join.setBounds (610, 50, 100, 60);
        jcomp8.setBounds (720, 85, 150, 25);
        jcomp9.setBounds (720, 55, 150, 30);
        exit.setBounds (65, 470, 100, 60);
        submit.setBounds (735, 470, 138, 60);
        a.setBounds (640, 470, 40, 25);
        c.setBounds (640, 500, 40, 25);
        b.setBounds (690, 470, 40, 25);
        d.setBounds (690, 500, 40, 25);
        jcomp16.setBounds (170, 470, 470, 60);
    }

	public JTextArea getJcomp1() {
		return jcomp1;
	}

	public void setJcomp1(JTextArea jcomp1) {
		this.jcomp1 = jcomp1;
	}

	public JLabel getJcomp2() {
		return jcomp2;
	}

	public void setJcomp2(JLabel jcomp2) {
		this.jcomp2 = jcomp2;
	}

	public JComboBox<String> getJcomp3() {
		return jcomp3;
	}

	public void setJcomp3(JComboBox<String> jcomp3) {
		this.jcomp3 = jcomp3;
	}

	public JButton getRank() {
		return rank;
	}

	public void setRank(JButton rank) {
		this.rank = rank;
	}

	public JButton getRoom() {
		return room;
	}

	public void setRoom(JButton room) {
		this.room = room;
	}

	public JButton getStart() {
		return start;
	}

	public void setStart(JButton start) {
		this.start = start;
	}

	public JButton getJoin() {
		return join;
	}

	public void setJoin(JButton join) {
		this.join = join;
	}

	public JTextField getJcomp8() {
		return jcomp8;
	}

	public void setJcomp8(JTextField jcomp8) {
		this.jcomp8 = jcomp8;
	}

	public JLabel getJcomp9() {
		return jcomp9;
	}

	public void setJcomp9(JLabel jcomp9) {
		this.jcomp9 = jcomp9;
	}

	public JButton getExit() {
		return exit;
	}

	public void setExit(JButton exit) {
		this.exit = exit;
	}

	public JButton getSubmit() {
		return submit;
	}

	public void setSubmit(JButton submit) {
		this.submit = submit;
	}

	public JCheckBox getA() {
		return a;
	}

	public void setA(JCheckBox a) {
		this.a = a;
	}

	public JCheckBox getC() {
		return c;
	}

	public void setC(JCheckBox c) {
		this.c = c;
	}

	public JCheckBox getB() {
		return b;
	}

	public void setB(JCheckBox b) {
		this.b = b;
	}

	public JCheckBox getD() {
		return d;
	}

	public void setD(JCheckBox d) {
		this.d = d;
	}

	public JTextArea getJcomp16() {
		return jcomp16;
	}

	public void setJcomp16(JTextArea jcomp16) {
		this.jcomp16 = jcomp16;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DefaultListModel<String> getListModel() {
		return listModel;
	}

	public void setListModel(DefaultListModel<String> listModel) {
		this.listModel = listModel;
	}

	public JList<String> getListOfQuestions() {
		return listOfQuestions;
	}

	public void setListOfQuestions(JList<String> listOfQuestions) {
		this.listOfQuestions = listOfQuestions;
	}
    
    
}
