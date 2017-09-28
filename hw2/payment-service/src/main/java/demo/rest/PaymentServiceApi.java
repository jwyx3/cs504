package demo.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.domain.Payment;
import demo.service.PaymentService;
import demo.service.PaymentWorkerFactory;
import demo.task.PaymentWorker;
import demo.task.PaymentWorkerInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@RestController
@EnableBinding(Sink.class)
@Slf4j
public class PaymentServiceApi {

    private PaymentService paymentService;

    private PaymentWorkerFactory paymentWorkerFactory;

    private AsyncTaskExecutor taskExecutor;

    private ObjectMapper objectMapper;

    @Autowired
    public PaymentServiceApi(PaymentService paymentService, PaymentWorkerFactory paymentWorkerFactory, AsyncTaskExecutor taskExecutor, ObjectMapper objectMapper) {
        this.paymentService = paymentService;
        this.paymentWorkerFactory = paymentWorkerFactory;
        this.taskExecutor = taskExecutor;
        this.objectMapper = objectMapper;
    }

    private Map<Long, PaymentWorkerInstance> taskFutures = new HashMap<>();

    //1. Transform payment to payment worker
    //2. taskExecutor.submit(worker);
    //3. start task
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public List<Payment> upload(@RequestBody List<Payment> payments) {
        List<Payment> result = paymentService.save(payments);

        for (Payment payment : payments) {
            log.info("create payment worker for payment: {}", payment);
            final PaymentWorker worker = paymentWorkerFactory.preparePaymentWorker(payment);
            final Future<?> task = taskExecutor.submit(worker);
            final PaymentWorkerInstance instance = new PaymentWorkerInstance(worker.getId(), worker, task);
            taskFutures.put(worker.getId(), instance);
        }

        return result;
    }

    //1. cancel all tasks
    //2. delete all payments
    @RequestMapping(value = "/purge", method = RequestMethod.DELETE)
    public void purge() {
        for (Map.Entry<Long, PaymentWorkerInstance> entry : taskFutures.entrySet()) {
            PaymentWorkerInstance instance = entry.getValue();
            instance.getWorker().cancel();
            instance.getTask().cancel(true);
        }
        taskFutures.clear();

        paymentService.deleteAll();
    }

    @RequestMapping(value = "/tasks", method = RequestMethod.GET)
    public Page<PaymentWorkerInstance> getTasks(
            @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        final Pageable pageable = new PageRequest(page, size);
        final List<PaymentWorkerInstance> instances = new ArrayList<>(taskFutures.values());
        int start = pageable.getOffset();
        int end = start + pageable.getPageSize() > instances.size() ? instances.size() : start + pageable.getPageSize();
        return new PageImpl<>(instances.subList(start, end), pageable, instances.size());
    }

    // get first payment by orderId
    @RequestMapping(value = "/payments/{orderId}", method = RequestMethod.GET)
    public Payment findFirstByOrderId(@PathVariable String orderId) {
        return paymentService.findFirstByOrderId(orderId);
    }

    @ServiceActivator(inputChannel = Sink.INPUT)
    public void handle(String input) throws IOException {
        log.info("Location input in updater: "+input);
        final Payment payment = paymentService.save(objectMapper.readValue(input, Payment.class));
        final PaymentWorker worker = paymentWorkerFactory.preparePaymentWorker(payment);
        final Future<?> task = taskExecutor.submit(worker);
        final PaymentWorkerInstance instance = new PaymentWorkerInstance(worker.getId(), worker, task);
        taskFutures.put(worker.getId(), instance);
    }
}
