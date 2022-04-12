create table IF not exists WEATHER (
city varchar(10),
currently_apparenttemperature double,
currently_humidity double,
currently_precipintensity double,
currently_precipprobability double,
currently_preciptype varchar(10),
currently_temperature double,
currently_visibility varchar(10),
currently_windspeed double,
date_time timestamp
);
