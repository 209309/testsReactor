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

    private WashingMachine washingMachine;
    private LaundryBatch laundryBatch;
    private ProgramConfiguration programConfiguration;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        washingMachine = new WashingMachine(dirtDetector, engine, waterPump);

        laundryBuilder = LaundryBatch.builder();
        laundryBuilder.withType(Material.COTTON);
        laundryBuilder.withWeightKg(5);
        laundryBatch = laundryBuilder.build();

        programBuilder = ProgramConfiguration.builder();
        programBuilder.withProgram(Program.MEDIUM);
        programBuilder.withSpin(true);
        programConfiguration = programBuilder.build();
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

}
