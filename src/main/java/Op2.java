/**
 * Calculates suspiciousness and confidence values according to the Op2 fault localization
 * technique.
 * The usage mode is to create a coverage matrix that specifies which program
 * elements are executed by which test cases. In terms of this implementation, each program element called
 * a statement. coverage matrix is encoded as a two dimensional array where the first dimension is indexed by
 * the test case number and the second dimension is indexed by the statement number.
 */
public class Op2 {
  private boolean[][] coverageMatrix; // coverage matrix -- [test][statement]
  private boolean[] failTestCases; // failing test cases -- [test]
  private boolean[] liveTestCases; // live test cases -- [test]
  private boolean[] badCoverage; // bad coverage (no coverage information, usually due to a segmentation fault) -- [test]
  private boolean[] coverableStatements; // coverable statements -- [statement]
  private boolean isBadCoverageCalculated = false;

  private int numberOfTests; // number of test cases
  private int numberOfStatements; // number of statements
  private int totalLiveFail;
  private int totalLivePass;
  private int[] passOnStatement; // p(s), for every s and considering liveness of test cases -- [statement]
  private int[] failOnStatement; // f(s), for every s and considering liveness of test cases -- [statemen]


  /**
   * Constructor of Op2 class
   */
  public Op2(boolean[][] coverageMatrix) {
    this.coverageMatrix = coverageMatrix;
    numberOfTests = coverageMatrix.length;
    numberOfStatements = coverageMatrix[0].length;

    // initialize so that all test cases are live
    liveTestCases = new boolean[numberOfTests];
    for (int i = 0; i < liveTestCases.length; i++) {
      liveTestCases[i] = true;
    }

  }

  private void calculateBadTestCoverage() {
    if (isBadCoverageCalculated)
      return;
    badCoverage = new boolean[numberOfTests];
    for (int i = 0; i < numberOfTests; i++) {
      badCoverage[i] = true;
      for (int j = 0; j < numberOfStatements; j++) {
        //if there is a statement covered for this test case this is not a bad test case
        if (coverageMatrix[i][j]) {
          badCoverage[i] = false;
          break;
        }
      }
      isBadCoverageCalculated = true;
    }
  }

  private void calculateTotalLiveFailAndPass() {
    totalLiveFail = 0;
    totalLivePass = 0;
    for (int i = 0; i < numberOfTests; i++) {
      if (liveTestCases[i]) {
        if (!badCoverage[i]) {
          if (failTestCases[i]) {
            totalLiveFail++;
          } else
            totalLivePass++;
        }
      }
    }
  }

  private void calculatePassOnStmtAndFailOnStmt() {
    passOnStatement = new int[numberOfStatements];
    failOnStatement = new int[numberOfStatements];

    // first only consider live test cases
    for (int i = 0; i < numberOfTests; i++) {
      // if this isn't a dead test case
      if (!badCoverage[i]) {
        // if this test case is live
        if (liveTestCases[i]) {
          for (int j = 0; j < numberOfStatements; j++) {
            if (coverableStatements[j]) {
              if (coverageMatrix[i][j]) {
                if (failTestCases[i])
                  failOnStatement[j]++;
                else
                  passOnStatement[j]++;

              }
            }
          }
        }
      }
    }
  }

}
