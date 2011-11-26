#!/bin/bash

nightly=`date -d yesterday "+%Y-%m-%d"`

jme="http://www.jmonkeyengine.com/nightly/jME3_${nightly}.zip"
town="http://www.jmonkeyengine.com/nightly/town.zip"


echo "Creating directories..."
mkdir -p _binaries
mkdir -p lib
mkdir -p data

cd _binaries

echo "Downloading jME..."
wget -c $jme
echo "Unpacking jME..."
jmezip=`basename $jme`
unzip $jmezip
echo "Copying libs..."
mv -f jMonkeyEngine3.jar ../lib
mv -f lib/* ../lib

echo "Downloading \"town\" data..."
wget $town
echo "Moving \"town\" data..."
mv town.zip ../data

echo "Deleting directories..."
cd ..
rm -rf _binaries
