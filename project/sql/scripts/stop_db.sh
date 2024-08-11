#! /bin/bash
# adapted from class material in CS166
# need variables here
folder=/tmp/$USER
export PGDATA=$folder/myDB/data
export PGSOCKETS=$folder/myDB/sockets

#/usr/lib/postgresql/12/bin/
# use what is in path
# if conda env is active, should be postgres 12
pg_ctl -o "-c unix_socket_directories=$PGSOCKETS -p $PGPORT" -D $PGDATA -l $folder/logfile stop