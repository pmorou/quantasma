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
import quantasma.app.db.tables.records.InstrumentsRecord;


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
public class Instruments extends TableImpl<InstrumentsRecord> {

    private static final long serialVersionUID = -1423448938;

    /**
     * The reference instance of <code>public.instruments</code>
     */
    public static final Instruments INSTRUMENTS = new Instruments();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InstrumentsRecord> getRecordType() {
        return InstrumentsRecord.class;
    }

    /**
     * The column <code>public.instruments.id</code>.
     */
    public final TableField<InstrumentsRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("nextval('instruments_id_seq'::regclass)", org.jooq.impl.SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>public.instruments.name</code>.
     */
    public final TableField<InstrumentsRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.instruments.precision</code>.
     */
    public final TableField<InstrumentsRecord, Short> PRECISION = createField("precision", org.jooq.impl.SQLDataType.SMALLINT.nullable(false), this, "");

    /**
     * The column <code>public.instruments.x_created_on</code>.
     */
    public final TableField<InstrumentsRecord, LocalDateTime> X_CREATED_ON = createField("x_created_on", org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false).defaultValue(org.jooq.impl.DSL.field("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>public.instruments.x_updated_on</code>.
     */
    public final TableField<InstrumentsRecord, LocalDateTime> X_UPDATED_ON = createField("x_updated_on", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.instruments.x_deleted_on</code>.
     */
    public final TableField<InstrumentsRecord, LocalDateTime> X_DELETED_ON = createField("x_deleted_on", org.jooq.impl.SQLDataType.LOCALDATETIME, this, "");

    /**
     * The column <code>public.instruments.x_active</code>.
     */
    public final TableField<InstrumentsRecord, Boolean> X_ACTIVE = createField("x_active", org.jooq.impl.SQLDataType.BOOLEAN.defaultValue(org.jooq.impl.DSL.field("true", org.jooq.impl.SQLDataType.BOOLEAN)), this, "");

    /**
     * Create a <code>public.instruments</code> table reference
     */
    public Instruments() {
        this(DSL.name("instruments"), null);
    }

    /**
     * Create an aliased <code>public.instruments</code> table reference
     */
    public Instruments(String alias) {
        this(DSL.name(alias), INSTRUMENTS);
    }

    /**
     * Create an aliased <code>public.instruments</code> table reference
     */
    public Instruments(Name alias) {
        this(alias, INSTRUMENTS);
    }

    private Instruments(Name alias, Table<InstrumentsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Instruments(Name alias, Table<InstrumentsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Instruments(Table<O> child, ForeignKey<O, InstrumentsRecord> key) {
        super(child, key, INSTRUMENTS);
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
        return Arrays.<Index>asList(Indexes.INSTRUMENTS_PK_ID, Indexes.INSTRUMENTS_UQ_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<InstrumentsRecord, Long> getIdentity() {
        return Keys.IDENTITY_INSTRUMENTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<InstrumentsRecord> getPrimaryKey() {
        return Keys.INSTRUMENTS_PK_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<InstrumentsRecord>> getKeys() {
        return Arrays.<UniqueKey<InstrumentsRecord>>asList(Keys.INSTRUMENTS_PK_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instruments as(String alias) {
        return new Instruments(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Instruments as(Name alias) {
        return new Instruments(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Instruments rename(String name) {
        return new Instruments(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Instruments rename(Name name) {
        return new Instruments(name, null);
    }
}
