package org.apache.jmeter.visualizers.backend;


import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.jmeter.samplers.SampleResult;

import java.util.Collection;

import static org.apache.commons.math3.stat.descriptive.DescriptiveStatistics.INFINITE_WINDOW;

public class BatchStatistic implements Statistic {
    private static int failures;
    private static int successes;
    private static DescriptiveStatistics okStatistic = infiniteDescriptiveStatistics();
    private static DescriptiveStatistics failStatistic = infiniteDescriptiveStatistics();
    private static DescriptiveStatistics allStatistic = infiniteDescriptiveStatistics();

    private BatchStatistic(Collection<SampleResult> batch) {
        for (SampleResult result : batch) {
            if (result.isSuccessful()) {
                successes += result.getSampleCount() - result.getErrorCount();
            } else {
                failures += result.getErrorCount();
            }
            long time = result.getTime();
            allStatistic.addValue(time);
            if (result.isSuccessful()) {
                okStatistic.addValue(time);
            } else {
                failStatistic.addValue(time);
            }
        }
    }


    public static Statistic getStatistic(Collection<SampleResult> batch) {
        return new BatchStatistic(batch);
    }

    public int getTotal() {
        return successes + failures;
    }

    public int getSuccesses() {
        return successes;
    }

    public int getFailures() {
        return failures;
    }

    public double getOkMaxTime() {
        return okStatistic.getMax();
    }

    public double getOkMinTime() {
        return okStatistic.getMin();
    }

    public double getOkMean() {
        return okStatistic.getMean();
    }

    public double getOkPercentile(double percentile) {
        return okStatistic.getPercentile(percentile);
    }


    public double getKoMaxTime() {
        return failStatistic.getMax();
    }


    public double getKoMinTime() {
        return failStatistic.getMin();
    }


    public double getKoMean() {
        return failStatistic.getMean();
    }


    public double getKoPercentile(double percentile) {
        return failStatistic.getPercentile(percentile);
    }


    public double getAllMaxTime() {
        return allStatistic.getMax();
    }


    public double getAllMinTime() {
        return allStatistic.getMin();
    }


    public double getAllMean() {
        return allStatistic.getMean();
    }


    public double getAllPercentile(double percentile) {
        return allStatistic.getPercentile(percentile);
    }

    private static DescriptiveStatistics infiniteDescriptiveStatistics() {
        return new DescriptiveStatistics(INFINITE_WINDOW);
    }
}
