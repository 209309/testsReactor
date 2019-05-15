package edu.iis.mto.testreactor.exc2;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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

        statusBuilder = LaundryStatus.builder();
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
}
