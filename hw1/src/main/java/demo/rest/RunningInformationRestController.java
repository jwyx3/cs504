package demo.rest;

import demo.domain.RunningInformation;
import demo.domain.RunningInformationRepository;
import demo.domain.RunningInformationSummary;
import demo.service.RunningInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RunningInformationRestController {
    @Autowired
    private RunningInformationService runningInformationService;

    @RequestMapping(value = "/running", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody List<RunningInformation> information) {
        runningInformationService.saveRunningInformation(information);
    }

    @RequestMapping(value = "/running/{runningId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String runningId) {
        runningInformationService.deleteAllByRunningId(runningId);
    }

    @RequestMapping(value = "/running", method = RequestMethod.GET)
    public Page<RunningInformationSummary> findByMovementType(@RequestParam(name = "page") int page) {
        return runningInformationService.findAllByOrderByHeartRateDesc(new PageRequest(page, 2));
    }
}
