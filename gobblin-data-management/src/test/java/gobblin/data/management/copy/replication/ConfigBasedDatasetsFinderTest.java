/*
 * Copyright (C) 2014-2016 LinkedIn Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 */

package gobblin.data.management.copy.replication;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.hadoop.fs.Path;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * Unit test for {@link ConfigBasedDatasetsFinder}
 * @author mitu
 *
 */
@Test(groups = {"gobblin.data.management.copy.replication"})
public class ConfigBasedDatasetsFinderTest {
  
  @Test
  public void testGetLeafDatasetURIs() throws URISyntaxException, IOException {
    Collection<URI> allDatasetURIs = new ArrayList<URI>();
    // leaf URI
    allDatasetURIs.add(new URI("/data/derived/browsemaps/entities/anet"));
    allDatasetURIs.add(new URI("/data/derived/browsemaps/entities/comp"));
    allDatasetURIs.add(new URI("/data/derived/gowl/pymk/invitationsCreationsSends/hourly_data/aggregation/daily"));
    allDatasetURIs.add(new URI("/data/derived/gowl/pymk/invitationsCreationsSends/hourly_data/aggregation/daily_dedup"));
    
    // None leaf URI
    allDatasetURIs.add(new URI("/data/derived"));
    allDatasetURIs.add(new URI("/data/derived/browsemaps"));
    allDatasetURIs.add(new URI("/data/derived/browsemaps/entities/"));
    
    allDatasetURIs.add(new URI("/data/derived/gowl/"));
    allDatasetURIs.add(new URI("/data/derived/gowl/pymk/"));
    allDatasetURIs.add(new URI("/data/derived/gowl/pymk/invitationsCreationsSends/"));
    allDatasetURIs.add(new URI("/data/derived/gowl/pymk/invitationsCreationsSends/hourly_data/aggregation"));

    // wrong root
    allDatasetURIs.add(new URI("/data/derived2"));
    
    // disabled
    Set<URI> disabled = new HashSet<URI>();
    disabled.add(new URI("/data/derived/gowl/pymk/invitationsCreationsSends/hourly_data/aggregation/daily"));
    
    Set<URI> validURIs = ConfigBasedDatasetsFinder.getValidDatasetURIs(allDatasetURIs, disabled, new Path("/data/derived"));
    
    Assert.assertTrue(validURIs.size() == 3);
    Assert.assertTrue(validURIs.contains(new URI("/data/derived/gowl/pymk/invitationsCreationsSends/hourly_data/aggregation/daily_dedup")));
    Assert.assertTrue(validURIs.contains(new URI("/data/derived/browsemaps/entities/comp")));
    Assert.assertTrue(validURIs.contains(new URI("/data/derived/browsemaps/entities/anet")));
  }
}
