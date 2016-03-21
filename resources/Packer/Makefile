COREOS_CHANNEL             ?= alpha
COREOS_VERSION             ?= $(shell curl -Ls http://$(COREOS_CHANNEL).release.core-os.net/amd64-usr/current/version.txt | grep COREOS_VERSION_ID= | sed -e 's,.*=,,')
COREOS_ISO_SHA512_CHECKSUM ?= $(shell curl -Ls http://$(COREOS_CHANNEL).release.core-os.net/amd64-usr/$(COREOS_VERSION)/coreos_production_iso_image.iso.DIGESTS | grep -A1 "SHA512 HASH" | head -2 | awk '{getline; print $1}' | sed 's/ .*//')

PWD                        := `pwd`

export COREOS_CHANNEL
export COREOS_VERSION
export COREOS_ISO_SHA512_CHECKSUM

all: shared/boxes/coreos-mmp-vmware.box


shared/boxes/coreos-mmp-vmware.box:
	@echo
	@echo "\t == Packing the CoreOS release $(COREOS_VERSION) [$(COREOS_CHANNEL) channel] as a Vagrant Box  =="
	@echo

	packer build -only=vmware templates/coreos-mmp/coreos-mmp.json

	vagrant box add --force --name coreos-mmp shared/boxes/coreos-mmp-vmware.box


clean:

	rm -rf templates/coreos-mmp/images
	rm -f shared/boxes/*.box
	rm -rf ./templates/coreos-mmp/.vagrant
	rm -rf .vagrant
	vagrant box remove --force --all coreos-mmp; true


.PHONY: clean all
