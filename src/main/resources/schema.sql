CREATE TABLE HOTEL(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  NAME VARCHAR(64) NOT NULL UNIQUE,
  ADDRESS VARCHAR(200),
  RATING TINYINT
);

CREATE TABLE BOOKING(
  ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  CUSTOMER_NAME VARCHAR(64) NOT NULL,
  CUSTOMER_LASTNAME VARCHAR(64) NOT NULL,
  PAX_NUMBER SMALLINT,
  PRICE DECIMAL(10,3),
  CURRENCY CHAR(3),
  HOTEL_ID BIGINT NOT NULL
);

-- FKs
ALTER TABLE BOOKING ADD FOREIGN KEY (HOTEL_ID) REFERENCES HOTEL(ID);

-- INDEXES
CREATE UNIQUE INDEX IDX_HOTEL_NAME ON HOTEL(NAME);
CREATE INDEX IDX_BOOKING_LASTNAME ON BOOKING(CUSTOMER_LASTNAME);
