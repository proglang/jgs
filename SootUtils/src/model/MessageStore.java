package model;

import java.io.Serializable;
import java.util.*;
import java.util.logging.*;

/**
 * 
 * @author Thomas Vogel
 * @version 0.1
 */
public class MessageStore implements Serializable {
	
	/** */
	private static final long serialVersionUID = -849378849011530590L;
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	private static class FileMessageStore implements Serializable {
		
		/** */
		private static final long serialVersionUID = 2030550093872726376L;
		/** */
		private Map<Long, SourceLineMessageStore> srcLnMessageStore = new HashMap<Long, SourceLineMessageStore>();

		/**
		 * 
		 * @param message
		 */
		private void add(Message message) {
			Long key = Long.valueOf(message.srcLn);
			if (! srcLnMessageStore.containsKey(key)) {
				srcLnMessageStore.put(key, new SourceLineMessageStore());
			}
			srcLnMessageStore.get(key).add(message);
		}
		
		/**
		 * 
		 * @return
		 */
		public List<Message> getAllMessages() {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				result.addAll(srcLnMessageStore.get(key).getAllMessages());
			}
			return result;
		}
		
		/**
		 * 
		 * @param level
		 * @return
		 */
		private List<Message> getAllMessages(Level level) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				result.addAll(srcLnMessageStore.get(key).getAllMessages(level));
			}
			return result;
		}

		/**
		 * 
		 * @param srcLn
		 * @return
		 */
		public List<Message> getAllMessages(long srcLn) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				if (Long.valueOf(srcLn).equals(key)) {
					result.addAll(srcLnMessageStore.get(key).getAllMessages());
				}	
			}
			return result;
		}

		/**
		 * 
		 * @param srcLn
		 * @param level
		 * @return
		 */
		public List<Message> getAllMessages(long srcLn, Level level) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : srcLnMessageStore.keySet()) {
				if (Long.valueOf(srcLn).equals(key)) {
					result.addAll(srcLnMessageStore.get(key).getAllMessages(level));
				}	
			}
			return result;
		}		
	}
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	private static class SourceLineMessageStore implements Serializable {

		/** */
		private static final long serialVersionUID = -7954741135188457L;
		/** */
		private Map<Level, List<Message>> levelMessageStore = new HashMap<Level, List<Message>>();

		/**
		 * 
		 * @param message
		 */
		private void add(Message message) {
			Level key = message.getLevel();
			if (! levelMessageStore.containsKey(key)) {
				levelMessageStore.put(key, new ArrayList<Message>());
			}
			levelMessageStore.get(key).add(message);
		}

		/**
		 * 
		 * @return
		 */
		private List<Message> getAllMessages() {
			List<Message> result = new ArrayList<Message>();
			for (Level key : levelMessageStore.keySet()) {
				result.addAll(levelMessageStore.get(key));
			}
			return result;
		}

		/**
		 * 
		 * @param level
		 * @return
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
	
	/**
	 * 
	 * @author Thomas Vogel
	 * @version 0.1
	 */
	public static class Message implements Serializable {

		/** */
		private static final long serialVersionUID = -1456520028819624896L;
		/** */
		private String message;
		/** */
		private String fileName;
		/** */
		private long srcLn;
		/** */
		private Level level; 
		
		/**
		 * 
		 * @param message
		 * @param fileName
		 * @param srcLn
		 * @param level
		 */
		public Message(String message, String fileName, long srcLn, Level level) {
			this.fileName = fileName;
			this.srcLn = srcLn;
			this.level = level;
			this.message = message;
		}
		
		/**
		 * 
		 * @return
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * 
		 * @return
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * 
		 * @return
		 */
		public long getSrcLn() {
			return srcLn;
		}

		/**
		 * 
		 * @return
		 */
		public Level getLevel() {
			return level;
		}
		
		/**
		 * 
		 * @param obj
		 * @return
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == this) return true;
	        if (obj == null || obj.getClass() != this.getClass()) return false;
	        Message other = (Message) obj;
			return message.equals(other.message) && fileName.equals(other.fileName) && level.equals(other.level) && srcLn == other.srcLn;
		}
		
	}
	
	/** */
	private Map<String, FileMessageStore> fileMessageStore = new HashMap<String, FileMessageStore>();
	
	/**
	 * 
	 * @param msg
	 * @param fileName
	 * @param srcLn
	 * @param level
	 */
	public void addMessage(String msg, String fileName, long srcLn, Level level) {
		if (! fileMessageStore.containsKey(fileName)) {
			fileMessageStore.put(fileName, new FileMessageStore());
		}
		List<Message> existing = getAllMessages(fileName, srcLn, level);
		Message msgToAdd = new Message(msg, fileName, srcLn, level);
		if (! existing.contains(msgToAdd)) {
			fileMessageStore.get(fileName).add(msgToAdd);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Message> getAllMessages() {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			result.addAll(fileMessageStore.get(key).getAllMessages());
		}
		return result;
	}
	
	/**
	 * 
	 * @param level
	 * @return
	 */
	public List<Message> getAllMessages(Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			result.addAll(fileMessageStore.get(key).getAllMessages(level));
		}
		return result;
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public List<Message> getAllMessages(String fileName) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages());
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param level
	 * @return
	 */
	public List<Message> getAllMessages(String fileName, Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(level));
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param srcLn
	 * @return
	 */
	public List<Message> getAllMessages(String fileName, long srcLn) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(srcLn));
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param fileName
	 * @param srcLn
	 * @param level
	 * @return
	 */
	public List<Message> getAllMessages(String fileName, long srcLn, Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(srcLn, level));
			}
		}
		return result;
	}

}
