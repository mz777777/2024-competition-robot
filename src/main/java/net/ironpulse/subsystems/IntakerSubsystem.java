package net.ironpulse.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Subsystem;
import lombok.Getter;
import net.ironpulse.data.IntakerData;

import java.util.function.Consumer;

import static net.ironpulse.Constants.IntakerConstants.INTAKER_MOTOR_ID;

public class IntakerSubsystem implements Subsystem {
    @Getter
    private final TalonFX intakerMotor;

    private final Consumer<IntakerData> telemetryFunction;

    public IntakerSubsystem(Consumer<IntakerData> telemetryFunction) {
        intakerMotor = new TalonFX(INTAKER_MOTOR_ID);

        this.telemetryFunction = telemetryFunction;
    }

    @Override
    public void periodic() {
        telemetryFunction.accept(
                new IntakerData()
        );
    }
}
