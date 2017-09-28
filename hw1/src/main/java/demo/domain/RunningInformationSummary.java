package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RunningInformationSummary {
    enum HealthWarningLevelType {
        LOW, NORMAL, HIGH;
    }

    private String runningId;
    private long userId;
    private double totalRunningTime;
    private int heartRate;
    private String userName;
    private String userAddress;
    private HealthWarningLevelType healthWarningLevel;

    public RunningInformationSummary(RunningInformation information) {
        this.runningId = information.getRunningId();
        this.userId = information.getId();
        this.totalRunningTime = information.getTotalRunningTime();
        this.heartRate = information.getHeartRate();
        this.userName = information.getUserInfo().getUsername();
        this.userAddress = information.getUserInfo().getAddress();
    }

    public HealthWarningLevelType getHealthWarningLevel() {
        if (this.heartRate >= 60 && this.heartRate <= 75) {
            return HealthWarningLevelType.LOW;
        } else if (this.heartRate > 75 && this.heartRate <= 120) {
            return HealthWarningLevelType.NORMAL;
        } else {
            return HealthWarningLevelType.HIGH;
        }
    }
}
