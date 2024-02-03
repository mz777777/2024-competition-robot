package net.ironpulse.commands;

import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import net.ironpulse.Constants;
import net.ironpulse.RobotContainer;
import net.ironpulse.drivers.Limelight;
import net.ironpulse.maths.Compare;
import net.ironpulse.subsystems.ShooterSubsystem;
import net.ironpulse.subsystems.drive.Drive;
import net.ironpulse.swerve.FieldCentricTargetHeading;

import static net.ironpulse.Constants.SwerveConstants.headingGains;
import static net.ironpulse.Constants.SwerveConstants.maxSpeed;

public class SpeakerAimingCommand extends Command {
    private final Drive swerveSubsystem;
    private final ShooterSubsystem shooterSubsystem;
    private final RobotContainer robotContainer;
    private final FieldCentricTargetHeading drive = new FieldCentricTargetHeading()
            .withDeadband(maxSpeed.magnitude() * 0.1)
            .withRotationalDeadband(0)
            .withDriveRequestType(SwerveModule.DriveRequestType.OpenLoopVoltage)
            .withSteerRequestType(SwerveModule.SteerRequestType.MotionMagicExpo);

    public SpeakerAimingCommand(
            RobotContainer robotContainer,
            ShooterSubsystem shooterSubsystem,
            Drive swerveSubsystem
    ) {
        this.swerveSubsystem = swerveSubsystem;
        this.shooterSubsystem = shooterSubsystem;
        this.robotContainer = robotContainer;
        drive.HeadingController.setPID(headingGains.kP, headingGains.kI, headingGains.kD);
    }

    @Override
    public void execute() {
        var targetOptional = Limelight.getTarget();
        var offset = Constants.ShooterConstants.speakerArmOffset.magnitude();
        if (targetOptional.isEmpty()) return;
        var target = targetOptional.get();
        var distance = target.
                targetPoseCameraSpace().
                getTranslation().
                getDistance(new Translation3d());
        if(distance >= Constants.ShooterConstants.shortShootMaxDistance.magnitude()){
                offset = Constants.ShooterConstants.speakerArmOffsetFar.magnitude();
        }
        if (!new Compare(90 - target.position().getY()
                + offset,
                shooterSubsystem.getArmMotor().getPosition().getValue())
                .epsilonEqual(3)
        ) {
            shooterSubsystem.getArmMotor()
                    .setControl(new MotionMagicVoltage(
                            Units.degreesToRotations(90 - target.position().getY() +
                                    offset)));
        }
//
//        swerveSubsystem.(() ->
//                drive
//                        .withVelocityX(-robotContainer.getDriverController().getLeftY()
//                                * maxSpeed.magnitude())
//                        .withVelocityY(-robotContainer.getDriverController().getLeftX()
//                                * maxSpeed.magnitude())
//                        .withCurrentTx(target.position().getX())
//        ).execute();
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.getArmMotor()
                .setControl(new MotionMagicVoltage(0).withSlot(1));
    }
}
