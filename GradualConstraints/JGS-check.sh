#!/usr/bin/env bash

JGSSUPPORT="aux-files-for-ecoop-vm/JGSSupport"

java -jar target/scala-2.11/GradualConstraints-assembly-0.1-SNAPSHOT.jar \
     --support-classes ${JGSSUPPORT}/bin \
     --external-annotations ${JGSSUPPORT}/external-annotations.json \
     --cast-methods ${JGSSUPPORT}/cast-methods.json \
     "${@}"

