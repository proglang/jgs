package junit;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import logging.SootLoggerLevel;
import main.Main;
import model.MessageStore;
import model.MessageStore.Message;

/**
 * TODO: documentation
 * 
 * @author Thomas Vogel
 * 
 */
public class JUnitTestUtils {

	/**
	 * TODO: documentation
	 * 
	 */
	private final static String[] CLASSPATH = new String[] {
			"-cp",
			".:/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/lib/rt.jar/" };
	/**
	 * TODO: documentation
	 * 
	 */
	private final static String[] PREPEND = new String[] { "-pp" };
	/**
	 * TODO: documentation
	 * 
	 */
	private final static String[] FORMAT = new String[] { "-f", "jimple" };
	/**
	 * TODO: documentation
	 * 
	 */
	private final static String[] NO_BODIES = new String[] { "-no-bodies-for-excluded" };
	/**
	 * TODO: documentation
	 * 
	 */
	private final static String[] LOG_LEVELS = new String[] { "-log-levels",
			"off" };
	/**
	 * TODO: documentation
	 * 
	 */
	private final static String[] FILE_EXPORT = new String[] { "-export-file" };
	/**
	 * TODO: documentation
	 * 
	 */
	private final static String[] LINE_NUMBERS = new String[] { "-keep-line-number" };

	/**
	 * TODO: documentation
	 * 
	 * @author Thomas Vogel
	 * 
	 * @param <T>
	 */
	public static class TestFile<T> {

		/**
		 * TODO: documentation
		 * 
		 * @return
		 */
		public Class<T> getTestFileClass() {
			return cl;
		}

		/**
		 * TODO: documentation
		 * 
		 * @return
		 */
		public String getName() {
			return cl.getName();
		}

		/**
		 * TODO: documentation
		 * 
		 * @return
		 */
		public String getSimpleName() {
			return cl.getSimpleName();
		}

		/**
		 * TODO: documentation
		 * 
		 * @return
		 */
		public String getPath() {
			return path;
		}

		/**
		 * TODO: documentation
		 * 
		 * @return
		 */
		public File getFile() {
			return file;
		}

		/**
		 * TODO: documentation
		 * 
		 */
		private final Class<T> cl;
		/**
		 * TODO: documentation
		 * 
		 */
		private final String path;
		/**
		 * TODO: documentation
		 * 
		 */
		private final File file;

		/**
		 * TODO: documentation
		 * 
		 * @param cl
		 */
		public TestFile(Class<T> cl) {
			this.cl = cl;
			this.path = cl.getName().replace(".", File.separator) + ".java";
			this.file = new File(System.getProperty("user.dir")
					+ File.separator + path);
		}

	}

	/**
	 * TODO: documentation
	 * 
	 * @param className
	 * @return
	 */
	public static String[] generateAnalysisArgumentsForAnalyzedFile(
			String className) {
		return concat(CLASSPATH, PREPEND, FORMAT, NO_BODIES, LOG_LEVELS,
				FILE_EXPORT, LINE_NUMBERS, new String[] { "-src-prec", "java",
						"-d", "./../output", "-W", "-main-class", className },
				new String[] { className });
	}

	/**
	 * TODO: documentation
	 * 
	 * @author Thomas Vogel
	 * 
	 */
	private static enum MessageType {
		EXCEPTION(SootLoggerLevel.EXCEPTION), ERROR(SootLoggerLevel.ERROR), SECURITY(
				SootLoggerLevel.SECURITY), SIDEEFFECT(
				SootLoggerLevel.SIDEEFFECT), SECURITYCHECKER(
				SootLoggerLevel.SECURITYCHECKER), WARNING(
				SootLoggerLevel.WARNING), INFORMATION(
				SootLoggerLevel.INFORMATION);

		/**
		 * TODO: documentation
		 * 
		 */
		private Level level;

		/**
		 * TODO: documentation
		 * 
		 * @param level
		 */
		private MessageType(Level level) {
			this.level = level;
		}

		/**
		 * TODO: documentation
		 * 
		 * @return
		 */
		public Level getLevel() {
			return this.level;
		}
	}

	/**
	 * TODO: documentation
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static MessageStore getMessages(File file) throws IOException {
		MessageStore messageStore = new MessageStore();
		String fileName = file.getName().replace(".java", "");
		List<String> queue = new ArrayList<String>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String lineContent;
		long lineNumber = 0;
		while ((lineContent = reader.readLine()) != null) {
			lineNumber++;
			if (lineContent.trim().matches("(?://.*)")) {
				String comment = lineContent.trim().replaceAll("// ", "");
				if (comment.matches("@.*\\(\".*\"\\)")) {
					queue.add(comment);
				}
			} else {
				for (String annotation : queue) {
					tryAddingErrorMessage(messageStore, fileName, lineNumber,
							annotation);
				}
				queue.clear();
			}
		}
		for (String annotation : queue) {
			tryAddingErrorMessage(messageStore, fileName, 0, annotation);
		}
		reader.close();
		return messageStore;
	}

	/**
	 * TODO: documentation
	 * 
	 * @param messageStore
	 * @param fileName
	 * @param lineNumber
	 * @param annotation
	 */
	private static void tryAddingErrorMessage(MessageStore messageStore,
			String fileName, long lineNumber, String annotation) {
		Matcher matcherType = Pattern.compile("@.*\\(").matcher(annotation);
		Matcher matcherMessage = Pattern.compile("\\(\".*\"\\)").matcher(
				annotation);
		if (matcherType.find()) {
			String type = matcherType.group();
			type = type.substring(1, type.length() - 1);
			String msg = "unknown";
			if (matcherMessage.find()) {
				msg = matcherMessage.group();
				msg = msg.substring(2, msg.length() - 2);
			}
			Level logType = MessageType.valueOf(type.toUpperCase()).getLevel();
			messageStore.addMessage(msg, fileName, lineNumber, logType);
		}
	}

	/**
	 * TODO: documentation
	 * 
	 * @param arrays
	 * @return
	 */
	private static String[] concat(String[]... arrays) {
		int lengh = 0;
		for (String[] array : arrays) {
			lengh += array.length;
		}
		String[] result = new String[lengh];
		int pos = 0;
		for (String[] array : arrays) {
			for (String element : array) {
				result[pos] = element;
				pos++;
			}
		}
		return result;
	}

	/**
	 * TODO: documentation
	 * 
	 * @param testFile
	 * @param levels
	 */
	public static void checkMethodStoreEquality(TestFile<?> testFile,
			Level[] levels) {
		try {
			MessageStore calculatedMessages = Main
					.executeAndReturnMessageStore(JUnitTestUtils
							.generateAnalysisArgumentsForAnalyzedFile(testFile
									.getName()));
			MessageStore expectedMessages = JUnitTestUtils.getMessages(testFile
					.getFile());
			int calculatedCountAll = 0;
			int expectedCountAll = 0;
			if (testFile.getTestFileClass().equals("fake")) {
				for (Message msg : calculatedMessages.getAllMessages(testFile
						.getSimpleName())) {
					System.err.println(msg.getSrcLn() + ": " + msg.getMessage()
							+ "[" + msg.getLevel().getName() + "]");
				}
			}
			for (Level level : levels) {
				checkForEquality(testFile, calculatedMessages,
						expectedMessages, level);
				int calculatedCount = calculatedMessages.getAllMessages(
						testFile.getSimpleName(), level).size();
				calculatedCountAll += calculatedCount;
				int expectedCount = expectedMessages.getAllMessages(
						testFile.getSimpleName(), level).size();
				expectedCountAll += expectedCount;
				if (calculatedCount != expectedCount) {
					fail(String
							.format("The count of the expected (%d) and the calculated (%d) messages does not match for the level '%s' in the failing '%s' analysis.",
									expectedCount, calculatedCount,
									level.getName(), testFile.getName()));
				}
			}
			if (calculatedCountAll != expectedCountAll) {
				fail(String
						.format("The count of the expected (%d) and the calculated (%d) messages does not match in the failing '%s' analysis.",
								expectedCountAll, calculatedCountAll,
								testFile.getName()));
			} else {
				assertTrue(
						String.format(
								"The failing '%s' analysis calculates the expected messages.",
								testFile.getName()),
						calculatedCountAll == expectedCountAll);
			}
		} catch (IOException e) {
			fail(String.format(
					"Can't execute the test of failing '%s' analysis.",
					testFile.getName()));
		}
	}

	/**
	 * TODO: documentation
	 * 
	 * @param testFile
	 * @param calculatedMessages
	 * @param expectedMessages
	 * @param level
	 */
	private static void checkForEquality(TestFile<?> testFile,
			MessageStore calculatedMessages, MessageStore expectedMessages,
			Level level) {
		TreeSet<Long> expectedLines = expectedMessages.getLines(
				testFile.getSimpleName(), level);
		TreeSet<Long> calculatedLines = calculatedMessages.getLines(
				testFile.getSimpleName(), level);
		for (Long expectedLine : expectedLines) {
			List<Message> expected = expectedMessages.getAllMessages(
					testFile.getSimpleName(), expectedLine, level);
			List<Message> calculated = calculatedMessages.getAllMessages(
					testFile.getSimpleName(), expectedLine, level);
			int expectedCount = expected.size();
			int calculatedCount = calculated.size();
			if (expectedCount != calculatedCount) {
				fail(String
						.format("The count of the expected (%d) and the calculated (%d) messages does not match for the level '%s' in the failing '%s' analysis at line %d.",
								expectedCount, calculatedCount,
								level.getName(), testFile.getName(),
								expectedLine.longValue()));
			}
		}
		for (Long calculateLine : calculatedLines) {
			List<Message> expected = expectedMessages.getAllMessages(
					testFile.getSimpleName(), calculateLine, level);
			List<Message> calculated = calculatedMessages.getAllMessages(
					testFile.getSimpleName(), calculateLine, level);
			int expectedCount = expected.size();
			int calculatedCount = calculated.size();
			if (expectedCount != calculatedCount) {
				fail(String
						.format("The count of the calculated (%d) and the expected (%d) messages does not match for the level '%s' in the failing '%s' analysis at line %d.",
								expectedCount, calculatedCount,
								level.getName(), testFile.getName(),
								calculateLine.longValue()));
			}
		}
	}
}
