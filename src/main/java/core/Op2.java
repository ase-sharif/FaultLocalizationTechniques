package core;

/**
 * Calculates suspiciousness and confidence values according to the Op2 fault localization
 * technique.
 * The usage mode is to create a coverage matrix that specifies which program
 * elements are executed by which test cases. In terms of this implementation, each program element called
 * a statement. coverage matrix is encoded as a two dimensional array where the first dimension is indexed by
 * the test case number and the second dimension is indexed by the statement number.
 */
public class Op2 extends FaultLocalizationTechnique {


  /**
   * Constructor of Op2 class
   *
   * @param coverageMatrix
   */
  public Op2(boolean[][] coverageMatrix) {
    super(coverageMatrix);
  }

  void calculateSuspiciousnessAndConfidence() {
    setSuspiciousness(new double[getNumberOfStatements()]);
    setConfidence(new double[getNumberOfStatements()]);
    for (int i = 0; i < getNumberOfStatements(); i++) {
      setSuspiciousness(i, getFailOnStatement()[i] - (1.0 * getPassOnStatement()[i] / (getTotalLivePass() + 1)));
      setSuspiciousness(i, Math.max(getPassRatio()[i], getFailRatio()[i]));
    }

  }
}
