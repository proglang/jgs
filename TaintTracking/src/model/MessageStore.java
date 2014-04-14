package model;

import static java.util.Collections.sort;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;

import logging.AnalysisLogLevel;
import logging.Settings;

/**
 * <h1>Store of logged messages</h1>
 * 
 * The {@link MessageStore} is an serializable store for logged messages ({@link Message}). Therefore, a message store provides methods for
 * adding new logged messages as well as method which return messages of specific levels, source line numbers and/or file names. Also a
 * message store provides separate method for storing and returning configuration information.
 * 
 * <h2>Internal behaviour</h2>
 * 
 * For efficiency reasons, these messages are stored in a map that maps for a specific file name to a {@link FileMessageStore} that stores
 * messages with this specific file name. Each such a {@link FileMessageStore} contains a map that maps a specific source line number to a
 * {@link SrcLnMessageStore} that stores messages with this source line number. Each such a {@link SrcLnMessageStore} contains a map that
 * maps a specific {@link AnalysisLogLevel} to a list of messages which have this specific level.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.2
 * @see Message
 * @see FileMessageStore
 * @see SrcLnMessageStore
 */
public class MessageStore {

	/**
	 * <h1>Logged message</h1>
	 * 
	 * The {@link Message} implements the {@link Comparable} and the {@link Serializable} interfaces to provide the possibility of comparing
	 * and of serializing message objects. A message represents a information which is logged by a {@link SootLogger}. Therefore a message
	 * contains the content of the information, the log level of the information as well as the reference to the file name of the file in
	 * which the information was logged and the reference to the source line number at which the information was logged. Also, the message
	 * provides the possibility to add a Throwable which is the reason of this logged message. A message can store multiple message because it
	 * is conceivable that in a specific file at the same source line a message will be logged with the same level and the same message caused
	 * by different throwables.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see AnalysisLogLevel
	 * @see MessageStore
	 * @see FileMessageStore
	 * @see SrcLnMessageStore
	 */
	public static class Message implements Comparable<Message> {

		/** Name of the file in which the logged message was generated. */
		private final String fileName;
		/** The {@link AnalysisLogLevel} of the logged message. */
		private final Level level;
		/** The logged message. */
		private final String message;
		/** Source line number at which the logged message was generated. */
		private final long srcLn;

		/**
		 * Constructor of a logged message, which is generated in the file with the given file name at the given source line number. The given
		 * text represents the content of the message which has also the given {@link AnalysisLogLevel}.
		 * 
		 * @param message
		 *          The content of the logged message.
		 * @param fileName
		 *          The file name of the file in which the logged message was generated.
		 * @param srcLn
		 *          The source line number at which the logged message was generated.
		 * @param level
		 *          The level of the logged message.
		 */
		protected Message(String message, String fileName, long srcLn, Level level) {
			this.fileName = fileName;
			this.srcLn = srcLn;
			this.level = level;
			this.message = message;
		}

		/**
		 * Compares this message with the given message for order. Returns a negative integer, zero, or a positive integer as this message is
		 * less than, equal to, or greater than the given message. The main aspect here is the file name, followed by the source line number,
		 * and finally the level.
		 * 
		 * @param msg
		 *          The message to be compared.
		 * @return a negative integer, zero, or a positive integer as this message is less than, equal to, or greater than the given message.
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Message msg) {
			if (this.getFileName().equals(msg.getFileName())) {
				if (this.getSrcLn() == msg.getSrcLn()) {
					return this.level.getLocalizedName().compareTo(msg.getLevel().getLocalizedName());
				} else if (this.getSrcLn() < msg.getSrcLn()) {
					return Integer.MIN_VALUE;
				} else {
					return Integer.MAX_VALUE;
				}
			} else {
				return this.getFileName().compareTo(msg.getFileName());
			}
		}

		/**
		 * Indicates whether some other object is "equal to" this one. <br/>
		 * An other object is only equals to this if the other object is this object or if the messages, the file names, the source line numbers
		 * and the levels are equals. Note that the list of throwables may differ.
		 * 
		 * @param obj
		 *          Object for which should be checked whether it is equals to this message.
		 * @return {@code true} if the given object is a message and if the message, the file name, the source line number and the level of this
		 *         message and the given message are equals, otherwise {@code false}.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == this) return true;
			if (obj == null || obj.getClass() != this.getClass()) return false;
			Message other = (Message) obj;
			return message.equals(other.message) && fileName.equals(other.fileName) && level.equals(other.level) && srcLn == other.srcLn;
		}

		/**
		 * Method returns the file name of the file in which this logged method was generated.
		 * 
		 * @return The file name of this logged message.
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * Method returns the {@link AnalysisLogLevel} of this logged method.
		 * 
		 * @return The level of this logged message.
		 */
		public Level getLevel() {
			return level;
		}

		/**
		 * Method returns the text of this logged message.
		 * 
		 * @return The text of this logged message.
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * Method returns the source line number at which this logged message was generated.
		 * 
		 * @return The source line number of this message.
		 */
		public long getSrcLn() {
			return srcLn;
		}

	}

	/**
	 * <h1>Internal file message store</h1>
	 * 
	 * The {@link FileMessageStore} is an internal serializable store which stores messages of the same file name. For efficiency reasons,
	 * these messages are stored in a map that maps a specific source line number to a {@link SrcLnMessageStore} that stores messages with
	 * this source line number. Note, that the {@link FileMessageStore} is used in the {@link MessageStore} which contains a map that maps for
	 * a specific file name to a {@link FileMessageStore} that contains only messages which have the specific file name.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see MessageStore
	 * @see SrcLnMessageStore
	 */
	private static class FileMessageStore {

		/** Map that stores a {@link SrcLnMessageStore} for each source line number. */
		private Map<Long, SrcLnMessageStore> srcLnMessageStore = new HashMap<Long, SrcLnMessageStore>();

		/**
		 * Adds the given logged message to the map, which contains a {@link SrcLnMessageStore} for each source line number.
		 * 
		 * @param message
		 *          Message that should be stored in the internal store.
		 */
		private void add(Message message) {
			Long key = Long.valueOf(message.srcLn);
			if (!srcLnMessageStore.containsKey(key)) {
				srcLnMessageStore.put(key, new SrcLnMessageStore());
			}
			srcLnMessageStore.get(key).add(message);
		}

		/**
		 * Returns all messages of this {@link FileMessageStore}.
		 * 
		 * @return Empty list if the {@link FileMessageStore} does not contain any message, otherwise a sorted list that contains all messages
		 *         of the {@link FileMessageStore}.
		 */
		private List<Message> getAllMessages() {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				result.addAll(srcLnMessageStore.get(key).getAllMessages());
			}
			sort(result);
			return result;
		}

		/**
		 * Returns all messages of this {@link FileMessageStore} which have the given level. This value should apply to all the messages of the
		 * resulting list.
		 * 
		 * @param level
		 *          The {@link AnalysisLogLevel} which the resulting messages should have.
		 * @return Empty list if none of the messages of this {@link FileMessageStore} have the given value, otherwise a sorted list that
		 *         contains those messages which have the given level.
		 */
		private List<Message> getAllMessages(Level level) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				result.addAll(srcLnMessageStore.get(key).getAllMessages(level));
			}
			sort(result);
			return result;
		}

		/**
		 * Returns all messages contained by this {@link FileMessageStore} which have the given source line number. This value should apply to
		 * all the messages of the resulting list.
		 * 
		 * @param srcLn
		 *          The source line number which the resulting messages should have.
		 * @return Empty list if the {@link FileMessageStore} does not contain a message with the given value, otherwise a sorted list that
		 *         contains those messages which have the given source line number.
		 */
		private List<Message> getAllMessages(long srcLn) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				if (Long.valueOf(srcLn).equals(key)) {
					result.addAll(srcLnMessageStore.get(key).getAllMessages());
				}
			}
			sort(result);
			return result;
		}

		/**
		 * Returns all messages contained by this {@link FileMessageStore} which have the given source line number as well as the given level.
		 * Both values should apply to all the messages of the resulting list.
		 * 
		 * @param srcLn
		 *          The source line number which the resulting messages should have.
		 * @param level
		 *          The {@link AnalysisLogLevel} which the resulting messages should have.
		 * @return Empty list if the {@link FileMessageStore} does not contain a message with the given values, otherwise a sorted list that
		 *         contains those messages which have the given source line number and also the given level.
		 */
		private List<Message> getAllMessages(long srcLn, Level level) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				if (Long.valueOf(srcLn).equals(key)) {
					result.addAll(srcLnMessageStore.get(key).getAllMessages(level));
				}
			}
			sort(result);
			return result;
		}
	}

	/**
	 * <h1>Internal source line message store</h1>
	 * 
	 * The {@link SrcLnMessageStore} is an internal serializable store which stores messages of the same source line. For efficiency reasons,
	 * these messages are stored in a map that maps a specific {@link AnalysisLogLevel} to a list of messages which have this specific level.
	 * Note, that the {@link SrcLnMessageStore} is used in the internal {@link FileMessageStore} which contains a map that maps for a specific
	 * source line number inside of a specific file to a {@link SrcLnMessageStore} that contains only messages which have the specific file
	 * name and the specific soruce line number.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 * @see MessageStore
	 * @see FileMessageStore
	 */
	private static class SrcLnMessageStore {

		/** Map that stores a list of messages for each {@link AnalysisLogLevel}. */
		private Map<Level, List<Message>> levelMessageStore = new HashMap<Level, List<Message>>();

		/**
		 * Adds the given logged message to the internal store, which contains a list of messages for each {@link AnalysisLogLevel}.
		 * 
		 * @param message
		 *          Message that should be stored in the internal store.
		 */
		private void add(Message message) {
			Level key = message.getLevel();
			if (!levelMessageStore.containsKey(key)) {
				levelMessageStore.put(key, new ArrayList<Message>());
			}
			levelMessageStore.get(key).add(message);
		}

		/**
		 * Returns all messages of this {@link SrcLnMessageStore}.
		 * 
		 * @return Empty list if the {@link SrcLnMessageStore} does not contain any message, otherwise a sorted list that contains all messages
		 *         of the {@link SrcLnMessageStore}.
		 */
		private List<Message> getAllMessages() {
			List<Message> result = new ArrayList<Message>();
			for (Level key : levelMessageStore.keySet()) {
				result.addAll(levelMessageStore.get(key));
			}
			sort(result);
			return result;
		}

		/**
		 * Returns all messages of this {@link SrcLnMessageStore} which have the given level. This value should apply to all the messages of the
		 * resulting list.
		 * 
		 * @param level
		 *          The {@link AnalysisLogLevel} which the resulting messages should have.
		 * @return Empty list if none of the messages of this {@link SrcLnMessageStore} have the given value, otherwise a sorted list that
		 *         contains those messages which have the given level.
		 */
		private List<Message> getAllMessages(Level level) {
			List<Message> result = new ArrayList<Message>();
			for (Level key : levelMessageStore.keySet()) {
				if (level.equals(key)) {
					result.addAll(levelMessageStore.get(key));
				}
			}
			return result;
		}
	}

	/** List of {@link Settings} which stores the logged setting information. */
	private final List<Settings> configurations = new ArrayList<Settings>();
	/** Map that stores a {@link FileMessageStore} for each file name String. */
	private Map<String, FileMessageStore> fileMessageStore = new HashMap<String, FileMessageStore>();

	/**
	 * Adds a logged configuration to the message store. This {@link Settings} object will be stored in the list
	 * {@link MessageStore#configurations}.
	 * 
	 * @param settings
	 *          Settings which should be added to the message store.
	 */
	public void addConfiguration(Settings settings) {
		this.configurations.add(settings);
	}

	/**
	 * Creates a logged message with the given file name, the given source line number, the given level and the given message and adds this
	 * created message to the message store, if this message is not already stored in the message store.
	 * 
	 * @param msg
	 *          The content of the logged message.
	 * @param fileName
	 *          The file name of the file in which the logged message was generated.
	 * @param srcLn
	 *          The source line number at which the logged message was generated.
	 * @param level
	 *          The level of the logged message.
	 * @see Message
	 */
	public void addMessage(String msg, String fileName, long srcLn, Level level) {
		if (!fileMessageStore.containsKey(fileName)) {
			fileMessageStore.put(fileName, new FileMessageStore());
		}
		List<Message> existing = getAllMessages(fileName, srcLn, level);
		Message temp = new Message(msg, fileName, srcLn, level);
		if (!existing.contains(temp)) {
			fileMessageStore.get(fileName).add(temp);
		}
	}

	/**
	 * Returns all logged configurations in a list of {@link Settings}.
	 * 
	 * @return All logged configurations.
	 */
	public List<Settings> getAllConfigurations() {
		return this.configurations;
	}

	/**
	 * DOC
	 * 
	 * @param level
	 * @return
	 */
	public Set<String> getAllFiles(Level level) {
		Set<String> files = new HashSet<String>();
		for (Message msg : this.getAllMessages(level)) {
			files.add(msg.getFileName());
		}
		return files;
	}

	/**
	 * Returns all logged messages.
	 * 
	 * @return Empty list if no message has been logged, otherwise a sorted list that contains all logged messages.
	 * @see FileMessageStore#getAllMessages()
	 */
	public List<Message> getAllMessages() {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			result.addAll(fileMessageStore.get(key).getAllMessages());
		}
		sort(result);
		return result;
	}

	/**
	 * Returns all logged messages which have the given level. This value should apply to all the messages of the resulting list.
	 * 
	 * @param level
	 *          The {@link AnalysisLogLevel} which the resulting messages should have.
	 * @return Empty list if none of the logged messages have the given value, otherwise a sorted list that contains those messages which have
	 *         the given level.
	 * @see FileMessageStore#getAllMessages(Level)
	 */
	public List<Message> getAllMessages(Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			result.addAll(fileMessageStore.get(key).getAllMessages(level));
		}
		sort(result);
		return result;
	}

	/**
	 * Returns all logged messages which have the given file name. This value should apply to all the messages of the resulting list.
	 * 
	 * @param fileName
	 *          The name of the file which the resulting messages should have.
	 * @return Empty list if none of the logged messages have the given value, otherwise a sorted list that contains those messages which have
	 *         the given file name.
	 * @see FileMessageStore#getAllMessages()
	 */
	public List<Message> getAllMessages(String fileName) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages());
			}
		}
		sort(result);
		return result;
	}

	/**
	 * Returns all logged messages which have the given file name as well as the given level. Both values should apply to all the messages of
	 * the resulting list.
	 * 
	 * @param fileName
	 *          The name of the file which the resulting messages should have.
	 * @param level
	 *          The {@link AnalysisLogLevel} which the resulting messages should have.
	 * @return Empty list if none of the logged messages have the given values, otherwise a sorted list that contains those messages which
	 *         have the given file name and also the given level.
	 * @see FileMessageStore#getAllMessages(Level)
	 */
	public List<Message> getAllMessages(String fileName, Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(level));
			}
		}
		sort(result);
		return result;
	}

	/**
	 * Returns all logged messages which have the given file name as well as the given source line number. Both values should apply to all the
	 * messages of the resulting list.
	 * 
	 * @param fileName
	 *          The name of the file which the resulting messages should have.
	 * @param srcLn
	 *          The source line number which the resulting messages should have.
	 * @return Empty list if none of the logged messages have the given values, otherwise a sorted list that contains those messages which
	 *         have the given file name and also the given source line number.
	 * @see FileMessageStore#getAllMessages(long)
	 */
	public List<Message> getAllMessages(String fileName, long srcLn) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(srcLn));
			}
		}
		sort(result);
		return result;
	}

	/**
	 * Returns all logged messages which have the given file name, the given source line number as well as the given level. All three values
	 * should apply to all the messages of the resulting list.
	 * 
	 * @param fileName
	 *          The name of the file which the resulting messages should have.
	 * @param srcLn
	 *          The source line number which the resulting messages should have.
	 * @param level
	 *          The {@link AnalysisLogLevel} which the resulting messages should have.
	 * @return Empty list if none of the logged messages have the given values, otherwise a sorted list that contains those messages which
	 *         have the given file name, the given source line number and also the given level.
	 * @see FileMessageStore#getAllMessages(long, Level)
	 */
	public List<Message> getAllMessages(String fileName, long srcLn, Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(srcLn, level));
			}
		}
		sort(result);
		return result;
	}

	/**
	 * DOC
	 * 
	 * @param fileName
	 * @param level
	 * @return
	 */
	public TreeSet<Long> getLines(String fileName, Level level) {
		TreeSet<Long> result = new TreeSet<Long>();
		if (fileMessageStore.containsKey(fileName)) {
			FileMessageStore fms = fileMessageStore.get(fileName);
			List<Message> list = fms.getAllMessages(level);
			for (Message message : list) {
				result.add(message.getSrcLn());
			}
		}
		return result;
	}
}
