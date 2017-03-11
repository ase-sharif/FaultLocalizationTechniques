package core;

/**
 * Calculates suspiciousness and confidence values according to the Barinel fault localization
 * technique.
 * The usage mode is to create a coverage matrix that specifies which program
 * elements are executed by which test cases. In terms of this implementation, each program element called
 * a statement. coverage matrix is encoded as a two dimensional array where the first dimension is indexed by
 * the test case number and the second dimension is indexed by the statement number.
 */
public class Barinel extends FaultLocalizationTechnique {
  /**
   * Constructor of Barinel class
   *
   * @param coverageMatrix
   */
  public Barinel(boolean[][] coverageMatrix) {
    super(coverageMatrix);
  }

  void calculateSuspiciousnessAndConfidence() {
    this.setSuspiciousness(new double[this.getNumberOfStatements()]);
    this.setConfidence(new double[this.getNumberOfStatements()]);
    for (int i = 0; i < this.getNumberOfStatements(); i++) {
      if (this.getFailOnStatement()[i] == 0 && this.getPassOnStatement()[i] == 0) {
        this.setSuspiciousness(i, -1);
        this.setConfidence(i, -1);
      } else {
        this.setSuspiciousness(i, 1 - 1.0 * this.getPassOnStatement()[i] / (this.getPassOnStatement()[i] + this.getFailOnStatement()[i]));
        this.setConfidence(i, Math.max(this.getPassRatio()[i], this.getFailRatio()[i]));
      }
    }
  }
}
