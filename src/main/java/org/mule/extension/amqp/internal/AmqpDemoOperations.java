package org.mule.extension.amqp.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.rabbitmq.client.*;
import org.mule.runtime.extension.api.annotation.license.RequiresEnterpriseLicense;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class AmqpDemoOperations {

  /**
   * Example of an operation that uses the configuration and a connection instance to perform some action.
   */
  @MediaType(value = ANY, strict = false)
  public String retrieveInfo(@Config AmqpDemoConfiguration configuration, @Connection AmqpDemoConnection connection){
    return "Using Configuration [" + configuration.getConfigId() + "] with Connection id [" + connection.getHost() + "]";
  }

  /**
   * Example of a simple operation that receives a string parameter and returns a new string message that will be set on the payload.
   */
  @MediaType(value = ANY, strict = false)
  public String sayHi(String person) {
    return buildHelloMessage(person);
  }

  @MediaType(value = ANY, strict = false)
  public void emitMessage(@Connection AmqpDemoConnection config, String exchangeName, String payload) throws TimeoutException, IOException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(config.getHost());
    factory.setPort(config.getPort());
    factory.setUsername(config.getUsername());
    factory.setPassword(config.getPassword());

    com.rabbitmq.client.Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    String message = payload;
    channel.basicPublish(exchangeName, "", null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + message + "'");

    connection.close();

  }

  @MediaType(value = ANY, strict = false)
  public String recieveMessage(@Connection AmqpDemoConnection config, String exchangeName) throws TimeoutException, IOException {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(config.getHost());
    factory.setPort(config.getPort());
    factory.setUsername(config.getUsername());
    factory.setPassword(config.getPassword());

    com.rabbitmq.client.Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    GetResponse response = channel.basicGet("tm4", true);

    connection.close();

    if (response == null) {
        return "no message";
      } else {
        AMQP.BasicProperties props = response.getProps();
        byte[] body = response.getBody();

        String resp = new String(body);

        return resp;
      }
  }

  /**
   * Private Methods are not exposed as operations
   */
  private String buildHelloMessage(String person) {
    return "Hello " + person + "!!!";
  }
}
