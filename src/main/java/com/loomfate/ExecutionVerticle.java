package com.loomfate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.*;

/**
 * Created by hitrix on 21.12.2017.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class ExecutionVerticle extends AbstractVerticle {
    private static final Logger LOG = LoggerFactory.getLogger(ExecutionVerticle.class);
    Set<String> names = NameDictionary.getNames();
    Set<String> snames = NameDictionary.getSurNames();
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eb = getVertx().eventBus();

        MessageConsumer<String> consumer = eb.consumer(SendMessage.KILL_CHANNEL);
        consumer.handler(message -> {
            String name = new JsonObject(message.body()).getString("name");
            String surname = new JsonObject(message.body()).getString("surname");
            if ( names.contains(name) && snames.contains(surname) ) {
                LOG.info("Must be killed: " + capitalizeFirstLetter(name)+
                        " "+ capitalizeFirstLetter(surname)
                );
            }
        });

        LOG.info("Execution verticle started");
    }

    private String capitalizeFirstLetter(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
