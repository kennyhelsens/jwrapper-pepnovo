package com.compomics.pepnovo.comparator;

import com.compomics.pepnovo.beans.IntensityPredictionBean;

import java.util.Comparator;

/**
 * This class is a
 */
public class PredictionRankComparator implements Comparator<IntensityPredictionBean> {

    /**
     * {@inheritDoc}
     * @param first
     * @param second
     * @return
     */
    public int compare(IntensityPredictionBean first, IntensityPredictionBean second) {
        if (first.getRank() == second.getRank()) {
            return 0;
        } else if (first.getRank() < second.getRank()) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean equals(Object o) {
        return false;
    }
}
