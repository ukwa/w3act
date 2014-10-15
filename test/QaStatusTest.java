import static org.junit.Assert.*;

import org.junit.Test;

import uk.bl.Const;
import uk.bl.Const.QAStatusType;


public class QaStatusTest {

    @Test
    public void testQaIssuesToQaStatus() {
    	String noIssuesFound = "No QA issues found (OK to publish)";
    	String qaIssuesFound = "QA issues found";
    	String unknown = "Unknown";
//		((Instance)obj).field_qa_issues = taxonomy.name;
		// No QA issues found (OK to publish), QA issues found, Unknown
		// PASSED_PUBLISH_NO_ACTION_REQUIRED, ISSUE_NOTED, None
		String passedPublishNoActionRequired = findQaStatusByName(noIssuesFound);
		String issuesNoted = findQaStatusByName(qaIssuesFound);
		String none = findQaStatusByName(unknown);
//		
		System.out.println(Const.QAStatusType.PASSED_PUBLISH_NO_ACTION_REQUIRED.name() + " - " + passedPublishNoActionRequired);
		System.out.println(Const.QAStatusType.ISSUE_NOTED.name() + " - " + issuesNoted);
		System.out.println(Const.NONE_VALUE + " - " + none);

		assertEquals(Const.QAStatusType.PASSED_PUBLISH_NO_ACTION_REQUIRED.name(), passedPublishNoActionRequired);
		assertEquals(Const.QAStatusType.ISSUE_NOTED.name(), issuesNoted);
		assertEquals(Const.NONE_VALUE, none);

		assertEquals("Passed, Publish, No Action Required", getQaStatusValue(passedPublishNoActionRequired));
		assertEquals("Issue Noted", getQaStatusValue(issuesNoted));
//		assertEquals("None", getQaStatusValue(none));

		
		assertEquals("Failed, Do Not Publish", getQaStatusValue(Const.QAStatusType.FAILED_DO_NOT_PUBLISH.name()));
		assertEquals("Failed, Pass to Engineer", getQaStatusValue(Const.QAStatusType.FAILED_PASS_TO_ENGINEER.name()));
		assertEquals("Recrawl Requested", getQaStatusValue(Const.QAStatusType.RECRAWL_REQUESTED.name()));

//		PASSED_PUBLISH_NO_ACTION_REQUIRED("Passed, Publish, No Action Required"),
//		FAILED_DO_NOT_PUBLISH("Failed, Do Not Publish"),
//		FAILED_PASS_TO_ENGINEER("Failed, Pass to Engineer"),
//		RECRAWL_REQUESTED("Recrawl Requested"),
//		ISSUE_NOTED("Issue Noted");
    }  
    
    public static String findQaStatusByName(String name) {
//    	Logger.info("findQaStatus name: " + name);
    	String result = name;
////    	Logger.info("findQaStatus taxonomy: " + taxonomy);
//		// No QA issues found (OK to publish), QA issues found, Unknown
//		// PASSED_PUBLISH_NO_ACTION_REQUIRED, ISSUE_NOTED, None
    	if (name.equals("No QA issues found (OK to publish)")) {
    		result = Const.QAStatusType.PASSED_PUBLISH_NO_ACTION_REQUIRED.name();
    	}
    	if (name.equals("QA issues found")) {
    		result = Const.QAStatusType.ISSUE_NOTED.name();
    	}
    	if (name.equals("Unknown")) {
    		result = Const.NONE_VALUE;
    	}
    	return result;
    }
    
	private static String getQaStatusValue(String name) {
		for (QAStatusType qaStatusType :QAStatusType.values()) {
			System.out.println("" + qaStatusType.name() + " - " + qaStatusType.getValue());
			if (qaStatusType.name().equals(name)) {
				return qaStatusType.getValue();
			}
		}
		return null;
	}

}
