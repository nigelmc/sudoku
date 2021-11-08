import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.UIManager;

public class SudokoEx {

	private JFrame frame;
	private JTextField[][] sudokoGrid;

	public final int sudoGridLen = 9; // so i do not have to type 9 everywhere

	// Preset block
	public final int[] cellBlockA = {0,1,2};
	public final int[] cellBlockB = {3,4,5};
	public final int[] cellBlockC = {6,7,8};
	SudokoPuzzleLoad oSudokoPuzzleLoad = new SudokoPuzzleLoad();
	private JTextField textPuzNum;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SudokoEx window = new SudokoEx();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SudokoEx() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();

		// grid corners
		int gridstartx = 20,gridstarty = 40;
		// text field size
		int offsetx = 20,offsety = 20;
		// space text boxes
		int offsetbumpx = 0,offsetbumpy = 0;
		int gridgap = 5;

		//
		int xbound = (gridstartx * 2) + (offsetx * sudoGridLen ) + (gridgap * 2) + offsetx;
		int ybound = (gridstarty * 2) + (offsety * sudoGridLen ) + (gridgap * 2) + (offsety * 2) + 20;

		System.out.println(" xbound " + xbound);
		System.out.println(" ybound " + ybound);

		frame.setBounds(100, 100, xbound, ybound);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setBackground(UIManager.getColor("textHighlight"));
		panel.setForeground(Color.LIGHT_GRAY);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JButton btnSolve = new JButton("Solve");
		btnSolve.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String[][] puzCur = new String[9][9];
				for (int y = 0; y < sudoGridLen; y++) {
					for (int x = 0; x < sudoGridLen; x++) {
						puzCur[x][y] = sudokoGrid[x][y].getText();
					}
				}

				//String[][] puzSol = oSudokoPuzzleLoad.solvePuzzle(puzCur);

				int maxattempts = 5;
				boolean bsolved = false;
				for (int curattempts = 0; curattempts < maxattempts; curattempts++) {
					if( oSudokoPuzzleLoad.solvePuzzle(puzCur) ) {
						bsolved = true;
						break;
					}
				}

				if(bsolved) {
					reloadGrid(oSudokoPuzzleLoad.getSol());
				}

			}
		});

		btnSolve.setBounds(145, 240, 80, 20);
		panel.add(btnSolve);

		JButton btnLoadPuzzle = new JButton("Load #");
		btnLoadPuzzle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int puzzlenum = Integer.parseInt(textPuzNum.getText());

				try {
					oSudokoPuzzleLoad.loadPuzzleFile(puzzlenum);
					reloadGrid(oSudokoPuzzleLoad.getBeg());
				} catch (IOException eio) {
					// TODO Auto-generated catch block
					eio.printStackTrace();
				}
			}
		});

		btnLoadPuzzle.setBounds(10, 240, 80, 20);
		panel.add(btnLoadPuzzle);

		textPuzNum = new JTextField();
		textPuzNum.addInputMethodListener(new InputMethodListener() {
			public void caretPositionChanged(InputMethodEvent event) {
			}
			public void inputMethodTextChanged(InputMethodEvent event) {
			}
		});
		textPuzNum.setText("1");
		textPuzNum.setBounds(95, 240, 45, 20);
		panel.add(textPuzNum);
		textPuzNum.setColumns(10);

		JButton btnRandom = new JButton("Random");
		btnRandom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				oSudokoPuzzleLoad.genPuzzle();
				reloadGrid(oSudokoPuzzleLoad.getBeg());
			}
		});
		btnRandom.setBounds(145, 265, 80, 20);
		panel.add(btnRandom);

		JButton btnReset = new JButton("Reset");
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				reloadGrid(oSudokoPuzzleLoad.getBeg());
			}
		});
		btnReset.setBounds(10, 264, 80, 20);
		panel.add(btnReset);

		// Gen incase file not available
		oSudokoPuzzleLoad.genPuzzle();

//		try {
//			oSudokoPuzzleLoad.loadPuzzleFile(1);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//

		String[][] puzBeg = oSudokoPuzzleLoad.getBeg();

		sudokoGrid = new JTextField[sudoGridLen][sudoGridLen];
		for (int y = 0; y < sudoGridLen; y++) {
    		  offsetbumpx = 0;
	    	  if( y == 3 || y == 6 ) {
	    		  offsetbumpy = offsetbumpy + gridgap;
	    	  }
		      for (int x = 0; x < sudoGridLen; x++) {

		    	  if( x == 3 || x == 6 ) {
		    		  offsetbumpx = offsetbumpx + gridgap;
		    	  }
	    		  String testing = Integer.toString(x+1) + "_" + Integer.toString(y+1);
	    		  sudokoGrid[x][y]  = new JTextField();
	    		  sudokoGrid[x][y].setBounds(gridstartx + (x * offsetx)+offsetbumpx, gridstarty + (y * offsety)+offsetbumpy, offsetx, offsety);

	    		  panel.add(sudokoGrid[x][y]);
	    		  sudokoGrid[x][y].setColumns(1);
	    		  sudokoGrid[x][y].setDocument(new JTextFieldLimit(1));

	    		  sudokoGrid[x][y].setText(puzBeg[x][y]);
	    	  }
	      }

	}

	void reloadGrid(String[][] puzNew) {
		for (int y = 0; y < sudoGridLen; y++) {
			for (int x = 0; x < sudoGridLen; x++) {
				sudokoGrid[x][y].setText(puzNew[x][y]);
			}
		}
	}

	void clearGrid() {
		for (int y = 0; y < sudoGridLen; y++) {
			for (int x = 0; x < sudoGridLen; x++) {
				sudokoGrid[x][y].setText("");
			}
		}
	}

}

class JTextFieldLimit extends PlainDocument {
  private int limit;
  JTextFieldLimit(int limit) {
    super();
    this.limit = limit;
  }

  JTextFieldLimit(int limit, boolean upper) {
    super();
    this.limit = limit;
  }

  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
    if (str == null) {
      return;
    }

    if ((getLength() + str.length()) <= limit) {
      super.insertString(offset, str, attr);
    }
  }
}

