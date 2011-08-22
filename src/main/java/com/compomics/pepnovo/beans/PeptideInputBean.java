package com.compomics.pepnovo.beans;

/**
 * This class is a
 */
public class PeptideInputBean {
    private String iPeptideSequence;
    private int iCharge;

    public PeptideInputBean() {
    }

    public PeptideInputBean(String aPeptideSequence, int aCharge) {
        iPeptideSequence = aPeptideSequence;
        iCharge = aCharge;
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

    public String getPepnovoInputNotation() {
        return iPeptideSequence + " " + iCharge;
    }
}
