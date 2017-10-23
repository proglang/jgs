package de.unifreiburg.cs.proglang.jgs.rt;

import analyzer.level2.SecurityMonitorLog;
import analyzer.level2.SimpleSecurityMonitorLog;
import util.logging.L2Logger;

/**
 * The default monitor log for testing. It does nothing except logging.
 */
public class CurrentSecurityMonitorLogInstance {

    private static SecurityMonitorLog
            instance = new SimpleSecurityMonitorLog(L2Logger.getLogger());



    public synchronized static SecurityMonitorLog get() {
        return instance;
    }

    public synchronized static void set(SecurityMonitorLog newInstance) {
        instance = newInstance;
    }
}
