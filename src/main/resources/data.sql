INSERT INTO HOTEL (ID, NAME, ADDRESS, RATING) VALUES (1, 'Hilton', 'Leof. Vasilissis Sofias 46, Athina 115 28', 4);
INSERT INTO HOTEL (ID, NAME, ADDRESS, RATING) VALUES (2, 'Grande Bretagne', '1 Vasileos Georgiou A, Syntagma Square Str, Athina 105 64', 5);
INSERT INTO HOTEL (ID, NAME, ADDRESS, RATING) VALUES (3, 'A for Athens', 'Miaouli 2, Athina 105 54', 3);
INSERT INTO HOTEL (ID, NAME, ADDRESS, RATING) VALUES (4, 'King George', '3 Vasileos Georgiou A, Syntagma Square Str, Athina 105 64', 5);

INSERT INTO BOOKING(CUSTOMER_NAME, CUSTOMER_LASTNAME, PAX_NUMBER, PRICE, CURRENCY, HOTEL_ID)
            VALUES ('Luke', 'Skywalker', 3, 199.99, 'EUR', 1);
INSERT INTO BOOKING(CUSTOMER_NAME, CUSTOMER_LASTNAME, PAX_NUMBER, PRICE, CURRENCY, HOTEL_ID)
            VALUES ('Luke', 'Skywalker', 1, 100.00, 'USD', 3);
INSERT INTO BOOKING(CUSTOMER_NAME, CUSTOMER_LASTNAME, PAX_NUMBER, PRICE, CURRENCY, HOTEL_ID)
            VALUES ('Luke', 'Skywalker', 2, 99, 'EUR', 3);
INSERT INTO BOOKING(CUSTOMER_NAME, CUSTOMER_LASTNAME, PAX_NUMBER, PRICE, CURRENCY, HOTEL_ID)
            VALUES ('Obi-Wan', 'Kenobi', 1, 199.99, 'GBP', 1);
INSERT INTO BOOKING(CUSTOMER_NAME, CUSTOMER_LASTNAME, PAX_NUMBER, PRICE, CURRENCY, HOTEL_ID)
            VALUES ('Leia', 'Organa', 2, 299.99, 'GBP', 1);
INSERT INTO BOOKING(CUSTOMER_NAME, CUSTOMER_LASTNAME, PAX_NUMBER, PRICE, CURRENCY, HOTEL_ID)
            VALUES ('Han', 'Solo', 2, 99.99, 'EUR', 1);
INSERT INTO BOOKING(CUSTOMER_NAME, CUSTOMER_LASTNAME, PAX_NUMBER, PRICE, CURRENCY, HOTEL_ID)
            VALUES ('Han', 'Solo', 1, 399.99, 'EUR', 2);