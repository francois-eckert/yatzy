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

    @Test public void yatzy_scores_50() {
        int expected = 50;
        int actual = Yatzy.yatzy(4,4,4,4,4);
        assertEquals(expected, actual);
        assertEquals(50, Yatzy.yatzy(6,6,6,6,6));
        assertEquals(0, Yatzy.yatzy(6,6,6,6,3));
    }

    @ParameterizedTest
    @DisplayName("sum of the dice that reads ")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void sumOfTheDiceThatReads(int value) {
        for(int count=0; count<=5; count++) {
            Yatzy yatzy = getYatzy(value, count);
            assertEquals(value * count, yatzy.sumOfTheDiceThatReads(value));
        }
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

    /**
     * A roll with a fixed number of dice that reads a specific value
     *
     * @param value
     * @param count number of dice that reads the value
     * @return
     */
    private Yatzy getYatzy(int value, int count) {
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
    public void one_pair() {
        assertEquals(6, Yatzy.score_pair(3,4,3,5,6));
        assertEquals(10, Yatzy.score_pair(5,3,3,3,5));
        assertEquals(12, Yatzy.score_pair(5,3,6,6,5));
    }

    @Test
    public void two_Pair() {
        assertEquals(16, Yatzy.two_pair(3,3,5,4,5));
        assertEquals(16, Yatzy.two_pair(3,3,5,5,5));
    }

    @Test
    public void three_of_a_kind() 
    {
        assertEquals(9, Yatzy.three_of_a_kind(3,3,3,4,5));
        assertEquals(15, Yatzy.three_of_a_kind(5,3,5,4,5));
        assertEquals(9, Yatzy.three_of_a_kind(3,3,3,3,5));
    }

    @Test
    public void four_of_a_knd() {
        assertEquals(12, Yatzy.four_of_a_kind(3,3,3,3,5));
        assertEquals(20, Yatzy.four_of_a_kind(5,5,5,4,5));
        assertEquals(9, Yatzy.three_of_a_kind(3,3,3,3,3));
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
