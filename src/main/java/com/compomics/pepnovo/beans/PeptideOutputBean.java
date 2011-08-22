package com.compomics.pepnovo.beans;

import java.util.Set;

/**
 * This class is a
 */
public class PeptideOutputBean {
    private String iPeptideSequence;
    private int iCharge;
    private Set<IntensityPredictionBean> iPredictionBeanSet;

    public PeptideOutputBean(String aPeptideSequence, int aCharge, Set<IntensityPredictionBean> aPredictionBeanSet) {
        iPeptideSequence = aPeptideSequence;
        iCharge = aCharge;
        iPredictionBeanSet = aPredictionBeanSet;
    }

    public PeptideOutputBean() {
    }

    public String getPeptideSequence() {
        return iPeptideSequence;
    }

    public void setPeptideSequence(String aPeptideSequence) {
        iPeptideSequence = aPeptideSequence;
    }

    public int getCharge() {
        return iCharge;
    }

    public void setCharge(int aCharge) {
        iCharge = aCharge;
    }

    public Set<IntensityPredictionBean> getPredictionBeanSet() {
        return iPredictionBeanSet;
    }

    public void setPredictionBeanSet(Set<IntensityPredictionBean> aPredictionBeanSet) {
        iPredictionBeanSet = aPredictionBeanSet;
    }
}
