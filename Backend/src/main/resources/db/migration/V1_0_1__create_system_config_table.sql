CREATE TABLE system_config (
    config_key   VARCHAR(100) PRIMARY KEY,
    config_value VARCHAR(500)
);

-- Add index for faster lookups
CREATE INDEX idx_system_config_key ON system_config(config_key);

-- Insert comment for documentation
COMMENT ON TABLE system_config IS 'Stores system-level configuration as key-value pairs, including admin party ID and DAML contract IDs';
COMMENT ON COLUMN system_config.config_key IS 'Configuration key (e.g., ADMIN_PARTY_ID, CC_FACTORY_CONTRACT_ID)';
COMMENT ON COLUMN system_config.config_value IS 'Configuration value';
