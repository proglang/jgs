package utils;

import utils.exceptions.LocalPcCalledException;


/**
 * Controller specifiying whether or not an {@link LocalPcCalledException} should be thrown if certain statements
 * are injected by the {@link analyzer.level1.JimpleInjector}, for example if checkLocalPC is injected.
 */
public enum Controller {
    ACTIVE,             // eg. throws said exception
    PASSIVE             // does not throw said exception
}