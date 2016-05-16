# Build JGS-check documentation to be submitted for ECOOP 2016
# artifact evaluation

# Requirements: pandoc

BUILD = _build
DEST = ../doc
ASSETS = assets
PAGES = $(BUILD)/index.html 

TEMPLATE = template.html
HEADER = header.yaml


.PHONY: all assets clean

all: $(PAGES) assets
	rsync -rP $(BUILD)/* $(DEST)

assets: $(BUILD)
	cp -r $(ASSETS) $(BUILD)

$(BUILD)/%.html: %.md $(TEMPLATE) $(HEADER) $(BUILD)
	pandoc --standalone --write html5 --template $(TEMPLATE) $(HEADER) $< -o $@

$(BUILD): 
	mkdir -p $(BUILD)

clean:
	rm -rf $(BUILD)
