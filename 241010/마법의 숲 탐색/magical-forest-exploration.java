import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static int r, c, k;
    static int[][] forest;
    // 동서남북
    static int[] dx = {0, 1, 0, -1}; // 북동남서
    static int[] dy = {-1, 0, 1, 0};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        st = new StringTokenizer(br.readLine());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        init();
        int answer = 0;

        while (k-- > 0) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken()) - 1;
            int x = 1;
            int exit = Integer.parseInt(st.nextToken());

            forest[x][y] = exit;
            forest[x-1][y] = 4;
            forest[x+1][y] = 4;
            forest[x][y-1] = 4;
            forest[x][y+1] = 4;

            boolean movedFlag = true;
            while (movedFlag) {
                movedFlag = false;
                // 남
                if (check(x, y, 2)) {
                    // move
                    move(x, y, 2);
                    x += dx[2];
                    y += dy[2];
                    // 바닥에 닿았으면 멈춰야됨,
                    if (x + 1 == r + 2) {
                        break;
                    }
                    movedFlag = true;
                }
                // 서
                 else if (check(x, y, 3)) {
                     // move
                    move (x, y, 3);
                    x += dx[3];
                    y += dy[3];
                    movedFlag = true;
                }
                // 동
                else if (check(x, y, 1)) {
                    // move
                    move(x, y, 1);
                    x += dx[1];
                    y += dy[1];
                    movedFlag = true;
                }
            }

            // 출구
            exit = forest[x][y];
            int exitX = x + dx[exit];
            int exitY = y + dy[exit];
            forest[exitX][exitY] = 5;

            // 골렘이 밖에 있는지
            if (isOutOfMap()) {
                init();
            } else {
                answer += bfs(x, y);
            }
        }
        System.out.println(answer);

    }

    private static boolean check(int x, int y, int exitDirection) {
        // 주어진 방향대로
        int nextX = x + dx[exitDirection];
        int nextY = y + dy[exitDirection];

        if (nextY - 1 < 0 || nextY + 1 >= c || nextX + 1 >= r + 3 || nextX - 1 < 0) {
            return false;
        }

        if (forest[nextX][nextY + 1] >= 4 || forest[nextX + 1][nextY] >= 4 || forest[nextX][nextY - 1] >= 4 || forest[nextX - 1][nextY] >= 4) {
            return false;
        }

        // 서
        if (exitDirection == 3) {
            if (nextX + 2 >= r + 3) {
                return false;
            }
            else if (forest[nextX + 1][nextY - 1] >= 4 || forest[nextX + 2][nextY] >= 4) {
                return false;
            }
        }
        // 동
        else if (exitDirection == 1) {
            if (nextX + 2 >= r + 3) {
                return false;
            } else if (forest[nextX + 1][nextY + 1] >= 4 || forest[nextX + 2][nextY] >= 4) {
                return false;
            }
        }
        return true;
    }

    // 골렘 움직임
    private static void move(int x, int y, int direction) {
        // 출구 방향
        int exit = forest[x][y];

        // 지도를 비운다.
        forest[x][y] = -1;
        forest[x-1][y] = -1;
        forest[x+1][y] = -1;
        forest[x][y+1] = -1;
        forest[x][y-1] = -1;

        // 옮김
        int nextX = x + dx[direction];
        int nextY = y + dy[direction];

        forest[nextX-1][nextY] = 4;
        forest[nextX+1][nextY] = 4;
        forest[nextX][nextY + 1] = 4;
        forest[nextX][nextY - 1] = 4;

        // 출구 변동
        if (direction == 2) {
            forest[nextX][nextY] = exit;
        } else if (direction == 3) {
            forest[nextX][nextY] = (exit + 3) % 4;
        } else if (direction == 1) {
            forest[nextX][nextY] = (exit + 1) % 4;
        }
    }

    // 지도 외 구역에 골렘이 있다면?
    private static boolean isOutOfMap() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < c; y++) {
                if (forest[x][y] > 0) {
                    return true;
                }
            }
        }
        return false;
    }


    // 정령의 움직임
    private static int bfs(int x, int y) {
        int max = Integer.MIN_VALUE;

        boolean[][] visited = new boolean[r+3][c];
        Queue<int[]> queue = new ArrayDeque<>();
        queue.add(new int[]{x, y});

        visited[x][y] = true;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();

            for (int i = 0; i < 4; i++) {
                int nx = curr[0] + dx[i];
                int ny = curr[1] + dy[i];

                if (nx < 0 || ny < 0 || nx >= r + 3 || ny >= c)
                    continue;
                if (visited[nx][ny])
                    continue;
                if (forest[nx][ny] < 0)
                    continue;
                if (forest[curr[0]][curr[1]] == 4 && forest[nx][ny] >= 4) {
                    continue;
                }

                queue.offer(new int[]{nx, ny});
                visited[nx][ny] = true;
                max = Math.max(max, nx);
            }
        }
        return max - 2;
    }


    private static void init() {
        forest = new int[r+3][c];
        for (int[] line : forest) {
            Arrays.fill(line, -1);
        }
    }

}