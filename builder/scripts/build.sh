# enter work directory
workdir=$(dirname "$(cd "$(dirname "$0")" || exit;pwd)")
cd "$workdir" || return

# reset resources
rm -rf ./resources/scaffold
cp -r ../scaffold ./resources/scaffold

platform="$1"
echo "$platform"
case $platform in
  "win")
    echo "Building Windows x86_64"
    rm -rf ./bin/carbon-builder
    CGO_ENABLED=0 GOOS=windows GOARCH=amd64 && go build -o ./bin/carbon-builder ./*.go
    ;;
  "darwin")
    echo "Building Darwin x86_64"
    rm -rf ./bin/carbon-builder
    CGO_ENABLED=0 GOOS=darwin GOARCH=arm64 && go build -o ./bin/carbon-builder  ./*.go
    ;;
  "darwin-silicon")
    echo "Building Darwin ARM_64"
    rm -rf ./bin/carbon-builder
    CGO_ENABLED=0 GOOS=darwin GOARCH=amd64 && go build -o ./bin/carbon-builder ./*.go
    ;;
  "linux")
    echo "Building Linux x86_64"
    rm -rf ./bin/carbon-builder
    CGO_ENABLED=0 GOOS=linux GOARCH=amd64 && go build -o ./bin/carbon-builder ./*.go
    ;;
  *)
    printf "Error: platform not set, the first argument of shell script is platform\nsupported platforms are [\"win\", \"darwin\", \"darwin-silicon\"] "
    exit
    ;;
esac
echo "Build Success"
