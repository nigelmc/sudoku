import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class SudokoPuzzleLoad {
	int[][] puzGenTemp = new int[9][9];

	String filein = "123456789.txt";

	int totalPuzzles = 2;
	int blockLen = 9;
	String errPuzzle = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
	String puzInput[] = {
			"123456789578139624496872153952381467641297835387564291719623548864915372235748916",
			"123456789578913624469728351245361897816297435937845216351672948792184563684539172"
			};
	String puzInit  = "_#___#____#__#______##______#_____#_____#_____#__#__#______#____#_____#____#__#__";
	char[][] puzInitar  = {
			{'_','#','_','_','_','#','_','_','_'},
			{'_','#','_','_','#','_','_','_','_'},
			{'_','_','#','#','_','_','_','_','_'},
			{'_','#','_','_','_','_','_','#','_'},
			{'_','_','_','_','#','_','_','_','_'},
			{'_','#','_','_','#','_','_','#','_'},
			{'_','_','_','_','_','#','_','_','_'},
			{'_','#','_','_','_','_','_','#','_'},
			{'_','_','_','#','_','_','#','_','_'}};
	String[][] puzBeg = new String[9][9];
	String[][] puzSol = new String[9][9];
	String curPuzzle = "";

	int lastPuzNum = 0;

	public SudokoPuzzleLoad() {
	}

	/**
	 * load from variable
	 *
	 */
	void loadPuzzleTest(int puzNum) {
		if( (puzNum < 1) || (puzNum > totalPuzzles) ) {
			curPuzzle = errPuzzle;
		}
		else {
			// rem 0+
			lastPuzNum = (puzNum - 1);
			curPuzzle = puzInput[lastPuzNum];
		}

		int x = 0, y = 0;
        for (int i = 0; i < curPuzzle.length(); i++) {
        	if(x >= blockLen) {
        		y = y + 1;
        		x = 0;
        	}

        	puzSol[x][y] = Character.toString( curPuzzle.charAt(i) );
        	puzBeg[x][y] = "";

        	if( puzInit.charAt(i) == '#' ) {
        		puzBeg[x][y] = puzSol[x][y];
        	}

        	x = x + 1;
        }
	}

	/**
	 * load from file
	 * http://www.kokolikoko.com/sudoku/
	 */
	void loadPuzzleFile(int puzNum) throws IOException {
		FileReader oFileReader=new FileReader(filein);
		BufferedReader oBufferedReader=new BufferedReader(oFileReader);
		String lineTemp;

		// using the file format raw, skip the first 22 lines
		int linenum_beg = 22;
		// arbitrary end
		int linenum_end = 50;
		//int linenum_end = 10211;
		int linenum = 0;
		// get line
		int lineget = (puzNum + linenum_beg );
		if(lineget > linenum_end) {
			lineget = linenum_beg;
		}

		while( (lineTemp=oBufferedReader.readLine()) != null) {
			linenum = linenum + 1;
			if( linenum < linenum_beg) {
				continue;
			}
			if( linenum >= linenum_end) {
				break;
			}
			if( linenum == lineget) {
				curPuzzle =  lineTemp;
				break;
			}

			//System.out.println(" linenum " + linenum + " " + lineTemp);

		}

		int x = 0, y = 0;
        for (int i = 0; i < curPuzzle.length(); i++) {
        	if(x >= blockLen) {
        		y = y + 1;
        		x = 0;
        	}

        	puzSol[x][y] = Character.toString( curPuzzle.charAt(i) );
        	puzBeg[x][y] = "";

        	if( puzInit.charAt(i) == '#' ) {
        		puzBeg[x][y] = puzSol[x][y];
        	}

        	x = x + 1;
        }
        oBufferedReader.close();
	}

	/**
	 * return puzzle with 17 "hints"
	 */
	String[][] getBeg() {
		return puzBeg;
	}

	/**
	 * return current solution
	 */
	String[][] getSol() {
		return puzSol;
	}

	/**
	 * update current puzzle for begin and solution
	 */
	void setCurrent(String[][] puzzleIn) {
		for (int y = 0; y < blockLen; y++) {
			for (int x = 0; x < blockLen; x++) {
				puzSol[x][y] = puzzleIn[x][y];
				puzBeg[x][y] = "";
				if( puzInitar[x][y] == '#' ) {
					puzBeg[x][y] = puzSol[x][y];
				}

			}
		}
	}
	void setCurrent(int[][] puzzleIn) {
		for (int y = 0; y < blockLen; y++) {
			for (int x = 0; x < blockLen; x++) {
				puzSol[x][y] = Integer.toString(puzzleIn[x][y]);
				puzBeg[x][y] = "";
				if( puzInitar[x][y] == '#' ) {
					puzBeg[x][y] = puzSol[x][y];
				}
			}
		}
	}

	/**
	 * solve puzzle using random shuffles
	 */
	boolean solvePuzzle( String[][] puzzleSolve) {
		for (int y = 0; y < blockLen; y++) {
			for (int x = 0; x < blockLen; x++) {
				if((puzzleSolve[x][y].length()>0)&&(puzzleSolve[x][y].length()<2)) {
					puzGenTemp[x][y] = Integer.parseInt(puzzleSolve[x][y]);
				}
				else {
					puzGenTemp[x][y] = 0;
				}
			}
		}

        int ncountsuccess = -1;
        // Max attempts to generate a puzzle
        int maxtries = 8100, curtries = 0;

		for (int y = 0; y < blockLen; y++) {
			int[] numberset = {1,2,3,4,5,6,7,8,9};

			// first time shuffle
			if(ncountsuccess==-1) {
				shuffle(numberset);
			}
			curtries = curtries + 1;
			if(curtries > maxtries) {
				//throw exception? failed to gen valid puzzle
				break;
			}

			// not init row, and was unable to allocate 9 numbers, rand shuffle try (or try again)
			if( (ncountsuccess==0) || ((ncountsuccess > 0) && (ncountsuccess != 9)) ) {
				shuffle(numberset);
				y = y - 1;

				// rest to try again
				for (int xx = 0; xx < blockLen; xx++) {
					if((puzzleSolve[xx][y].length()>0)&&(puzzleSolve[xx][y].length()>2)) {
						puzGenTemp[xx][y] = Integer.parseInt(puzzleSolve[xx][y]);
					}
					else {
						puzGenTemp[xx][y] = 0;
					}
				}

			}

			// count successes in row
			ncountsuccess=0;
			for (int x = 0; x < blockLen; x++) {
				if( puzGenTemp[x][y] == 0 ) {
					for (int i : numberset) {
						if( checkCoord( x, y, i) ) {
							puzGenTemp[x][y] = i;
							ncountsuccess = ncountsuccess + 1;
							break;
						}
					}
				}
				else {
					//if already set, then it is original
					ncountsuccess = ncountsuccess + 1;
				}
			}

		}

		System.out.println(" Loops " + curtries );
		if(curtries >= maxtries) {
			return false;
		}

		setCurrent(puzGenTemp);
		return true;

	}

	/**
	 * gen puzzle randomly
	 */
	boolean genPuzzle() {
		for (int y = 0; y < blockLen; y++) {
			for (int x = 0; x < blockLen; x++) {
				puzGenTemp[x][y] = 0;
			}
		}

        int ncountsuccess = -1;
        // Max attempts to generate a puzzle
        int maxtries = 81, curtries = 0;

		for (int y = 0; y < blockLen; y++) {
			int[] numberset = {1,2,3,4,5,6,7,8,9};

			// first time shuffle
			if(ncountsuccess==-1) {
				shuffle(numberset);
			}
			curtries = curtries + 1;
			if(curtries > maxtries) {
				//throw exception? failed to gen valid puzzle
				break;
			}

			// not init row, and was unable to allocate 9 numbers, rand shuffle try (or try again)
			if( (ncountsuccess==0) || ((ncountsuccess > 0) && (ncountsuccess != 9)) ) {
				shuffle(numberset);
				y = y - 1;
				for (int x = 0; x < blockLen; x++) {
					puzGenTemp[x][y] = 0;
				}
			}

			// count successes in row
			ncountsuccess=0;
			for (int x = 0; x < blockLen; x++) {
				for (int i : numberset) {
					if( checkCoord( x, y, i) ) {
						puzGenTemp[x][y] = i;
						ncountsuccess = ncountsuccess + 1;
						break;
					}
				}
			}

		}

		if(curtries >= maxtries) {
			return false;
		}

		setCurrent(puzGenTemp);
		return true;
	}

	/**
	 * rand shuffle values
	 */
	void shuffle(int[] arrayin) {
		Random rnd = new Random();
	    for (int i = arrayin.length - 1; i > 0; i--)
	    {
	      int idx = rnd.nextInt(i + 1);
	      int tmp = arrayin[idx];
	      arrayin[idx] = arrayin[i];
	      arrayin[i] = tmp;
	    }
    }

	/**
	 * check col, row, and block for number
	 * false == not found
	 */
	boolean checkCoord( int x, int y, int check) {
		if( checkNumY( y, check) && checkNumX( x, check) && checkNumBlock( getBlock(x,y), check) ) {
			return true;
		}
		return false;
	}

	/**
	 * check block (by id) for used number
	 * false == not found
	 */
	boolean checkNumBlock( int blocknum, int check) {

		int[] colrowA = {0,1,2}, colrowB = {3,4,5},colrowC = {6,7,8};

		switch(blocknum) {
			case 1:
				return checkBlock(colrowA,colrowA,check);
			case 2:
				return checkBlock(colrowA,colrowB,check);
			case 3:
				return checkBlock(colrowA,colrowC,check);
			case 4:
				return checkBlock(colrowB,colrowA,check);
			case 5:
				return checkBlock(colrowB,colrowB,check);
			case 6:
				return checkBlock(colrowB,colrowC,check);
			case 7:
				return checkBlock(colrowC,colrowA,check);
			case 8:
				return checkBlock(colrowC,colrowB,check);
			case 9:
				return checkBlock(colrowC,colrowC,check);
			default:
				//should throw exception
				break;
		}
		return true;
	}

	/**
	 * check block for used number
	 * false == not found
	 */
	boolean checkBlock(int[] cols, int[] rows, int check) {
		for (int col : cols) {
			for (int row : rows) {
	        	if(puzGenTemp[row][col] == check) {
	        		return false;
	        	}
			}
		}
		return true;
	}

	/**
	 * check col for used number
	 * false == not found
	 */
	boolean checkNumX(int x, int check) {
		//System.out.println(" checkNumX " + x);
        for (int i = 0; i < blockLen; i++) {
        	if(puzGenTemp[x][i] == check) {
        		return false;
        	}
        }
        return true;
	}

	/**
	 * check row for used number
	 * false == not found
	 */
	boolean checkNumY(int y, int check) {
		//System.out.println(" checkNumY " + y);
        for (int i = 0; i < blockLen; i++) {
        	if(puzGenTemp[i][y] == check) {
        		return false;
        	}
        }
        return true;
	}

	/**
	 * get the sudoku block
	 * return 1-9
	 */
	int getBlock(int x, int y) {
		int blocknum = 0;
		if (x <= 2) {
			if (y <= 2) {
				blocknum = 1;
			} else if( (y > 2) && (y <= 5) ) {
				blocknum = 4;
			} else {
				blocknum = 7;
			}
		} else if( (x > 2) && (x <= 5) ) {
			if (y <= 2) {
				blocknum = 2;
			} else if( (y > 2) && (y <= 5) ) {
				blocknum = 5;
			} else {
				blocknum = 8;
			}
		} else {
			if (y <= 2) {
				blocknum = 3;
			} else if( (y > 2) && (y <= 5) ) {
				blocknum = 6;
			} else {
				blocknum = 9;
			}
		}
		return blocknum;
	}

}
