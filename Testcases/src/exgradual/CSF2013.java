package exgradual;

import static security.Definition.*;

public class CSF2013 {
	
	static class Report {
		@FieldSecurity("high")
		private final String content;

		@ParameterSecurity({"low"})
		public Report(String content) {
			super();
			this.content = content;
		}

		@ReturnSecurity("low")
		public String getContent() {
			return content;
		}
		
		@ReturnSecurity("low")
		@ParameterSecurity({"low"})
		public Report append(Report other) {
			return new Report(this.getContent() + other.getContent());
		}
	}
	
	static class Worker {
		@FieldSecurity("low")
		private final String dest;

		@ParameterSecurity({"low"})
		public Worker(String dest) {
			super();
			this.dest = dest;
		}

		@ParameterSecurity({"low"})
		public void send(Report r) {
			System.out.println(String.format("%s --> %s", r.getContent(), dest));
		}
	}
	
	@FieldSecurity("low")
	private static final Report highInfo = new Report("This is a secret!");
	@FieldSecurity("low")
	private static final Worker manager = new Worker("Manager");
	@FieldSecurity("low")
	private static final Worker facebook = new Worker("Facebook");
	
	@FieldSecurity("high")
	private static final Report r1 = new Report("R1");
	@FieldSecurity("low")
	private static final Report r2 = new Report("R2");
	@FieldSecurity("low")
	private static final Report r3 = new Report("R3");
	@FieldSecurity("low")
	private static final Report r4 = new Report("R4");
	
	public static void standardProcessing() {
		manager.send(r1);
		manager.send(r2.append(highInfo));
		facebook.send(r3);
		facebook.send(r4.append(highInfo));
	}

	@ParameterSecurity({"low"})
	public static void main(String[] args) {
		standardProcessing();
	}

}
