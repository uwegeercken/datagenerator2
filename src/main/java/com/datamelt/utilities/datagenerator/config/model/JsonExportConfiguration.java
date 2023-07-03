package com.datamelt.utilities.datagenerator.config.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class JsonExportConfiguration
{
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    boolean asArray = true;

    public boolean isAsArray() {
        return asArray;
    }

    public void setAsArray(boolean asArray) {
        this.asArray = asArray;
    }
}
