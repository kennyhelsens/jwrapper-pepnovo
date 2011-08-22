package com.compomics.pepnovo.config;

import com.google.common.io.Files;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * This class is a
 */
public class PepnovoConfiguration {
    private static Logger logger = Logger.getLogger(PepnovoConfiguration.class);
    private static PropertiesConfiguration config;
    private static File iWorkSpace = null;

// -------------------------- STATIC METHODS --------------------------



    public static File getExecutable() {
        String lPath = getPepnovoFolder();

        String lExecutable;
        if(Utilities.isUnix()){
            lExecutable = config.getString("pepnovo.executable.unix");
        }else{
            lExecutable = config.getString("pepnovo.executable.windows");
        }

        return new File(lPath, lExecutable);
    }

    public static String getPepnovoFolder() {
        return config.getString("pepnovo.path");
    }

    public static int getNumberOfTopRankIons() {
        return new Integer(config.getString("toprank.count"));
    }

    /**
     *
     * @return
     */
    public static File getWorkSpace(){
        if(iWorkSpace == null){
            iWorkSpace = Files.createTempDir();
        }
        return iWorkSpace;
    }

    static {
        try {
            config = new PropertiesConfiguration("config/pepnovo-jwrapper.properties");
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static String getModelName() {
        return config.getString("prediction.model");

    }
}
