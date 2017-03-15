package core;

/**
 * Calculates suspiciousness and confidence values according to the DStar fault localization
 * technique.
 * The usage mode is to create a coverage matrix that specifies which program
 * elements are executed by which test cases. In terms of this implementation, each program element called
 * a statement. coverage matrix is encoded as a two dimensional array where the first dimension is indexed by
 * the test case number and the second dimension is indexed by the statement number.
 */
public class DStar extends FaultLocalizationTechnique {
  /**
   * Constructor of DStar class
   *
   * @param coverageMatrix
   */
  DStar(boolean[][] coverageMatrix) {
    super(coverageMatrix);
  }

  void calculateSuspiciousnessAndConfidence() {
    setSuspiciousness(new double[getNumberOfStatements()]);
    setConfidence(new double[getNumberOfStatements()]);
    for (int i = 0; i < getNumberOfStatements(); i++) {
      if (getPassOnStatement()[i] + getTotalLiveFail() - getFailOnStatement()[i] == 0) {
        setSuspiciousness(i, -1);
        setConfidence(i, -1);
      } else {
        setSuspiciousness(i, 1.0 * Math.pow(getFailOnStatement()[i], 2) / (getPassOnStatement()[i] + getTotalLiveFail() - getFailOnStatement()[i]));
        setConfidence(i, Math.max(getFailRatio()[i], getPassRatio()[i]));
      }
    }
  }
}
