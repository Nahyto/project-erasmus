package fr.ulille.iut;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class Application {

	private JFrame frame;
	private JScrollPane scrollPane;
	private JPanel panel;
	private JButton buttonOrder;
	private JButton buttonDone;
	private JButton buttonExport;
	private List<Product> table;
	private ProductResource productress;
	private String[][] tempTable;
	private String[] titreColonne = { "ID", "PRIORITY", "AMOUNT", "COLOR", "DESCRIPTION"};
	private JTable EndTable;
	private JTextField countryTxt = new JTextField();
	private DefaultTableModel model;
	private final File text = new File("Orders.txt");

	/**
	 * Launch the application.
	 */
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
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
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 924, 630);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		productress = new ProductResource();
		table = productress.getProducts();

		tempTable = new String[table.size()][];

		int i = 0;
		for (Product next : table) {
			tempTable[i++] = next.toArray();
		}
		model = new DefaultTableModel(tempTable, titreColonne);
		EndTable = new JTable();
		EndTable.setModel(model);
		EndTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent event) {
				if(!event.getValueIsAdjusting()){

					int i = EndTable.getSelectedRow();
					if(i >= 0) {
						countryTxt.setText((String) model
								.getValueAt(i, 0));
					}
				}
			}
		});

		scrollPane.setViewportView(EndTable);

		panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.EAST);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		buttonOrder = new JButton("Get the orders");
		buttonOrder.setMaximumSize(new Dimension(200, 100));
		buttonOrder.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOrder.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		panel.add(buttonOrder);

		buttonDone = new JButton("Order done");
		buttonDone.setMaximumSize(new Dimension(200, 100));
		buttonDone.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonDone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int i = EndTable.getSelectedRow();
				// if there's no selection, but there are some rows,
				// we'll just delete the first row
				if(i < 0 && model.getRowCount() > 0) {
					i = 0;
				}

				if(countryTxt.getText().equals("")){
					System.out.println("coucou");
					model.removeRow(0);
				}
				// if we have a valid row to delete, do the deletion
				if(i >= 0) {
					productress.deleteProduct(Integer.parseInt(countryTxt.getText()));
					countryTxt.setText("");
					model.removeRow(i);
					EndTable.revalidate();
				}
			}
		});
		panel.add(buttonDone);

		buttonExport = new JButton("Export to .txt");
		buttonExport.setMaximumSize(new Dimension(200, 100));
		buttonExport.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					PrintWriter out = new PrintWriter(text);
					String orders = "";
					for (Product product : productress.getProducts()) {
						orders += product.toString() + "\n";
					}
					out.println(orders);
					out.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(buttonExport);
	}

	public void refresh(){
		table = productress.getProducts();
		tempTable = new String[table.size()][];

		int i = 0;
		for (Product next : table) {
			tempTable[i++] = next.toArray();
		}
		model = new DefaultTableModel(tempTable, titreColonne);
		EndTable.setModel(model);
		scrollPane.repaint();
	}
}
