package net.ironpulse.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Subsystem;
import lombok.Getter;
import net.ironpulse.Constants;
import net.ironpulse.data.IndexerData;

import java.util.function.Consumer;

import static edu.wpi.first.units.Units.*;

public class IndexerSubsystem implements Subsystem {
    @Getter
    private final TalonFX indexerMotor;

    private Consumer<IndexerData> telemetryFunction;

    public IndexerSubsystem(Consumer<IndexerData> telemetryFunction) {
        indexerMotor = new TalonFX(Constants.IndexerConstants.INDEXER_MOTOR_ID);

        this.telemetryFunction = telemetryFunction;
    }

    @Override
    public void periodic() {
        telemetryFunction.accept(
                new IndexerData(
                        RotationsPerSecond.of(indexerMotor.getVelocity().getValue()),
                        Degrees.of(Degrees.convertFrom(
                                indexerMotor.getPosition().getValue(), Rotations))
                )
        );
    }
}
