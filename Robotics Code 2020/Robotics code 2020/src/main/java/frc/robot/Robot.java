/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	private final int FRONT_LEFT_TALON = 1;
	private final int FRONT_RIGHT_TALON = 0;
	private final int BOTTOM_LEFT_TALON = 2;
	private final int BOTTOM_RIGHT_TALON = 3;
	private XboxController cont;
	private Talon topLeft,bottomLeft,topRight,bottomRight;
	private Talon newMotor;
	private Solenoid arm,ball;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() 
	{
		m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
		m_chooser.addOption("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);	
		topLeft = new Talon(FRONT_LEFT_TALON);
		bottomLeft = new Talon(BOTTOM_LEFT_TALON);
		topRight = new Talon(FRONT_RIGHT_TALON);
		bottomRight = new Talon(BOTTOM_RIGHT_TALON);
		newMotor = new Talon(4);

		cont = new XboxController(0);
		arm = new Solenoid(0);
		ball = new Solenoid(7);
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		
		topLeft.set(0.5);
		topRight.set(-0.5);
		bottomLeft.set(0.5);
		bottomRight.set(-0.5);
		
		Timer.delay(4.5);
		
		topLeft.set(0);
		topRight.set(0);
		bottomLeft.set(0);
		bottomRight.set(0);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto: 
				// Put custom-- auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		double y = cont.getRawAxis(1);
		double x = cont.getRawAxis(0);	
		if(Math.abs(y)<=0.1){y = 0;}
		if(Math.abs(x)<=0.1){x = 0;}
		drive(y*3/5,x*3/5); //multiplies y axis and x axis by 3/5 when driving
		//button a & b pressed and released
		boolean aPressed = cont.getAButtonPressed();
		boolean bPressed = cont.getBButtonPressed();
		boolean aReleased = cont.getAButtonReleased();
		boolean bReleased = cont.getBButtonReleased();
		
		if(aPressed && !arm.get()){arm.set(true);} //activate hatch lever
		if(bPressed && !ball.get()){ball.set(true);} //activate ball dumper
		
		if(aReleased){arm.set(false);} //deactivate hatch lever
		if(bReleased){ball.set(false);} //deactivate ball dumper
		
	if(cont.getRawButton(5))
		{
			drive(-1,-1);
			Timer.delay(0.648);
			drive(0,0);
		}
		if(cont.getRawButtonPressed(6))
		{
			drive(1,1);
			Timer.delay(0.71);
			drive(0,0);
		}
		
	}

	/**  
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
	
	private void drive(double y, double x)
	{
		double tLeft = -y+x;
		double tRight = y+x;
		double bLeft = -y+x;
		double bRight = y+x;
		
		bottomLeft.set(bLeft);
		bottomRight.set(bRight);
		topLeft.set(tLeft);
		topRight.set(tRight);
	}
}
