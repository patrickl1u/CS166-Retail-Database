#!/bin/bash
PGPORT=1024

# missing create DB statement?
createdb -h localhost -p $PGPORT $USER"_DB"

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/create_tables.sql
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/create_indexes.sql
psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/triggers.sql

if [ "${PWD##*/}" = "scripts" ];
then
	echo "ASFLasdfajfoweij"
	cd ../../data
fi

psql -h localhost -p $PGPORT $USER"_DB" < $DIR/../src/load_data.sql

