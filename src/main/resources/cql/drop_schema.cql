use datastax_stockmarket_demo; 

truncate historic_data;
truncate dividends;
truncate exchange_metadata;

drop table historic_data;
drop table dividends;
drop table exchange_metadata;

drop keyspace datastax_stockmarket_demo; 

