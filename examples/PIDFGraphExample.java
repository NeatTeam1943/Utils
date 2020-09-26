package frc.robot.commands;

import java.io.IOException;

import com.neatteam1943.utils.PIDFGraph;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.subsystems.DriveSubsystem;

// this example is built around a drive subsystem (chassis)
// that tries to turn to a specific angle (target)
public class PIDFGraphExample extends PIDCommand {
  public PIDFGraphExample(double target, DriveSubsystem drive) {
    super(
        // The controller that the command will use
        new PIDController(0.1, 0.03, 0.7),
        // This should return the measurement
        () -> {
          double value = drive.getHeadinig();

          // add current system state to the graph
          PIDFGraph.add(value);

          return value;
        },
        // This should return the setpoint (can also be a constant)
        target,
        // This uses the output
        output -> drive.arcadeDrive(0.0, output));

    final double tolerance = 5.0;

    m_controller.enableContinuousInput(-180.0, 180.0);
    m_controller.setTolerance(tolerance);

    // this config line is the first method that should be called
    // it configures kP, kI and kD (and kF if you choose) among several others
    PIDFGraph.config(m_controller.getP(), m_controller.getI(), m_controller.getD());

    // never miss the target configuration, it can be the
    // difference between a complete graph and a lame line
    PIDFGraph.configTarget(target, tolerance);

    addRequirements(drive);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_controller.atSetpoint()) {

      // save the generated graph and datasheet inside deploy directory
      // also possible using a flash drive
      // see https://www.chiefdelphi.com/t/anyone-used-a-roborio-usb-port-for-a-flash-drive/140171/2
      try {
        PIDFGraph.save(Filesystem.getDeployDirectory().toPath().toString());
      } catch (IOException e) {
        e.printStackTrace();
      }

      return true;
    }

    return false;
  }
}
