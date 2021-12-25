package com.yiran.client.factory;

import java.util.Objects;

public class PoolConfig {

    private int maxIdle = 8;

    private int minIdle = 4;

    private int maxTotal = 8;

    private int maxWaitMillis = -1;

    private boolean testOnBorrow = true;

    private boolean testWhileIdle = true;

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PoolConfig that = (PoolConfig) o;
        return maxIdle == that.maxIdle && minIdle == that.minIdle && maxTotal == that.maxTotal && maxWaitMillis == that.maxWaitMillis && testOnBorrow == that.testOnBorrow && testWhileIdle == that.testWhileIdle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxIdle, minIdle, maxTotal, maxWaitMillis, testOnBorrow, testWhileIdle);
    }

    @Override
    public String toString() {
        return "PoolConfig{" +
                "maxIdle=" + maxIdle +
                ", minIdle=" + minIdle +
                ", maxTotal=" + maxTotal +
                ", maxWaitMillis=" + maxWaitMillis +
                ", testOnBorrow=" + testOnBorrow +
                ", testWhileIdle=" + testWhileIdle +
                '}';
    }
}
