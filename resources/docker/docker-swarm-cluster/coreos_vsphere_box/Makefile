VM_NAME  := CoreOS VMware Packer
BOX_NAME := CoreOS VMware Box

CHANNEL    ?= alpha
VERSION_ID ?= $(shell curl -Ls http://$(CHANNEL).release.core-os.net/amd64-usr/current/version.txt | grep COREOS_VERSION_ID= | sed -e 's,.*=,,')
BUILD_ID   ?= $(shell date -u '+%Y-%m-%d-%H%M')
BOX_ID     := $(CHANNEL)-$(VERSION_ID)

PWD := `pwd`

export CHANNEL
export VERSION_ID

all: clean coreos-$(BOX_ID)-vmware.box

coreos-$(BOX_ID)-vmware.box: coreos.vmdk
	@echo
	@echo "\t == Packing CoreOS release $(VERSION_ID) [$(CHANNEL) channel] for VMware =="
	@echo
	
#	curl http://$(CHANNEL).release.core-os.net/amd64-usr/$(VERSION_ID)/coreos_production_iso_image.iso -o coreos.iso
	
	
	rm -f coreos.vmx
	
	cp coreos.vmx.template coreos.vmx
	
	packer build coreos.json	 	
	
	
coreos.vmdk: coreos.vmdk.bz2

	bunzip2 -k coreos.vmdk.bz2


coreos.vmdk.bz2:

	curl http://$(CHANNEL).release.core-os.net/amd64-usr/$(VERSION_ID)/coreos_production_vmware_image.vmdk.bz2 -o coreos.vmdk.bz2  	
	
clean:

	@echo "http://$(CHANNEL).release.core-os.net/amd64-usr/$(VERSION_ID)/coreos_production_vmware_image.vmdk.bz2"

	rm -f *.vmxf
	rm -f *.box
	rm -f *$(BOX_ID)*.box
	rm -rf tmp/
	rm -f coreos.vmx

.PHONY: clean all




#	sed -i "" -e 's/CLOUD_CONFIG_DATA/$(CLOUD_CONFIG_DATA)/g' coreos.vmx




	


