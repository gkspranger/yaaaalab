#!/usr/bin/env bash

echo -e "unsetting all YAAAALAB_* environment variables\n"

while read -r env_var
do
  echo "unsetting ${env_var}"
  unset "${env_var}"
done < <(env | grep -i YAAAALAB_ | cut -d "=" -f1)

echo -e "\ndone"
