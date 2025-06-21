CREATE TABLE documents (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           title VARCHAR(255) NOT NULL,
                           description TEXT,
                           text_content TEXT,
                           canvas_content JSONB,
                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE document_role AS ENUM ('AUTHOR', 'EDITOR', 'VIEWER');

CREATE TABLE document_user_access (
                                      id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                      document_id UUID NOT NULL,
                                      user_id VARCHAR(255) NOT NULL,
                                      role document_role NOT NULL,
                                      CONSTRAINT fk_document_user_access_document
                                          FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

CREATE INDEX idx_documents_title ON documents(title);
CREATE INDEX idx_documents_created_at ON documents(created_at);
CREATE INDEX idx_document_user_access_user_id ON document_user_access(user_id);
CREATE INDEX idx_document_user_access_document_id ON document_user_access(document_id);
CREATE UNIQUE INDEX idx_document_user_access_unique ON document_user_access(document_id, user_id);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_documents_updated_at
    BEFORE UPDATE ON documents
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();