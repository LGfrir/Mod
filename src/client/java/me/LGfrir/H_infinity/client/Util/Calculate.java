package me.LGfrir.H_infinity.client.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.*;

public class Calculate {
    public static double calculateHorizontalDistance(double curX, double curZ,double tarX,double tarZ) {
        return sqrt((curX - tarX) * (curX - tarX) + (curZ - tarZ) * (curZ - tarZ));
    }

    public static float calculateYaw(double curX, double curZ,double tarX,double tarZ){
        double deltaX = tarX - curX;
        double deltaZ = tarZ - curZ;
        if(deltaZ > 0 && deltaX >= 0){ return -(float)(atan(abs(deltaX / deltaZ)) * 180 / PI);}
        else if(deltaZ > 0 && deltaX <= 0) {return (float)(atan(abs(deltaX / deltaZ)) * 180 / PI);}
        else if(deltaZ < 0 && deltaX <= 0) {return 180 - (float)(atan(abs(deltaX / deltaZ)) * 180 / PI);}
        else if(deltaZ < 0 && deltaX >= 0) {return - 180 + (float)(atan(abs(deltaX / deltaZ)) * 180 / PI);}
        else if(deltaX > 0) return -90f;
        else if(deltaX < 0) return 90f;
        else return 0f;
    }
    public static float calculateYaw2(double curX, double curZ,int tarX,int tarZ){
        float tar = calculateYaw(curX,curZ,tarX,tarZ) + 60;
        if(tar > 180) return tar - 360;
        if(tar <= -180) return tar + 360;
        return tar;
    }

    public static class TSPSolver {

        public static class TSPResult {
            public double length;
            public List<Integer> path;

            public TSPResult(double length, List<Integer> path) {
                this.length = length;
                this.path = path;
            }
        }

        public static TSPResult solveTSP(int[][] points) {
            int n = points.length;
            if (n == 0) {
                return new TSPResult(0, Collections.emptyList());
            }

            // 计算点之间的距离
            double[][] dis = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int dx = points[i][0] - points[j][0];
                    int dy = points[i][1] - points[j][1];
                    dis[i][j] = Math.sqrt(dx * dx + dy * dy);
                }
            }

            int totalMasks = 1 << n;
            double[][] dp = new double[totalMasks][n];
            int[][] preId = new int[totalMasks][n];

            // 初始化数组
            for (int mask = 0; mask < totalMasks; mask++) {
                Arrays.fill(dp[mask], Double.POSITIVE_INFINITY);
                Arrays.fill(preId[mask], -1);
            }
            dp[1][0] = 0.0; // 初始状态：起点0，路径长度为0

            // 预处理每个掩码的位数
            List<Integer>[] masksByBits = new List[n + 1];
            for (int i = 0; i <= n; i++) {
                masksByBits[i] = new ArrayList<>();
            }
            for (int mask = 0; mask < totalMasks; mask++) {
                masksByBits[Integer.bitCount(mask)].add(mask);
            }

            // 动态规划处理，按位数从小到大遍历
            for (int cnt = 1; cnt <= n; cnt++) {
                for (int mask : masksByBits[cnt]) {
                    // 遍历掩码中的每个当前节点b
                    int tempMask = mask;
                    while (tempMask != 0) {
                        int lsb = tempMask & -tempMask;
                        int b = Integer.numberOfTrailingZeros(lsb);
                        tempMask ^= lsb;

                        int prevMask = mask ^ (1 << b);
                        if (prevMask == 0) continue; // 跳过无效状态

                        // 遍历prevMask中的每个前驱节点k
                        int tempPrevMask = prevMask;
                        while (tempPrevMask != 0) {
                            int prevLsb = tempPrevMask & -tempPrevMask;
                            int k = Integer.numberOfTrailingZeros(prevLsb);
                            tempPrevMask ^= prevLsb;

                            if (dp[prevMask][k] + dis[k][b] < dp[mask][b]) {
                                dp[mask][b] = dp[prevMask][k] + dis[k][b];
                                preId[mask][b] = k;
                            }
                        }
                    }
                }
            }

            // 提取最短路径
            int finalMask = (1 << n) - 1;
            double minLength = dp[finalMask][n - 1];
            if (minLength == Double.POSITIVE_INFINITY) {
                return new TSPResult(-1, Collections.emptyList());
            }

            // 回溯路径
            List<Integer> path = new ArrayList<>();
            int current = n - 1;
            int mask = finalMask;
            while (current != -1) {
                path.add(current);
                int prev = preId[mask][current];
                if (prev == -1) break;
                mask ^= (1 << current);
                current = prev;
            }
            Collections.reverse(path);

            // 验证路径起点是否为0
            if (path.isEmpty() || path.get(0) != 0) {
                return new TSPResult(-1, Collections.emptyList());
            }

            return new TSPResult(minLength, path);
        }

    }
}
