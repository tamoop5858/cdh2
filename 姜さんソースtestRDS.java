package com.amazonaws.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudwatch.model.CloudWatchException;
import software.amazon.awssdk.services.cloudwatchevents.CloudWatchEventsClient;
import software.amazon.awssdk.services.cloudwatchevents.model.PutRuleRequest;
import software.amazon.awssdk.services.cloudwatchevents.model.PutRuleResponse;
import software.amazon.awssdk.services.cloudwatchevents.model.PutTargetsRequest;
import software.amazon.awssdk.services.cloudwatchevents.model.PutTargetsResponse;
import software.amazon.awssdk.services.cloudwatchevents.model.RuleState;
import software.amazon.awssdk.services.cloudwatchevents.model.Target;


public class testRDS implements RequestHandler<Map<String,Object>,Map<String,Object>>{

	  @Override
	  public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
	    Map<String, Object> output = new HashMap<>();
	    output = input;

//		AWSCredentials credentials = new BasicAWSCredentials(
//				"AKIAQ3CGY5MR3V7LSWFQ",
//				"++HC2JZloemH/HgJAUIGc+pgFE3tt2gTw5aTxDJS");
	    String ruleName = "newRule";
	    String roleArn = "bbbbb";
	    String functionArn = "ccc";
	    String targetId = "dddd";

	    CloudWatchEventsClient cwe =
	    		CloudWatchEventsClient.builder() .region(Region.of("ap-northeast-1")).build();

	    putCWRule(cwe, ruleName, roleArn);
	    putCWTargets(cwe, ruleName, functionArn, targetId);

		Connection conn = getRemoteConnection();

		System.out.println(conn.toString());
		Statement setupStatement = null;
		Statement readStatement = null;
		ResultSet resultSet = null;
		String results = "";
		try {
		setupStatement = conn.createStatement();
	    String createTable = "CREATE TABLE Test5 (Resource char(50));";
	    String insertRow1 = "INSERT INTO Test5 (Resource) VALUES ('EC2 Instance');";
	    String insertRow2 = "INSERT INTO Test5 (Resource) VALUES ('RDS Instance');";

	    setupStatement.addBatch(createTable);
	    setupStatement.addBatch(insertRow1);
	    setupStatement.addBatch(insertRow2);
	    setupStatement.executeBatch();///////////////////////////初期だけ

	    readStatement = conn.createStatement();
	    resultSet = readStatement.executeQuery("SELECT Resource FROM Test5;");


	    while(resultSet.next()) {
	    	results += ", " + resultSet.getString("Resource");
	    }

	    System.out.println(results);
	    resultSet.close();
	    readStatement.close();
	    setupStatement.close();

	    conn.close();
		} catch (SQLException ex) {
		    // Handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		    ex.printStackTrace();
		  } finally {
		    System.out.println("Closing the connection.");
		    if (conn != null) try { conn.close(); } catch (SQLException ignore) {}
		  }
		return output;

	}

	private static Connection getRemoteConnection() {
			try {
				Class.forName("org.postgresql.Driver");
				String dbName = "postgres";
				String userName = "postgres";
				String password = "12345678";
				String hostname = "database-2.cjlvl0gcoihe.ap-northeast-1.rds.amazonaws.com";
				String port = "5432";
				String jdbcUrl = "jdbc:postgresql://" + hostname + ":" + port + "/" + dbName + "?user=" + userName
						+ "&password=" + password;

				Connection con = DriverManager.getConnection(jdbcUrl);
				return con;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
	}


// snippet-start:[cloudwatch.java2.put_rule.main]
public static void putCWRule(CloudWatchEventsClient cwe, String ruleName, String roleArn) {

    PutRuleResponse response = null;

    try {
        PutRuleRequest request = PutRuleRequest.builder()
            .name(ruleName)
            //.roleArn("arn:aws:events:ap-northeast-1:058130492195:rule/newRule")
            //.roleArn("arn:aws:iam::335446330391:role/testRole1") ここ
            //.scheduleExpression("rate(5 minutes)")
            .eventPattern("{\r\n" +
            		"  \"source\": [\r\n" +
            		"    \"aws.emr\"\r\n" +
            		"  ],\r\n" +
            		"  \"detail-type\": [\r\n" +
            		"    \"EMR Step Status Change\"\r\n" +
            		"  ],\r\n" +
            		"  \"detail\": {\r\n" +
            		"    \"state\": [\r\n" +
            		"      \"COMPLETED\",\r\n" +
            		"      \"CANCELLED\",\r\n" +
            		"      \"FAILED\"\r\n" +
            		"    ]\r\n" +
            		"  }\r\n" +
            		"}")
            .state(RuleState.ENABLED)
            .build();


        response = cwe.putRule(request);

    } catch (
        CloudWatchException e) {
        System.err.println(e.awsErrorDetails().errorMessage());
}


    System.out.printf(
            "Successfully created CloudWatch events rule %s with ARN %s",
            roleArn, response.ruleArn());
    System.out.println();
}
// snippet-end:[cloudwatch.java2.put_rule.main]
public static void putCWTargets(CloudWatchEventsClient cwe, String ruleName, String functionArn, String targetId ) {

    Target target = Target.builder()
            .arn("arn:aws:lambda:ap-northeast-1:058130492195:function:suwabe_lambda01")
            .id(targetId)
            .build();

    PutTargetsRequest request = PutTargetsRequest.builder()
            .targets(target)
            .rule(ruleName)
            .build();

    PutTargetsResponse response = cwe.putTargets(request);
    // snippet-end:[cloudwatch.java2.put_targets.main]

    System.out.printf(
            "Successfully created CloudWatch event target for rule %s",
            ruleName);
}
}
