# sudoku
Simple Sudoku app, uses a simple random shuffle to generate or solve puzzles.

Designed using windowbuilder in eclipse, uses only standard imports

Build
  Create a new java project in eclipse
  Drop the files into src
  right-click SudokoEx.java -> "Run As" -> "Java Application"
  * add file to project root to load from file

Extra
  puzzles can be loaded from a file
  * 123456789.txt in project root directory, original test file from http://www.kokolikoko.com/sudoku/
  * line format is e.g. 123456789876139524549827361365798412481265937792314856957682143214573698638941275
  * loader skips 21 lines as the original file has header from the creator of the puzzle file (see above)
  solver uses bruteforce-type shuffle, it will eventually solve a puzzle, but may not on first execution
  * there are many better ways to do this
  The "clues" are based on a template that masks all but 17 values, same 17 locations for all puzzles
  * the "clue" template was arbitrarily chosen, no true validation  
  * there are better ways to create unique starts
  * the 17 "clues" being constant does not guarantee unique solution
  * the 17 number is from https://www.nature.com/articles/nature.2012.9751
  A solution is created (or loaded) first and then masked, however "Solve" will always re-solve the puzzle
  Allows modifying values in grid, but does not validate the values, solutions will not be correct if values are not valid, i.e. adding "1" twice in same row will still result in a solution
  * this was left on purpose
  Solver will try to re-solve each row up to 8100 times
  * fails are generally due to impossible solution in initial rows, so the result looks like single, double, triple digit attempts or fails
  * to further clarify a few hundred executions the attempts are either less than 200 or 8100, which implies an optimization at lower level with this simple method

How to
  Load - Loads form file 
    *textbox - line number from file
  Reset - return to "clue" 
  Solve - will use "solvePuzzle()" *will always solve the puzzle again despite solution being stored
  Random - will generate a random puzzle 

Potential improvements
  * On text change validate entry
  * Memoize shuffle to speed up generation/solution
  * Can be refactored
  * Could be built into proper app/jar
