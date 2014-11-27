import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.DefaultComboBoxModel;

public class App {

	private JFrame frame;
	/**
	 * Поле сообщений, где будут выводиться сообщения об ошибках
	 */
	private JTextField messageField;
	/**
	 * Лист строк, куда будет записываться результат
	 */
	private ArrayList<String> uncovResList = new ArrayList<String>();

	/**
	 * Преобразование списка строк в массив строк
	 */
	public String[] listToArray(ArrayList<String> list) {
		String[] array = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			array[i] = list.get(i).toString();
		}
		return array;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					App window = new App();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public App() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("FSActivity");
		frame.setBounds(100, 100, 571, 414);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		final JComboBox timeBox = new JComboBox();
		timeBox.setModel(new DefaultComboBoxModel(new String[] { "5",
				"10", "15", "20", "30", "60" }));
		timeBox.setBounds(202, 12, 83, 24);
		frame.getContentPane().add(timeBox);

		final JComboBox measureBox = new JComboBox();
		measureBox.setModel(new DefaultComboBoxModel(new String[] {
				"сек", "мин", "час" }));
		measureBox.setBounds(297, 12, 83, 24);
		frame.getContentPane().add(measureBox);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 52, 543, 250);
		frame.getContentPane().add(scrollPane);

		final JList list = new JList();
		scrollPane.setViewportView(list);

		messageField = new JTextField();
		messageField.setHorizontalAlignment(SwingConstants.CENTER);
		messageField.setEditable(false);
		messageField.setColumns(10);
		messageField.setBackground(UIManager.getColor("Button.background"));
		messageField.setBounds(10, 314, 545, 49);
		frame.getContentPane().add(messageField);

		JButton btnInfo = new JButton("");
		btnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String info = "	 ID    field description \r\n n  = name \r\n a  = access \r\n L  = login name \r\n p  = PID \r\n g  = GYD \r\n t  = type \r\n \r\n Authors: \r\n Олег Галушкин \r\n Антон Медведев";

				JOptionPane.showMessageDialog(null, info, "HELP",
						JOptionPane.DEFAULT_OPTION);
			}
		});
		btnInfo.setIcon(new ImageIcon(
				App.class
						.getResource("/com/sun/java/swing/plaf/motif/icons/Question.gif")));
		btnInfo.setBounds(527, 12, 28, 25);
		frame.getContentPane().add(btnInfo);
		/**
		 * 
		 */
		final ArrayList<ScheduledExecutorService> services = new ArrayList<ScheduledExecutorService>();

		JButton btnStart = new JButton("Начать");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					for (int i = 0; i < services.size(); i++) {
						if (!services.get(i).isShutdown()) {
							services.get(i).shutdown();
						}
						if (services.size() == 10)
							services.clear();
					}
					int time = Integer.parseInt((String) timeBox
							.getSelectedItem());
					System.out.println(time);
					TimeUnit measure = null;
					String curMeasure = (String) measureBox.getSelectedItem();
					if (curMeasure.matches("сек"))
						measure = TimeUnit.SECONDS;
					if (curMeasure.matches("мин"))
						measure = TimeUnit.MINUTES;
					if (curMeasure.matches("час"))
						measure = TimeUnit.HOURS;
					System.out.println(measure.toString());
					services.add(Executors.newSingleThreadScheduledExecutor());
					System.out.println("Services number: " + services.size());
					services.get(services.size() - 1).scheduleWithFixedDelay(
							new Runnable() {
								PsWorker worker = new PsWorker();

								@SuppressWarnings("serial")
								@Override
								public void run() {
									uncovResList = worker.executeCommand(
											"lsof -F naLpgt", uncovResList);
									final ArrayList<String> convResList = StringWorker
											.convertOutput(uncovResList);
									list.setModel(new AbstractListModel() {

										String[] values = listToArray(convResList);

										public int getSize() {
											return values.length;
										}

										public String getElementAt(int index) {
											return values[index];
										}
									});
									convResList.clear();
									uncovResList.clear();
								}
							}, 0, time, measure);
				} catch (Exception e) {
					e.printStackTrace();
					messageField.setText("Ошибка");
				}

			}
		});
		btnStart.setBounds(398, 12, 117, 25);
		frame.getContentPane().add(btnStart);

		JLabel lblNewLabel = new JLabel("Интервал обновления:");
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 13));
		lblNewLabel.setBounds(12, 12, 178, 24);
		frame.getContentPane().add(lblNewLabel);

	}
}
