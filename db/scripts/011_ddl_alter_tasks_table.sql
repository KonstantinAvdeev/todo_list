ALTER TABLE tasks
    ADD COLUMN category_id int REFERENCES categories (id);