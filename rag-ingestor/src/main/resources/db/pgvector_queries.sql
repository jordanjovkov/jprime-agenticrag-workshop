select count(*) from vector_store

select * from vector_store

delete from vector_store

DROP TABLE vector_store;

TRUNCATE TABLE vector_store


EXPLAIN ANALYZE
SELECT * FROM vector_store LIMIT 1;


SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename = 'vector_store';


DROP TABLE IF EXISTS vector_store;


SELECT vector_dims(embedding) as dimensions
FROM vector_store
LIMIT 1;

select * from vector_store
where content like '%486 processor%';

SELECT
    id,
    content,
    embedding IS NULL as missing_embedding,
    vector_dims(embedding) as dimensions
FROM vector_store
LIMIT 5;




-- Проверете кога са създадени записите
SELECT id, content, metadata,
       pg_size_pretty(pg_column_size(embedding)) as vector_size
FROM vector_store
WHERE content LIKE '%486%'
LIMIT 3;

SELECT * FROM pg_extension WHERE extname = 'vector';


SELECT extversion FROM pg_extension WHERE extname = 'vector';

SELECT column_name, data_type
FROM information_schema.columns
WHERE table_name = 'vector_store';


SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename = 'vector_store';


SELECT indexname, indexdef
FROM pg_indexes
WHERE tablename = 'vector_store'
  AND indexdef LIKE '%vector%';


SELECT * FROM pg_extension WHERE extname = 'vector';

SELECT column_name, data_type, udt_name
FROM information_schema.columns
WHERE table_name = 'vector_store';

-- Проверете dimensions на embedding колоната
SELECT vector_dims(embedding) as dimensions
FROM vector_store
LIMIT 1;

SELECT vector_dims(embedding) as dimensions
FROM vector_store
LIMIT 1;


SELECT
    id,
    content,
    embedding IS NOT NULL as has_embedding,
    vector_dims(embedding) as dims
FROM vector_store
LIMIT 5;

SELECT embedding
FROM vector_store
WHERE content LIKE '%486%'
LIMIT 1;

SELECT
    id,
    content,
    1 - (embedding <=> '[0.1,0.2,...]'::vector) as similarity
FROM vector_store
ORDER BY embedding <=> '[0.1,0.2,...]'::vector
LIMIT 5;


SELECT DISTINCT vector_dims(embedding) as dims
FROM vector_store;


SELECT id, content
FROM vector_store
WHERE content LIKE '%486%'
LIMIT 3;