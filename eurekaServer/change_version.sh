NEW_VERSION=$1

YAML_CONTENT=`awk '/image:/' momukji-eureka.yml`
CURRENT_VERSION="${YAML_CONTENT#*momukji-eureka:}"

sed -i "s/eureka:${CURRENT_VERSION}/eureka:${NEW_VERSION}/g" momukji-eureka.yml