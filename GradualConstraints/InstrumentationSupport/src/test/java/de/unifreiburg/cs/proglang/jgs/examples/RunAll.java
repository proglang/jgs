package de.unifreiburg.cs.proglang.jgs.examples;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Runn all Tests in this folder at once
 */

@RunWith(Suite.class)
@SuiteClasses({ ExampleTests.class,
                ExampleTests2.class,
                ExampleTests3.class})
public class RunAll {

}
