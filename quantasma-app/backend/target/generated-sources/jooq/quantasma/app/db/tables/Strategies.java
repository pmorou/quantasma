/*
 * This file is generated by jOOQ.
 */
package quantasma.app.db.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import quantasma.app.db.Indexes;
import quantasma.app.db.Keys;
import quantasma.app.db.Public;
import quantasma.app.db.enums.StrategyStatus;
import quantasma.app.db.enums.StrategyType;
import quantasma.app.db.tables.records.StrategiesRecord;


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
public class Strategies extends TableImpl<StrategiesRecord> {

    private static final long serialVersionUID = -858686411;

    /**
     * The reference instance of <code>public.strategies</code>
     */
    public static final Strategies STRATEGIES = new Strategies();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StrategiesRecord> getRecordType() {
        return StrategiesRecord.class;
    }

    /**
     * The column <code>public.strategies.id</code>.
     */
    public final TableField<StrategiesRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('strategies_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.strategies.name</code>.
     */
    public final TableField<StrategiesRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.strategies.class</code>.
     */
    public final TableField<StrategiesRecord, String> CLASS = createField("class", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.strategies.checksum</code>.
     */
    public final TableField<StrategiesRecord, String> CHECKSUM = createField("checksum", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.strategies.active</code>.
     */
    public final TableField<StrategiesRecord, Boolean> ACTIVE = createField("active", org.jooq.impl.SQLDataType.BOOLEAN.nullable(false), this, "");

    /**
     * The column <code>public.strategies.status</code>.
     */
    public final TableField<StrategiesRecord, StrategyStatus> STATUS = createField("status", org.jooq.impl.SQLDataType.VARCHAR.nullable(false).asEnumDataType(quantasma.app.db.enums.StrategyStatus.class), this, "");

    /**
     * The column <code>public.strategies.type</code>.
     */
    public final TableField<StrategiesRecord, StrategyType[]> TYPE = createField("type", org.jooq.impl.SQLDataType.VARCHAR.asEnumDataType(quantasma.app.db.enums.StrategyType.class).getArrayDataType(), this, "");

    /**
     * The column <code>public.strategies.x_created_on</code>.
     */
    public final TableField<StrategiesRecord, LocalDateTime> X_CREATED_ON = createField("x_created_on", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.strategies.x_updated_on</code>.
     */
    public final TableField<StrategiesRecord, LocalDateTime> X_UPDATED_ON = createField("x_updated_on", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.strategies.x_deleted_on</code>.
     */
    public final TableField<StrategiesRecord, LocalDateTime> X_DELETED_ON = createField("x_deleted_on", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.strategies.x_active</code>.
     */
    public final TableField<StrategiesRecord, Boolean> X_ACTIVE = createField("x_active", org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>public.strategies</code> table reference
     */
    public Strategies() {
        this(DSL.name("strategies"), null);
    }

    /**
     * Create an aliased <code>public.strategies</code> table reference
     */
    public Strategies(String alias) {
        this(DSL.name(alias), STRATEGIES);
    }

    /**
     * Create an aliased <code>public.strategies</code> table reference
     */
    public Strategies(Name alias) {
        this(alias, STRATEGIES);
    }

    private Strategies(Name alias, Table<StrategiesRecord> aliased) {
        this(alias, aliased, null);
    }

    private Strategies(Name alias, Table<StrategiesRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Strategies(Table<O> child, ForeignKey<O, StrategiesRecord> key) {
        super(child, key, STRATEGIES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.STRATEGIES_PK_ID, Indexes.STRATEGIES_UQ_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<StrategiesRecord, Long> getIdentity() {
        return Keys.IDENTITY_STRATEGIES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<StrategiesRecord> getPrimaryKey() {
        return Keys.STRATEGIES_PK_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<StrategiesRecord>> getKeys() {
        return Arrays.<UniqueKey<StrategiesRecord>>asList(Keys.STRATEGIES_PK_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Strategies as(String alias) {
        return new Strategies(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Strategies as(Name alias) {
        return new Strategies(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Strategies rename(String name) {
        return new Strategies(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Strategies rename(Name name) {
        return new Strategies(name, null);
    }
}
