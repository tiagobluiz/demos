INSERT INTO stations (id, coordinates, address, parish, status)
VALUES (1, ST_GeomFromText('POINT(38.77066006800165 -9.160284356927665)', 4326), 'Rua Luís de Freitas Branco', 'LUMIAR', 'ACTIVE'),
       (2, ST_GeomFromText('POINT(38.772483954508274 -9.154035912185192)', 4326), 'Rua Nóbrega e Sousa', 'LUMIAR', 'PLANNED');

INSERT INTO docks(id, station_id, dock_number, status)
VALUES (11, 1, 1, 'ACTIVE'),
       (22, 1, 2, 'ACTIVE'),
       (33, 1, 3, 'ACTIVE'),
       (44, 1, 4, 'REQUIRING_MAINTENANCE'),
       (55, 2, 1, 'ACTIVE'),
       (66, 2, 2, 'INACTIVE');

INSERT INTO bikes (id, type, status, km, last_maintenance_date)
VALUES (111, 'ELECTRIC', 'ACTIVE', 555, '03/15/2022'),
       (222, 'ELECTRIC', 'ACTIVE', 777, '03/14/2022'),
       (333, 'CLASSIC', 'REQUIRING_MAINTENANCE', 1234, '03/15/2021');

INSERT INTO user_bike_reviews(id, stars, review, target_bike_id)
VALUES (1, 5, 'Review 1 Bike 111', 111),
       (2, 4, 'Review 1 Bike 222', 222),
       (3, 5, 'Review 2 Bike 222', 222),
       (4, 2, 'Review 1 Bike 333', 111);