package demo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Random;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Entity
@Table(name = "RUNNING_ANALYSIS")
public class RunningInformation {
    @Id
    @GeneratedValue
    private long id;

    private String runningId;

    private double latitude;
    private double longitude;
    private double runningDistance;
    private double totalRunningTime;
    private int heartRate = 0;
    private Date timestamp = new Date();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "username", column = @Column(name = "user_name")),
            @AttributeOverride(name = "address", column = @Column(name = "user_address"))
    })
    private UserInfo userInfo;

    public RunningInformation() {

    }

    public void setHeartRate(int heartRate) {
        Random rand = new Random();
        this.heartRate = rand.nextInt((200 - 60) + 1) + 60;
    }
}
