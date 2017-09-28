package demo.domain;

import com.mongodb.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.jmx.DataEndpointMBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;


@Slf4j
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public int completeOrder(Order order) {
        Query query = new Query(Criteria.where("id").is(order.getId()));
        Update update = new Update();
        update.set("paymentId", order.getPaymentId());
        update.set("status", order.getStatus());
        update.set("deliveryEstimatedTime", order.getDeliveryEstimatedTime());
        update.set("updatedAt", new Date());

        WriteResult result = mongoTemplate.updateFirst(query, update, Order.class);
        log.info("complete order: {}", result);

        if (result != null)
            return result.getN();
        else
            return 0;
    }
}
