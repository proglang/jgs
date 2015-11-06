package utils.parser;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;

import org.apache.commons.cli.*;

public class ArgumentParser {

	private Options options;
	private CommandLineParser lvParser;
	private CommandLine lvCmd;
    HelpFormatter lvFormater;
	
	@SuppressWarnings({ "deprecation", "static-access" })
	public ArgumentParser(String[] args) {
		lvFormater = new HelpFormatter();
		
		options = new Options();
		options.addOption(OptionBuilder
        		.withDescription("Determine output format.")
        		.isRequired()
        		.withValueSeparator('=')
        		.hasArg()
        		.create("f"));
		
		options.addOption(OptionBuilder
        		.withLongOpt("classes")
        		.withDescription("CList of classes to be analyzed.")
        		.isRequired()
        		.withValueSeparator('=')
        		.hasArg()
        		.create());
		
		options.addOption(OptionBuilder
        		.withLongOpt("main_class")
        		.withDescription("Class containing the main method.")
        		.isRequired()
        		.withValueSeparator('=')
        		.hasArg()
        		.create());
		
		
		OptionGroup lvGroup = new OptionGroup();
		lvGroup.addOption(OptionBuilder
						.withLongOpt("lvl_all")
						.withDescription("Level.ALL")
						.create());
		lvGroup.addOption(OptionBuilder
						.withLongOpt("lvl_info")
						.withDescription("Level.INFO")
						.create());
		lvGroup.addOption(OptionBuilder
						.withLongOpt("lvl_severe")
						.withDescription("Level.SEVERE")
						.create());
		options.addOptionGroup(lvGroup);
		
		
		lvParser = new BasicParser();
		lvCmd = null;
		try {
		            lvCmd = lvParser.parse(options, args);
		    } catch (ParseException pvException) {
		    		lvFormater.printHelp("Usage: ", options);
		            System.out.println(pvException.getMessage());
		    }
		lvCmd.getOptionValue("f");
	}
	
	
	public Level getLoggerLevel() {
		Level level = Level.ALL;
		
		if (lvCmd.hasOption("la")) {
			level = Level.ALL;
		} else if (lvCmd.hasOption("li")) {
			level = Level.INFO;
		} else if (lvCmd.hasOption("ls")) {
			level = Level.SEVERE;
		}
		
		return level;
	}
	
	public String[] getSootOptions() {
		// new String[]{"-f","c", "-main-class", "main.testclasses.Simple", "main.testclasses.Simple"}
		LinkedList<String> soot_options = new LinkedList<String>();
		soot_options.add("-f");
		soot_options.add(lvCmd.getOptionValue("f"));
		soot_options.add("-main-class");
		soot_options.add(lvCmd.getOptionValue("main_class"));
		soot_options.add(lvCmd.getOptionValue("classes"));
		String[] result = new String[soot_options.size()];
		soot_options.toArray(result);
		System.out.println(Arrays.deepToString(result));
		return result;
	}
}
