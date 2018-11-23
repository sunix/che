#!/bin/bash
#
# Copyright (c) 2018 Red Hat, Inc.
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0

base_dir=$(cd "$(dirname "$0")"; pwd)

cd $base_dir && \
  echo "-- Local che-theia build"  && \
  echo "---- base_dir: $base_dir" && \
  echo "---- Running check-github-limit.sh" && \
    ./src/builder/check-github-limit.sh && \
  echo "---- Running build-and-publish-theia.sh" && \
    ./src/builder/build-and-publish-theia.sh && \
  echo "-- done"


