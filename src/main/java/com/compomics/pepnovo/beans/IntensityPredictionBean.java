package com.compomics.pepnovo.beans;

import com.compomics.util.experiment.biology.ions.PeptideFragmentIon;

/**
 * This class is a
 */
public class IntensityPredictionBean {
    private int iRank;
    private PeptideFragmentIon iPeptideFragmentIon;
    private double iMZ;
    private double iScore;
    private int iCharge = 1;

    public IntensityPredictionBean(int aRank, PeptideFragmentIon aPeptideFragmentIon, double aMZ, double aScore, int aCharge) {
        iRank = aRank;
        iPeptideFragmentIon = aPeptideFragmentIon;
        iMZ = aMZ;
        iScore = aScore;
        iCharge = aCharge;
    }

    public IntensityPredictionBean() {
    }

    public int getRank() {
        return iRank;
    }

    public void setRank(int aRank) {
        iRank = aRank;
    }

    public PeptideFragmentIon getPeptideFragmentIon() {
        return iPeptideFragmentIon;
    }

    public void setPeptideFragmentIon(PeptideFragmentIon aPeptideFragmentIon) {
        iPeptideFragmentIon = aPeptideFragmentIon;
    }

    public double getMZ() {
        return iMZ;
    }

    public void setMZ(double aMZ) {
        iMZ = aMZ;
    }

    public double getScore() {
        return iScore;
    }

    public void setScore(double aScore) {
        iScore = aScore;
    }

    public int getCharge() {
        return iCharge;
    }

    public void setCharge(int aCharge) {
        iCharge = aCharge;
    }


}
