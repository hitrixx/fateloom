package com.loomfate;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Created by hitrix on 23.12.2017.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
public class SendMessage extends AbstractVerticle {
    static String KILL_CHANNEL="name.channel";
    private static final Logger LOG = LoggerFactory.getLogger(SendMessage.class);
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        this.start();
        LOG.info("SendMessage start");
    }

    public void sendMessage(String address, String message){
        EventBus eb = Application.vertx.eventBus();
            eb.send(address, message);
    };

}
