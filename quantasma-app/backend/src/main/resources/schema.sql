DROP TABLE IF EXISTS instruments;
DROP TABLE IF EXISTS strategies;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS transactions;

CREATE TABLE instruments (
  id BIGSERIAL,
  name VARCHAR(255),

  created TIMESTAMP,
  updated TIMESTAMP,

  CONSTRAINT pk_t_instruments PRIMARY KEY(id)
);

CREATE TABLE strategies (
  id BIGSERIAL,
  name VARCHAR(255),
  class VARCHAR(255),
  checksum VARCHAR(255),
  active BOOLEAN,

  created TIMESTAMP,
  updated TIMESTAMP,
  deleted TIMESTAMP,

  CONSTRAINT pk_t_strategies PRIMARY KEY(id)
);

CREATE TABLE orders (
  id BIGSERIAL,
  instrument_id BIGINT,
  side VARCHAR(4),
  amount BIGINT,
  price DECIMAL,

  created TIMESTAMP,

  CONSTRAINT pk_t_orders PRIMARY KEY(id)
);

CREATE TABLE transactions (
  id BIGINT,
  open_ts TIMESTAMP,
  close_ts TIMESTAMP,
  open_order_id BIGINT,
  close_order_id BIGINT,
  pips_profit DECIMAL,
  strategy_id BIGINT,

  created TIMESTAMP,
  updated TIMESTAMP,

  CONSTRAINT pk_t_transactions PRIMARY KEY(id)
);
