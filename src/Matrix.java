import processing.core.PApplet;

public class Matrix extends PApplet {

	private int rows, cols;
	private double[][] matrix;
	private float mutationWeight;

	public Matrix(int r, int c) {
		rows = r;
		cols = c;
		matrix = new double[rows][cols];
		mutationWeight=50;
	}

	public Matrix(double[][] m) {
		matrix = m;
		cols = m.length;
		rows = m[0].length;
	}

	public void print() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(matrix[i][j] + "  ");
			}
			System.out.println();
		}

	}

	public Matrix multiply(double n) {

		Matrix newMatrix = new Matrix(rows, cols);

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				newMatrix.matrix[i][j] = matrix[i][j] * n;
			}
		}
		return newMatrix;

	}

	public static Matrix multiply(Matrix m, Matrix n) {
		Matrix newMatrix = new Matrix(m.rows, m.cols);
		if (m.cols != n.cols && m.rows != n.rows) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < m.rows; i++) {
			for (int j = 0; j < m.cols; j++) {
				newMatrix.matrix[i][j] = m.matrix[i][j] * n.matrix[i][j];
			}
		}
		return newMatrix;

	}

	public Matrix dot(Matrix n) {
		Matrix result = new Matrix(rows, n.cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < n.cols; j++) {
				double sum = 0;
				for (int k = 0; k < cols; k++) {
					sum += matrix[i][k] * n.matrix[k][j];
				}
				result.matrix[i][j] = sum;
			}
		}

		return result;
	}

	public void randomize(double lowerBound, double higherBound) {
		double range = higherBound - lowerBound;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = Math.random() * range + lowerBound;
			}
		}
	}

	public Matrix add(double n) {
		Matrix newMatrix = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				newMatrix.matrix[i][j] = matrix[i][j] + n;
			}
		}
		return newMatrix;
	}

	public Matrix add(Matrix n) {
		Matrix newMatrix = new Matrix(rows, cols);
		if (cols != n.cols || rows != n.rows) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				newMatrix.matrix[i][j] = matrix[i][j] + n.matrix[i][j];
			}
		}

		return newMatrix;
	}

	public Matrix multiply(Matrix n) {
		return multiply(this, n);
	}

	public Matrix transpose() {
		Matrix n = new Matrix(cols, rows);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				n.matrix[j][i] = matrix[i][j];
			}
		}
		return n;
	}

	public double getValue(int x, int y){
		return matrix[y][x];
	}

	public static Matrix singleColumnMatrixFromArray(double[] arr) {
		Matrix n = new Matrix(arr.length, 1);
		for (int i = 0; i < arr.length; i++) {
			n.matrix[i][0] = arr[i];
		}
		return n;
	}

	public static Matrix fromArray(double[] arr) {
		return singleColumnMatrixFromArray(arr).transpose();
	}

	public double[] toArray() {
		double[] arr = new double[rows * cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				arr[j + i * cols] = matrix[i][j];
			}
		}
		return arr;
	}

	public Matrix addBias() {
		Matrix n = new Matrix(rows + 1, 1);
		for (int i = 0; i < rows; i++) {
			n.matrix[i][0] = matrix[i][0];
		}
		n.matrix[rows][0] = 0.99;
		return n;
	}

	public Matrix activate() {
		Matrix n = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				n.matrix[i][j] = sigmoid(matrix[i][j]);
			}
		}
		return n;
	}

	public double sigmoid(double x) {
		double y = 1 / (1 + Math.exp(-x));
		return y;
	}

	public Matrix sigmoidPrime() {
		Matrix n = new Matrix(rows, cols);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				n.matrix[i][j] = (matrix[i][j] * (1 - matrix[i][j]));
			}
		}
		return n;
	}

	public Matrix removeBottomLayer() {
		Matrix n = new Matrix(rows - 1, cols);
		for (int i = 0; i < n.rows; i++) {
			for (int j = 0; j < cols; j++) {
				n.matrix[i][j] = matrix[i][j];
			}
		}
		return n;
	}



	public void set(double value, int i, int k) {
		matrix[i][k] = value;
	}

	public double sumRow(int rowNum) {
		double runningSum = 0;
		for (int i = 0; i < matrix[rowNum].length; i++) {
			runningSum += matrix[rowNum][i];
		}
		return runningSum;
	}

	public void toAllValue(double f) {
		for (int i = 0; i < rows; i++) {
			for (int k = 0; k < cols; k++) {
				matrix[i][k] = f;
			}
		}
	}

	public void mutate(double mutationRate) {

		for (int i =0; i<rows; i++) {
			for (int j = 0; j<cols; j++) {

				if (random(1)<mutationRate) {
					matrix[i][j] += randomGaussian()/mutationWeight;
					if (matrix[i][j]>1) {
						matrix[i][j] = 1;
					}
					else if (matrix[i][j] <-1) {
						matrix[i][j] = -1;
					}
				}
			}
		}
	}

	public Matrix crossover(Matrix partner) {
		Matrix child = new Matrix(rows, cols);


		int randC = (int) Math.ceil(random(cols));
		int randR = (int) Math.ceil(random(rows));
		for (int i =0; i<rows; i++) {
			for (int j = 0; j<cols; j++) {

				if ((i< randR)|| (i==randR && j<=randC)) {
					child.matrix[i][j] = matrix[i][j];
				} else {
					child.matrix[i][j] = partner.matrix[i][j];
				}
			}
		}
		return child;
	}

	public Matrix clone() {
		Matrix clone = new Matrix(rows, cols);
		for (int i =0; i<rows; i++) {
			for (int j = 0; j<cols; j++) {
				clone.matrix[i][j] = matrix[i][j];
			}
		}
		return clone;
	}

}
