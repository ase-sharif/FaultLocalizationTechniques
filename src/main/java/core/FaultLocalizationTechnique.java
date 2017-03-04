package core;

abstract class FaultLocalizationTechnique {
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
  private int[] failOnStatement; // f(s), for every s and considering liveness of test cases -- [statement]

  private double[] passRatio; // (p(s)/total live pass), for every s -- [statement]
  private double[] failRatio; // (f(s)/total live  fail), for every s -- [statement]
  private double[] suspiciousness; // -- [statement]
  private double[] confidence;// -- [statement]


  /**
   * Constructor of fault localization technique class
   */
  public FaultLocalizationTechnique(boolean[][] coverageMatrix) {
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

  private void calculatePassRatioAndFailRatio() {
    passRatio = new double[numberOfStatements];
    failRatio = new double[numberOfStatements];

    for (int i = 0; i < numberOfStatements; i++) {
      if (totalLivePass == 0) {
        passRatio[i] = 0d;
      } else {
        passRatio[i] = (double) passOnStatement[i] / (double) totalLivePass;
      }
      if (totalLiveFail == 0) {
        failRatio[i] = 0d;
      } else {
        failRatio[i] = (double) failOnStatement[i] / (double) totalLiveFail;
      }
    }
  }

  double[] getSuspiciousness() {
    return suspiciousness;
  }

  void setSuspiciousness(double[] suspiciousness) {
    this.suspiciousness = suspiciousness;
  }

  void setSuspiciousness(int i, double value) {
    this.suspiciousness[i] = value;
  }

  double[] getConfidence() {
    return confidence;
  }

  void setConfidence(double[] confidence) {
    this.confidence = confidence;
  }

  void setConfidence(int i, double value) {
    this.confidence[i] = value;
  }

  int getNumberOfStatements() {
    return numberOfStatements;
  }


  int[] getPassOnStatement() {
    return getPassOnStatement();
  }

  int[] getFailOnStatement() {
    return getFailOnStatement();
  }

  int getTotalLivePass() {
    return totalLivePass;
  }

  int getTotalLiveFail() {
    return totalLiveFail;
  }

  double[] getPassRatio() {
    return passRatio;
  }

  double[] getFailRatio() {
    return failRatio;
  }

  abstract void calculateSuspiciousnessAndConfidence();
}
