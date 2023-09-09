-- Создаем таблицу для хранения банковских счетов
CREATE TABLE IF NOT EXISTS Accounts (
    AccountNumber SERIAL PRIMARY KEY,
    Name VARCHAR(255) NOT NULL,
    PIN_HASH VARCHAR(60) NOT NULL,
    Balance DECIMAL(10, 2) DEFAULT 0.00
    );

-- Создаем таблицу для хранения операций по счетам (если нужно)
CREATE TABLE IF NOT EXISTS Transactions (
    TransactionID UUID PRIMARY KEY,
    AccountNumber INT NOT NULL,
    Amount DECIMAL(10, 2) NOT NULL,
    TransactionType VARCHAR(255) NOT NULL,
    TransactionDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (AccountNumber) REFERENCES Accounts(AccountNumber)
);