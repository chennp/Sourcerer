/* 
 * Sourcerer: an infrastructure for large-scale source code analysis.
 * Copyright (C) by contributors. See CONTRIBUTORS.txt for full list.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package edu.uci.ics.sourcerer.tools.java.utilization.model.cluster;

import java.util.Set;

import edu.uci.ics.sourcerer.tools.java.utilization.model.jar.FqnVersion;
import edu.uci.ics.sourcerer.tools.java.utilization.model.jar.Jar;
import edu.uci.ics.sourcerer.tools.java.utilization.model.jar.JarSet;

/**
 * @author Joel Ossher (jossher@uci.edu)
 */
public class ClusterVersion {
  private final Cluster cluster;
  private final Set<FqnVersion> fqns;
  private JarSet jars;
  
  private ClusterVersion(Cluster cluster, Set<FqnVersion> fqns) {
    this.cluster = cluster;
    this.fqns = fqns;
    this.jars = JarSet.create();
  }
  
  static ClusterVersion create(Cluster cluster, Set<FqnVersion> fqns) {
    return new ClusterVersion(cluster, fqns);
  }
  
  public Cluster getCluster() {
    return cluster;
  }
  
  public Set<FqnVersion> getFqns() {
    return fqns;
  }
  
  void addJar(Jar jar) {
    jars = jars.add(jar);
  }
  
  public JarSet getJars() {
    return jars;
  }
}
