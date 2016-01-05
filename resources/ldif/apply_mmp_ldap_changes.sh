#!/bin/sh
ldapmodify -v -c -h localhost -p 389 -D cn=root -w Password1  -f mmp_schema_modifications.ldif
ldapmodify -v -c -h localhost -p 389 -D cn=root -w Password1  -f mmp_schema_permission_changes.ldif
ldapmodify -v -c -h localhost -p 389 -D cn=root -w Password1  -f mmp.ldif


