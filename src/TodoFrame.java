import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TodoFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JComboBox<String> comboBox = new JComboBox<String>();
	private final JLabel lblTasks = new JLabel("Tasks");
	private final JCheckBox chckbxStartstopTime = new JCheckBox("Start/Stop Time");
	private String currentTask = "";
	private LinkedList<String> taskList = new LinkedList<String>();
	public long start = 0;
	public long end = 0;
	private final JTextField textField = new JTextField();
	private final JLabel lblEnterTask = new JLabel("Enter Task:");
	private final JButton addButton = new JButton("Add Task");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TodoFrame frame = new TodoFrame();
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
	public TodoFrame() {
		textField.setColumns(10);
		lblTasks.setFont(new Font("Tahoma", Font.PLAIN, 19));
		taskList = this.taskList();
		int i = 0;
		while(i < taskList.size()){
			comboBox.addItem(taskList.get(i));
			i++;
		}
		initGUI();
		CheckBoxListener chkListener = new CheckBoxListener();
		chckbxStartstopTime.addItemListener(chkListener);
		
		AddButtonListener addListener = new AddButtonListener();
		addButton.addActionListener(addListener);
	}
	
	private void initGUI() {
		setTitle("TODO List");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 230);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(7)
							.addComponent(lblEnterTask)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, 324, GroupLayout.PREFERRED_SIZE)
								.addComponent(addButton, GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE))
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblTasks, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, 221, Short.MAX_VALUE)
							.addComponent(chckbxStartstopTime)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(comboBox, 0, 406, Short.MAX_VALUE)
							.addGap(8))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblEnterTask))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(addButton)
					.addPreferredGap(ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTasks, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(chckbxStartstopTime))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(9))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	private LinkedList<String> taskList(){
		LinkedList<String> taskList = new LinkedList<String>();
		String fileName = "src\\tasks.txt";
		try {
			Scanner sc = new Scanner(new File(fileName));
			while (sc.hasNextLine()) {
				String currLine = sc.nextLine();
				StringBuilder task = new StringBuilder();
				int i = 0;
				while(currLine.charAt(i) != '|'){
					task.append(currLine.charAt(i));
					i++;
				}
				taskList.add(task.toString());
			}
			sc.close();
		} catch (FileNotFoundException exception) {
			// file not found exception.
			exception.printStackTrace();
		}
		return taskList;
	}
	
	private void addTimeToTaskList(String time){
		String fileName = "src\\tasks.txt";
		Path path = Paths.get(fileName);
		try {
			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			String newLine = "";
			for(int i = 0; i < lines.size(); i++){
				if(lines.get(i).contains(currentTask + "|")){
					int separator = lines.get(i).indexOf('|') + 1;
					newLine = lines.get(i).substring(0, separator) + time;
					lines.set(i, newLine);
				}
			}
			Files.write(path, lines, StandardCharsets.UTF_8);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getTime(long start, long end) {
        long milliTime = end - start;
        StringBuilder out = new StringBuilder();
        out.append((int)(milliTime / 3600000));
        out.append(".");
        out.append((int)(milliTime / 60000) % 60);
        out.append(".");
        out.append((int)(milliTime / 1000) % 60);
        out.append(".");
        out.append((int)(milliTime) % 1000);
        
        addTimeToTaskList(out.toString());
        taskList.remove(currentTask);
		comboBox.removeItem(currentTask);
        return out.toString();
    }

	
	private class CheckBoxListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if(chckbxStartstopTime.isSelected()){
				currentTask = comboBox.getSelectedItem().toString();
				start = System.currentTimeMillis();
			} else {
				end = System.currentTimeMillis();
				getTime(start, end);
			}
		}
	}
	
	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!textField.getText().equals("")){
				taskList.add(textField.getText());
				comboBox.addItem(taskList.get(taskList.size()-1));
				
				String fileName = "src\\tasks.txt";
				Path path = Paths.get(fileName);
				try {
					List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
					String newLine = textField.getText() + "|";
					lines.add(newLine);
					Files.write(path, lines, StandardCharsets.UTF_8);			
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			textField.setText("");
		}
	}
}
