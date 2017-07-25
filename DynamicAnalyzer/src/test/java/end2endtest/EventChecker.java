package end2endtest;

import analyzer.level2.SecurityMonitorLog;
import analyzer.level2.SecurityMonitoringEvent;

import java.util.EnumSet;

public class EventChecker /* implements SecurityMonitorLog */ {

    public static class UnexpectedEventException extends RuntimeException {
        private final SecurityMonitoringEvent event;
        public UnexpectedEventException(SecurityMonitoringEvent event) {
            super(event.toString());
            this.event = event;
        }

        public SecurityMonitoringEvent getEvent() {
            return event;
        }
    }


    public static class MissingEventException extends RuntimeException {

        private final EnumSet<SecurityMonitoringEvent> missingEvents;

        public MissingEventException(EnumSet <SecurityMonitoringEvent> missingEvents) {
            this.missingEvents = missingEvents;
        }

        public EnumSet<SecurityMonitoringEvent> getMissingEvents() {
            return missingEvents;
        }
    }

    private final EnumSet<SecurityMonitoringEvent> required;
    private final EnumSet<SecurityMonitoringEvent> forbidden;
    private final EnumSet<SecurityMonitoringEvent> observed =
            EnumSet.noneOf(SecurityMonitoringEvent.class);

    private EventChecker(EnumSet<SecurityMonitoringEvent> required,
                         EnumSet <SecurityMonitoringEvent> forbidden) {
        this.required = required;
        this.forbidden = forbidden;
    }

/*
    public static SecurityMonitorLog of(EnumSet<SecurityMonitoringEvent> required,
                                              EnumSet<SecurityMonitoringEvent> forbidden) {
        return new EventChecker(required, forbidden);
    }
    */

    private void observerAndCheckEvent(SecurityMonitoringEvent evt) {
        if (forbidden.contains(evt)) {
            throw new UnexpectedEventException(evt);
        }
        observed.add(evt);
    }


    public void checkGlobalPCLEPutfield(String fieldSignature,
                                        Object globalPc,
                                        Object fieldLevel) {
        observerAndCheckEvent(SecurityMonitoringEvent.CHECK_GLOBAL_PC_LE_PUTFIELD);
    }

    public void endOfMonitoring() {
        EnumSet<SecurityMonitoringEvent> missing = EnumSet.copyOf(required);
        missing.removeAll(observed);
        if (!missing.isEmpty()) {
           throw new MissingEventException(missing);
        }
    }
}
