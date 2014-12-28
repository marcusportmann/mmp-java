#!/bin/sh
DATABASE_DIRECTORY=`pwd`
sed 's|DATABASE_DIRECTORY|'$DATABASE_DIRECTORY'|g' SamplePostgres.sql > SamplePostgres.sql.tmp
sudo su postgres -c '/opt/local/lib/postgresql93/bin/psql -d sampledb -f SamplePostgres.sql.tmp'
rm -f SamplePostgres.sql.tmp









