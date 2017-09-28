package demo.service.impl;

import demo.domain.RunningInformation;
import demo.domain.RunningInformationRepository;
import demo.domain.RunningInformationSummary;
import demo.service.RunningInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RunningInformationServiceImpl implements RunningInformationService {
    @Autowired
    private RunningInformationRepository runningInformationRepository;

    RunningInformationSummary convertToSummary(RunningInformation information) {
        return new RunningInformationSummary(information);
    }

    @Override
    public List<RunningInformation> saveRunningInformation(List<RunningInformation> runningInformation) {
        return runningInformationRepository.save(runningInformation);
    }

    @Override
    public Page<RunningInformationSummary> findAllByOrderByHeartRateDesc(Pageable pageable) {
        Page<RunningInformation> items = runningInformationRepository.findAllByOrderByHeartRateDesc(pageable);
        return items.map(this::convertToSummary);
    }

    @Override
    public void deleteAllByRunningId(String runningId) {
        runningInformationRepository.deleteAllByRunningId(runningId);
    }
}
