/*
 * This file is generated by jOOQ.
 */
package quantasma.app.db;


import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;

import quantasma.app.db.tables.Brokers;
import quantasma.app.db.tables.Instruments;
import quantasma.app.db.tables.Orders;
import quantasma.app.db.tables.Strategies;
import quantasma.app.db.tables.Transactions;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index BROKERS_PK_ID = Indexes0.BROKERS_PK_ID;
    public static final Index BROKERS_UQ_NAME = Indexes0.BROKERS_UQ_NAME;
    public static final Index INSTRUMENTS_PK_ID = Indexes0.INSTRUMENTS_PK_ID;
    public static final Index INSTRUMENTS_UQ_NAME = Indexes0.INSTRUMENTS_UQ_NAME;
    public static final Index ORDERS_PK_ID = Indexes0.ORDERS_PK_ID;
    public static final Index STRATEGIES_PK_ID = Indexes0.STRATEGIES_PK_ID;
    public static final Index STRATEGIES_UQ_NAME = Indexes0.STRATEGIES_UQ_NAME;
    public static final Index TRANSACTIONS_IDX_OPEN_ORDER_ID = Indexes0.TRANSACTIONS_IDX_OPEN_ORDER_ID;
    public static final Index TRANSACTIONS_IDX_OPEN_ORDER_ID_CLOSE_ORDER_ID = Indexes0.TRANSACTIONS_IDX_OPEN_ORDER_ID_CLOSE_ORDER_ID;
    public static final Index TRANSACTIONS_IDX_STRATEGY_ID = Indexes0.TRANSACTIONS_IDX_STRATEGY_ID;
    public static final Index TRANSACTIONS_PK_ID = Indexes0.TRANSACTIONS_PK_ID;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index BROKERS_PK_ID = Internal.createIndex("brokers_pk_id", Brokers.BROKERS, new OrderField[] { Brokers.BROKERS.ID }, true);
        public static Index BROKERS_UQ_NAME = Internal.createIndex("brokers_uq_name", Brokers.BROKERS, new OrderField[] { Brokers.BROKERS.NAME }, true);
        public static Index INSTRUMENTS_PK_ID = Internal.createIndex("instruments_pk_id", Instruments.INSTRUMENTS, new OrderField[] { Instruments.INSTRUMENTS.ID }, true);
        public static Index INSTRUMENTS_UQ_NAME = Internal.createIndex("instruments_uq_name", Instruments.INSTRUMENTS, new OrderField[] { Instruments.INSTRUMENTS.NAME }, true);
        public static Index ORDERS_PK_ID = Internal.createIndex("orders_pk_id", Orders.ORDERS, new OrderField[] { Orders.ORDERS.ID }, true);
        public static Index STRATEGIES_PK_ID = Internal.createIndex("strategies_pk_id", Strategies.STRATEGIES, new OrderField[] { Strategies.STRATEGIES.ID }, true);
        public static Index STRATEGIES_UQ_NAME = Internal.createIndex("strategies_uq_name", Strategies.STRATEGIES, new OrderField[] { Strategies.STRATEGIES.NAME }, true);
        public static Index TRANSACTIONS_IDX_OPEN_ORDER_ID = Internal.createIndex("transactions_idx_open_order_id", Transactions.TRANSACTIONS, new OrderField[] { Transactions.TRANSACTIONS.OPEN_ORDER_ID }, false);
        public static Index TRANSACTIONS_IDX_OPEN_ORDER_ID_CLOSE_ORDER_ID = Internal.createIndex("transactions_idx_open_order_id_close_order_id", Transactions.TRANSACTIONS, new OrderField[] { Transactions.TRANSACTIONS.OPEN_ORDER_ID, Transactions.TRANSACTIONS.CLOSE_ORDER_ID }, false);
        public static Index TRANSACTIONS_IDX_STRATEGY_ID = Internal.createIndex("transactions_idx_strategy_id", Transactions.TRANSACTIONS, new OrderField[] { Transactions.TRANSACTIONS.STRATEGY_ID }, false);
        public static Index TRANSACTIONS_PK_ID = Internal.createIndex("transactions_pk_id", Transactions.TRANSACTIONS, new OrderField[] { Transactions.TRANSACTIONS.ID }, true);
    }
}
