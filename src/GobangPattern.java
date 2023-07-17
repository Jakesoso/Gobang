import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 1 ： AI
 * 2 ：玩家
 * 3 : 边缘
 */

public class GobangPattern {
    enum Pattern {
        OTHER,
        // AI方
        CONT_FIVE,  // 连五
        ACT_FOUR, ACT_THREE, ACT_TWO, ACT_ONE,   // 活一二三四
        SLP_FOUR, SLP_THREE, SLP_TWO, SLP_ONE,   // 眠一二三四
        // 玩家方
        _CONT_FIVE,  // 连五
        _ACT_FOUR, _ACT_THREE, _ACT_TWO, _ACT_ONE,   // 活一二三四
        _SLP_FOUR, _SLP_THREE, _SLP_TWO, _SLP_ONE,   // 眠一二三四
    }

    public final Map<String, Integer> scoreMap;

    public GobangPattern() {
        scoreMap = new HashMap<>();
        init();
    }

    // 枚举和分数的对应表
    private int calScore(Pattern pattern) {
        switch (pattern) {
            case OTHER:      return 0;
            case ACT_ONE:    return 1;
            case ACT_TWO:    return 20;
            case ACT_THREE:  return 400;
            case ACT_FOUR:   return 50000;
            case SLP_FOUR:   return 400;
//            case SLP_ONE:    return 1;
            case SLP_TWO:    return 1;
            case SLP_THREE:  return 30;
            case CONT_FIVE:  return 10000000;

            case _ACT_ONE:   return -3;
            case _ACT_TWO:   return -50;
            case _ACT_THREE: return -8000;
            case _ACT_FOUR:  return -100000;
            case _SLP_FOUR:  return -100000;
//            case _SLP_ONE:   return 1;
            case _SLP_TWO:   return -3;
            case _SLP_THREE: return -50;
            case _CONT_FIVE: return -10000000;
        }
        return 0;
    }

    // 将本地pattern存入scoreMap
    // 存入时将Pattern枚举转化成对应的分数
    private void init() {
        if (!new File("scoremap.txt").exists()) {
            GeneratePattern generatePattern = new GeneratePattern();
            generatePattern.saveScoreMapIntoFile();
            System.out.println("Saved scoreMap into a file!");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader("scoremap.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0];
                    Pattern pattern = Pattern.valueOf(parts[1]);
                    scoreMap.put(key, calScore(pattern));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}