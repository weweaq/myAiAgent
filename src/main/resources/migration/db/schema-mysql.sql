CREATE
    DATABASE vboss
    CHARACTER
        SET utf8mb4
    COLLATE utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS ai_chat_memory
(
    conversation_id VARCHAR(36) NOT NULL,
    content         TEXT        NOT NULL,
    type            VARCHAR(10) NOT NULL,
    `timestamp`     TIMESTAMP   NOT NULL,
    CONSTRAINT TYPE_CHECK CHECK (type IN ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL'))
);

create index ai_chat_memory_conversation_id_timestamp_index
    on ai_chat_memory (conversation_id, timestamp);

