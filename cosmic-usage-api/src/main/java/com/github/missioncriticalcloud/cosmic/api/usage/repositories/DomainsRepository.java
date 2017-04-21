package com.github.missioncriticalcloud.cosmic.api.usage.repositories;

import java.util.List;
import java.util.Map;

import com.github.missioncriticalcloud.cosmic.usage.core.model.Domain;

public interface DomainsRepository {

    List<Domain> list(String path);

    Map<String, Domain> map(String path);

}
