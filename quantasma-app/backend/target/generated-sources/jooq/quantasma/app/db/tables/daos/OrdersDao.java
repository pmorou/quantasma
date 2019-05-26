/*
 * This file is generated by jOOQ.
 */
package quantasma.app.db.tables.daos;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;

import quantasma.app.db.enums.OrderStatus;
import quantasma.app.db.tables.Orders;
import quantasma.app.db.tables.records.OrdersRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class OrdersDao extends DAOImpl<OrdersRecord, quantasma.app.db.tables.pojos.Orders, Long> {

    /**
     * Create a new OrdersDao without any configuration
     */
    public OrdersDao() {
        super(Orders.ORDERS, quantasma.app.db.tables.pojos.Orders.class);
    }

    /**
     * Create a new OrdersDao with an attached configuration
     */
    public OrdersDao(Configuration configuration) {
        super(Orders.ORDERS, quantasma.app.db.tables.pojos.Orders.class, configuration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long getId(quantasma.app.db.tables.pojos.Orders object) {
        return object.getId();
    }

    /**
     * Fetch records that have <code>id IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchById(Long... values) {
        return fetch(Orders.ORDERS.ID, values);
    }

    /**
     * Fetch a unique record that has <code>id = value</code>
     */
    public quantasma.app.db.tables.pojos.Orders fetchOneById(Long value) {
        return fetchOne(Orders.ORDERS.ID, value);
    }

    /**
     * Fetch records that have <code>instrument_id IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByInstrumentId(Long... values) {
        return fetch(Orders.ORDERS.INSTRUMENT_ID, values);
    }

    /**
     * Fetch records that have <code>side IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchBySide(String... values) {
        return fetch(Orders.ORDERS.SIDE, values);
    }

    /**
     * Fetch records that have <code>amount IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByAmount(Long... values) {
        return fetch(Orders.ORDERS.AMOUNT, values);
    }

    /**
     * Fetch records that have <code>price IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByPrice(BigDecimal... values) {
        return fetch(Orders.ORDERS.PRICE, values);
    }

    /**
     * Fetch records that have <code>status IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByStatus(OrderStatus... values) {
        return fetch(Orders.ORDERS.STATUS, values);
    }

    /**
     * Fetch records that have <code>x_created_on IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByXCreatedOn(LocalDateTime... values) {
        return fetch(Orders.ORDERS.X_CREATED_ON, values);
    }

    /**
     * Fetch records that have <code>x_updated_on IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByXUpdatedOn(LocalDateTime... values) {
        return fetch(Orders.ORDERS.X_UPDATED_ON, values);
    }

    /**
     * Fetch records that have <code>x_deleted_on IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByXDeletedOn(LocalDateTime... values) {
        return fetch(Orders.ORDERS.X_DELETED_ON, values);
    }

    /**
     * Fetch records that have <code>x_active IN (values)</code>
     */
    public List<quantasma.app.db.tables.pojos.Orders> fetchByXActive(Boolean... values) {
        return fetch(Orders.ORDERS.X_ACTIVE, values);
    }
}
