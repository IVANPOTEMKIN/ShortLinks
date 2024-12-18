INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/all',
        'http://localhost:8080/redirect/ga',
        'ga', DEFAULT, CURRENT_DATE + INTERVAL '5 DAYS');

INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/1',
        'http://localhost:8080/redirect/g1',
        'g1', DEFAULT, CURRENT_DATE + INTERVAL '1 DAY');

INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/2',
        'http://localhost:8080/redirect/g2',
        'g2', DEFAULT, CURRENT_DATE + INTERVAL '1 DAY');

INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/3',
        'http://localhost:8080/redirect/g3',
        'g3', DEFAULT, CURRENT_DATE + INTERVAL '1 DAY');

INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/4',
        'http://localhost:8080/redirect/g4',
        'g4', CURRENT_DATE - INTERVAL '2 DAY', CURRENT_DATE - INTERVAL '1 DAY');

INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/5',
        'http://localhost:8080/redirect/g5',
        'g5', CURRENT_DATE - INTERVAL '2 DAY', CURRENT_DATE - INTERVAL '1 DAY');

INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/6',
        'http://localhost:8080/redirect/g6',
        'g6', CURRENT_DATE - INTERVAL '2 DAY', CURRENT_DATE - INTERVAL '1 DAY');

INSERT INTO test_links(original_url, short_url, alias, created_at, ttl)
    OVERRIDING SYSTEM VALUE
VALUES ('http://localhost:8080/link/get/all/alias?alias=g',
        'http://localhost:8080/redirect/gg',
        'gg', DEFAULT, CURRENT_DATE + INTERVAL '10 DAY');