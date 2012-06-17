import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EasyRename extends JFrame implements ActionListener,
		ListSelectionListener {

	JButton bttnBrowse, bttnRename, bttnRenameAll;
	JLabel lblHeading, lblAddr, lblFile, lblOriginal, lblReplace;
	JTextField txtAddr, txtFile, txtOriginal, txtReplace;
	JList lstFile;
	JScrollPane scrollPane;
	JFileChooser chooser;
	File file;
	String dirname, strOriginal, strNew;
	final int top = 70, height = 25, margin = 25;

	
	public void initElements() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		lblHeading = new JLabel("Easy Rename");
		lblAddr = new JLabel("Select Directory");
		txtAddr = new JTextField();
		bttnBrowse = new JButton("Browse");
		bttnBrowse.addActionListener(this);
		lstFile = new JList();
		lstFile.addListSelectionListener(this);
		scrollPane = new JScrollPane(lstFile);
		lblFile = new JLabel("Current File");
		txtFile = new JTextField();
		bttnRename = new JButton("Rename");
		bttnRename.addActionListener(this);
		lblOriginal = new JLabel("String to be Removed/Replaced");
		txtOriginal = new JTextField();
		lblReplace = new JLabel("String to be Replaced with");
		txtReplace = new JTextField();
		bttnRenameAll = new JButton("Rename All");
		bttnRenameAll.addActionListener(this);
	}
	
	public void setElementsBounds() {
		lblHeading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 29));
		lblHeading.setBounds(300, margin, 200, height + 10);
		
		lblAddr.setBounds(margin, top, 100, height);
		txtAddr.setBounds(margin + 100, top, 520, height);
		bttnBrowse.setBounds(margin + 640, top, 100, height);
		
		scrollPane.setBounds(margin, top + 50, 740, height * 10);
		
		lblFile.setBounds(margin, top + 320, 100, height);
		txtFile.setBounds(margin + 80, top + 320, 540, height);
		bttnRename.setBounds(margin + 640, top + 320, 100, height);
		
		lblOriginal.setBounds(margin, top + 380, 180, height);
		txtOriginal.setBounds(margin + 170, top + 380, 450, height);
		bttnRenameAll.setBounds(margin + 640, top + 380, 100, height * 3);
		lblReplace.setBounds(margin, top + 430, 180, height);
		txtReplace.setBounds(margin + 170, top + 430, 450, height);
		
	}
	
	public void addElements() {
		add(lblHeading);
		add(lblAddr);
		add(txtAddr);
		add(bttnBrowse);
		add(scrollPane);
		add(lblFile);
		add(txtFile);
		add(bttnRename);
		add(new JSeparator(SwingConstants.HORIZONTAL)).setBounds(20, 435, 750, 2);
		add(lblOriginal);
		add(txtOriginal);
		add(lblReplace);
		add(txtReplace);
		add(bttnRenameAll);
	}
	
	public EasyRename(String title) {

		super(title);
		initElements();
		setElementsBounds();
		addElements();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setSize(800, 600);
		setVisible(true);
	}
	
	public void showError(String errorMessage) {
		JOptionPane.showMessageDialog(this, errorMessage);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == bttnBrowse) {
			dirname = txtAddr.getText();
			if (dirname.equalsIgnoreCase("")) {
				chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					txtAddr.setText(chooser.getSelectedFile().toString());
					dirname = txtAddr.getText();
				} else {
					showError("No Directory Selected");
				}
			}
			file = new File(dirname);
			if (file.isDirectory()) {
				lstFile.setListData(file.list());
			}

		} else if (ae.getSource() == bttnRename) {
			if (!txtFile.getText().equalsIgnoreCase("")) {
				strOriginal = lstFile.getSelectedValue().toString();
				strNew = txtFile.getText();
				File f = new File(dirname + "/" + strOriginal);
				f.renameTo(new File(dirname + "/" + strOriginal.replace(strOriginal, strNew)));
				lstFile.setListData(file.list());
				txtFile.setText("");
			} else {
				showError("No File Selected");
			}
		} else if (ae.getSource() == bttnRenameAll) {
			strOriginal = txtOriginal.getText();
			strNew = txtReplace.getText();

			if (strOriginal.equals("")) {
				showError("Specify string to be replaced");
				return;
			}
			
			if (file == null) {
				showError("No file selected");
				return;
			}
			
			if (file.isDirectory()) {
				String s[] = file.list();

				for (int i = 0; i < s.length; i++) {
					File f = new File(dirname + "/" + s[i]);
					f.renameTo(new File(dirname + "/"
							+ s[i].replace(strOriginal, strNew)));
					lstFile.setListData(file.list());
				}
				txtOriginal.setText("");
				txtReplace.setText("");
				showError("Operation Completed");
			} else {
				showError("Selection is not a Directory");
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		try {
			txtFile.setText(lstFile.getSelectedValue().toString());
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) {
		EasyRename er = new EasyRename("Easy Rename");
	}
}