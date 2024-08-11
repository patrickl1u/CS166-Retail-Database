#! /bin/bash
# taken from class material in CS166 

folder=/tmp/$USER
export PGDATA=$folder/myDB/data
export PGSOCKETS=$folder/myDB/sockets

echo $folder

#Clear folder
rm -rf $folder

#Initialize folders
mkdir $folder
mkdir $folder/myDB
mkdir $folder/myDB/data
mkdir $folder/myDB/sockets
sleep 1

#Initialize DB
# initdb
# for some reason, initdb not included in default path... just hardcode for now
# /usr/lib/postgresql/12/bin/
# use what is in path
# if conda env is active, should be postgres 12
initdb

sleep 1
#Start folder
export PGPORT=1024
# pg_ctl -o "-c unix_socket_directories=$PGSOCKETS -p $PGPORT" -D $PGDATA -l $folder/logfile start
# ubuntu recommends using pg_ctlcluster
# lazy... hardcode pg_ctl in

# /usr/lib/postgresql/12/bin/
# use what is in path
# if conda env is active, should be postgres 12
pg_ctl -o "-c unix_socket_directories=$PGSOCKETS -p $PGPORT" -D $PGDATA -l $folder/logfile start
