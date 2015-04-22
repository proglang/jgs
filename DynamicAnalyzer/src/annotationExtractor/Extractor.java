package annotationExtractor;

import java.util.List;

import soot.SootMethod;
import soot.tagkit.Tag;

public class Extractor {
	
	List<Tag> annotationList;

	public List<Tag> extractAnnotations(SootMethod method) {
		annotationList = method.getTags();
		return annotationList;
	}
	
	
}
