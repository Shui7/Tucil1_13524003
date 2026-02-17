package stima.modules;

class Result {
    int[][] position;
    long calculationTime = 0;
    int cases = 0;
}

public class Algorithm {
    public static boolean isDiagonalNeighbor(int posX, int posY, int newX, int newY) {
        return (posX - newX == 1 && posY - newY == 1) || (posX - newX == -1 && posY - newY == 1) || (posX - newX == 1 && posY - newY == -1) || (posX - newX == -1 && posY - newY == -1);
    }

    public static boolean isValid(Matrix board, int[][] position, int newX, int newY) {
        boolean result = true;
        int n = board.getRow();

        for(int i = 0; i < n-1; i++) {
            int posX = position[i][0];
            int posY = position[i][1];
            if(posX == -1) break;
            if(newX == posX || newY == posY || board.getElmt(newY, newX) == board.getElmt(posY, posX) || isDiagonalNeighbor(posX, posY, newX, newY)) {
                result = false;
                break;
            }
        }

        return result;
    }

    public static boolean isValidAll(Matrix board, int[][] position) {
        boolean result = true;
        int n = board.getRow();

        for(int i = 0; i < n-1; i++) {
            for(int j = i+1; j < n; j++) {
                int posX1 = position[i][0];
                int posY1 = position[i][1];
                int posX2 = position[j][0];
                int posY2 = position[j][1];
                if(posY1 == -1 || posY2 == -1) break;
                if(posX1 == posX2 || posY1 == posY2 || board.getElmt(posY1, posX1) == board.getElmt(posY2, posX2) || isDiagonalNeighbor(posX1, posY1, posX2, posY2)) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    public static int[][] solve(Matrix board, boolean showStep) {
        long startTime = System.nanoTime();
        int n = board.getRow();

        // Initialize queens' position, (0, -1) being the default position (not placed yet)
        int[][] position = new int[n][2];
        for(int i = 0; i < n; i++) {
            position[i][0] = 0;
            position[i][1] = -1;
        }

        int placed = 0;
        
        // Candidate position to place the next queen
        int newX = 0;
        int newY = -1;
        boolean possible = true;

        while(placed < n && possible) {
            // System.out.print("\n\n");

            // Increment algorithm so it doesn't overflow beyond board's size
            newY++;
            if(newY >= n) {
                newY = 0;
                newX += 1;
            }
            if(newX >= n) {
                placed--;
                if(placed < 0) {
                    possible = false;
                    break;
                }
                if(placed >= 0) {
                    newX = position[placed][0];
                    newY = position[placed][1];
                    position[placed][0] = 0;
                    position[placed][1] = -1;
                }
                continue;
            }

            // Print board with placed queens, queens' position is denoted as '#'
            if(showStep) {
                Matrix newBoard = new Matrix(board);
                for(int i = 0; i < n; i++) {
                    if(position[i][1] == -1) break;
                    newBoard.setElmt('#', position[i][0], position[i][1]);
                }
                newBoard.setElmt('#', newX, newY);
                System.out.print(newBoard);
            }

            // Check if the candidate position is valid
            // Valid: record the position in the position array
            // Not valid: do nothing
            if(isValid(board, position, newX, newY)) {
                position[placed][0] = newX;
                position[placed][1] = newY;
                placed++;
            }

            // Delay per iteration, adjustable (soon)
            try {
                Thread.sleep(0);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }   
        }

        if(possible) {
            
            System.out.println("Possible to solve! Solution:");
            for(int i = 0; i < n; i++) {
                System.out.printf("Pos %d: (%d,%d)\n", i+1, position[i][0], position[i][1]);
            }
        }
        else {
            System.out.println("Not possible to solve!");
        }

        long endTime = System.nanoTime();
        String time = Long.toString(endTime - startTime);
        System.out.print("Calculation time: ");
        // To get accurate time without floating point error
        for(int i = 0; i < time.length(); i++) {
            System.out.print(time.charAt(i));
            if(time.length() - i == 7) System.out.print('.');
        }
        System.out.println(" milliseconds");
        Matrix newBoard = new Matrix(board);
        for(int i = 0; i < n; i++) {
            if(position[i][1] == -1) break;
            newBoard.setElmt('#', position[i][0], position[i][1]);
        }
        newBoard.setElmt('#', newX, newY);
        System.out.print(newBoard);

        return position;
    }

    static void exhaustiveSolve(Matrix board, boolean showStep, Result result) {
        long startTime = System.nanoTime();
        int n = board.getRow();
        int cases = 1;

        // Initialize queens' position on first iteration
        int[][] position = new int[n][2];
        for(int i = 0; i < n; i++) {
            position[i][0] = i;
            position[i][1] = 0;
        }

        int placed = n;
        int toMove = n-1; // Last queen moving first
        
        // Candidate position to place the next queen
        boolean possible = true;

        while(toMove < n && possible) {
            // Check if the position is valid
            // Valid: current position is solution
            // Not valid: iterate more
            // Only check when all queens are placed
            if(placed == n) {
                if(isValidAll(board, position)) {
                    break;
                }
                cases++;
                // Print board with placed queens, queens' position is denoted as '#'
                if(showStep) {
                    Matrix newBoard = new Matrix(board);
                    for(int i = 0; i < n; i++) {
                        if(position[i][1] == -1) break;
                        newBoard.setElmt('#', position[i][1], position[i][0]);
                    }
                    System.out.print(newBoard);
                }
            }

            int newX = position[toMove][0];
            int newY = position[toMove][1];

            if(newX == -1) {
                newX = position[toMove-1][0];
                newY = position[toMove-1][1];
                placed++;
            }

            // Increment algorithm so it doesn't overflow beyond board's size
            newX++;
            if(newX >= n) {
                newX = 0;
                newY += 1;
            }
            if(newY >= n) {
                // Remove from board
                position[toMove][0] = -1;
                position[toMove][1] = 0;
                placed--;
                toMove--;
                if(toMove < 0) {
                    possible = false;
                    break;
                }
                continue;
            }
            else {
                position[toMove][0] = newX;
                position[toMove][1] = newY;
            }

            if(toMove < n-1) {
                toMove++;
                continue;
            }

            // Delay per iteration, adjustable (soon)
            try {
                Thread.sleep(0);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }   
        }

        if(possible) {
            System.out.println("Possible to solve! Solution:");
            for(int i = 0; i < n; i++) {
                System.out.printf("Pos %d: (%d,%d)\n", i+1, position[i][0], position[i][1]);
            }
        }
        else {
            System.out.println("Not possible to solve!");
            for(int i = 0; i < n; i++) {
                position[i][0] = -1;
                position[i][1] = 0;
            }
        }

        long endTime = System.nanoTime();
        long time = endTime - startTime;
        String timeText = Long.toString(time);
        System.out.print("Calculation time: ");
        // To get accurate time without floating point error
        for(int i = 0; i < timeText.length(); i++) {
            System.out.print(timeText.charAt(i));
            if(timeText.length() - i == 7) System.out.print('.');
        }
        System.out.println(" milliseconds");
        Matrix newBoard = new Matrix(board);
        for(int i = 0; i < n; i++) {
            if(position[i][1] == -1) break;
            newBoard.setElmt('#', position[i][1], position[i][0]);
        }
        System.out.print(newBoard);

        result.position = position;
        result.calculationTime = time;
        result.cases = cases;
    }
}
