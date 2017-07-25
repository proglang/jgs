package analyzer.level2;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleSecurityMonitorLog implements SecurityMonitorLog {

    private final Logger logger;

    public SimpleSecurityMonitorLog(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void checkGlobalPCLEPutfield(String fieldSignature,
                                        Object globalPc,
                                        Object fieldLevel) {
    }

    @Override
    public void checkLocalPCLELevel(String level) {
    }

    @Override
    public void checkLocalPCLELocal(String signature) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void checkLocalLELevel(String signature, String level) {
    }

    @Override
    public void assignArgumentToLocal(int pos, String signatureOfLocal) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void assignReturnValueToLocal(String signatureOfLocal, Object
            returnLevel) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void returnConstant() {
    }

    @Override
    public void returnLocal(String signature, Object level) {
        logger.log(Level.INFO, "Return Local {0} with level {1}", new Object[]{
                signature, level});
    }

    @Override
    public void setReturnLevelAfterInvokeStmt(String signature) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void joinWithLevelOfCurrentExpressions(String local) {
        throw new RuntimeException("NOT IMPLEMENTED");
    }

    @Override
    public void endOfMonitoring() {
        logger.info("End of monitoring");
    }

}
