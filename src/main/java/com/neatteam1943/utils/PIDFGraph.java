/**
 * Provided by NeatTeam #1943's software team
 * 
 * github : https://github.com/NeatTeam1943
 * website: https://neatteam1943.com
 */

package com.neatteam1943.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * PIDGraph offers to visually present your PID subsystem's state over time
 * 
 * @author Guy Avital <neatteamsoftware @ gmail.com>
 * @version 1.0.0
 */
public class PIDFGraph {

    /**
     * Your system's kP term
     */
    private static double p;

    /**
     * Your system's kI term
     */
    private static double i;

    /**
     * Your system's kD term
     */
    private static double d;

    /**
     * Your system's kF term
     */
    private static double f;

    /**
     * Your system's setpoint
     */
    private static double setpoint;

    /**
     * Your system's tolerance
     */
    private static double tolerance;

    /**
     * Width of the graph final PNG
     */
    private static int graphWidth;

    /**
     * Height of the graph final PNG
     */
    private static int graphHeight;

    /**
     * Leap in time
     * <p>
     * Amount of time between every point on the graph. Defaults to 0.02 to match
     * cycle commonly found on frc robots
     * </p>
     */
    private static double leap;

    /**
     * Current point in timeline
     */
    private static double currentTime;

    /**
     * Values over time dataset
     * <p>
     * Values can be everything that represents your system's state (i.e. Gyro
     * angle, Position, Speed...)
     * </p>
     */
    private static XYSeries series;

    /**
     * Initializes class properties and creates the name of the function
     */
    private static void setup() {
        String name = "P=" + p + " , " + "I=" + i + " , " + "D=" + d + " , " + "F=" + f;
        series = new XYSeries(name);
        currentTime = 0.0;
        leap = 0.02;
    }

    /**
     * Configure resolution for the graph final PNG
     * 
     * @param width  Corresponds with {@link graphWidth}
     * @param height Corresponds with {@link graphHeight}
     */
    public static void configResolution(int width, int height) {
        graphWidth = width;
        graphHeight = height;
    }

    /**
     * Configure kP, kI and kD terms, excluding kF
     * 
     * @param kP Corresponds with {@link p}
     * @param kI Corresponds with {@link i}
     * @param kD Corresponds with {@link d}
     */
    public static void config(double kP, double kI, double kD) {
        p = kP;
        i = kI;
        d = kD;
        f = 0.0;

        setup();
        configResolution(700, 500);
    }

    /**
     * Extends {@link config} with a kF term
     * 
     * @param kP Corresponds with {@link p}
     * @param kI Corresponds with {@link i}
     * @param kD Corresponds with {@link d}
     * @param kF Corresponds with {@link f}
     */
    public static void config(double kP, double kI, double kD, double kF) {
        config(kP, kI, kD);
        f = kF;

        setup();
    }

    /**
     * Configure the target your system aspires to
     * 
     * @param setpoint  Corresponds with {@link setpoint}
     * @param tolerance Corresponds with {@link tolerance}
     */
    public static void configTarget(double setpoint, double tolerance) {
        PIDFGraph.setpoint = setpoint;
        PIDFGraph.tolerance = tolerance;
    }

    /**
     * Get kP term
     * 
     * @return {@link p}
     */
    public static double getP() {
        return p;
    }

    /**
     * Set kP term
     * 
     * @param value New value for {@link p}
     */
    public static void setP(double value) {
        p = value;
    }

    /**
     * Get kI term
     * 
     * @return {@link i}
     */
    public static double getI() {
        return i;
    }

    /**
     * Set kI term
     * 
     * @param value New value for {@link i}
     */
    public static void setI(double value) {
        i = value;
    }

    /**
     * Get kD term
     * 
     * @return {@link d}
     */
    public static double getD() {
        return d;
    }

    /**
     * Set kD term
     * 
     * @param value New value for {@link d}
     */
    public static void setD(double value) {
        d = value;
    }

    /**
     * Get kF term
     * 
     * @return {@link f}
     */
    public static double getF() {
        return f;
    }

    /**
     * Set kF term
     * 
     * @param value New value for {@link f}
     */
    public static void setF(double value) {
        f = value;
    }

    /**
     * Get setpoint
     * 
     * @return {@link setpoint}
     */
    public static double getSetpoint() {
        return setpoint;
    }

    /**
     * Set setpoint
     * 
     * @param value New value for {@link setpoint}
     */
    public static void setSetpoint(double value) {
        setpoint = value;
    }

    /**
     * Get tolerance
     * 
     * @return {@link tolerance}
     */
    public static double getTolerance() {
        return tolerance;
    }

    /**
     * Set tolerance
     * 
     * @param value New value for {@link tolerance}
     */
    public static void setTolerance(double value) {
        tolerance = value;
    }

    /**
     * Get leap
     * 
     * @return {@link leap}
     */
    public static double getLeap() {
        return leap;
    }

    /**
     * Set leap
     * 
     * @param value New value for {@link leap}
     */
    public static void setLeap(double value) {
        leap = value;
    }

    /**
     * Add a new value regardless of current stream
     * 
     * @param value System state
     * @param time  Current time (By your consideration)
     */
    public static void add(double value, double time) {
        series.add(value, time);
    }

    /**
     * Add new value for the dataset
     * 
     * @param value Current system state
     */
    public static void add(double value) {
        series.add(currentTime, value);
        currentTime += leap;
    }

    /**
     * Use JFreeChart to create a graph using the dataset
     * 
     * @return Newly created graph
     */
    private static JFreeChart createGraph() {
        return ChartFactory.createXYLineChart("PIDFGraph", "Time", "Value", null, PlotOrientation.VERTICAL, true, true,
                false);
    }

    /**
     * Configure the graph (The visual side)
     * 
     * @return Completely configured graph
     */
    private static JFreeChart getGraph() {
        XYDataset values = new XYSeriesCollection(series); // values over time dataset

        ValueMarker setpointMarker = new ValueMarker(setpoint); // setpoint marker line
        ValueMarker minToleranceMarker = new ValueMarker(setpoint - tolerance); // minimum tolerance marker line
        ValueMarker maxToleranceMarker = new ValueMarker(setpoint + tolerance); // maximum tolerance marker line

        BasicStroke dashedStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
                new float[] { 10.0f, 15.0f }, 0.0f); // dashed stroke for the tolerance markers

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false); // line renderer (used to change paints)
        renderer.setSeriesPaint(0, new Color(0x1F77B4)); // state function paint
        renderer.setSeriesStroke(0, new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // state function stroke

        // set paints for the marker lines
        setpointMarker.setPaint(Color.BLACK);
        minToleranceMarker.setPaint(Color.RED);
        maxToleranceMarker.setPaint(Color.RED);

        // set strokes for the marker lines
        setpointMarker.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
                new float[] { 30.0f, 10.0f }, 0.0f));
        minToleranceMarker.setStroke(dashedStroke);
        maxToleranceMarker.setStroke(dashedStroke);

        // create graph and enable antialiasing
        JFreeChart graph = createGraph();
        graph.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));

        XYPlot plot = graph.getXYPlot(); // function plot tool

        // set time range from 0.0 to current time
        NumberAxis domain = (NumberAxis) plot.getDomainAxis();
        domain.setRange(0.0, currentTime);

        // plot the graph
        plot.setRenderer(renderer);
        plot.setDataset(0, values);

        plot.addRangeMarker(setpointMarker);
        plot.addRangeMarker(minToleranceMarker);
        plot.addRangeMarker(maxToleranceMarker);

        return graph;
    }

    /**
     * Saves a graph PNG and a datasheet
     * 
     * @param path Directory to use to save the files
     * @throws IOException If occurred an error while handling files
     */
    public static void save(String path) throws IOException {
        JFreeChart graph = getGraph();

        File graphImg = new File(path + "/graph.png");
        FileWriter dataTxt = new FileWriter(path + "/data.txt");

        // write settings in the beginning of the datasheet
        dataTxt.write(p + " , " + i + " , " + d + " , " + f + "\n");
        dataTxt.write(setpoint + " , " + tolerance + "\n\n");

        // write all the points in time (format: value , time)
        int j = 0;
        double[][] arr = series.toArray();
        for (double i : arr[1]) {
            dataTxt.write(i + " , " + arr[0][j] + "\n");
            j++;
        }

        ChartUtils.saveChartAsPNG(graphImg, graph, graphWidth, graphHeight);
        dataTxt.close();
    }
}
