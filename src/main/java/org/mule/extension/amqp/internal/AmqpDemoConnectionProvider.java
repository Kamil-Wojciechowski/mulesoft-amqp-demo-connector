package org.mule.extension.amqp.internal;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * This class (as it's name implies) provides connection instances and the funcionality to disconnect and validate those
 * connections.
 * <p>
 * All connection related parameters (values required in order to create a connection) must be
 * declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares that connections resolved by this provider
 * will be pooled and reused. There are other implementations like {@link CachedConnectionProvider} which lazily creates and
 * caches connections or simply {@link ConnectionProvider} if you want a new connection each time something requires one.
 */
public class AmqpDemoConnectionProvider implements PoolingConnectionProvider<AmqpDemoConnection> {

  private final Logger LOGGER = LoggerFactory.getLogger(AmqpDemoConnectionProvider.class);

 /**
  * A parameter that is always required to be configured.
  */
  @Parameter
  private String host;

  @Parameter
  private Integer port;

  @Parameter
  private String username;

  @Parameter
  @Password
  private String password;

  @Override
  public AmqpDemoConnection connect() throws ConnectionException {
    return new AmqpDemoConnection(host, port, username, password);
  }

  @Override
  public void disconnect(AmqpDemoConnection connection) {
    try {
      connection.invalidate();
    } catch (Exception e) {
      LOGGER.error("Error while disconnecting [" + connection.getHost() + "]: " + e.getMessage(), e);
    }
  }

  @Override
  public ConnectionValidationResult validate(AmqpDemoConnection amqpConnection) {
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost(amqpConnection.getHost());
      factory.setPort(amqpConnection.getPort());
      factory.setUsername(amqpConnection.getUsername());
      factory.setPassword(amqpConnection.getPassword());

      try {
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        System.out.println("Success!");

        connection.close();
      } catch (IOException error) {
          return ConnectionValidationResult.failure("Connection failed!", error);
      } catch (TimeoutException error) {
          return ConnectionValidationResult.failure("Connection failed!", error);
      }

    return ConnectionValidationResult.success();
  }
}
