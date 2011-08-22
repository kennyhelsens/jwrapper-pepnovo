package com.compomics.pepnovo.config;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.URL;

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
        if (Utilities.isUnix()) {
            lExecutable = config.getString("pepnovo.executable.unix");
        } else {
            lExecutable = config.getString("pepnovo.executable.windows");
        }

        return new File(lPath, lExecutable);
    }

    public static String getPepnovoFolder() {
        return config.getString("pepnovo.path");
    }

    public static int getNumberOfTopRankIons() {
        return config.getInt("toprank.count");
    }

    /**
     * @return
     */
    public static File getWorkSpace() {
        if (iWorkSpace == null) {
            iWorkSpace = Files.createTempDir();
        }
        return iWorkSpace;
    }

    static {
        try {
            URL lResource = Resources.getResource("config/pepnovo-jwrapper.properties");
            config = new PropertiesConfiguration(lResource);
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static String getModelName() {
        return config.getString("prediction.model");

    }
}
