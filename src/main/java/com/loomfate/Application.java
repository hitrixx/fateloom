package com.loomfate;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.print.attribute.standard.MediaSize;
import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Created by hitrix on 21.12.2017.
 */
@Configuration
@ComponentScan("com.loomfate")
public class Application {
    static Vertx vertx = Vertx.vertx();
    private static int NAME_MAX_LENGTH=11;
    private static int SURNAME_MAX_LENGTH=11;
    private static int NAME_MIN_LENGTH=2;
    private static int SURNAME_MIN_LENGTH=2;

    private static void vertxRegister() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        VerticleFactory verticleFactory = context.getBean(SpringVerticleFactory.class);
        vertx.registerVerticleFactory(verticleFactory);
        DeploymentOptions options = new DeploymentOptions().setInstances(1);
        vertx.deployVerticle(verticleFactory.prefix() + ":" + ExecutionVerticle.class.getName(), options);
        vertx.deployVerticle(verticleFactory.prefix() + ":" + SendMessage.class.getName(), options);
    }

    public static void main(String[] args) throws Exception {
        vertxRegister();
        Scanner scanner = new Scanner(System.in);
        String byteStream = "";
        while (scanner.hasNext()) {
            JsonObject name = new JsonObject();
            byteStream += scanner.next();
            if (byteStream.length() >= NAME_MAX_LENGTH+SURNAME_MAX_LENGTH) {
                String possibleFirstName = new String(byteStream.substring(0,NAME_MAX_LENGTH-1).getBytes(), "UTF8");
                possibleFirstName = leftOnlyLatin(possibleFirstName);

                if (possibleFirstName.length() >= NAME_MIN_LENGTH && possibleFirstName.length() <= NAME_MAX_LENGTH) {
                    name.put("name",possibleFirstName);
                }
                String possibleLastName = new String(byteStream.substring(NAME_MAX_LENGTH,NAME_MAX_LENGTH+SURNAME_MAX_LENGTH-1).getBytes(), "ISO-8859-1");
                possibleLastName = leftOnlyLatin(possibleLastName);
                if (possibleLastName.length() >= SURNAME_MIN_LENGTH && possibleLastName.length() <= SURNAME_MAX_LENGTH) {
                    name.put("surname",possibleLastName);
                }
                byteStream = "";
            }

            if ( name.containsKey("name") && name.containsKey("surname") ){
                new SendMessage().sendMessage(
                        SendMessage.KILL_CHANNEL, name.toString()
                );
            }
        }
    }

    private static String leftOnlyLatin(String input){
        return input.replaceAll("[^a-zA-Z]+", "").toLowerCase();
    }
}