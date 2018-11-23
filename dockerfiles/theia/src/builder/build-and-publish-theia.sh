#!/bin/sh
#
# Copyright (c) 2018-2018 Red Hat, Inc.
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors:
#   Red Hat, Inc. - initial API and implementation
#

set -e
set -u

if [[ ! -v THEIA_SOURCE_CODE ]]; then
  THEIA_SOURCE_CODE=${HOME}/theia-source-code
  # Clone specific tag of a Theia version
  git clone --branch v${THEIA_VERSION} https://github.com/theia-ide/theia ${THEIA_SOURCE_CODE}

  # Apply patches (if any)
  if [ -d "/home/theia-build/patches/${THEIA_VERSION}" ]; then
      echo "Applying patches for Theia version ${THEIA_VERSION}";
      for file in $(find "/home/theia-build/patches/${THEIA_VERSION}" -name '*.patch'); do
          echo "Patching with ${file}";
          cd ${THEIA_SOURCE_CODE} && patch -p1 < ${file};
      done
  fi
fi
# Compile Theia
#cd ${THEIA_SOURCE_CODE} && yarn

# add registry and start it
if [ !-d ${HOME}/verdaccio ]; then
  npm install -g verdaccio
  mkdir ${HOME}/verdaccio
fi
cd ${HOME}/verdaccio
verdaccio &
sleep 3

# Update registry URL to local one
cd ${HOME}
yarn config set registry http://localhost:4873
npm config set registry http://localhost:4873


# Create user for local registry
export USERNAME=theia
export PASSWORD=theia
export EMAIL=che-theia@eclipse.org

/usr/bin/expect <<EOD
spawn npm adduser --registry http://localhost:4873
expect {
  "Username:" {send "$USERNAME\r"; exp_continue}
  "Password:" {send "$PASSWORD\r"; exp_continue}
  "Email: (this IS public)" {send "$EMAIL\r"; exp_continue}
}
EOD

# Now go to source code of theia and publish it
cd ${THEIA_SOURCE_CODE}

# using 0.4 there to bump major version so we're sure to not download any 0.3.x dependencies
# Set the version of Theia
export THEIA_VERSION=0.4.1-che

./node_modules/.bin/lerna publish --registry=http://localhost:4873 --exact --repo-version=${THEIA_VERSION} --skip-git --force-publish --npm-tag=latest  --yes
cd ${HOME}

# Code has been published, let's delete it
if [ $THEIA_SOURCE_CODE = ${HOME}/theia-source-code ]; then
  rm -rf ${HOME}/theia-source-code
fi
