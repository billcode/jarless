#!/bin/bash

set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE jarless_test;
    GRANT ALL PRIVILEGES ON DATABASE jarless_test TO jarless;

EOSQL
