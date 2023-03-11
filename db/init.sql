CREATE TABLE IF NOT EXISTS stations (
    id serial PRIMARY KEY,
    coordinates varchar(256) NOT NULL,
    address varchar(256) NOT NULL,
    parish varchar(256) NOT NULL,
    capacity smallint NOT NULL,
    status varchar(50) NOT NULL, -- PLANNED / INSTALLING / TESTING / INACTIVE / ACTIVE
    electricBikes smallint NOT NULL DEFAULT 0,
    classicBikes smallint NOT NULL DEFAULT 0,
    CONSTRAINT UK_coordinates UNIQUE(coordinates)
);

CREATE INDEX IF NOT EXISTS idx_station_status ON stations(status);

CREATE TABLE IF NOT EXISTS bikes (
    id serial PRIMARY KEY,
    type varchar(50) NOT NULL, -- ELECTRIC / CLASSIC
    status varchar(50) NOT NULL, -- ON_USE / AVAILABLE / ON_MAINTENANCE / REQUIRING_MAINTENANCE
    km int NOT NULL DEFAULT 0,
    last_maintenance_date date NULL
);

CREATE TABLE IF NOT EXISTS trips(
    id bigserial PRIMARY KEY,
    departure_station_id int NOT NULL,
    arrival_station_id int NOT NULL,
    bike_id int NOT NULL,
    travel_time time NOT NULL,
    CONSTRAINT FK_trips_departureStation FOREIGN KEY (departure_station_id) REFERENCES stations(id),
    CONSTRAINT FK_trips_arrivalStation FOREIGN KEY (arrival_station_id) REFERENCES stations(id),
    CONSTRAINT FK_trips_bikes FOREIGN KEY (bike_id) REFERENCES bikes(id)
);

CREATE TABLE IF NOT EXISTS user_bike_reviews (
    id bigserial PRIMARY KEY,
    stars smallint NOT NULL,
    review varchar(512) NULL,
    target_bike_id int NOT NULL,
    CONSTRAINT FK_userBikeReviews_bikes FOREIGN KEY (target_bike_id) REFERENCES bikes(id)
);