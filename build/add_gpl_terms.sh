while true
do
  echo -n "Please confirm (y or n) :"
  read CONFIRM
  case $CONFIRM in
    y|Y|YES|yes|Yes) break ;;
    n|N|no|NO|No)
      echo Aborting - you entered $CONFIRM
      exit
      ;;
    *) echo Please enter only y or n
  esac
done
echo You entered $CONFIRM.  Continuing ...

for f in $(find -name '*.java'); do
    sed -i '
    /^ \* All rights reserved.$/ a\
 *\
 * This file is part of CCC.\
 *\
 * CCC is free software: you can redistribute it and/or modify\
 * it under the terms of the GNU General Public License as published by\
 * the Free Software Foundation, either version 3 of the License, or\
 * (at your option) any later version.\
 *\
 * CCC is distributed in the hope that it will be useful,\
 * but WITHOUT ANY WARRANTY; without even the implied warranty of\
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\
 * GNU General Public License for more details.\
 *\
 * You should have received a copy of the GNU General Public License\
 * along with CCC.  If not, see <http://www.gnu.org/licenses/>.' $f
done

