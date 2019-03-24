DROP TABLE IF EXISTS instruments;
DROP TABLE IF EXISTS strategies;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS brokers;

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
  status VARCHAR(16) NOT NULL,

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
  status VARCHAR(16) NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,

  CONSTRAINT pk_t_orders PRIMARY KEY(id)
);

CREATE TABLE transactions (
  id BIGSERIAL,
  broker_id BIGINT NOT NULL,
  open_on TIMESTAMP NOT NULL,
  close_on TIMESTAMP,
  open_order_id BIGINT NOT NULL,
  close_order_id BIGINT,
  pips_profit DECIMAL,
  strategy_id BIGINT NOT NULL,
  status VARCHAR(16) NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,

  CONSTRAINT pk_t_transactions PRIMARY KEY(id)
);

CREATE TABLE brokers (
  id BIGSERIAL,
  name VARCHAR(255) NOT NULL,

  x_created_on TIMESTAMP NOT NULL,
  x_updated_on TIMESTAMP,
  x_deleted_on TIMESTAMP,

  CONSTRAINT pk_t_brokers PRIMARY KEY(id)
)
