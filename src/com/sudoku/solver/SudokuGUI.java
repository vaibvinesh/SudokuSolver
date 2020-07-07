package com.sudoku.solver;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import com.sudoku.solver.SudokuSolver;
import javax.swing.*;


public class SudokuGUI extends JFrame {
	//Grid Size
	public static final int gridSize = 9;
	public static final int subgridSize = 3;
	
	//Cell dimensions
	public static final int cellSize = 60;
	public static final int canvasWidth = cellSize*gridSize;
	public static final int canvasHeight = cellSize*gridSize;
	
	//color of text, cells for contested, valid i/ps
	public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
	public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0);  // RGB
	public static final Color OPEN_CELL_TEXT_NO = Color.RED;
	public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
	public static final Color CLOSED_CELL_TEXT = Color.BLACK;
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
    //menu bar
    JMenuBar menuBar = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu solve = new JMenu("Solve/Validate");
    JMenuItem create = new JMenuItem("Create");
	JMenuItem exit = new JMenuItem("Exit");
    JMenuItem complete = new JMenuItem("Solve");
    JMenuItem validate = new JMenuItem("Validate");
    //Text Cells
    private static JTextField[][] numCells = new JTextField[gridSize][gridSize];
    private static boolean[][] mask = new boolean[gridSize][gridSize];
    //Define the base for the puzzle.
    public static int[][] puzzle = new int[gridSize][gridSize];
    //OptionPane for dialogs
    JOptionPane pane = new JOptionPane();
    //Set up the GUI output and instantiate the listeners
    // 1 listener for the cells
    // 1 listener for the menus
    public SudokuGUI() {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Container cp = getContentPane();
    	cp.setLayout(new GridLayout(gridSize,gridSize));
    	//cp.add(pane);
       	//listeners for the menu items
    	create.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		actionCreate();
        		}
        });
    	exit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        		}
        });
    	complete.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		actionSolve();
        		}
        });
    	validate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		actionValidate();
        		}
        });
    	for(int i = 0; i<gridSize; i++) {
    		for(int j = 0; j< gridSize; j++) {
    			numCells[i][j] = new JTextField();
    			cp.add(numCells[i][j]);
    			mask[i][j] = false;
    			numCells[i][j].setEditable(false);
    			numCells[i][j].setText("0");
    			numCells[i][j].setBackground(CLOSED_CELL_BGCOLOR);
    			numCells[i][j].setForeground(CLOSED_CELL_TEXT);
    		}
    	}
    	 cp.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    	file.add(create);
    	file.add(exit);
    	solve.add(complete);
    	solve.add(validate);
    	menuBar.add(file);
    	menuBar.add(solve);
    	setJMenuBar(menuBar);
    	setTitle("Sudoku");
    	pack();
    	setVisible(true);
    	
    }
	private void actionCreate() {
		// TODO Auto-generated method stub
		CellEvent e1  = new CellEvent();
		SudokuSolver.boxNums.clear();
		SudokuSolver.colNums.clear();
		SudokuSolver.rowNums.clear();
		for(int i = 0; i<gridSize; i++) {
			SudokuSolver.rowNums.computeIfAbsent(i, x-> new HashSet<>());
    		for(int j = 0; j< gridSize; j++) {
    			mask[i][j] = true;
    			numCells[i][j].setEditable(true);
    			numCells[i][j].setText("0");
    			numCells[i][j].setBackground(OPEN_CELL_BGCOLOR);
    			numCells[i][j].setForeground(OPEN_CELL_TEXT_YES);
    			numCells[i][j].addActionListener(e1);
    			numCells[i][j].setHorizontalAlignment(JTextField.CENTER);
    			int boxNum = SudokuSolver.calcBoxNum(i, j, gridSize);
    			SudokuSolver.boxNums.computeIfAbsent(boxNum, x-> new HashSet<>());
    			SudokuSolver.colNums.put(j, new HashSet<>());
    		}
    	}
		
	}
	private void actionSolve() {
		// TODO Auto-generated method stub
		for(int i = 0; i< gridSize; i++ ) {
			for(int j = 0; j<gridSize; j++) {
				numCells[i][j].setEditable(false);
				numCells[i][j].setBackground(CLOSED_CELL_BGCOLOR);
    			numCells[i][j].setForeground(CLOSED_CELL_TEXT);
			}
		}
		if(SudokuSolver.solveSudoku(puzzle, 9)) {
			for(int i = 0; i< gridSize; i++ ) {
				for(int j = 0; j<gridSize; j++) {
					numCells[i][j].setText(Integer.toString(puzzle[i][j]));
				}
			}
		}
	}
	private void actionValidate() {
		// TODO Auto-generated method stub
		
	}
    private class CellEvent implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int row = -1;
			int col = -1;
			JTextField selected  = (JTextField) e.getSource();
			boolean found = false;
			for(int i = 0; i< gridSize && !found ; i++ ) {
				for(int j = 0; j<gridSize && !found; j++) {
					if(numCells[i][j]== selected) {
						found = true;
						row = i;
						col = j;
					}
				}
			}
			System.out.println(SudokuSolver.rowNums.containsKey(row)+","+SudokuSolver.colNums.containsKey(col));
			int num = Integer.valueOf(numCells[row][col].getText());
			int boxNum = SudokuSolver.calcBoxNum(row, col, gridSize);
			if(num ==0) return;
			if(!SudokuSolver.isValid(row,col,num, boxNum)) {
				numCells[row][col].setForeground(OPEN_CELL_TEXT_NO);
				pane.showMessageDialog(new JFrame(), "Invalid Entry", "Dialog", JOptionPane.ERROR_MESSAGE);
			}
			else {
				numCells[row][col].setForeground(CLOSED_CELL_TEXT);
				SudokuSolver.boxNums.get(boxNum).add(num);
				SudokuSolver.rowNums.get(row).add(num);
				SudokuSolver.colNums.get(col).add(num);
				puzzle[row][col] = num;
			}
		}
    	
    }
    public static void main(String[] args) {
    	SudokuGUI obj = new SudokuGUI();
    }
    
}
