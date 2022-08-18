shell_dir="$( cd "$( dirname "$0" )" && pwd -P )"
WORKSPACE=$shell_dir/build_output
mkdir -p ${WORKSPACE}
#!/bin/sh
#JAVA_HOME=${HOME}/build/java
echo "JAVA HOME : ${JAVA_HOME}"

PRODUCT_DIR=cubridmanager
PRODUCT_NAME=CUBRIDManager
ECLIPSE_HOME=/home/hwanyseo/eclipse_build/eclipse
BUILD_HOME=${WORKSPACE}
BUILD_DIR=${BUILD_HOME}/../../com.cubrid.cubridmanager.build
VERSION_DIR=${BUILD_HOME}/../../com.cubrid.cubridmanager.ui
VERSION_FILE_PATH=${VERSION_DIR}/version.properties
VERSION=`grep buildVersionId ${VERSION_FILE_PATH} | awk 'BEGIN {FS="="} ; {print $2}'`
CUR_VER_DIR=`date +%Y%m%d`
CUR_VER_DIR=cubridmanager-deploy/${CUR_VER_DIR}_${VERSION}
OUTPUT_DIR=${BUILD_HOME}/${CUR_VER_DIR}
MAKENSIS_EXEC_PATH=${HOME}/eclipse_build/nsis/makensis.exe
MAKENSIS_INPUT_PATH="c:/build/src/${PRODUCT_DIR}/com.cubrid.cubridmanager.build/deploy"
MAKENSIS_OUTPUT_PATH="c:/build/src/${CUR_VER_DIR}"

echo "${PRODUCT_NAME} ${VERSION} build is started..."
rm -rf ${OUTPUT_DIR}
mkdir -p ${OUTPUT_DIR}
cd ${BUILD_HOME}
#202206
${JAVA_HOME}/bin/java -jar ${ECLIPSE_HOME}/plugins/org.eclipse.equinox.launcher_1.6.400.v20210924-0641.jar -application org.eclipse.ant.core.antRunner -buildfile ${BUILD_DIR}/build.xml -Doutput.path=${OUTPUT_DIR} -Declipse.home=${ECLIPSE_HOME} -Dmakensis.path=${MAKENSIS_EXEC_PATH} -Dmakensis.input.path=${MAKENSIS_INPUT_PATH} -Dmakensis.output.path=${MAKENSIS_OUTPUT_PATH} -Dproduct.version=${VERSION} dist
