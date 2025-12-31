-- Users table
CREATE TABLE users (
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE accounts (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES users(id)
);


-- Account balances
CREATE TABLE account_balances (
    account_id BINARY(16) PRIMARY KEY,
    balance DECIMAL(19,4) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL
);

-- create_transfers
CREATE TABLE transfers (
    id BINARY(16) PRIMARY KEY,          -- UUID of the transfer (txnId)
    from_account_id BINARY(16) NOT NULL,
    to_account_id BINARY(16) NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transfer_from_account FOREIGN KEY (from_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transfer_to_account FOREIGN KEY (to_account_id) REFERENCES accounts(id)
);


-- Ledger entries
CREATE TABLE ledger_entries (
	 id BINARY(16) PRIMARY KEY,
	  txn_id BINARY(16) NOT NULL,
	   account_id BINARY(16) NOT NULL,
	    entry_type VARCHAR(20) NOT NULL, 
	    -- e.g. DEBIT or CREDIT 
	    amount DECIMAL(19,4) NOT NULL, 
	    currency VARCHAR(10) NOT NULL, 
	    description VARCHAR(255) NOT NULL, 
	    created_at TIMESTAMP NOT NULL 
	    DEFAULT CURRENT_TIMESTAMP, 
	    CONSTRAINT fk_ledger_account 
	    FOREIGN KEY (account_id) 
	    REFERENCES accounts(id) );

-- Password reset tokens

CREATE TABLE password_reset_tokens (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    token VARCHAR(255) NOT NULL,
    expiry TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_password_reset_user FOREIGN KEY (user_id) REFERENCES users(id)
);





CREATE TABLE otp_codes (
    id BINARY(16) PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    code VARCHAR(10) NOT NULL,
    expiry TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_otp_user FOREIGN KEY (user_id) REFERENCES users(id)
);


-- Indexes
CREATE INDEX idx_otp_user ON otp_codes(user_id);
CREATE INDEX idx_otp_code ON otp_codes(code);
CREATE INDEX idx_ledger_txn ON ledger_entries(txn_id);
CREATE INDEX idx_transfer_from_account ON transfers(from_account_id);
CREATE INDEX idx_transfer_to_account ON transfers(to_account_id);
CREATE INDEX idx_transfer_created_at ON transfers(created_at);
CREATE INDEX idx_ledger_account ON ledger_entries(account_id); 
CREATE INDEX idx_ledger_entry_type ON ledger_entries(entry_type);
CREATE INDEX idx_password_reset_user ON password_reset_tokens(user_id);
CREATE INDEX idx_password_reset_token ON password_reset_tokens(token);
