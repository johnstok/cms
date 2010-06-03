password=`python -c 'import uuid; print uuid.uuid4()'`

mysql -h $1 -u $2 -p -e "\
CREATE DATABASE $3 CHARACTER SET utf8 COLLATE utf8_bin;\
CREATE USER $3 IDENTIFIED BY '$password';\
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER on $3.* TO '$3';"

echo "Created schema $3 with password $password."