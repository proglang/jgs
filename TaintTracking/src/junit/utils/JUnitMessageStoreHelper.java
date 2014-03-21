package junit.utils;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.model.MessageType;
import junit.model.TestFile;

import main.Main;
import model.MessageStore;
import model.MessageStore.Message;

public class JUnitMessageStoreHelper {

	/**
	 * DOC
	 * 
	 * @param levels
	 * @param calculatedMessages
	 * @param expectedMessages
	 */
	public static void printDifferences(Level[] levels, MessageStore calculatedMessages, MessageStore expectedMessages) {
		List<Level> levelList = Arrays.asList(levels);
		Map<Level, Map<String, Set<Long>>> alreadyPrinted = new HashMap<Level, Map<String, Set<Long>>>();
		for (Level level : levels) {
			alreadyPrinted.put(level, new HashMap<String, Set<Long>>());
		}
		for (Message msg : calculatedMessages.getAllMessages()) {
			if (levelList.contains(msg.getLevel())) {
				if (!alreadyPrinted.get(msg.getLevel()).containsKey(msg.getFileName())) {
					alreadyPrinted.get(msg.getLevel()).put(msg.getFileName(), new HashSet<Long>());
				}
				if (!alreadyPrinted.get(msg.getLevel()).get(msg.getFileName()).contains(new Long(msg.getSrcLn()))) {
					List<Message> calcInLine = calculatedMessages.getAllMessages(msg.getFileName(), msg.getSrcLn(), msg.getLevel());
					List<Message> expeInLine = expectedMessages.getAllMessages(msg.getFileName(), msg.getSrcLn(), msg.getLevel());
					if (calcInLine.size() != expeInLine.size()) {
						alreadyPrinted.get(msg.getLevel()).get(msg.getFileName()).add(new Long(msg.getSrcLn()));
					}
				}
			}
		}
		for (Message msg : expectedMessages.getAllMessages()) {
			if (levelList.contains(msg.getLevel())) {
				if (!alreadyPrinted.get(msg.getLevel()).containsKey(msg.getFileName())) {
					alreadyPrinted.get(msg.getLevel()).put(msg.getFileName(), new HashSet<Long>());
				}
				if (!alreadyPrinted.get(msg.getLevel()).get(msg.getFileName()).contains(new Long(msg.getSrcLn()))) {
					List<Message> calcInLine = calculatedMessages.getAllMessages(msg.getFileName(), msg.getSrcLn(), msg.getLevel());
					List<Message> expeInLine = expectedMessages.getAllMessages(msg.getFileName(), msg.getSrcLn(), msg.getLevel());
					if (calcInLine.size() != expeInLine.size()) {
						alreadyPrinted.get(msg.getLevel()).get(msg.getFileName()).add(new Long(msg.getSrcLn()));
					}
				}
			}
		}
		for (Level level : alreadyPrinted.keySet()) {
			for (String filename : alreadyPrinted.get(level).keySet()) {
				for (Long line : alreadyPrinted.get(level).get(filename)) {
					List<Message> calculated = calculatedMessages.getAllMessages(filename, line.longValue(), level);
					List<Message> expected = expectedMessages.getAllMessages(filename, line.longValue(), level);
					System.err.println(String.format(" # Difference between calculation and expectation in file '%s' at line %d for level '%s':", filename, line.longValue(), level.getName()));
					for (Message message : expected) {
						System.err.println(String.format(" ## Expected: %s", message.getMessage()));
					}
					for (Message message : calculated) {
						System.err.println(String.format(" ## Calculated: %s", message.getMessage()));
					}
				}
			}
		}
	}

	/**
	 * DOC
	 * 
	 * @param expectedMessages
	 * @param calculatedMessages
	 * @param levels
	 */
	private static void printDetails(MessageStore expectedMessages, MessageStore calculatedMessages, Level[] levels) {
		System.out.println("Statistics:");
		for (Level level : levels) {
			Set<String> files = new HashSet<String>();
			files.addAll(expectedMessages.getAllFiles(level));
			files.addAll(calculatedMessages.getAllFiles(level));
			String fileList = "";
			int countExpected = expectedMessages.getAllMessages(level).size();
			int countCalculated = calculatedMessages.getAllMessages(level).size();
			for (String file : files) {
				if (! fileList.equals("")) fileList += ", ";
				fileList += file;
			}
			System.out.println(String.format("   %s: calculated %d / expected %d [%s]", level.getName(), countCalculated, countExpected, fileList));
		}		
		
	}

	/**
	 * DOC
	 * 
	 * @param calculatedMessages
	 * @param expectedMessages
	 * @param testFile
	 * @param level
	 */
	private static void checkMethodStoreEqualityForLevel(MessageStore calculatedMessages, MessageStore expectedMessages,
			TestFile testFile, Level level) {
		Set<Long> affectedLines = new HashSet<Long>();
		for (Message msg : calculatedMessages.getAllMessages(testFile.getClassName(), level)) {
			affectedLines.add(msg.getSrcLn());
		}
		for (Message msg : expectedMessages.getAllMessages(testFile.getClassName(), level)) {
			affectedLines.add(msg.getSrcLn());
		}
		for (Long lineNumber : affectedLines) {
			List<Message> calculated = calculatedMessages.getAllMessages(testFile.getClassName(), lineNumber.longValue(), level);
			List<Message> expected = expectedMessages.getAllMessages(testFile.getClassName(), lineNumber.longValue(), level);
			if (calculated.size() != expected.size()) {
				if (calculated.size() > expected.size()) {
					fail(String
							.format(
									"In file %s at line %d for level %s: More calculated (%d) than expected messages (%d).",
									testFile.getClassName(), lineNumber.longValue(), level.getName(), calculated.size(), expected.size()));
				} else {
					fail(String
							.format(
									"In file %s at line %d for level %s: More expected (%d) than calculated messages (%d).",
									testFile.getClassName(), lineNumber.longValue(), level.getName(), expected.size(), calculated.size()));
				}
			}
		}		
	}

	/**
	 * DOC
	 * 
	 * @param testFile
	 * @param levels
	 */
	public static void checkMethodStoreEquality(TestFile testFile, Level[] levels) {
		try {
			String[] args = JUnitHelper.generateAnalysisArgumentsForAnalyzedFile(testFile
					.getClassClasspath());
			MessageStore calculatedMessages = Main.executeAndReturnMessageStore(args);
			MessageStore expectedMessages = extractMessagesFromFile(testFile.getSourceFile());
			printDifferences(levels, calculatedMessages, expectedMessages);
			int calculatedCountAll = 0;
			int expectedCountAll = 0;
			for (Level level : levels) {
				checkMethodStoreEqualityForLevel(calculatedMessages, expectedMessages, testFile, level);
				int calculatedCount = calculatedMessages.getAllMessages(testFile.getClassName(), level).size();
				calculatedCountAll += calculatedCount;
				int expectedCount = expectedMessages.getAllMessages(testFile.getClassName(), level).size();
				expectedCountAll += expectedCount;
				if (calculatedCount != expectedCount) {
	
					fail(String
							.format(
									"The count of the expected (%d) and the calculated (%d) messages does not match for the level '%s' in the analysis of file '%s'.",
									expectedCount, calculatedCount, level.getName(), testFile.getClassName()));
				}
				for (String other : calculatedMessages.getAllFiles(level)) {
					if (! other.equals(testFile.getClassName())) {
						fail(String.format("The test analysis calculates also messages with level '%s' for file '%s'.", level.getName(), other));
					}
				}
			}
			if (calculatedCountAll != expectedCountAll) {
				fail(String.format("The overall count of the expected (%d) and the calculated (%d) messages does not match in the analysis of file '%s'.",
						expectedCountAll, calculatedCountAll, testFile.getClassName()));
			} else {
				assertTrue(String.format("The analysis of file '%s' calculates the expected messages.", testFile.getClassName()),
						calculatedCountAll == expectedCountAll);
			}
			printDetails(expectedMessages, calculatedMessages, levels);
		}
		catch (IOException e) {
			fail(String.format("Can't execute the test analysis of file '%s'.", testFile.getClassName()));
		}
	}


	/**
	 * DOC
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static MessageStore extractMessagesFromFile(File file) throws IOException {
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
					tryAddingErrorMessage(messageStore, fileName, lineNumber, annotation);
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
	 * DOC
	 * 
	 * @param messageStore
	 * @param fileName
	 * @param lineNumber
	 * @param annotation
	 */
	private static void tryAddingErrorMessage(MessageStore messageStore, String fileName, long lineNumber, String annotation) {
		Matcher matcherType = Pattern.compile("@.*\\(").matcher(annotation);
		Matcher matcherMessage = Pattern.compile("\\(\".*\"\\)").matcher(annotation);
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
	
}
