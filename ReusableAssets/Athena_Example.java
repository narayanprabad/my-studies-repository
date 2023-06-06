import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.*;

import java.util.ArrayList;
import java.util.List;

public class AthenaService {

    private final AthenaClient athenaClient;
    private final String databaseName;
    private final String tableName;

    public AthenaService(String region, String databaseName, String tableName) {
        this.athenaClient = AthenaClient.builder()
                .region(Region.of(region))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    public List<ReconciliationItem> queryReconciliationItems(int pageNumber, int recordsPerPage, String sortBy, String groupBy) {
        List<ReconciliationItem> items = new ArrayList<>();

        try {
            // Create the query string with parameters
            String queryString = "SELECT * FROM " + tableName +
                    " ORDER BY " + sortBy +
                    " GROUP BY " + groupBy +
                    " LIMIT " + recordsPerPage +
                    " OFFSET " + (pageNumber - 1) * recordsPerPage;

            // Create the QueryExecutionContext
            QueryExecutionContext queryExecutionContext = QueryExecutionContext.builder()
                    .database(databaseName)
                    .build();

            // Create the ResultConfiguration
            ResultConfiguration resultConfiguration = ResultConfiguration.builder()
                    .outputLocation("s3://your-bucket-name/query-results/")
                    .build();

            // Create the StartQueryExecutionRequest
            StartQueryExecutionRequest startQueryExecutionRequest = StartQueryExecutionRequest.builder()
                    .queryString(queryString)
                    .queryExecutionContext(queryExecutionContext)
                    .resultConfiguration(resultConfiguration)
                    .build();

            // Start the query execution
            StartQueryExecutionResponse startQueryExecutionResponse = athenaClient.startQueryExecution(startQueryExecutionRequest);
            String queryExecutionId = startQueryExecutionResponse.queryExecutionId();

            // Wait for the query execution to complete
            GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
                    .queryExecutionId(queryExecutionId)
                    .build();

            QueryExecutionStatus queryExecutionStatus = null;
            do {
                GetQueryExecutionResponse getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
                queryExecutionStatus = getQueryExecutionResponse.queryExecution().status();
            } while (!queryExecutionStatus.state().equals(QueryExecutionState.SUCCEEDED));

            // Get the query results
            GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
                    .queryExecutionId(queryExecutionId)
                    .build();

            GetQueryResultsResponse getQueryResultsResponse = athenaClient.getQueryResults(getQueryResultsRequest);

            // Process the query results
            List<Row> rows = getQueryResultsResponse.resultSet().rows();
            if (rows.size() > 1) { // Skip the header row
                for (int i = 1; i < rows.size(); i++) {
                    List<Datum> rowData = rows.get(i).data();
                    // Parse the rowData and create ReconciliationItem objects
                    // Assuming the order of the columns: Record_ID, Record_Status, Instrument, Account, Quantity, Price, User_Comments
                    String recordId = rowData.get(0).varCharValue();
                    String recordStatus = rowData.get(1).varCharValue();
                    String instrument = rowData.get(2).varCharValue();
                    String account = rowData.get(3).varCharValue
                      
                                          int quantity = Integer.parseInt(rowData.get(4).varCharValue());
                    double price = Double.parseDouble(rowData.get(5).varCharValue());
                    String userComments = rowData.get(6).varCharValue();

                    // Create a ReconciliationItem object
                    ReconciliationItem item = new ReconciliationItem(recordId, recordStatus, instrument, account, quantity, price, userComments);
                    items.add(item);
                }
            }
        } catch (AthenaException e) {
            // Handle Athena exceptions
            e.printStackTrace();
        }

        return items;
    }
}

