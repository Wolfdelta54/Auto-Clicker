import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AutoClicker implements NativeKeyListener
{
	
	// initialize other variables
	private Point mouse;
	private int mouseX = 100;
	private int mouseY = 100;
	private int threads = 5;
	public int rate;
	
	// create objects
	private ExecutorService exe = Executors.newFixedThreadPool(threads);
	private AutoClickerStart start = new AutoClickerStart(rate);
	
	// initialize component holders
	private JFrame frame = new JFrame("Auto-Clicker");
	private JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
	
	// initialize components
	private JSlider speedSlider = new JSlider();
	private JLabel speedField = new JLabel();
	private JLabel startButton = new JLabel();
	private JLabel stopButton = new JLabel();
	private JLabel inDelay = new JLabel();
	private JLabel deDelay = new JLabel();
	private JLabel setLoc = new JLabel();
	private JLabel mouseLoc = new JLabel();
	
	// initialize component 'variables'
	private Dimension maxSize = new Dimension(1500, 1500);
	private Dimension minSize = new Dimension(500, 500);
	private Rectangle maximized = new Rectangle(maxSize);
	
	public static void main(String args[])
	{
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		GlobalScreen.addNativeKeyListener(new AutoClicker());
	}
	
	public AutoClicker()
	{
		// calls the setup methods
		setupSlider();
		setupFields();
		setupPanel();
		setupFrame();
	}
	
	private void setupPanel()
	{
		// setting visibility
		panel.setVisible(true);
		
		// adding JComponents to JPanel
		panel.add(speedField);
		panel.add(speedSlider);
		panel.add(startButton);
		panel.add(stopButton);
		panel.add(inDelay);
		panel.add(deDelay);
		panel.add(setLoc);
		panel.add(mouseLoc);
	}
	
	private void setupFrame()
	{
		// setting visibility
		frame.setVisible(true);
		
		// size and resize restraints
		frame.setResizable(false);
		frame.setSize(500, 500);
		frame.setMaximizedBounds(maximized);
		frame.setMaximumSize(maxSize);
		frame.setMinimumSize(minSize);
		
		// adding JPanel to JFrame
		frame.add(panel);
		
		// close operation
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{
				try {
					GlobalScreen.unregisterNativeHook();
				} catch (NativeHookException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void setupFields()
	{
		// setting default text of JLabels
		speedField.setText("Speed : " + speedSlider.getValue());
		rate = speedSlider.getValue();
		speedField.setFocusable(true);
		startButton.setText("Start : \'K\'");
		stopButton.setText("Stop : \'L\'");
		inDelay.setText("<html>Decrease Speed <br/> by 10ms : \'-\' <br/> by 50ms : \'{\' <br/> by 100ms : \'I\'</html>");
		deDelay.setText("<html>Increase Speed <br/> by 10ms : \'+\' <br/> by 50ms : \'}\' <br/> by 100ms : \'O\'</html>");
		setLoc.setText("Change Mous Location : \'P\'");
		mouseLoc.setText("<html>Click X : " + mouseX + "<br/>Click Y : " + mouseY + "</html>");
	}
	
	private void setupSlider()
	{
		// Setup speedSlider range
		speedSlider.getModel().setMaximum(1000);
		speedSlider.getModel().setMinimum(0);
		speedSlider.getModel().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				rate = speedSlider.getValue();
				speedField.setText("Speed: " + rate);
			}
		});
		
		// Setup speedSlider (JSlider)
		speedSlider.setMajorTickSpacing(50);
		speedSlider.setMinorTickSpacing(25);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(false);
	}
	
	public void increaseSpeed(int mod)
	{
		// increases rate by x, x is an interval determined by mod
		if(mod == 1)
		{
			// mod = 1; x = 10;
			if(rate >= 10)
				rate = rate - 10;
			else
				rate = 0;
		}
		else if(mod == 2)
		{
			// mod = 2; x = 50;
			if(rate >= 50)
				rate = rate - 50;
			else
				rate = 0;
		}
		else if(mod == 3)
		{
			// mod = 3; x = 100;
			if(rate >= 100)
				rate = rate - 100;
			else
				rate = 0;
		}
		
		updateSlider();
	}
	
	public void decreaseSpeed(int mod)
	{
		// decreases rate by x, x is an interval determined by mod
		if(mod == 1)
		{
			// mod = 1; x = 10;
			if(rate <= 990)
				rate = rate + 10;
			else
				rate = 1000;
		}
		else if(mod == 2)
		{
			// mod = 2; x = 50;
			if(rate <= 950)
				rate = rate + 50;
			else
				rate = 1000;
		}
		else if(mod == 3)
		{
			// mod = 3; x = 100;
			if(rate <= 900)
				rate = rate + 100;
			else
				rate = 1000;
		}
		
		updateSlider();
	}
	
	private void updateSlider()
	{
		// update slider location
		speedSlider.setValue(rate);
		
		// update speedField
		speedField.setText("Speed : " + speedSlider.getValue());
		rate = speedSlider.getValue();
	}
	
	private void updateLocation()
	{
		// updates the mouse location label
		mouseLoc.setText("<html>Click X : " + mouseX + "<br/>Click Y : " + mouseY + "<html/>");
	}
	
	// KeyListener methods
	
	public void nativeKeyTyped(NativeKeyEvent e)
	{
		// do nothing
	}
	
	public void nativeKeyPressed(NativeKeyEvent e)
	{
		if(e.getKeyCode() == NativeKeyEvent.VC_K)
		{
			// changes isActive in the Robot to 'true' which activates the robot's while loop
			start.isActive = true;
			exe.execute(start);
		}
		else if(e.getKeyCode() == NativeKeyEvent.VC_L)
		{
			// changes isActive in the Robot to 'false' which deactivates the robot's while loop
			start.isActive = false;
		}
		else if(e.getKeyCode() == NativeKeyEvent.VC_MINUS || e.getKeyCode() == NativeKeyEvent.VC_OPEN_BRACKET || e.getKeyCode() == NativeKeyEvent.VC_I)
		{
			// stores the Robot's isActive state
			boolean prevState = start.isActive;
			
			// then checks if the Robot's isActive state and changes it to 'false' if isActive is 'true'
			if(start.isActive == true)
				start.isActive = false;
			
			// then increases the click delay by x milliseconds based on mod
			// (e = minus, mod = 1, x = 10); (e = [, mod = 2; x = 50); (e = I, mod = 3, x = 100);
			if(e.getKeyCode() == NativeKeyEvent.VC_MINUS)
				decreaseSpeed(1);
			
			else if(e.getKeyCode() == NativeKeyEvent.VC_OPEN_BRACKET)
				decreaseSpeed(2);
			
			else if(e.getKeyCode() == NativeKeyEvent.VC_I)
				decreaseSpeed(3);
			
			start.rate = speedSlider.getValue();
			
			// checks the previous state of the Robot's isActive value and changes it to 'true' if it was 'true' before delay change
			if(prevState == true)
				start.isActive = true;
		}
		else if(e.getKeyCode() == NativeKeyEvent.VC_EQUALS || e.getKeyCode() == NativeKeyEvent.VC_CLOSE_BRACKET || e.getKeyCode() == NativeKeyEvent.VC_O)
		{
			// stores the Robot's isActive state
			boolean prevState = start.isActive;
			
			// then checks if the Robot's isActive state and changes it to 'false' if isActive is 'true'
			if(start.isActive == true)
				start.isActive = false;
			
			// then increases the click delay by x milliseconds based on mod
			// (e = equals/plus, mod = 1, x = 10); (e = ], mod = 2; x = 50); (e = O, mod = 3, x = 100);
			if(e.getKeyCode() == NativeKeyEvent.VC_EQUALS)
				increaseSpeed(1);
			
			else if(e.getKeyCode() == NativeKeyEvent.VC_CLOSE_BRACKET)
				increaseSpeed(2);
			
			else if(e.getKeyCode() == NativeKeyEvent.VC_O)
				increaseSpeed(3);
			
			start.rate = speedSlider.getValue();
			
			// checks the previous state of the Robot's isActive value and changes it to 'true' if it was 'true' before delay change
			if(prevState == true)
				start.isActive = true;
		}
		else if(e.getKeyCode() == NativeKeyEvent.VC_P)
		{
			// get mouse location and update the robot's mouse location, only works if the robot is not currently active
			if(start.isActive == false)
			{
				mouse = MouseInfo.getPointerInfo().getLocation();
				start.mouseX = mouse.x;
				start.mouseY = mouse.y;
				mouseX = mouse.x;
				mouseY = mouse.y;
			
				updateLocation();
				start.updateLocation();
			}
		}
		else if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			// stops the programs global key listener
			try {
				GlobalScreen.unregisterNativeHook();
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void nativeKeyReleased(NativeKeyEvent e)
	{
		// do nothing
	}
}
