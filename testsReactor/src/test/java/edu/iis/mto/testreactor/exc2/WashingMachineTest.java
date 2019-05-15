package edu.iis.mto.testreactor.exc2;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WashingMachineTest {
    @Mock DirtDetector dirtDetector;
    @Mock Engine engine;
    @Mock WaterPump waterPump;
    private static LaundryBatch.Builder laundryBuilder;
    private static ProgramConfiguration.Builder programBuilder;
    private static LaundryStatus.Builder statusBuilder;

    private WashingMachine washingMachine;
    private LaundryBatch laundryBatch;
    private ProgramConfiguration programConfiguration;
    private LaundryStatus laundryStatus;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);

        // Default LaundryBatch
        laundryBuilder = LaundryBatch.builder();
        laundryBuilder.withType(Material.COTTON);
        laundryBuilder.withWeightKg(5);
        laundryBatch = laundryBuilder.build();

        // Default ProgramConfiguration
        programBuilder = ProgramConfiguration.builder();
        programBuilder.withProgram(Program.MEDIUM);
        programBuilder.withSpin(true);
        programConfiguration = programBuilder.build();

        // Default LaundryStatus
        statusBuilder = LaundryStatus.builder();
        statusBuilder.withResult(Result.FAILURE);
        statusBuilder.withRunnedProgram(null);
        statusBuilder.withErrorCode(ErrorCode.TOO_HEAVY);
        laundryStatus = statusBuilder.build();
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

    @Test
    public void shouldReturnGivenLaundryStatus() {
        programBuilder.withProgram(Program.MEDIUM);
        programConfiguration = programBuilder.build();

        statusBuilder.withResult(Result.SUCCESS);
        statusBuilder.withRunnedProgram(Program.MEDIUM);
        statusBuilder.withErrorCode(null);
        laundryStatus = statusBuilder.build();

        assertThat(washingMachine.start(laundryBatch, programConfiguration), is(laundryStatus));
    }

    @Test
    public void shouldReturnFailureIfLaundryIsTooHeavy() {
        laundryBuilder.withWeightKg(10);
        laundryBatch = laundryBuilder.build();

        statusBuilder.withResult(Result.FAILURE);
        statusBuilder.withRunnedProgram(null);
        statusBuilder.withErrorCode(ErrorCode.TOO_HEAVY);
        laundryStatus = statusBuilder.build();

        assertThat(washingMachine.start(laundryBatch, programConfiguration), is(laundryStatus));
    }

    @Test
    public void checkIfSpinWasCalledWhenSpinIsFalse() {
        programBuilder.withSpin(false);
        programConfiguration = programBuilder.build();

        washingMachine.start(laundryBatch, programConfiguration);

        verify(engine, never()).spin();
    }

    @Test
    public void checkIfSpinWasCalledWhenSpinIsTrue() {
        programBuilder.withSpin(true);
        programConfiguration = programBuilder.build();

        washingMachine.start(laundryBatch, programConfiguration);

        verify(engine, times(1)).spin();
    }

    @Test
    public void checkIfShortProgramReturnsProperLength() {
        programBuilder.withProgram(Program.SHORT);
        programConfiguration = programBuilder.build();

        washingMachine.start(laundryBatch, programConfiguration);

        verify(engine, times(1)).runWashing(20);
    }

    @Test
    public void checkIfLongProgramReturnsProperLength() {
        programBuilder.withProgram(Program.LONG);
        programConfiguration = programBuilder.build();

        washingMachine.start(laundryBatch, programConfiguration);

        verify(engine, times(1)).runWashing(120);
    }

    @Test
    public void checkIfWaterPumpWorksProperly() {
        laundryBuilder.withWeightKg(3);
        laundryBatch = laundryBuilder.build();

        washingMachine.start(laundryBatch, programConfiguration);

        verify(waterPump, times(1)).pour(3);
        verify(waterPump, times(1)).release();
    }
}
