NEW_VERSION=$1

YAML_CONTENT=`awk '/image:/' momukji-gateway.yml`
CURRENT_VERSION="${YAML_CONTENT#*momukji-gateway:}"

sed -i "s/gateway:${CURRENT_VERSION}/gateway:${NEW_VERSION}/g" momukji-gateway.yml