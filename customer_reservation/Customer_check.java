package HDMS2;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Customer_check implements ActionListener{
	static Connection dbTest;
	PreparedStatement stmt;
	ResultSet rs;
	Connect connect;
	
	public JFrame main_frame = new JFrame();
	public JFrame frame = new JFrame();
	private JPanel titlePanel = new JPanel();
	private JPanel contentPanel = new JPanel();
	private JLabel nameLabel = new JLabel();
	private JLabel descriptionLabel = new JLabel("고객님의 예약 내역입니다.");
	private JButton backButton = new JButton("");
	
	String name;	
	String checkInDate;
	String checkOutDate;
	String id;
	String grade;
	String bedType;
	String view;
	String[][] value;
	String s1;
	JLabel backImage = new JLabel();
	
	public Customer_check() throws SQLException {
		backImage.setIcon(new ImageIcon("/Users/koserim/Desktop/check_back.png"));
		backImage.setBounds(0,0,800,600);
		connect = new Connect();
		
		contentPanel.setLayout(null);
		contentPanel.setBounds(0, 0, 800, 600);
	
		nameLabel.setBounds(100, 100, 150, 40);
		nameLabel.setFont(new Font("Courier", Font.PLAIN, 15));
		
		name = this.getName();
		nameLabel.setText(name);
		descriptionLabel.setBounds(200, 100, 400, 40);
		descriptionLabel.setFont(new Font("Courier", Font.PLAIN, 15));
		
		makeTable();
		
		backButton.setBounds(600, 390, 110, 50);
		backButton.setBorderPainted(false);
		backButton.setFocusPainted(false);
		backButton.setContentAreaFilled(false);
		backButton.addActionListener(this);
		
		contentPanel.add(nameLabel);
		contentPanel.add(descriptionLabel);
		contentPanel.add(backButton);
		contentPanel.add(backImage);

		frame.setTitle("My Page - Customer - Check Reservation");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(titlePanel);
		frame.add(contentPanel);
		frame.setVisible(false);
		
	}
	
	public static void main(String[] args) throws SQLException {
		new Customer_check();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == backButton) {
			try {
				frame.setVisible(false);
				Customer_main customer_main = new Customer_main();
		    	main_frame = customer_main.frame;
				main_frame.setVisible(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	

	public String getName() throws SQLException {
		try {
			String sqlStr = "SELECT first_name, last_name FROM customer WHERE login_id = 'kimjieun' ";
			stmt = dbTest.prepareStatement(sqlStr);
			rs = stmt.executeQuery(sqlStr);
			while(rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return name;
	}
	
	public void getList() throws SQLException {
		try {
			String sqlStr = "SELECT id, grade, bed_type, view, check_in, check_out  "
					+ "FROM room, room_reservation "
					+ "WHERE room.room_number = room_reservation.room_id "
					+ "and room_reservation.customer_id = 9";

			stmt = dbTest.prepareStatement(sqlStr);
			rs = stmt.executeQuery(sqlStr);
			
			int i = 1;
			while(rs.next()) {
				i++;
			}
			value = new String[i][6];
			rs.close();
			
			stmt = dbTest.prepareStatement(sqlStr);
			rs = stmt.executeQuery(sqlStr);
			i = 0;
			while(rs.next()) {
				id = rs.getString("id");
				grade = rs.getString("grade");
				bedType = rs.getString("bed_type");
				view = rs.getString("view");
				checkInDate = rs.getString("check_in");
				checkOutDate = rs.getString("check_out");
				value[i][0] = id;
				value[i][1] = grade;
				value[i][2] = bedType;
				value[i][3] = view;
				value[i][4] = checkInDate;
				value[i][5] = checkOutDate;
				i++;			
			}
			rs.close();
			stmt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void makeTable() throws SQLException {
		getList();
		String[] menu = {"예약번호", "객실 등급", "침대 타입", "view", "Check In", "Check Out"};
		DefaultTableModel model = new DefaultTableModel(value, menu) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable roomListTable = new JTable(model);
		JScrollPane scroll = new JScrollPane(roomListTable);
		scroll.setBounds(100, 150, 600, 200);

		contentPanel.add(scroll);
		
	}

}
