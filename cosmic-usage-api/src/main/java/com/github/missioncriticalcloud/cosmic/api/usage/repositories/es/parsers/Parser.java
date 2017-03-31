package com.github.missioncriticalcloud.cosmic.api.usage.repositories.es.parsers;

import java.util.Map;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;
import io.searchbox.core.SearchResult;

public interface Parser {

    void parse(Map<String, Domain> domainsMap, SearchResult searchResult);

}
