package com.sudoku.solver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
	Read Sudoku grid.
	Keep the number in rows, columns and boxes in 3 hash maps, reduces need for loop to check everytime. lookup is O(1).
	Keep updating this for every correct entry
	Remove in case backtracked.
	Check if row/column/ box has the number if yes backtrack else continue.
	Enter all values from 1 to 9 to check if it is safe. if not safe backtrack.
	
	methods - 
	1) Solve Sudoku(Recursive)
	2) Check whether number is safe. (Straight forward)
	3) Calculate Box Number for element. (Straight Forward)
 */
public class SudokuSolver {
	
	public static Map<Integer, Set<Integer>> rowNums = new HashMap<>();
	public static Map<Integer, Set<Integer>> colNums = new HashMap<>();
	public static Map<Integer, Set<Integer>> boxNums = new HashMap<>();
	
	public static boolean solveSudoku(int[][] board, int n) {
		boolean isEmpty = false;
		int row = -1;
		int col = -1;
		int box = -1;
		for(int i= 0; i<n; i++) {
			for(int j = 0; j<n; j++) {
				if(board[i][j]==0) {
					row= i;
					col = j;
					isEmpty = true;
					break;
				}
			if(isEmpty)
				break;
			}
		}
		if(!isEmpty) return true;
		else {
			for(int i =1; i<=n; i++) {
				int boxNum = calcBoxNum(row,col, n);
				if(isValid(row, col, i, boxNum)) {
					board[row][col] = i;
				if(solveSudoku(board, n))
					return true;
				else {
					board[row][col]=0;
					rowNums.get(row).remove(i);
					colNums.get(col).remove(i);
					boxNums.get(boxNum).remove(i);
					}
				}
			}
		}
		
		return false;
		
	}
	
	public static boolean isValid(int row, int col, int num, int boxNum) {
		//Row already has num?
		if(rowNums.get(row).contains(num)) {
			System.out.println("row clash");
			return false;}
		//Column already has number?
		else if(colNums.get(col).contains(num)) {
			System.out.println("col clash");
			return false;
		}
		//box already has number
		if(boxNums.get(boxNum).contains(num)) {
			System.out.println("box clash");
			return false;
		}
		else{
			rowNums.get(row).add(num);
			colNums.get(col).add(num);
			boxNums.get(boxNum).add(num);
			return true;
		}
	}
	
	public static int calcBoxNum(int row, int col, int num) {
		int boxSize = (int)Math.sqrt(num);
		int boxStartX = row - row%boxSize;
		int boxStartY = col - col%boxSize;
		int boxNum = boxStartX + boxStartY/boxSize;
		return boxNum;	
	}
	
}
