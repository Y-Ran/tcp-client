package com.yiran.client.factory;

import com.yiran.client.Connection;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.DestroyMode;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPolledObjectFactory<REQ, RES> extends BasePooledObjectFactory<Connection<REQ, RES>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionPolledObjectFactory.class);

    private final ConnectionFactory<REQ, RES> connectionFactory;

    public ConnectionPolledObjectFactory(ConnectionFactory<REQ, RES> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }


    @Override
    public Connection<REQ, RES> create() throws Exception {
        try {
            Connection<REQ, RES> connection = connectionFactory.getConnection();
            connection.connect();
            return connection;
        } finally {
            LOGGER.debug("makeObject connection ...");
        }
    }

    @Override
    public PooledObject<Connection<REQ, RES>> wrap(Connection<REQ, RES> obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<Connection<REQ, RES>> p, DestroyMode destroyMode) throws Exception {
        try {
            passivateObject(p);
            Connection<REQ, RES> connection = p.getObject();
            connection.close();
            p.markAbandoned();
        } finally {
            LOGGER.debug("destroyObject connection ...");
        }
    }

    @Override
    public boolean validateObject(PooledObject<Connection<REQ, RES>> p) {
        LOGGER.debug("validateObject connection ...");
        if (p.getObject() == null) {
            return false;
        }
        Connection<REQ, RES> connection = p.getObject();
        return !connection.isClosed();
    }

    @Override
    public void activateObject(PooledObject<Connection<REQ, RES>> p) throws Exception {
        try {
            Connection<REQ, RES> connection = p.getObject();
            if (!connection.isClosed()) {
                return;
            }
            connection.connect();
        } finally {
            LOGGER.debug("activateObject connection ...");
        }
    }

    @Override
    public void destroyObject(PooledObject<Connection<REQ, RES>> p) throws Exception {
        LOGGER.debug("passivateObject connection ...");
    }
}
