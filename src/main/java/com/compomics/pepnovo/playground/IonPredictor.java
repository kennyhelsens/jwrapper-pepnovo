package com.compomics.pepnovo.playground;

import com.compomics.pepnovo.FragmentPredictionTask;
import com.compomics.pepnovo.beans.IntensityPredictionBean;
import com.compomics.pepnovo.beans.PeptideInputBean;
import com.compomics.pepnovo.beans.PeptideOutputBean;
import com.compomics.pepnovo.comparator.PredictionRankComparator;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is a
 */
public class IonPredictor {

    private static Logger logger = Logger.getLogger(IonPredictor.class);


// --------------------------- main() method ---------------------------

    /**
     * This class takes an input file with ids, peptide sequences and a variable number of attributes to each sequence.
     * A BLASTP will be performed to all peptide sequences, and the resulting protein coordinates are then transformed into
     * genome coordinates, and the resulting output files produce a .igv file to visualize the given peptides as a genome track.
     *
     * @param args -input The input file formatted as <ID><SEQUENCE><ATTRIBUTE_1><ATTRIBUTE_2><...><ATTRIBUTE_n>
     *             -species The desired species, the available species are shown in the help (-h)
     *             -output The output .igv file.
     */
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Options lOptions = new Options();
            createOptions(lOptions);

            BasicParser parser = new BasicParser();
            CommandLine line = parser.parse(lOptions, args);

            if (isValidStartup(line) == false) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("TransformPeptides", lOptions);
            } else {
                logger.debug("parameters ok!");
                logger.info("Starting new IonPredictor task");

                String lInput = line.getOptionValue("input");
                BufferedReader lReader = Files.newReader(new File(lInput), Charset.defaultCharset());

                String lInputLine = "";
                HashSet<PeptideInputBean> lPeptideInputBeans = new HashSet<PeptideInputBean>();
                while ((lInputLine = lReader.readLine()) != null) {
                    String[] lSplit = lInputLine.split(" ");
                    PeptideInputBean lPeptideInputBean = new PeptideInputBean(lSplit[0], Integer.parseInt(lSplit[1]));
                    lPeptideInputBeans.add(lPeptideInputBean);
                }

                Set<PeptideOutputBean> lPeptideOutputBeans = FragmentPredictionTask.predictFragmentIons(lPeptideInputBeans);

                String lOutput = line.getOptionValue("output");
                File lOutputFile = new File(lOutput);
                if (lOutputFile.exists()) {
                    lOutputFile.delete();
                }
                lOutputFile.createNewFile();

                BufferedWriter lWriter = Files.newWriter(lOutputFile, Charset.defaultCharset());
                for (PeptideOutputBean lOutputBean : lPeptideOutputBeans) {
                    Joiner join = Joiner.on("\t");
                    lWriter.write(join.join("#", lOutputBean.getPeptideSequence(), lOutputBean.getCharge()));
                    lWriter.newLine();

                    Set<IntensityPredictionBean> lPredictionBeanSet = lOutputBean.getPredictionBeanSet();
                    ArrayList<IntensityPredictionBean> list = new ArrayList<IntensityPredictionBean>(lPredictionBeanSet);
                    Collections.sort(list, new PredictionRankComparator());

                    for (IntensityPredictionBean lPrediction : list) {
                        lWriter.write(join.join(
                                lPrediction.getRank(),
                                lPrediction.getScore(),
                                lPrediction.getPeptideFragmentIon().getIonType(),
                                lPrediction.getPeptideFragmentIon().getNumber()));
                        lWriter.newLine();
                    }

                }
                lWriter.flush();
                lWriter.close();

                logger.info("exiting");
                System.exit(0);

            }
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }


    private static void createOptions(Options aOptions) {
        // Prepare.

        // Set.
        aOptions.addOption("input", true, "The input with peptides. Format: <SEQUENCE> <CHARGE>");
        aOptions.addOption("output", true, "The output file.");
    }

    /**
     * Verifies the command line start parameters.
     *
     * @return
     */
    public static boolean isValidStartup(CommandLine aLine) {
        // No params.
        if (aLine.getOptions().length == 0) {
            return false;
        }

        // Required params.
        if (aLine.getOptionValue("input") == null || aLine.getOptionValue("output") == null) {
            logger.debug("input/output file not given!!");

            return false;
        }

        // input exists?
        String lFile = aLine.getOptionValue("input");
        File lInputFile = new File(lFile);
        if (lInputFile.exists() == false) {
            logger.debug("input file does not exist!!");
            return false;
        }

        // if output given, does it exist? if not, make it!
        String lOutputFileName = aLine.getOptionValue("output");
        if (lOutputFileName != null) {
            File lOutputFile = new File(lOutputFileName);
            if (lOutputFile.exists() == false) {
                try {
                    lOutputFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        // All is fine!
        return true;
    }
}
