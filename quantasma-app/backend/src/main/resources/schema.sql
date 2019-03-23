DROP TABLE IF EXISTS instruments;
DROP TABLE IF EXISTS strategies;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS transactions;

CREATE TABLE instruments (
  id BIGSERIAL,
  name VARCHAR(255) NOT NULL,
  precision SMALLINT NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT pk_t_instruments PRIMARY KEY(id)
);

CREATE TABLE strategies (
  id BIGSERIAL,
  name VARCHAR(255) NOT NULL,
  class VARCHAR(255) NOT NULL,
  checksum VARCHAR(255) NOT NULL,
  active BOOLEAN NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT pk_t_strategies PRIMARY KEY(id)
);

CREATE TABLE orders (
  id BIGSERIAL,
  instrument_id BIGINT NOT NULL,
  side VARCHAR(4) NOT NULL,
  amount BIGINT NOT NULL,
  price DECIMAL NOT NULL,

  x_created_on TIMESTAMP NOT NULL,

  CONSTRAINT pk_t_orders PRIMARY KEY(id)
);

CREATE TABLE transactions (
  id BIGINT,
  open_ts TIMESTAMP NOT NULL,
  close_ts TIMESTAMP,
  open_order_id BIGINT NOT NULL,
  close_order_id BIGINT,
  pips_profit DECIMAL,
  strategy_id BIGINT NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,

  CONSTRAINT pk_t_transactions PRIMARY KEY(id)
);
