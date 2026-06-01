-- V2__insert_initial_data.sql
-- Insert initial data

INSERT INTO VIDEO_EDITING_CARD (ID, NAME, MANUFACTURER, DESCRIPTION, PRICE) VALUES
    (1, 'Movie Machine Pro', 'Fast Multimedia AG', 'For enthusiasts and professional Productions', 350.00),
    (2, 'DPS Velocity',      'DPS',               'Professional for TV and Productions',          1200.00),
    (3, 'Media 100',         'Data Translation',  'MAC oriented Professional for TV and Productions', 450.00),
    (4, 'MiroMotion DC30',   'Pinacle',           'For enthusiasts and professionals',            280.00);

INSERT INTO CUSTOMER (ID, NAME, EMAIL, PHONE, ADDRESS, NOTES) VALUES
    (1, 'Ivan Ivanov',      'ivan.ivanov@gmail.com',    '0888111222', 'Sofia, Vitosha 1',       'VIP client'),
    (2, 'Petar Petrov',     'petar.petrov@gmail.com',   '0888333444', 'Plovdiv, Maritsa 5',     'Unable to pick up the phone by 2 pm'),
    (3, 'Georgi Georgiev',  'georgi.georgiev@gmail.com','0888555666', 'Varna, Cherno more 12',  'He prefers quick delivery'),
    (4, 'Nikolay Nikolaev', 'nikolay.nikolaev@gmail.com','0888777888','Sofia, Tech Park',       'The building lift is not working');

INSERT INTO STOCK_AVAILABILITY (ID, VIDEO_EDITING_CARD_ID, AVAILABILITY) VALUES
    (1, 1, 5),
    (2, 2, 23),
    (3, 3, 47),
    (4, 4, 12);

INSERT INTO ORDERS (ID, CUSTOMER_ID, VIDEO_EDITING_CARD_ID, ORDER_DATE, ORDER_NOTE) VALUES
    (1, 1, 1, '2025-01-15', 'Urgent delivery'),
    (2, 1, 3, '2025-02-20', 'Only credit card payment'),
    (3, 2, 2, '2025-03-10', 'Customer gift'),
    (4, 3, 1, '2025-03-25', 'The same delivery for the next order'),
    (5, 3, 4, '2025-04-05', 'Use light weight packages'),
    (6, 4, 2, '2025-04-18', 'See the invoice details');
