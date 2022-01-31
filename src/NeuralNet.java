import processing.core.PApplet;
import processing.core.PVector;

public class NeuralNet {
    private static final double LEARNING_RATE = 0.1;
    private static final int circleWidth = 20;
    private static final int NUM_LAYERS = 3;
    private static final double MAX_STROKE = 8;
    private int iNodes;
    private int hNodes;
    private int oNodes;

    private Matrix whi;

    private Matrix woh;

    private Matrix errorO;
    private Matrix errorH1;
    private Matrix errorI;


    private double[] nodeErrorH1;
    private double[] nodeErrorI;


    private double[] outputsArr;
    private Matrix outputInputs;
    private Matrix outputs;
    private Matrix hiddenOutputs;
    private Matrix hiddenInputs;
    private Matrix inputs;
    private Matrix inputsOutputs;

    private double totalError;
    private int dist;
    private int xDist;

    public NeuralNet(int inputs, int hiddenNo, int outputNo) {

        iNodes = inputs;
        hNodes = hiddenNo;
        oNodes = outputNo;


        whi = new Matrix(hNodes, iNodes);

        woh = new Matrix(oNodes, hNodes);

        whi.randomize(-1, 1);
        woh.randomize(-1, 1);

        nodeErrorH1 = new double[hNodes];
        nodeErrorI = new double[iNodes];

        dist = 50;
        xDist = 100;
    }

    public double[] feedForward(double[] inputsArr) {

        inputs = Matrix.singleColumnMatrixFromArray(inputsArr);

        hiddenInputs = whi.dot(inputs.addBias());

        hiddenOutputs = hiddenInputs.activate();

        Matrix hiddenBias = hiddenOutputs.addBias();

        outputInputs = woh.dot(hiddenBias);
        outputs = outputInputs.activate();

        outputsArr = outputs.toArray();

        return outputsArr;

    }

    public void backpropagate(double[] desired) {
        double[] errors = new double[outputsArr.length];
        for (int i = 0; i < outputsArr.length; i++) {
            errors[i] = (desired[i] - outputsArr[i]) * (desired[i] - outputsArr[i]);
        }
        errorFunction(errors);
        System.out.println(totalError);

        Matrix wohT = woh.transpose();
        errorO = Matrix.singleColumnMatrixFromArray(errors);

        errorH1 = wohT.dot(errorO);

        Matrix whiT = whi.transpose();
        errorI = whiT.dot(errorH1);

        for (int i = 0; i < hNodes; i++) {
            nodeErrorH1[i] = errorH1.sumRow(i);
        }

        for (int i = 0; i < iNodes; i++) {
            nodeErrorI[i] = errorI.sumRow(i);
        }

    }

    private void errorFunction(double[] errors) {
        double runningSum = 0;
        for (int i = 0; i < errors.length; i++) {
            runningSum += errors[i];
        }
        totalError = runningSum;

    }

    public void gradientDecent() {
        Matrix deltaWho = errorO.multiply(outputs.sigmoidPrime()).dot(hiddenOutputs.transpose());

        woh.add(deltaWho.multiply(-LEARNING_RATE));

        Matrix deltaWhi = errorH1.multiply(inputsOutputs.sigmoidPrime()).dot(inputs.transpose());

        whi = whi.add(deltaWhi.multiply(-LEARNING_RATE));
    }

    public void mutate() {
        whi.mutate(LEARNING_RATE);
        woh.mutate(LEARNING_RATE);
    }

    public NeuralNet clone() {
        NeuralNet clone = new NeuralNet(iNodes, hNodes, oNodes);
        clone.whi = whi.clone();
        clone.woh = woh.clone();

        return clone;
    }

    public void draw(PApplet window) {
        window.fill(255);

        drawWeights(window, iNodes, hNodes, 1, 2, whi);
        drawWeights(window, hNodes, oNodes, 2, 3, woh);

        window.strokeWeight(1);
        window.stroke(0);

        drawLayer(1, iNodes, window);
        drawLayer(2, hNodes, window);
        drawLayer(3, oNodes, window);
    }

    private void drawWeights(PApplet window, int nodes, int nextLayerNodes, int layer, int nextLayer, Matrix layerOn) {
        for (int i = 0; i < nodes; i++) {
            for (int j = 0; j < nextLayerNodes; j++) {
                if (layerOn.getValue(i, j) > 0) {
                    window.stroke(255, 0, 0);
                } else {
                    window.stroke(0, 0, 255);
                }
                window.strokeWeight((float) (Math.abs(layerOn.getValue(i, j)) * MAX_STROKE));
                window.line(layer * xDist + Main.WIDTH, setY(i), nextLayer * xDist + Main.WIDTH, setY(j));
            }
        }
    }

    private void drawLayer(int layer, int numNodes, PApplet window) {
        for (int i = 0; i < numNodes; i++) {
            window.ellipse(layer * xDist + Main.WIDTH, setY(i), circleWidth, circleWidth);
        }
    }

    private float setY(int i) {
        return i * (circleWidth + dist) + circleWidth / 2;
    }

    public NeuralNet crossover(NeuralNet partner) {
        NeuralNet child = new NeuralNet(iNodes, hNodes, oNodes);
        child.whi = whi.crossover(partner.whi);
        child.woh = woh.crossover(partner.woh);
        return child;
    }
}
