import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.swing.*;

public class DogeUI extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JFrame gameFrame;
	JLabel counterLabel, perSecLabel,countDownLabel;
	JButton autoClickUpgrade;
	JButton doubleClick; //Activated for 10s -> Double doges per click
	int dogeCounter;
	int timerSpeed;
	int autoClickPrice = 150;
	double perSecond = 0;
	boolean timerOn;
	boolean autoClickActivated = false;
	boolean doubleDogeActivated = false;
	Font font1, font2;
	dogeHandler dHandler = new dogeHandler();
	Timer timer;
	final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
	
	final Runnable runnable = new Runnable() {
		int countDown = 10;
		
		public void run()
		{
			countDownLabel.setText(countDown + " Seconds left!");
			doubleDogeActivated = true;
			countDown--;
			
			//When timer ends
			if(countDown < 0)
			{
				countDownLabel.setText("");
				doubleDogeActivated = false;
				schedule.shutdown();
			}
		}
	};


	
	public DogeUI()
	{
		super("DogeClicker");
		dogeCounter = 0; //Default value 
		//TO DO: Implement way to store player score so it does not reset
		timerOn = false;
		createFont();
		createGUI();
	}
	
	public void setTimer()
	{
		timer = new Timer(timerSpeed, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				dogeCounter++;
				counterLabel.setText(dogeCounter + " Doge");
			}
			
		});
	}

	public void timerUpdate() {
		if(!timerOn){
			timerOn = true;
		}
		else if(timerOn)
		{
			timer.stop();
		}
		
		if(dogeCounter>=100) {
		dogeCounter -= 100;
		counterLabel.setText(dogeCounter + " Doge");
		}
		else if(dogeCounter < 100)
		{
			
		}
		double speed = (1/perSecond)*1000;
		timerSpeed = (int)Math.round(speed); 
		String s = String.format("%.1f",perSecond);
		perSecLabel.setText("Doge per second: " + s);
		
		setTimer();
		timer.start();
		
	}

	
	public void createFont()
	{
		font1 = new Font("Arial Black",Font.BOLD,32);
		font2 = new Font("Arial Black",Font.BOLD,15);
	}
	
	/*
	 * Doge handler class
	 */
	public class dogeHandler implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {

			String action = e.getActionCommand();
			
			switch(action)
			{
			case "doge":
				if(!doubleDogeActivated) {
				dogeCounter++;
				counterLabel.setText(dogeCounter + " Doge");
				}
				else if(doubleDogeActivated) {
					dogeCounter+=2;
					counterLabel.setText(dogeCounter +" Doge");
				}
				break;
			case "autoClick":
				if(dogeCounter >= autoClickPrice) {
					dogeCounter -= autoClickPrice;
					counterLabel.setText(dogeCounter + " Doge");
					autoClickPrice += 100; //Increase price after each buy
					autoClickUpgrade.setText("$"+autoClickPrice + " Auto Click: 1 click per 10s");
					perSecond += 0.1;
					timerUpdate();
					break;
				}
				else
				{
					break;
				}
			case "dblClick":
				schedule.scheduleAtFixedRate(runnable, 0,1,SECONDS);
				
				}
			}
			
		}
	
	public void createGUI()
	{
		try
		{
			/*
			 * Initialize DogeClicker game frame
			 * Set background color : #C7A366
			 */
			gameFrame = new JFrame();
			gameFrame.setSize(400,800);
			gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			gameFrame.getContentPane().setBackground(Color.decode("#C7A366")); 
			gameFrame.setLayout(null);
			
			
			/*
			 * Set Doge clicker
			 */
			JPanel dogeClicker = new JPanel();
			dogeClicker.setBounds(35, 200, 311, 311);
			dogeClicker.setBackground(Color.decode("#C7A366"));
			gameFrame.add(dogeClicker);
			
			ImageIcon dogeImg = new ImageIcon(getClass().getClassLoader().getResource("plain-doge-removebg-preview.png"));
			JButton dogeButton = new JButton();
			dogeButton.setBackground(Color.decode("#C7A366"));
			dogeButton.setBorder(null);
			dogeButton.setIcon(dogeImg);
			//Add button handler
			dogeClicker.add(dogeButton);
			dogeButton.addActionListener(dHandler);
			dogeButton.setActionCommand("doge");
			
			
			/*
			 * Create doge counter
			 */
			
			JPanel counterPanel = new JPanel();
			counterPanel.setBounds(130,100,200,100);
			counterPanel.setBackground(null);
			counterPanel.setLayout(new GridLayout(3,1)); //ROW 1: COOKIES OBTAINED ROW 2: COOKIES PER SECOND
			gameFrame.add(counterPanel);
			
			/*
			 * Create counter, per second and countdown label
			 * Initialize size and font
			 */
				
			counterLabel = new JLabel(dogeCounter + " Doge");
			counterLabel.setForeground(Color.white);
			counterLabel.setFont(font1);
			counterPanel.add(counterLabel);
			
			perSecLabel = new JLabel();
			perSecLabel.setForeground(Color.white);
			perSecLabel.setFont(font2);
			counterPanel.add(perSecLabel);
			
			countDownLabel = new JLabel();
			countDownLabel.setForeground(Color.white);
			countDownLabel.setFont(font2);
			counterPanel.add(countDownLabel);
			
			/*
			 * Create upgrade buttons
			 */
			JPanel upgradePanel = new JPanel();
			upgradePanel.setBounds(95,550,200,130);
			upgradePanel.setBackground(Color.decode("#C7A366"));
			upgradePanel.setLayout(new GridLayout(3,1));
			gameFrame.add(upgradePanel);
			
			JLabel upgradeLabel = new JLabel("UPGRADES");
			upgradePanel.add(upgradeLabel);
			
			//TO DO:
			/*
			 *  Only enable button when 100 doge unlocked
			 */
			
			autoClickUpgrade = new JButton(); //Adds one click every 10s
			upgradePanel.add(autoClickUpgrade);
			autoClickUpgrade.setEnabled(true);
			autoClickUpgrade.setBackground(Color.yellow);
			autoClickUpgrade.setText("$"+autoClickPrice + " Auto Click: 1 click per 10s");
			autoClickUpgrade.addActionListener(dHandler);
			autoClickUpgrade.setActionCommand("autoClick");

			
			doubleClick = new JButton(); //Adds 100 clicks
			doubleClick.setBackground(Color.yellow);
			doubleClick.setText("Double Clicks");
			doubleClick.addActionListener(dHandler);
			doubleClick.setActionCommand("dblClick");
			upgradePanel.add(doubleClick);
			
			
			gameFrame.setVisible(true);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Unable to create GUI");
		}
	}

}
