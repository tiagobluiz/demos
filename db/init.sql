CREATE TABLE IF NOT EXISTS Station (
    id serial PRIMARY KEY,
    coordinates point NOT NULL,
    address varchar(256) NOT NULL,
    parish varchar(256) NOT NULL,
    capacity smallint NOT NULL,
    status varchar(50) NOT NULL, -- PLANNED / INSTALLING / TESTING / INACTIVE / ACTIVE
    electricBikes smallint NOT NULL DEFAULT 0,
    classicBikes smallint NOT NULL DEFAULT 0,
    CONSTRAINT UK_address UNIQUE(address)
);

CREATE INDEX IF NOT EXISTS idx_station_status ON Station(status);

CREATE TABLE IF NOT EXISTS Bike (
    id serial PRIMARY KEY,
    type varchar(50) NOT NULL, -- ELECTRIC / CLASSIC
    status varchar(50) NOT NULL, -- ON_USE / AVAILABLE / ON_MAINTENANCE / REQUIRING_MAINTENANCE
    km int NOT NULL DEFAULT 0,
    last_maintenance_date date NULL
);

CREATE TABLE IF NOT EXISTS Trip(
    id bigserial PRIMARY KEY,
    departure_station int NOT NULL,
    arrival_station int NOT NULL,
    bike_id int NOT NULL,
    travel_time time NOT NULL,
    CONSTRAINT FK_Trip_DepartureStation FOREIGN KEY (departure_station) REFERENCES Station(id),
    CONSTRAINT FK_Trip_ArrivalStation FOREIGN KEY (arrival_station) REFERENCES Station(id),
    CONSTRAINT FK_Trip_Bike FOREIGN KEY (bike_id) REFERENCES Bike(id)
);

CREATE TABLE IF NOT EXISTS UserBikeReview (
    id serial PRIMARY KEY,
    stars smallint NOT NULL,
    review varchar(512) NULL,
    target_bike_id int NOT NULL,
    CONSTRAINT FK_UserReview_Bike FOREIGN KEY (target_bike_id) REFERENCES Bike(id)
);