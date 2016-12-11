package main.scala.com.bahram.util;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Median;

public class ConfidenceIntervalApp {

    public static void main(String args[]) {
        double data2[] = new double[]{2.333333, 44.333333, 58.333333, 66.000000, 1001.333333, 62.333333};
        Median median = new Median();
        double medianValue = median.evaluate(data2);
        System.out.println(medianValue);
        // data we want to evaluate: average height of 30 one year old male and female toddlers
        // interestingly, at this age height is not bimodal yet
        double data[] = new double[]{63.5, 81.3, 88.9, 63.5, 76.2, 67.3, 66.0, 64.8, 74.9, 81.3, 76.2, 72.4, 76.2, 81.3, 71.1, 80.0, 73.7, 74.9, 76.2, 86.4, 73.7, 81.3, 68.6, 71.1, 83.8, 71.1, 68.6, 81.3, 73.7, 74.9};
        // Build summary statistics of the dataset "data"
        SummaryStatistics stats = new SummaryStatistics();
        for (double val : data) {
            stats.addValue(val);
        }

        // Calculate 95% confidence interval
        double ci = calcMeanCI(stats, 0.95);
        System.out.println(String.format("Mean: %f", stats.getMean()));
        double lower = stats.getMean() - ci;
        double upper = stats.getMean() + ci;
        System.out.println(String.format("Confidence Interval 95%%: %f, %f", lower, upper));
    }

    private static double calcMeanCI(SummaryStatistics stats, double level) {
        try {
            // Create T Distribution with N-1 degrees of freedom
            TDistribution tDist = new TDistribution(0);//stats.getN() - 1);
            // Calculate critical value
            double alpha = 1.0 - (1 - level) / 2;
            double critVal = tDist.inverseCumulativeProbability(alpha);
            System.out.println(alpha + " :: " + critVal);
            // Calculate confidence interval
            return critVal * stats.getStandardDeviation() / Math.sqrt(stats.getN());
        } catch (MathIllegalArgumentException e) {
            return Double.NaN;
        }
    }

}