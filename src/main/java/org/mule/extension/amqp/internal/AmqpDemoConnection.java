package org.mule.extension.amqp.internal;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class AmqpDemoConnection {

  private final String host;
  private final Integer port;
  private final String username;
  private final String password;

  public AmqpDemoConnection(String host, Integer port, String username, String passowrd) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = passowrd;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void invalidate() {

  }
}
