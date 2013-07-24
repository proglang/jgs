package model;

import java.util.*;
import java.util.logging.*;

public class MessageStore {
	
	private static class FileMessageStore {
		
		private Map<Long, SourceLineMessageStore> sourceLineMessageStore = new HashMap<Long, SourceLineMessageStore>();

		private void add(Message message) {
			Long key = Long.valueOf(message.sourceLine);
			if (! sourceLineMessageStore.containsKey(key)) {
				sourceLineMessageStore.put(key, new SourceLineMessageStore());
			}
			sourceLineMessageStore.get(key).add(message);
		}
		
		public List<Message> getAllMessages() {
			List<Message> result = new ArrayList<Message>();
			for (Long key : sourceLineMessageStore.keySet()) {
				result.addAll(sourceLineMessageStore.get(key).getAllMessages());
			}
			return result;
		}
		
		private List<Message> getAllMessages(Level level) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : sourceLineMessageStore.keySet()) {
				result.addAll(sourceLineMessageStore.get(key).getAllMessages(level));
			}
			return result;
		}

		public List<Message> getAllMessages(long sourceLine) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : sourceLineMessageStore.keySet()) {
				if (Long.valueOf(sourceLine).equals(key)) {
					result.addAll(sourceLineMessageStore.get(key).getAllMessages());
				}	
			}
			return result;
		}

		public List<Message> getAllMessages(long sourceLine, Level level) {
			List<Message> result = new ArrayList<Message>();
			for (Long key : sourceLineMessageStore.keySet()) {
				if (Long.valueOf(sourceLine).equals(key)) {
					result.addAll(sourceLineMessageStore.get(key).getAllMessages(level));
				}	
			}
			return result;
		}

		
	}
	
	private static class SourceLineMessageStore {
		private Map<Level, List<Message>> levelMessageStore = new HashMap<Level, List<Message>>();

		private void add(Message message) {
			Level key = message.getLevel();
			if (! levelMessageStore.containsKey(key)) {
				levelMessageStore.put(key, new ArrayList<Message>());
			}
			levelMessageStore.get(key).add(message);
		}

		private List<Message> getAllMessages() {
			List<Message> result = new ArrayList<Message>();
			for (Level key : levelMessageStore.keySet()) {
				result.addAll(levelMessageStore.get(key));
			}
			return result;
		}

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
	
	public static class Message {

		private String message;
		private String fileName;
		private long sourceLine;
		private Level level; 
		
		public Message(String message, String fileName, long sourceLine, Level level) {
			this.fileName = fileName;
			this.sourceLine = sourceLine;
			this.level = level;
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}

		public String getFileName() {
			return fileName;
		}

		public long getSourceLine() {
			return sourceLine;
		}

		public Level getLevel() {
			return level;
		}
		
	}
	
	private Map<String, FileMessageStore> fileMessageStore = new HashMap<String, FileMessageStore>();
	
	public void addMessage(String msg, String fileName, long sourceLine, Level level) {
		if (! fileMessageStore.containsKey(fileName)) {
			fileMessageStore.put(fileName, new FileMessageStore());
		}
		fileMessageStore.get(fileName).add(new Message(msg, fileName, sourceLine, level));
	}
	
	public List<Message> getAllMessages() {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			result.addAll(fileMessageStore.get(key).getAllMessages());
		}
		return result;
	}
	
	public List<Message> getAllMessages(Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			result.addAll(fileMessageStore.get(key).getAllMessages(level));
		}
		return result;
	}
	
	public List<Message> getAllMessages(String fileName) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages());
			}
		}
		return result;
	}
	
	public List<Message> getAllMessages(String fileName, Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(level));
			}
		}
		return result;
	}
	
	public List<Message> getAllMessages(String fileName, long sourceLine) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(sourceLine));
			}
		}
		return result;
	}
	
	public List<Message> getAllMessages(String fileName, long sourceLine, Level level) {
		List<Message> result = new ArrayList<Message>();
		for (String key : fileMessageStore.keySet()) {
			if (fileName.equals(key)) {
				result.addAll(fileMessageStore.get(key).getAllMessages(sourceLine, level));
			}
		}
		return result;
	}

}
