import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class AutoClickerStart extends Thread
{
	public boolean isActive = false;
	public int rate;
	public int mouseX = 100, mouseY = 100;
	Robot robot;
	
	public AutoClickerStart(int ra)
	{
		rate = ra;
		
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// do nothing
		}
		
		robot.mouseMove(mouseX, mouseY);
		
		start();
	}
	
	public void updateLocation()
	{
		robot.mouseMove(mouseX, mouseY);
	}
	
	public void run()
	{
		while(isActive == true) {
			try {
				Thread.sleep(rate);
				robot.mouseMove(mouseX, mouseY);
				robot.mousePress(InputEvent.BUTTON1_MASK);
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			} catch (InterruptedException ex) {
				//do nothing
			}
		}
	}
}
