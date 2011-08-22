package com.compomics.pepnovo;

import com.compomics.pepnovo.beans.PeptideInputBean;
import com.compomics.pepnovo.beans.PeptideOutputBean;
import com.compomics.pepnovo.config.PepnovoConfiguration;
import com.compomics.pepnovo.threads.CommandThread;
import com.google.common.io.Files;
import org.apache.log4j.Logger;
import sun.misc.ConditionLock;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class is a
 */
public class FragmentPredictionTask {
    private static Logger logger = Logger.getLogger(FragmentPredictionTask.class);

    /**
     * This method predicts the fragmentation behaviour of a set of PeptideInputBeans
     *
     * @param aPeptideInputBeans
     * @return
     */
    public static Set<PeptideOutputBean> predictFragmentIons(Set<PeptideInputBean> aPeptideInputBeans) {
        Set<PeptideOutputBean> lPeptideOutputBeans = null;

        try {
            // Ok, first write the given inputbeans to a tmp file.
            File lWorkSpace = PepnovoConfiguration.getWorkSpace();
            File lTmpInputFile = new File(lWorkSpace, "pepnovo-jwrapper-in-" + System.currentTimeMillis() + ".tmp");
            File lTmpOutputFile = new File(lWorkSpace, "pepnovo-jwrapper-out-" + System.currentTimeMillis() + ".tmp");

            // Create the file
            lTmpInputFile.createNewFile();
            lTmpOutputFile.createNewFile();

            // Open a BufferedWriter.
            BufferedWriter lWriter = Files.newWriter(lTmpInputFile, Charset.defaultCharset());
            for (PeptideInputBean lPeptideInputBean : aPeptideInputBeans) {
                lWriter.write(lPeptideInputBean.getPepnovoInputNotation());
                lWriter.write("\n");
            }
            lWriter.flush();
            lWriter.close();

            // Ok, this file must now be passed to PepNovo.
            File lExecutable = PepnovoConfiguration.getExecutable();
            String[] lCommand = makePepnovoCommand(lExecutable, lTmpInputFile);
            CommandThread lCommandThread = new CommandThread(lTmpInputFile.getName(), lCommand, lTmpOutputFile);

            // make a RRunner from this RSource.
            logger.info("submitting pepnovo task");
            Future lFuture = Executors.newSingleThreadExecutor().submit(lCommandThread);
            ConditionLock lConditionLock = new ConditionLock();

            // Keep busy until the Thread has finished.
            synchronized (lConditionLock) {
                while (lFuture.isDone() != true) {
                    lConditionLock.wait(1000);
                    System.out.println(".");
                }
            }

            // Ok, fragmention prediction has finished. Now parse the output file.
            lPeptideOutputBeans = FragmentPredictionParser.parse(lTmpOutputFile);
            // Return the result.

            return lPeptideOutputBeans;
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        return lPeptideOutputBeans;
    }

    /**
     * This convenience method creates an appropriate Command to launch pepnovo.
     *
     * @param aInputFile
     * @return
     */
    private static String[] makePepnovoCommand(File aPath, File aInputFile) throws IOException {
        String lNumberOfTopRankIons = "" + PepnovoConfiguration.getNumberOfTopRankIons();
        String lModelName = PepnovoConfiguration.getModelName();

        // PepNovo_bin -model CID_IT_TRYP -PTMs C+57:M+16  -predict_fragmentation input.txt -num_peaks 25 > output.txt
//        String lCommand = lJoiner.join("\"" + aPath, "-model", lModelName, "-predict_fragmentation", aInputFile.getAbsolutePath(), "-num_peaks", lNumberOfTopRankIons + "\"", ">", aOutputFile.getAbsolutePath());
        String[] lCommand = new String[]{aPath.getCanonicalPath(), "-model", lModelName, "-predict_fragmentation", aInputFile.getAbsolutePath(), "-num_peaks", lNumberOfTopRankIons};
        logger.debug("pepnovo command:\t" + lCommand);

        return lCommand;

    }

}
