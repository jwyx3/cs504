package demo.service;

import demo.domain.RunningInformation;
import demo.domain.RunningInformationSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RunningInformationService {
    List<RunningInformation> saveRunningInformation(List<RunningInformation> runningInformation);

    Page<RunningInformationSummary> findAllByOrderByHeartRateDesc(Pageable pageable);

    void deleteAllByRunningId(String runningId);
}
