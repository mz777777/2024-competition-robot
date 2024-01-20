package net.ironpulse.commands;

import edu.wpi.first.wpilibj2.command.Command;
import net.ironpulse.Constants;
import net.ironpulse.RobotContainer;
import net.ironpulse.subsystems.ShooterSubsystem;

public class ShootCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;
    private final RobotContainer robotContainer;


    public ShootCommand(RobotContainer robotContainer, ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        this.robotContainer = robotContainer;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        shooterSubsystem.getShootMotor()
                .setVoltage(Constants.ShooterConstants.shootVoltage.magnitude());
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted)
            robotContainer.getGlobalState().transfer(RobotContainer.Actions.INTERRUPT_SHOOT);
        shooterSubsystem.getShootMotor().setVoltage(0);
    }

    @Override
    public boolean isFinished() {
        return robotContainer.getGlobalState().getCurrentState() == RobotContainer.States.IDLE;
    }
}
