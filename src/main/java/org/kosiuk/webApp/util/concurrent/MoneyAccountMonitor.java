package org.kosiuk.webApp.util.concurrent;

import java.util.Objects;

public class MoneyAccountMonitor {
    private int id;

    public MoneyAccountMonitor(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyAccountMonitor that = (MoneyAccountMonitor) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MoneyAccountMonitor{" +
                "id=" + id +
                '}';
    }
}
