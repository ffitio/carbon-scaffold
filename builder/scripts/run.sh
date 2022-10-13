# enter work directory
workdir=$(dirname "$(cd "$(dirname "$0")" || exit;pwd)")
cd "$workdir" || return

# reset resources
rm -rf ./resources/scaffold
cp -r ../scaffold ./resources/scaffold

go run ./*.go