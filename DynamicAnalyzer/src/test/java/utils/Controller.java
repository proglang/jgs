package utils;

import utils.exceptions.NSUCheckCalledException;


/**
 * Controller specifiying whether or not an {@link NSUCheckCalledException} should be thrown if certain statements
 * are injected by the {@link analyzer.level1.JimpleInjector}, for example if checkLocalPC is injected.
 */
public enum Controller {
    ACTIVE,             // eg. throws said exception
    PASSIVE             // does not throw said exception
}