package com.example.runity.repository;

import com.example.runity.DTO.FeedbackDTO;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RunningPathTSRepository {

    private final InfluxDBClient influxDBClient;
    private final String org = "myorg";
    private final String bucket = "running_data";

    public RunningPathTSRepository(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    // ----------------------러닝 좌표 저장 ---------------------- //

    public void saveRunningPoint(Long userId, RunningPathTS pointData) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            Point point = Point.measurement("running_path")
                    .addTag("user", userId.toString())
                    .addField("latitude", pointData.getLatitude())
                    .addField("longitude", pointData.getLongitude())
                    .addField("distance", pointData.getDistance())
                    .addField("pace", pointData.getPace())
                    .addField("speed", pointData.getSpeed())
                    .addField("elapsedTime", pointData.getElapsedTime().toSecondOfDay())
                    .time(pointData.getTimestamp(), WritePrecision.MS);
            writeApi.writePoint(point);
        }
    }


    public void saveAllWithCheck(Long userId, List<RunningPathTS> fullPaths) {
        Map<Instant, RunningPathTS> existingData = influxQueryUserData(userId);

        for (RunningPathTS path : fullPaths) {
            RunningPathTS existing = existingData.get(path.getTimestamp());
            if (existing == null || !existing.equals(path)) {
                saveRunningPoint(userId, path);
            }
        }
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
                double latitude = ((Number) record.getValueByKey("latitude")).doubleValue();
                double longitude = ((Number) record.getValueByKey("longitude")).doubleValue();
                double pace = ((Number) record.getValueByKey("pace")).doubleValue();
                float distance = ((Number) record.getValueByKey("distance")).floatValue();
                float speed = ((Number) record.getValueByKey("speed")).floatValue();
                LocalTime elapsedTime = LocalTime.ofSecondOfDay(timestamp.toEpochMilli());

                RunningPathTS runningPathTS = new RunningPathTS(timestamp, latitude, longitude, pace, distance, speed, elapsedTime);
                resultMap.put(timestamp, runningPathTS);
            }
        }

        return resultMap;
    }

    public List<RunningPathTS> findBySessionId(Long recordId) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String flux = String.format("""
                from(bucket: "%s")
                    |> range(start: -7d)
                    |> filter(fn: (r) => r._measurement == "running_path_ts" and r.recordId == "%d")
                    |> pivot(rowKey:["_time"], columnKey: ["_field"], valueColumn: "_value")
                    |> sort(columns: ["_time"])
                """, bucket, recordId);

        List<FluxTable> tables = queryApi.query(flux);
        List<RunningPathTS> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Instant timestamp = record.getTime();
                double latitude = ((Number) record.getValueByKey("latitude")).doubleValue();
                double longitude = ((Number) record.getValueByKey("longitude")).doubleValue();
                double pace = ((Number) record.getValueByKey("pace")).doubleValue();
                float distance = ((Number) record.getValueByKey("distance")).floatValue();
                float speed = ((Number) record.getValueByKey("speed")).floatValue();
                int elapsedSeconds = ((Number) record.getValueByKey("elapsedTime")).intValue();
                LocalTime elapsedTime = LocalTime.ofSecondOfDay(elapsedSeconds); // ✅

                result.add(new RunningPathTS(timestamp, latitude, longitude, pace, distance, speed, elapsedTime));
            }
        }


        return result;
    }

    // ---------------------- 피드백 저장---------------------- //

    public void saveFeedbackPoint(Long userId, Long sessionId, FeedbackDTO feedback) {
        try (WriteApi writeApi = influxDBClient.getWriteApi()) {
            Point point = Point.measurement("running_feedback")
                    .addTag("user", userId.toString())
                    .addTag("sessionId", sessionId.toString())
                    .addTag("type", feedback.getType())
                    .addField("semiType", feedback.getSemiType())
                    .addField("message", feedback.getMessage())
                    .time(feedback.getTimeStamp().atDate(LocalDate.now()).atZone(java.time.ZoneId.systemDefault()).toInstant(), WritePrecision.S);
            writeApi.writePoint(point);
        }
    }

    public void saveAllFeedback(Long userId, Long sessionId, List<FeedbackDTO> feedbacks) {
        for (FeedbackDTO fb : feedbacks) {
            saveFeedbackPoint(userId, sessionId, fb);
        }
    }

    public List<FeedbackDTO> findFeedbackByUserAndSession(Long userId, Long sessionId) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String flux = String.format("""
                from(bucket: "%s")
                    |> range(start: -7d)
                    |> filter(fn: (r) => r._measurement == "running_feedback")
                    |> filter(fn: (r) => r["user"] == "%d" and r["sessionId"] == "%d")
                    |> pivot(rowKey:["_time"], columnKey: ["_field"], valueColumn: "_value")
                    |> sort(columns: ["_time"])
                """, bucket, userId, sessionId);

        List<FluxTable> tables = queryApi.query(flux);
        List<FeedbackDTO> result = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                Instant timestamp = record.getTime();
                String type = (String) record.getValueByKey("type");
                String semiType = (String) record.getValueByKey("semiType");
                String message = (String) record.getValueByKey("message");

                FeedbackDTO dto = FeedbackDTO.builder()
                        .timeStamp(timestamp.atZone(java.time.ZoneId.systemDefault()).toLocalTime())
                        .type(type)
                        .semiType(semiType)
                        .message(message)
                        .build();

                result.add(dto);
            }
        }

        return result;
    }
}

