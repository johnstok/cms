echo "CC7 upgrade tool"

echo -n "$A CC version: "
read cc_version

echo -n "$A App name: "
read app_name

wget "http://apollo-vm226:8081/nexus/content/repositories/releases/CCC7/application-ear/"$cc_version"/application-ear-"$cc_version".ear"
wget "http://apollo-vm226:8081/nexus/content/repositories/releases/CCC7/shell-tools/"$cc_version"/shell-tools-"$cc_version"-jar-with-dependencies.jar"

mkdir cc-$cc_version.ear
unzip application-ear-"$cc_version".ear -d ./cc-$cc_version.ear

cd cc-$cc_version.ear
../rename.sh cc7 $app_name
../dbconfig.sh
../mailconfig.sh
cd ..

