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

  private int numberOfTests; // number of test cases
  private int numberOfStatements;

  public Op2(boolean[][] coverageMatrix) {
    this.coverageMatrix = coverageMatrix;
    numberOfTests = coverageMatrix.length;
    numberOfStatements = coverageMatrix[0].length;

  }

}
