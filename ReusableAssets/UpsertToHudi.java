//SELECT CONVERT(DATETIME, CONVERT(VARCHAR(13), GETDATE(), 120) + ':00:00', 120)
import org.apache.hudi.common.model.HoodieKey;
import org.apache.hudi.common.model.HoodieRecord;
import org.apache.hudi.common.util.HoodieAvroUtils;
import org.apache.hudi.config.HoodieCompactionConfig;
import org.apache.hudi.config.HoodieWriteConfig;
import org.apache.hudi.keygen.SimpleKeyGenerator;
import org.apache.hudi.spark.client.HoodieSparkClient;
import org.apache.hudi.spark.client.SparkRDDWriteClient;
import org.apache.hudi.spark.util.SparkDataSourceUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.UUID;

public class UpsertToHudi {
    public static void main(String[] args) throws Exception {
        // Create Spark configuration
        SparkConf sparkConf = new SparkConf()
                .setAppName("UpsertToHudi")
                .setMaster("local[*]")
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");

        // Create Spark context
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        // Create SQL context
        SQLContext sqlContext = new SQLContext(jsc);

        // Create HoodieWriteConfig object to configure the write operation
        HoodieWriteConfig config = HoodieWriteConfig.newBuilder()
                .withPath("s3://my-bucket/my-table/")
                .withSchema(HoodieAvroUtils.createHoodieWriteSchema(Schema.Parser().parse(SCHEMA)))
                .withKeyGenerator(SimpleKeyGenerator.class)
                .withBulkInsertParallelism(1500, 1500)
                .withWriteConcurrency(10)
                .withCompactionConfig(HoodieCompactionConfig.newBuilder().build())
                .withAutoCommit(false)
                .withProps(props)
                .forTable(TABLE_NAME)
                .build();

        // Create HoodieTable object for the table you want to write to
        SparkRDDWriteClient<HoodieRecord> writeClient = new SparkRDDWriteClient<>(new HoodieSparkClient<>(sparkConf), config);

        // Read the data to upsert from Parquet file
        DataFrameReader reader = sqlContext.read().format("parquet");
        Dataset<Row> dataFrame = reader.load("s3://my-bucket/my-table/");

        // Convert DataFrame to JavaRDD<HoodieRecord>
        JavaRDD<HoodieRecord> hoodieRecords = dataFrame.javaRDD().map(row -> {
            HoodieKey key = new HoodieKey(row.getString(0), row.getString(1));
            GenericRecord record = (GenericRecord) row.get(2);
            return new HoodieRecord(key, record);
        });

        // Perform upsert operation
        writeClient.upsert(hoodieRecords, UUID.randomUUID().toString());

        // Commit the write operation
        writeClient.commit(UUID.randomUUID().toString(), hoodieRecords);

        // Close Spark context
        jsc.close();
    }
}
