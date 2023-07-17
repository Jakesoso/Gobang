/*
* 该类用于生成五子棋的棋型对应的字符串表示
* 将在本地生成scoremap.txt文件
* 每条记录分别为 字符串=棋形
*
*     enum Pattern {
*       OTHER,
*       // AI方
*       CONT_FIVE,  // 连五
*       ACT_FOUR, ACT_THREE, ACT_TWO, ACT_ONE,   // 活一二三四
*       SLP_FOUR, SLP_THREE, SLP_TWO, SLP_ONE,   // 眠一二三四
*       // 玩家方
*       _CONT_FIVE,  // 连五
*       _ACT_FOUR, _ACT_THREE, _ACT_TWO, _ACT_ONE,   // 活一二三四
*       _SLP_FOUR, _SLP_THREE, _SLP_TWO, _SLP_ONE,   // 眠一二三四
 *    }
*
* */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class GeneratePattern {
    public final Map<String, GobangPattern.Pattern> scoreMap;
    private static final String FILE_PATH = "scoremap.txt"; // 本地文件路径

    public GeneratePattern() {
        this.scoreMap = new HashMap<>();
        generate();
    }

    private void setPatternScoreMap(String[] strs, GobangPattern.Pattern pattern, GobangPattern.Pattern _pattern) {
        String[] _strs = new String[strs.length];
        for (int i = 0; i < _strs.length; i++) {
            // 生成反方数组
            _strs[i] = strs[i].replace("1", "2");
            // 存入
            scoreMap.put(strs[i], (pattern));
            scoreMap.put(_strs[i], (_pattern));
        }
    }

    private void generate() {
        // 连五
        String[] contFive_map = {"111111", "111110", "011111", "211111", "111112", "111113", "311111"};
        setPatternScoreMap(contFive_map, GobangPattern.Pattern.CONT_FIVE, GobangPattern.Pattern._CONT_FIVE);
        // 活四
        String[] actFour_map = {"011110"};
        setPatternScoreMap(actFour_map, GobangPattern.Pattern.ACT_FOUR, GobangPattern.Pattern._ACT_FOUR);
        // 活三
        String[] actThree_map = {"011100", "001110", "010110", "011010"};
        setPatternScoreMap(actThree_map, GobangPattern.Pattern.ACT_THREE, GobangPattern.Pattern._ACT_THREE);
        // 活二
        String[] actTwo_map = {"011000", "010100", "001100", "001010", "000110"};
        setPatternScoreMap(actTwo_map, GobangPattern.Pattern.ACT_TWO, GobangPattern.Pattern._ACT_TWO);
        // 活一
        String[] actOne_map = {"010000", "001000", "000100", "000010"};
        setPatternScoreMap(actOne_map, GobangPattern.Pattern.ACT_ONE, GobangPattern.Pattern._ACT_ONE);

        // 死
        int p1, p2, p3, p4, p5, p6, x, y, ix, iy;//x:左5中黑个数,y:左5中白个数,ix:右5中黑个数,iy:右5中白个数
        for(p1 = 0; p1 < 4; p1 ++) {
            for (p2 = 0; p2 < 3; p2 ++) {
                for (p3 = 0; p3 < 3; p3 ++) {
                    for (p4 = 0; p4 < 3; p4 ++) {
                        for (p5 = 0; p5 < 3; p5 ++) {
                            for (p6 = 0; p6 < 4; p6 ++) {
                                x = 0;
                                y = 0;
                                ix = 0;
                                iy = 0;
                                if (p1 == 1)
                                    x ++;
                                else if (p1 == 2)
                                    y ++;

                                if (p2 == 1) {
                                    x ++;
                                    ix ++;
                                }
                                else if (p2 == 2) {
                                    y ++;
                                    iy ++;
                                }

                                if (p3 == 1) {
                                    x ++;
                                    ix ++;
                                }
                                else if (p3 == 2) {
                                    y ++;
                                    iy ++;
                                }

                                if (p4 == 1) {
                                    x ++;
                                    ix ++;
                                }
                                else if (p4 == 2) {
                                    y ++;
                                    iy ++;
                                }

                                if (p5 == 1) {
                                    x ++;
                                    ix ++;
                                }
                                else if (p5 == 2) {
                                    y ++;
                                    iy ++;
                                }

                                if (p6 == 1)
                                    ix++;
                                else if (p6 == 2)
                                    iy++;
                                String temp = "";
                                temp += p1;
                                temp += p2;
                                temp += p3;
                                temp += p4;
                                temp += p5;
                                temp += p6;
                                if (p1 == 3 || p6 == 3) {//有边界
                                    if (p1 == 3 && p6 != 3) {//左边界
                                        //白冲4
                                        if (ix == 0 && iy == 4) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern._SLP_FOUR);
                                        }
                                        //黑冲4
                                        if (ix == 4 && iy == 0) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern.SLP_FOUR);
                                        }
                                        //白眠3
                                        if (ix == 0 && iy == 3) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern._SLP_THREE);
                                        }
                                        //黑眠3
                                        if (ix == 3 && iy == 0) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern.SLP_THREE);
                                        }
                                        //白眠2
                                        if (ix == 0 && iy == 2) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern._SLP_TWO);
                                        }
                                        //黑眠2
                                        if (ix == 2 && iy == 0) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern.SLP_TWO);
                                        }
                                    }
                                    else if (p6 == 3 && p1 != 3) {//右边界
                                        //白冲4
                                        if (x == 0 && y == 4) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern._SLP_FOUR);
                                        }
                                        //黑冲4
                                        if (x == 0 && y == 4) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern.SLP_FOUR);
                                        }
                                        //黑眠3
                                        if (x == 3 && y == 0) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern._SLP_THREE);
                                        }
                                        //白眠3
                                        if (x == 0 && y == 3) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern.SLP_THREE);
                                        }
                                        //黑眠2
                                        if (x == 2 && y == 0) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern._SLP_TWO);
                                        }
                                        //白眠2
                                        if (x == 0 && y == 2) {
                                            if (!scoreMap.containsKey(temp))
                                                scoreMap.put(temp, GobangPattern.Pattern.SLP_TWO);
                                        }
                                    }
                                }
                                else {//无边界
                                    //白冲4
                                    if ((x == 0 && y == 4) || (ix == 0 && iy == 4)) {
                                        if (!scoreMap.containsKey(temp))
                                            scoreMap.put(temp, GobangPattern.Pattern._SLP_FOUR);
                                    }
                                    //黑冲4
                                    if ((x == 4 && y == 0) || (ix == 4 && iy == 0)) {
                                        if (!scoreMap.containsKey(temp))
                                            scoreMap.put(temp, GobangPattern.Pattern.SLP_FOUR);
                                    }
                                    //白眠3
                                    if ((x == 0 && y == 3) || (ix == 0 && iy == 3)) {
                                        if (!scoreMap.containsKey(temp))
                                            scoreMap.put(temp, GobangPattern.Pattern._SLP_THREE);
                                    }
                                    //黑眠3
                                    if ((x == 3 && y == 0) || (ix == 3 && iy == 0)) {
                                        if (!scoreMap.containsKey(temp))
                                            scoreMap.put(temp, GobangPattern.Pattern.SLP_THREE);
                                    }
                                    //白眠2
                                    if ((x == 0 && y == 2) || (ix == 0 && iy == 2)) {
                                        if (!scoreMap.containsKey(temp))
                                            scoreMap.put(temp, GobangPattern.Pattern._SLP_TWO);
                                    }
                                    //黑眠2
                                    if ((x == 2 && y == 0) || (ix == 2 && iy == 0)) {
                                        if (!scoreMap.containsKey(temp))
                                            scoreMap.put(temp, GobangPattern.Pattern.SLP_TWO);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void saveScoreMapIntoFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<String, GobangPattern.Pattern> entry : scoreMap.entrySet()) {
                String key = entry.getKey();
                GobangPattern.Pattern pattern = entry.getValue();
                writer.println(key + ":" + pattern.name()); // 按行写入 key:value
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
