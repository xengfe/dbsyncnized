package com.yeecare.main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.yeecare.bean.DataBaseConfigInfo;
import com.yeecare.bz.DBManager;
import com.yeecare.bz.PropertiesXmlFileUtil;
import com.yeecare.util.DateStrUtl;
import com.yeecare.util.ScreenSize;
import com.yeecare.util.TextAreaOutputStream2;



public class SyncDB {
	private static final String SUBORDINATE_DATABASE_XML = "subordinate_database.xml";
	private static final String MASTER_DATABASE_XML = "master_database.xml";
	private static final int SCREEN_HEIGHT = ScreenSize.getScreenHeight();
	private static final int SCREEN_WIDTH = ScreenSize.getScreenWidth();
	private static final int FRAME_HEIGHT = SCREEN_HEIGHT / 2;
	private static final int FRAME_WIDTH = SCREEN_WIDTH / 2;
	private static final int marge = 50;

	private static Timer timer ;
	private static JMenuItem databaseMenuItem, startMenuItem, pauseMenuItem;
	private static JFrame frame;
	private static int windowWidth, windowHeight, locationX, locationY;
	private static boolean isbegin = false;
	private static boolean isEditable = false;
	private static boolean isRunable = false;
	private static JTextField urlTextField;
	private static JTextField accountTextField;
	private static JPasswordField pwdTextField;
	private static JTextField subUrlTextField;
	private static JTextField subAccountTextField;
	private static JPasswordField subPwdTextField;
	private static JDialog dialog;
	
	private static ToastDialog toastDialog;
	private static JTextField timeField;
	
	private static int period ;
	
	

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(new Runnable() {
		
		@Override
		public void run() {
			frame = new JFrame("数据库同步管理终端");
			frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
			windowWidth = frame.getWidth();
			windowHeight = frame.getHeight();
			locationX = (SCREEN_WIDTH - windowWidth) / 2;
			locationY = (SCREEN_HEIGHT - windowHeight) / 2;
			frame.setLocation(locationX, locationY);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setBackground(Color.white);
			JTextArea ta = new JTextArea();
			ta.setEditable(false);
			TextAreaOutputStream2 taos = new TextAreaOutputStream2(ta, DateStrUtl.getFormatDate(new Date()) + "  log");
			PrintStream ps = new PrintStream(taos);
			System.setOut(ps);
			System.setErr(ps);
			frame.add(new JScrollPane(ta));
			
			frame.setVisible(true);
			System.out.println("application start run");
			initMenuBar();
			initMenuListener();
			System.out.println("application component init successfully");
		}
	});
		
		
	}

	/**
	 * @param frame
	 * @param windowWidth
	 * @param windowHeight
	 */
	public static void initMenuBar() {
		JMenuBar menubar = new JMenuBar();
		JMenu setting = new JMenu("设置");
		databaseMenuItem = new JMenuItem("数据库信息");
		
		databaseMenuItem.setSelected(!isRunable);
		databaseMenuItem.setFocusable(!isRunable);
		databaseMenuItem.setEnabled(!isRunable);

		setting.add(databaseMenuItem);
		menubar.add(setting);

		JMenu oprate = new JMenu("操作");
		startMenuItem = new JMenuItem("开始同步");
		pauseMenuItem = new JMenuItem("暂停同步");
		
		startMenuItem.setSelected(!isRunable);
		startMenuItem.setFocusable(!isRunable);
		startMenuItem.setEnabled(!isRunable);
		
		pauseMenuItem.setSelected(isRunable);
		pauseMenuItem.setFocusable(isRunable);
		pauseMenuItem.setEnabled(isRunable);

		oprate.add(startMenuItem);
		oprate.add(pauseMenuItem);
		menubar.add(oprate);

		frame.setJMenuBar(menubar);
	}

	private static void initMenuListener() {
		databaseMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				initDialog();
			}
		});

		startMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startSyncDataBase();
				isRunable = true;
				
				startMenuItem.setSelected(isRunable);
				startMenuItem.setFocusable(isRunable);
				startMenuItem.setEnabled(!isRunable);
				
				databaseMenuItem.setSelected(isRunable);
				databaseMenuItem.setFocusable(isRunable);
				databaseMenuItem.setEnabled(!isRunable);
				
				pauseMenuItem.setSelected(!isRunable);
				pauseMenuItem.setFocusable(!isRunable);
				pauseMenuItem.setEnabled(isRunable);
			}
		});

		pauseMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopSyncDataBase();
				
				isRunable = false;
				
				startMenuItem.setSelected(!isRunable);
				startMenuItem.setFocusable(!isRunable);
				startMenuItem.setEnabled(!isRunable);
				
				databaseMenuItem.setSelected(!isRunable);
				databaseMenuItem.setFocusable(!isRunable);
				databaseMenuItem.setEnabled(!isRunable);
				
				pauseMenuItem.setSelected(isRunable);
				pauseMenuItem.setFocusable(isRunable);
				pauseMenuItem.setEnabled(isRunable);

			}
		});
	}
	
	public static void initDialog() {
		dialog = new JDialog(frame, "同步配置信息");
		dialog.setLocation(locationX + marge / 2, locationY + marge
				/ 2);
		dialog.setSize(windowWidth - marge, windowHeight - marge);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		Container c = dialog.getContentPane();
		c.setLayout(new BorderLayout());
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new FlowLayout());
		titlePanel.add(new JLabel("主从数据库配置"));
		c.add(titlePanel,"North");
		
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(null);
		
		JLabel urlLabel = new JLabel("主数据库 URL：");
		urlLabel.setBounds(40, 40, 100, 20);
		urlTextField = new JTextField();
		urlTextField.setBounds(140,40,120,20);
		fieldPanel.add(urlLabel);
		fieldPanel.add(urlTextField);
		
		JLabel accountLabel = new JLabel("主数据库账号：");
		accountLabel.setBounds(40, 80, 100, 20);
		accountTextField = new JTextField();
		accountTextField.setBounds(140,80,120,20);
		fieldPanel.add(accountLabel);
		fieldPanel.add(accountTextField);
		
		JLabel pwdLabel = new JLabel("主数据库密码：");
		pwdLabel.setBounds(40, 120, 100, 20);
		pwdTextField = new JPasswordField();
		pwdTextField.setBounds(140,120,120,20);
		fieldPanel.add(pwdLabel);
		fieldPanel.add(pwdTextField);
		
		
		JLabel subUrlLabel = new JLabel("从数据库 URL：");
		subUrlLabel.setBounds(350, 40, 100, 20);
		subUrlTextField = new JTextField();
		subUrlTextField.setBounds(450,40,120,20);
		fieldPanel.add(subUrlLabel);
		fieldPanel.add(subUrlTextField);
		
		JLabel subAccountLabel = new JLabel("从数据库账号：");
		subAccountLabel.setBounds(350, 80, 100, 20);
		subAccountTextField = new JTextField();
		subAccountTextField.setBounds(450,80,120,20);
		fieldPanel.add(subAccountLabel);
		fieldPanel.add(subAccountTextField);
		
		JLabel subPwdLabel = new JLabel("从数据库密码：");
		subPwdLabel.setBounds(350, 120, 100, 20);
		subPwdTextField = new JPasswordField();
		subPwdTextField.setBounds(450,120,120,20);
		fieldPanel.add(subPwdLabel);
		fieldPanel.add(subPwdTextField);
		
		JLabel timeJLabel = new JLabel("同步间隔时长：");
		timeJLabel.setBounds(40, 160, 100, 20);
		timeField = new JTextField();
		timeField.setBounds(140,160,120,20);
		fieldPanel.add(timeJLabel);
		fieldPanel.add(timeField);
		
		c.add(fieldPanel,"Center");
		
		
		JButton ok = new JButton("确定");
		ok.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				saveDataBaseConfig();
			}
		});
		JButton cancel = new JButton("取消");
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		c.add(buttonPanel,"South");
		

		restoreDataBaseConfig();
		
		dialog.show();

	
	}

	protected static void saveDataBaseConfig() {
		DataBaseConfigInfo config = new DataBaseConfigInfo();

		config.url = urlTextField.getText().toString();
		config.username = accountTextField.getText().toString();
		config.password = pwdTextField.getText().toString();
		config.time = timeField.getText().toString();

		DataBaseConfigInfo subConfig = new DataBaseConfigInfo();
		subConfig.url = subUrlTextField.getText().toString();
		subConfig.username = subAccountTextField.getText().toString();
		subConfig.password = subPwdTextField.getText().toString();
		subConfig.time = timeField.getText().toString();

		if (isValid(config.url) && isValid(config.username)
				&& isValid(config.password) && isValid(config.time) && isNumeric(config.time)
				&& isValid(subConfig.url) && isValid(subConfig.username) && isValid(subConfig.password)) {
			PropertiesXmlFileUtil.writeXML("subordinate_database.xml",subConfig);
			PropertiesXmlFileUtil.writeXML("master_database.xml", config);
			period  = Integer.valueOf(config.time);
			dialog.dispose();
		} else {
			if (!isValid(config.url)) {
				toastDialog = new ToastDialog(frame, "请输入主数据库url");
				toastDialog.show();
				
			} else if (!isValid(config.username)) {
				toastDialog = new ToastDialog(frame, "请输入主数据库用户名");
				toastDialog.show();
				
			} else if (!isValid(config.password)) {
				toastDialog = new ToastDialog(frame, "请输入主数据库密码");
				toastDialog.show();
				
			} else if (!isValid(config.time)||!isNumeric(config.time)) {
				toastDialog = new ToastDialog(frame, "请输入有整数");
				toastDialog.show();
				
			}else if (!isValid(subConfig.url)) {
				toastDialog = new ToastDialog(frame, "请输入从数据库url");
				toastDialog.show();
				
			} else if (!isValid(subConfig.username)) {
				toastDialog = new ToastDialog(frame, "请输入从数据库用户名");
				toastDialog.show();
				
			} else if (!isValid(subConfig.password)) {
				toastDialog = new ToastDialog(frame, "请输入从数据库密码");
				toastDialog.show();
				
			}

		}

	}
	
	protected static void restoreDataBaseConfig() {
		 DataBaseConfigInfo master_config = PropertiesXmlFileUtil.readXML(MASTER_DATABASE_XML);
		 
		 urlTextField.setText(master_config.url);
		 accountTextField.setText(master_config.username);
		 pwdTextField.setText(master_config.password);
		 timeField.setText(master_config.time);
		 if (isValid(master_config.time)) {
			 period = Integer.valueOf(master_config.time);
		}
		 
		 DataBaseConfigInfo sub_config = PropertiesXmlFileUtil.readXML(SUBORDINATE_DATABASE_XML);
		 
		 subUrlTextField.setText(sub_config.url);
		 subAccountTextField.setText(sub_config.username);
		 subPwdTextField.setText(sub_config.password);
	}

	protected synchronized static void stopSyncDataBase() {
		if (isbegin ) {
			timer.cancel();
			isbegin = false;
		}else {
			toastDialog = new ToastDialog(frame, "please start application before stop !");
			toastDialog.show();
		}
		
	}

	protected synchronized static void startSyncDataBase() {
		
		if (!isbegin) {
			int delay = 0;// 毫秒
			int time = period * 1000;// 1s

			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					long beginTime = System.currentTimeMillis();
					DBManager.save2DistDB(false);
					// DBManager.deleteTempSourceData();
					long endTime = System.currentTimeMillis();
					System.out.println("running time:"+(endTime - beginTime) + " ms");
				}

			};
			timer = new Timer();
			timer.schedule(task, delay, time);
			isbegin = true;
			
		}else {
			System.out.println("application is running...");
		}

		
	}
	
	private static boolean isValid(String str){
		boolean flag = false;
		if (!str.equals("") && str != null) {
			flag = true;
		}
		return flag;
	}
	
	
	/**
	 * 正则表达式：判断是否数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 }
	
	static class ToastDialog extends JDialog {
		private String msg;

		private static final long serialVersionUID = 1L;

		public ToastDialog(JFrame frame) {
			super(frame);
			initToastDialogComponents(frame);
		}
		
		public ToastDialog (JFrame frame,String showMsg){
			super(frame);
			this.msg = showMsg;
			initToastDialogComponents(frame);
			
		}

		/**
		 * @param frame
		 */
		private void initToastDialogComponents(JFrame frame) {
			this.setSize(300, 150);
			this.setLocationRelativeTo(frame);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			Container c = this.getContentPane();
			c.setLayout(new BorderLayout());
			
			JPanel titlePanel = new JPanel();
			titlePanel.setLayout(new FlowLayout());
			titlePanel.add(new JLabel("操作提醒"));
			c.add(titlePanel,"North");
			
			JPanel fieldPanel = new JPanel();
			fieldPanel.setLayout(null);
			
			JLabel hintLabel = new JLabel(msg);
			hintLabel.setBounds(40, 5, 200,30);
			hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
			hintLabel.setVerticalAlignment(SwingConstants.CENTER);
			
			fieldPanel.add(hintLabel);
			c.add(fieldPanel,"Center");
			
			JButton ok = new JButton("确定");
			ok.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(ok);
			c.add(buttonPanel,"South");
		}
		
	}

}
