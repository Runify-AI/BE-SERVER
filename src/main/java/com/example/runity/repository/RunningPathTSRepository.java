package com.example.runity.repository;

import com.example.runity.domain.RunningPathTS;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public class RunningPathTSRepository {

    private final InfluxDBClient influxDBClient;

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
                    .time(Instant.now(), WritePrecision.MS);
            writeApi.writePoint(point);
        }
    }
}
