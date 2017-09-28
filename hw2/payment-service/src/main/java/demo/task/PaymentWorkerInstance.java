package demo.task;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.Future;

@Data
@AllArgsConstructor
public class PaymentWorkerInstance {
    private long id;
    private PaymentWorker worker;
    private Future<?> task;

    @Override
    public String toString() {
        return "PaymentWorkerInstance [id=" + id + ", worker=" + worker + ", task=" + task + "]";
    }
}
