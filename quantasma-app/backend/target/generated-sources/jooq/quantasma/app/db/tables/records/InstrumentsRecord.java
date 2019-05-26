/*
 * This file is generated by jOOQ.
 */
package quantasma.app.db.tables.records;


import java.time.LocalDateTime;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.UpdatableRecordImpl;

import quantasma.app.db.tables.Instruments;
import quantasma.app.db.tables.interfaces.IInstruments;


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
@Entity
@Table(name = "instruments", schema = "public", indexes = {
    @Index(name = "instruments_pk_id", unique = true, columnList = "id ASC"),
    @Index(name = "instruments_uq_name", unique = true, columnList = "name ASC")
})
public class InstrumentsRecord extends UpdatableRecordImpl<InstrumentsRecord> implements Record7<Long, String, Short, LocalDateTime, LocalDateTime, LocalDateTime, Boolean>, IInstruments {

    private static final long serialVersionUID = 1175950862;

    /**
     * Setter for <code>public.instruments.id</code>.
     */
    @Override
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.instruments.id</code>.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, precision = 64)
    @Override
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>public.instruments.name</code>.
     */
    @Override
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.instruments.name</code>.
     */
    @Column(name = "name", nullable = false, length = 255)
    @NotNull
    @Size(max = 255)
    @Override
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.instruments.precision</code>.
     */
    @Override
    public void setPrecision(Short value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.instruments.precision</code>.
     */
    @Column(name = "precision", nullable = false, precision = 16)
    @NotNull
    @Override
    public Short getPrecision() {
        return (Short) get(2);
    }

    /**
     * Setter for <code>public.instruments.x_created_on</code>.
     */
    @Override
    public void setXCreatedOn(LocalDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.instruments.x_created_on</code>.
     */
    @Column(name = "x_created_on", nullable = false)
    @Override
    public LocalDateTime getXCreatedOn() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>public.instruments.x_updated_on</code>.
     */
    @Override
    public void setXUpdatedOn(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.instruments.x_updated_on</code>.
     */
    @Column(name = "x_updated_on")
    @Override
    public LocalDateTime getXUpdatedOn() {
        return (LocalDateTime) get(4);
    }

    /**
     * Setter for <code>public.instruments.x_deleted_on</code>.
     */
    @Override
    public void setXDeletedOn(LocalDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.instruments.x_deleted_on</code>.
     */
    @Column(name = "x_deleted_on")
    @Override
    public LocalDateTime getXDeletedOn() {
        return (LocalDateTime) get(5);
    }

    /**
     * Setter for <code>public.instruments.x_active</code>.
     */
    @Override
    public void setXActive(Boolean value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.instruments.x_active</code>.
     */
    @Column(name = "x_active")
    @Override
    public Boolean getXActive() {
        return (Boolean) get(6);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record7 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Long, String, Short, LocalDateTime, LocalDateTime, LocalDateTime, Boolean> fieldsRow() {
        return (Row7) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row7<Long, String, Short, LocalDateTime, LocalDateTime, LocalDateTime, Boolean> valuesRow() {
        return (Row7) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Instruments.INSTRUMENTS.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return Instruments.INSTRUMENTS.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field3() {
        return Instruments.INSTRUMENTS.PRECISION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field4() {
        return Instruments.INSTRUMENTS.X_CREATED_ON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field5() {
        return Instruments.INSTRUMENTS.X_UPDATED_ON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<LocalDateTime> field6() {
        return Instruments.INSTRUMENTS.X_DELETED_ON;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field7() {
        return Instruments.INSTRUMENTS.X_ACTIVE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component3() {
        return getPrecision();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component4() {
        return getXCreatedOn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component5() {
        return getXUpdatedOn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime component6() {
        return getXDeletedOn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component7() {
        return getXActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value3() {
        return getPrecision();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value4() {
        return getXCreatedOn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value5() {
        return getXUpdatedOn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime value6() {
        return getXDeletedOn();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value7() {
        return getXActive();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord value2(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord value3(Short value) {
        setPrecision(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord value4(LocalDateTime value) {
        setXCreatedOn(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord value5(LocalDateTime value) {
        setXUpdatedOn(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord value6(LocalDateTime value) {
        setXDeletedOn(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord value7(Boolean value) {
        setXActive(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InstrumentsRecord values(Long value1, String value2, Short value3, LocalDateTime value4, LocalDateTime value5, LocalDateTime value6, Boolean value7) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        return this;
    }

    // -------------------------------------------------------------------------
    // FROM and INTO
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void from(IInstruments from) {
        setId(from.getId());
        setName(from.getName());
        setPrecision(from.getPrecision());
        setXCreatedOn(from.getXCreatedOn());
        setXUpdatedOn(from.getXUpdatedOn());
        setXDeletedOn(from.getXDeletedOn());
        setXActive(from.getXActive());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E extends IInstruments> E into(E into) {
        into.from(this);
        return into;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached InstrumentsRecord
     */
    public InstrumentsRecord() {
        super(Instruments.INSTRUMENTS);
    }

    /**
     * Create a detached, initialised InstrumentsRecord
     */
    public InstrumentsRecord(Long id, String name, Short precision, LocalDateTime xCreatedOn, LocalDateTime xUpdatedOn, LocalDateTime xDeletedOn, Boolean xActive) {
        super(Instruments.INSTRUMENTS);

        set(0, id);
        set(1, name);
        set(2, precision);
        set(3, xCreatedOn);
        set(4, xUpdatedOn);
        set(5, xDeletedOn);
        set(6, xActive);
    }
}
