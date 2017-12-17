#!/bin/bash

export GOPATH=$(go env GOPATH):$(pwd)
PROTO_PATH=$(readlink -f ../idl)