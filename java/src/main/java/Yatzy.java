import java.util.stream.IntStream;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class Yatzy {

    private int[] dice;

    public Yatzy(int d1, int d2, int d3, int d4, int d5) {
        dice = new int[5];
        dice[0] = d1;
        dice[1] = d2;
        dice[2] = d3;
        dice[3] = d4;
        dice[4] = d5;
    }


    public int chance() {
        return IntStream.of(dice).sum();
    }

    /**
     * If all dice have the same number, the player scores 50 points.
     * @return
     */
    public int yatzy() {
        for (int i = 1; i < 5; i++) {
            if (dice[i] != dice[0]) {
                return 0;
            }
        }
        return 50;
    }


    /**
     * The player scores the sum of the dice that reads one, two, three, four, five or six, respectively.
     * @param value
     * @return
     */
    public int sumOfTheDiceThatReads(@Min(1) @Max(6) int value) {
        return IntStream.of(dice)
            .filter(die -> die==value)
            .sum();
    }


    /**
     * The player scores the sum of the two highest matching dice.
     * @return
     */
    public int pair() {
        return several_of_a_kind(2);
    }

    /**
     * If there are two pairs of dice with the same number, the player scores the sum of these dice
     *
     * @return
     */
    public int two_pair() {
        int[] counts = getDistribution();
        int n = 0;
        int score = 0;
        for (int i = 0; i < 6; i++)
            if (counts[i] >= 2) {
                n++;
                score += (i+1)*2;
            }        
        if (n == 2) {
            return score;
        } else {
            return 0;
        }
    }

    /**
     * If there are three dice with the same number, the player scores the sum of these dice.
     * @return
     */
    public int three_of_a_kind() {
        return several_of_a_kind(3);
    }

    /**
     * If there are four dice with the same number, the player scores the sum of these dice.
     * @return
     */
    public int four_of_a_kind() {
        return several_of_a_kind(4);
    }

    /**
     * compte le nombre de dés de chaque face
     * @return
     */
    private int[] getDistribution() {
        int[] counts = new int[] {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 5; i++) {
            counts[dice[i]-1]++;
        }
        return counts;
    }

    private int several_of_a_kind(int value) {
        int[] counts = getDistribution();
        for (int i = 5; i >= 0; i--) {
            if(counts[i]>=value) {
                return (i + 1) * value;
            }
        }
        return 0;
    }


    /**
     * the player scores 15 (the sum of all the dice) if the dice read 1,2,3,4,5
     * @return
     */
    public int smallStraight() {
        int[] counts = getDistribution();
        for (int i = 0; i <= 4; i++) {
            if(counts[i]!=1) {
                return 0;
            }
        }
        return 15;
    }

    /**
     * the player scores 20 (the sum of all the dice) if the dice read 2,3,4,5,6
     * @return
     */
    public int largeStraight() {
        int[] counts = getDistribution();
        for (int i = 1; i <= 5; i++) {
            if(counts[i]!=1) {
                return 0;
            }
        }
        return 20;
    }


    /**
     * the player scores the sum of all the dice if the dice are two of a kind and three of a kind,
     * @return
     */
    public int fullHouse() {
        int[] counts = getDistribution();
        boolean twoOfaKind = false;
        boolean threeOfaKind = false;
        int score = 0;
        for (int i = 0; i < 6; i++) {
            if (counts[i] == 2) {
                twoOfaKind = true;
                score += (i+1)*2;
            }
            if (counts[i] == 3) {
                threeOfaKind = true;
                score += (i+1)*3;
            }
        }
        return twoOfaKind & threeOfaKind ? score : 0;
    }

}



