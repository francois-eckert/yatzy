import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class YatzyTest {

    private static ExecutableValidator validator;


    @BeforeAll
    private static void initValidatorFactory() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator().forExecutables();
    }

    @Test
    @DisplayName("chance scores sum of all dice")
    public void chance_scores_sum_of_all_dice() {
        Yatzy yatzy = new Yatzy(2,3,4,5,1);
        assertEquals(15, yatzy.chance());
    }

    @Test
    @DisplayName("yatzy scores 50")
    public void yatzy_scores_50() {
        Yatzy roll = new Yatzy(2,3,4,5,1);
        assertEquals(0, roll.yatzy());
        roll = new Yatzy(1,1,1,1,1);
        assertEquals(50, roll.yatzy());
    }

    @ParameterizedTest
    @DisplayName("sum of the dice that reads ")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void sumOfTheDiceThatReads(int value) {
        for(int count=0; count<=5; count++) {
            Yatzy yatzy = getRollWith(value, count);
            assertEquals(value * count, yatzy.sumOfTheDiceThatReads(value));
        }
    }

    /**
     * A roll with a fixed number of dice that reads a specific value
     *
     * @param value
     * @param count number of dice that reads the value
     * @return
     */
    private Yatzy getRollWith(int value, int count) {
        int[] dice = new int[5];
        for(int i=1; i<=5; i++) {
            if(i<=count) {
                dice[i-1] = value;
            } else {
                dice[i-1] = value==1 ? 2 : 1;
            }
        }
        return new Yatzy(dice[0], dice[1], dice[2], dice[3], dice[4]);
    }


    @Test
    @DisplayName("sumOfTheDiceThatReads accepts a value between 1 and 6")
    public void sumOfTheDiceThatReads£_accepted_values() throws NoSuchMethodException {
        Yatzy yatzy = new Yatzy(1, 2, 3, 4, 5);
        Method method = Yatzy.class.getMethod("sumOfTheDiceThatReads", int.class);
        assertFalse(validator.validateParameters(yatzy, method, new Object[]{ 0 }).isEmpty());
        assertTrue(validator.validateParameters(yatzy, method, new Object[]{ 1 }).isEmpty());
        assertTrue(validator.validateParameters(yatzy, method, new Object[]{ 2 }).isEmpty());
        assertTrue(validator.validateParameters(yatzy, method, new Object[]{ 3 }).isEmpty());
        assertTrue(validator.validateParameters(yatzy, method, new Object[]{ 4 }).isEmpty());
        assertTrue(validator.validateParameters(yatzy, method, new Object[]{ 5 }).isEmpty());
        assertTrue(validator.validateParameters(yatzy, method, new Object[]{ 6 }).isEmpty());
        assertFalse(validator.validateParameters(yatzy, method, new Object[]{ 7 }).isEmpty());
    }


    @Test
    @DisplayName("one pair scores the sum of the two highest matching dice")
    public void one_pair() {
        Yatzy roll = new Yatzy(1,1,2,4,4);
        assertEquals(8, roll.pair());
        roll = new Yatzy(1,1,6,2,6);
        assertEquals(12, roll.pair());
        roll = new Yatzy(3,3,3,4,1);
        assertEquals(6, roll.pair());
        roll = new Yatzy(3,3,3,3,1);
        assertEquals(6, roll.pair());
        roll = new Yatzy(1,2,3,4,5);
        assertEquals(0, roll.pair());
    }

    @Test
    @DisplayName("two pairs scores the sum of these dice")
    public void two_Pair() {
        Yatzy roll = new Yatzy(1,1,2,3,3);
        assertEquals(8, roll.two_pair());
        roll = new Yatzy(1,1,2,3,4);
        assertEquals(0, roll.two_pair());
        roll = new Yatzy(1,1,2,2,2);
        assertEquals(6, roll.two_pair());
    }

    @Test
    public void three_of_a_kind() {
        Yatzy roll = new Yatzy(3,3,3,4,5);
        assertEquals(9, roll.three_of_a_kind());
        roll = new Yatzy(3,3,4,5,6);
        assertEquals(0, roll.three_of_a_kind());
        roll = new Yatzy(3,3,3,3,1);
        assertEquals(9, roll.three_of_a_kind());
    }

    @Test
    public void four_of_a_knd() {
        Yatzy roll = new Yatzy(2,2,2,2,5);
        assertEquals(8, roll.four_of_a_kind());
        roll = new Yatzy(2,2,2,5,5);
        assertEquals(0, roll.four_of_a_kind());
        roll = new Yatzy(2,2,2,2,2);
        assertEquals(8, roll.four_of_a_kind());
    }

    @Test
    public void smallStraight() {
        assertEquals(15, Yatzy.smallStraight(1,2,3,4,5));
        assertEquals(15, Yatzy.smallStraight(2,3,4,5,1));
        assertEquals(0, Yatzy.smallStraight(1,2,2,4,5));
    }

    @Test
    public void largeStraight() {
        assertEquals(20, Yatzy.largeStraight(6,2,3,4,5));
        assertEquals(20, Yatzy.largeStraight(2,3,4,5,6));
        assertEquals(0, Yatzy.largeStraight(1,2,2,4,5));
    }

    @Test
    public void fullHouse() {
        assertEquals(18, Yatzy.fullHouse(6,2,2,2,6));
        assertEquals(0, Yatzy.fullHouse(2,3,4,5,6));
    }
}
