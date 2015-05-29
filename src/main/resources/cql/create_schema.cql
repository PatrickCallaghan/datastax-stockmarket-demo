create keyspace if not exists datastax_stockmarket_demo WITH replication = {'class': 'NetworkTopologyStrategy', 'Cassandra': 3};

use datastax_stockmarket_demo; 

CREATE TABLE if not exists historic_data ( 
	exchange text,
	symbol text,
	date timestamp,
	open double,
	high double,
	low double,
	close double,
	volume int,
	adj_close double,
	PRIMARY KEY ((exchange, symbol), date)
) WITH CLUSTERING ORDER BY (date DESC);

CREATE TABLE if not exists dividends ( 
	exchange text,
	symbol text,
	date timestamp,
	dividend double,
	PRIMARY KEY ((exchange, symbol), date)
) WITH CLUSTERING ORDER BY (date DESC);

CREATE TABLE if not exists exchange_metadata(
	exchange text,
	symbol text,
	name text,
	price double,
	last_updated_date timestamp,
	PRIMARY KEY (exchange, symbol)
);

CREATE TABLE if not exists latest_prices(
	exchange text,
	symbol text,
	date timestamp,
	price double,
	change double,
	percentChange double,
	todaysStart double, 
	daysMin double,
	daysMax double,
	volume double,
	PRIMARY KEY ((exchange), symbol)
) WITH compaction = {'class' : 'LeveledCompactionStrategy'};


truncate historic_data;
truncate dividends;
truncate exchange_metadata;
truncate latest_prices;
