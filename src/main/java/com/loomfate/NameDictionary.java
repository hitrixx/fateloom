package com.loomfate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Created by hitrix on 23.12.2017.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class NameDictionary {
    private static final Logger LOG = LoggerFactory.getLogger(NameDictionary.class);
    private static Set<String> names;
    private static Set<String> surNames;
    static {
        try {
            loadNames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadNames() throws IOException {
        if (names==null){
            names = load("names.txt");
            LOG.info("surname dictionary has been loaded");
        }
        if (surNames ==null){
            surNames = load("snames.txt");
            LOG.info("surname dictionary has been loaded");
        }
    }

    private static Set<String> load(String filename)  throws IOException {
        Set<String> ret = new HashSet<>();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null){
            ret.add(line.toLowerCase());
        }
        br.close();
        isr.close();
        is.close();
        return ret;
    }

    public static Set<String> getNames(){
        return names;
    }
    public static Set<String> getSurNames(){
        return surNames;
    }
}
