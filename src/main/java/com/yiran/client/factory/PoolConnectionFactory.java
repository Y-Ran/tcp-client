package com.yiran.client.factory;

import com.yiran.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 池化连接工厂
 * 每次从池中返回一个连接
 *
 * @param <REQ>
 * @param <RES>
 */
public class PoolConnectionFactory<REQ, RES> implements ConnectionFactory<REQ, RES> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PoolConnectionFactory.class);

    private final GenericObjectPool<Connection<REQ, RES>> internalPool;

    public PoolConnectionFactory(ConnectionFactory<REQ, RES> connectionFactory, PoolConfig poolConfig) {
        GenericObjectPoolConfig<Connection<REQ, RES>> config = new GenericObjectPoolConfig<>();
        config.setMaxIdle(poolConfig.getMaxIdle());
        config.setMinIdle(poolConfig.getMinIdle());
        config.setMaxTotal(poolConfig.getMaxTotal());
        config.setTestOnBorrow(poolConfig.isTestOnBorrow());
        config.setTestWhileIdle(poolConfig.isTestWhileIdle());
        // 非长连接 需要断开
        BasePooledObjectFactory<Connection<REQ, RES>> connectionPolledObjectFactory = new ConnectionPolledObjectFactory<>(connectionFactory);
        this.internalPool = new GenericObjectPool<>(connectionPolledObjectFactory, config);
        this.initPool(poolConfig.getMinIdle());
    }

    @Override
    public Connection<REQ, RES> getConnection() {
        try {
            Connection<REQ, RES> connection = internalPool.borrowObject();
            return new Connection<>() {
                private boolean close = false;

                @Override
                public RES sendRequest(REQ req) throws IOException {
                    return connection.sendRequest(req);
                }

                @Override
                public void connect() throws IOException {
                    connection.connect();
                }

                @Override
                public void close() throws IOException {
                    internalPool.returnObject(this);
                    close = true;
                }

                @Override
                public boolean isClosed() {
                    return close && connection.isClosed();
                }
            };
        } catch (NoSuchElementException nse) {
            throw nse;
        } catch (Exception e) {
            throw new RuntimeException("Could not get a resource from the pool", e);
        }
    }

    public void initPool(final int minIdle) {
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(minIdle);
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        Executor executor = new ThreadPoolExecutor(minIdle, minIdle, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> {
                    Thread thread = new Thread(r);
                    thread.setName("thread-tcp-client-pool-init-" + atomicInteger.incrementAndGet());
                    return thread;
                });
        for (int i = 1; i <= minIdle; i++) {
            executor.execute(() -> {
                try {
                    this.internalPool.addObject();
                } catch (Exception e) {
                    LOGGER.error("初始化tcp连接池异常 ", e);
                } finally {
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
            long endTime = System.currentTimeMillis();
            LOGGER.info("***********init conn pool size: {},spend times: {}ms.**********",
                    minIdle, (endTime - startTime));
        } catch (InterruptedException e) {
            LOGGER.error("", e);
        }
    }
}
