package com.example.runity.repository;

import com.example.runity.domain.RunningPathTS;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RunningPathTSRepository {

    private final InfluxDBClient influxDBClient;
    private final String org = "myorg";        // application.properties 또는 config에서 관리하는 게 좋음
    private final String bucket = "running_data";

    public RunningPathTSRepository(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public void saveRunningPoint(Long userId, RunningPathTS pointData) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            Point point = Point.measurement("running_path")
                    .addTag("user", userId.toString())
                    .addField("latitude", pointData.getLatitude())
                    .addField("longitude", pointData.getLongitude())
                    .addField("distance", pointData.getDistance())
                    .addField("pace", pointData.getPace())
                    .addField("speed", pointData.getSpeed())
                    .time(pointData.getTimestamp(), WritePrecision.MS);
            writeApi.writePoint(point);
        }
    }

    public void saveAllWithCheck(Long userId, List<RunningPathTS> fullPaths) {
        // 1. 인플럭스DB에서 userId 해당 데이터 모두 조회
        Map<Instant, RunningPathTS> existingData = influxQueryUserData(userId);

        // 2. 전체 경로 돌면서 비교
        for (RunningPathTS path : fullPaths) {
            RunningPathTS existing = existingData.get(path.getTimestamp());
            if (existing == null || !existing.equals(path)) {
                // 누락됐거나 값이 다르면 저장
                saveRunningPoint(userId, path);
            }
        }
    }

    // 해당 날짜의 실 러닝 기록을 가져오기
    public Map<Instant, RunningPathTS> influxQueryUserData(Long userId, LocalDate date) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String start = date.atStartOfDay().toString() + "Z";
        String stop = date.plusDays(1).atStartOfDay().toString() + "Z";

        String flux = String.format("""
        from(bucket: "running_data")
        |> range(start: %s, stop: %s)
        |> filter(fn: (r) => r._measurement == "running_path")
        |> filter(fn: (r) => r["user"] == "%s")
        |> pivot(rowKey:["_time"], columnKey: ["_field"], valueColumn: "_value")
        |> sort(columns: ["_time"])
    """, start, stop, userId);

        List<FluxTable> tables = queryApi.query(flux);

        Map<Instant, RunningPathTS> result = new HashMap<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Instant timestamp = record.getTime();
                Map<String, Object> values = record.getValues();

                double latitude = ((Number) values.get("latitude")).doubleValue();
                double longitude = ((Number) values.get("longitude")).doubleValue();
                double pace = ((Number) values.get("pace")).doubleValue();
                float distance = ((Number) values.get("distance")).floatValue();
                float speed = ((Number) values.get("speed")).floatValue();

                RunningPathTS point = new RunningPathTS(timestamp, latitude, longitude, pace, distance, speed);
                result.put(timestamp, point);
            }
        }

        return result;
    }


    private Map<Instant, RunningPathTS> influxQueryUserData(Long userId) {
        QueryApi queryApi = influxDBClient.getQueryApi();

        String flux = String.format("""
                from(bucket:"%s")
                  |> range(start: 0)
                  |> filter(fn: (r) => r["_measurement"] == "running_path")
                  |> filter(fn: (r) => r["user"] == "%d")
                  |> pivot(rowKey:["_time"], columnKey: ["_field"], valueColumn: "_value")
                """, bucket, userId);

        List<FluxTable> tables = queryApi.query(flux);

        Map<Instant, RunningPathTS> resultMap = new HashMap<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Instant timestamp = record.getTime();

                double latitude = ((Number)record.getValueByKey("latitude")).doubleValue();
                double longitude = ((Number)record.getValueByKey("longitude")).doubleValue();
                double pace = ((Number)record.getValueByKey("pace")).doubleValue();
                float distance = ((Number)record.getValueByKey("distance")).floatValue();
                float speed = ((Number)record.getValueByKey("speed")).floatValue();

                RunningPathTS runningPathTS = new RunningPathTS(timestamp, latitude, longitude, pace, distance, speed);

                resultMap.put(timestamp, runningPathTS);
            }
        }

        return resultMap;
    }
}
